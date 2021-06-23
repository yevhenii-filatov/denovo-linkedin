package com.dataox.imagedownloader.security.services;

import com.dataox.imagedownloader.security.models.User;
import com.dataox.imagedownloader.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userName));
        return UserDetailsImpl.build(user);
    }
}
