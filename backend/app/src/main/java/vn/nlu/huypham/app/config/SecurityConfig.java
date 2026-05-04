package vn.nlu.huypham.app.config;

import java.security.SecureRandom;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import vn.nlu.huypham.app.constant.Roles;
import vn.nlu.huypham.app.middleware.JWTFilter;
import vn.nlu.huypham.app.service.imp.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{

	final AppConfig appConfig;
	final CustomUserDetailsService userDetailsService;
	final JWTFilter jwtFilter;

	@Bean
	SecureRandom secureRandom()
	{
		return new SecureRandom();
	}

	@Bean
	SecurityFilterChain filterChain(
		HttpSecurity http) throws Exception
	{
		http.sessionManagement(sessionManagement -> sessionManagement
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.cors(Customizer.withDefaults());
		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(auth ->
		{
			auth.requestMatchers(HttpMethod.OPTIONS).permitAll();
			auth.requestMatchers("/swagger-ui.html").permitAll();
			auth.requestMatchers("/auth/**", "/resource/**", "/swagger-ui/**", "/v3/api-docs/**",
					"/api-docs/**").permitAll();
			auth.requestMatchers("/storage/**").hasAnyRole(Roles.ADMIN.toString());
			auth.requestMatchers("/tutor/**").hasAnyRole(Roles.TUTOR.toString(),
					Roles.ADMIN.toString());
			auth.anyRequest().authenticated();
		});
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling(exception -> exception.authenticationEntryPoint((
			request,
			response,
			authException) ->
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}).accessDeniedHandler((
			request,
			response,
			accessDeniedException) ->
		{
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}));

		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource()
	{
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(
				List.of(appConfig.getEndpoint().getClient(), "http://localhost:5555"));
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedMethods(List.of("*"));
		corsConfiguration.setAllowedHeaders(List.of("*"));
		source.registerCorsConfiguration("/**", corsConfiguration);

		return source;
	}

	@Bean
	PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(
		AuthenticationConfiguration config) throws Exception
	{
		return config.getAuthenticationManager();
	}
}
