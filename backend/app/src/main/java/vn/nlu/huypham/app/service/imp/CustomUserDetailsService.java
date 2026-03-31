package vn.nlu.huypham.app.service.imp;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.nlu.huypham.app.entity.User;
import vn.nlu.huypham.app.repository.UserRepo;
import vn.nlu.huypham.app.security.basic.UserPrincipal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    
    final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        return new UserPrincipal(user);
    }
}
