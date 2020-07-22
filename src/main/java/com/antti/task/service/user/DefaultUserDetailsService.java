package com.antti.task.service.user;

import com.antti.task.core.Translator;
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
    
    @Autowired
    private Translator translator;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByName(username); 
        if (user == null) {
            throw new UsernameNotFoundException(translator.trans("user.user_does_not_exist", username));
        }
        return new UserPrincipal(user);
    }
}
