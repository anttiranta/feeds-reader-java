package com.antti.task.repository;

import com.antti.task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByName(String name);

    /*@Query("select u from User u where u.admin = true")
    User findAdmin();

    @Modifying
    @Query("update User u set u.password = ?1 where u.admin = true")
    void updateAdminPassword(String password);

    @Modifying
    @Query("update User u set u.name = ?1 where u.admin = true")
    void updateAdminName(String name);

    @Query("select distinct u from User u left join fetch u.roles")
    List<User> findAllFetchRoles();*/
}
