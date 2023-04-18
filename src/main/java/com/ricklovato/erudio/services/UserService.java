package com.ricklovato.erudio.services;


import com.ricklovato.erudio.controllers.PersonController;
import com.ricklovato.erudio.data.vo.v1.PersonVO;
import com.ricklovato.erudio.exceptions.RequiredObjectIsNullException;
import com.ricklovato.erudio.mapper.DozerMapper;
import com.ricklovato.erudio.mapper.custom.PersonMapper;
import com.ricklovato.erudio.model.Person;
import com.ricklovato.erudio.repositories.PersonRepository;
import com.ricklovato.erudio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service // Spring Boot encara como um objeto que vai ser injetado em runtime em outras classes
public class UserService implements UserDetailsService {

    // cria um objeto Logger para registrar mensagens de log
    private Logger logger = Logger.getLogger(UserService.class.getName());

    // injeta um objeto PersonRepository para fazer a comunicação com o banco de dados
    @Autowired
    UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding one user by name " + username +"!");
        var user = repository.findByUserName(username);
        if(user != null){
            return user;
        }else {
            throw new UsernameNotFoundException(("Username "+ username + " not found!"));
        }
    }
}
