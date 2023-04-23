package com.ricklovato.erudio.repositories;

import com.ricklovato.erudio.model.Person;
import com.ricklovato.erudio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    //update person p onde p.id = ao id passado
    @Modifying
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id")
    void disablePerson(@Param("id") Long id);
}
