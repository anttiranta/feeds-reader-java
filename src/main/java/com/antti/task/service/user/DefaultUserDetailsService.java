package com.antti.task.service.user;

import com.antti.task.entity.User;
import com.antti.task.repository.UserRepository;
import com.antti.task.user.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByName(username); 
        if (user == null) {
            throw new UsernameNotFoundException("No such user: " + username); // TODO: translate
        }
        return new UserPrincipal(user);
    }
}
