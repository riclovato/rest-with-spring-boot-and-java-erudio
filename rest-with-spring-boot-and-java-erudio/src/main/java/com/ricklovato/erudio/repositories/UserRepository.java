package com.ricklovato.erudio.repositories;

import com.ricklovato.erudio.model.Person;
import com.ricklovato.erudio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userName =:userName")
    User findByUserName(@Param("userName") String userName);
}
