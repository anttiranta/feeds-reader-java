package com.antti.task.service.initdb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import com.antti.task.entity.Role;
import com.antti.task.entity.User;
import com.antti.task.repository.RoleRepository;
import com.antti.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class InitDbService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() throws IOException {
        // Init db, only for testing!
        if (roleRepository.findByName("testioikeus") == null) {
            Role role = new Role();
            role.setName("testioikeus");
            roleRepository.save(role);

            Role role2 = new Role();
            role2.setName("other_resource");
            roleRepository.save(role2);

            User userAdmin = new User();
            userAdmin.setEnabled(true);
            userAdmin.setAdmin(true);
            userAdmin.setName("user");
            
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            userAdmin.setPassword(encoder.encode("pass123"));
            
            List<Role> roles = new ArrayList<>();
            roles.add(role);
            roles.add(role2);
            
            userAdmin.setRoles(roles);
            userRepository.save(userAdmin);
        }
    }
}
