package com.antti.task.service.initdb;

import com.antti.task.entity.Category;
import com.antti.task.entity.Item;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import com.antti.task.entity.Role;
import com.antti.task.entity.User;
import com.antti.task.repository.CategoryRepository;
import com.antti.task.repository.ItemRepository;
import com.antti.task.repository.RoleRepository;
import com.antti.task.repository.UserRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class InitDbService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PostConstruct
    public void init() throws IOException {
        addSampleRolesAndUsers();
        addSampleItemsAndCategories();
    }

    @Transactional
    private void addSampleRolesAndUsers() {
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

    private void addSampleItemsAndCategories() {
        String domain = "http://www.dmoz.com";
        
        if (categoryRepository.findByDomain(domain) == null) {
            Category category = new Category();
            category.setName("Computers/Software/Internet/Site " 
                    + " Management/Content Management")
                    .setDomain(domain);

            categoryRepository.save(category);

            Item item = (new Item())
                    .setTitle("RSS Solutions for Restaurants")
                    .setDescription("<b>FeedForAll </b>helps Restaurant's" 
                            + " communicate with customers.")
                    .setLink("http://www.feedforall.com/restaurant.htm")
                    .setCategory(category)
                    .setComments("http://www.feedforall.com/forum")
                    .setPubDate(new Date());

            itemRepository.save(item);

            item = (new Item())
                    .setTitle("RSS Solutions for Schools and Colleges")
                    .setDescription("FeedForAll helps Educational Institutions " 
                            + " communicate with students about school wide " 
                            + " activities, events, and schedules.")
                    .setLink("http://www.feedforall.com/schools.htm")
                    .setCategory(category)
                    .setComments("http://www.feedforall.com/forum")
                    .setPubDate(new Date());

            itemRepository.save(item);

            item = (new Item())
                    .setTitle("RSS Solutions for Computer Service Companies")
                    .setDescription("FeedForAll helps Computer Service Companies " 
                            + " communicate with clients about cyber security " 
                            + " and related issues.")
                    .setLink("http://www.feedforall.com/computer-service.htm")
                    .setCategory(category)
                    .setComments("http://www.feedforall.com/forum")
                    .setPubDate(new Date());

            itemRepository.save(item);
            
            item = (new Item())
                    .setTitle("RSS Solutions for Governments")
                    .setDescription("FeedForAll helps Governments communicate "
                         +"with the general public about positions on various issues, " 
                         + "and keep the community aware of changes in important legislative issues. " 
                         + "<b><i><br> </b></i><br> RSS uses Include:<br> <i>" 
                         + "Legislative Calendar<br> Votes<br> Bulletins</i>"
                    ).setLink("http://www.feedforall.com/government.htm")
                    .setCategory(category)
                    .setComments("http://www.feedforall.com/forum")
                    .setPubDate(new Date());

            itemRepository.save(item);
            
            item = (new Item())
                    .setTitle("RSS Solutions for Politicians")
                    .setDescription("FeedForAll helps Politicians communicate " 
                         + "with the general public about positions on various issues."
                    ).setLink("http://www.feedforall.com/computer-service.htm")
                    .setCategory(category)
                    .setComments("http://www.feedforall.com/forum")
                    .setPubDate(new Date());

            itemRepository.save(item);
            
            item = (new Item())
                    .setTitle("RSS Solutions for Meteorologists")
                    .setDescription("FeedForAll helps Meteorologists communicate with "
                         + "the general public about storm warnings and weather alerts."
                    ).setLink("http://www.feedforall.com/computer-service.htm")
                    .setCategory(category)
                    .setComments("http://www.feedforall.com/forum")
                    .setPubDate(new Date());

            itemRepository.save(item);
        }
    }
}
