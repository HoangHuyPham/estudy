package vn.nlu.huypham.app.middleware;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.security.basic.UserPrincipal;
import vn.nlu.huypham.app.service.JWTService;
import vn.nlu.huypham.app.service.JWTService.JWTInfo;

@Component
@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter
{

	final JWTService jwtService;
	final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException
	{
		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer "))
		{
			try
			{
				String token = authHeader.substring(7);

				JWTInfo jwtInfo = jwtService.extractFrom(token);
				UserPrincipal principal = (UserPrincipal) userDetailsService
						.loadUserByUsername(jwtInfo.getUsername());

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						principal, null, principal.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			catch (Exception e)
			{
				log.warn("JWT authentication failed: {}, IP: {}", e.getMessage(),
						request.getRemoteAddr());
			}
		}

		filterChain.doFilter(request, response);
	}

}
