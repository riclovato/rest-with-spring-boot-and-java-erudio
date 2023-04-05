package com.ricklovato.erudio.controllers;


import com.ricklovato.erudio.data.vo.v1.PersonVO;
import com.ricklovato.erudio.data.vo.v2.PersonVOV2;
import com.ricklovato.erudio.services.PersonService;
import com.ricklovato.erudio.util.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {
    @Autowired
    private PersonService service;
    //private PersonVOServices service = new PersonVOService();
    private final AtomicLong counter = new AtomicLong();

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
    public PersonVO findById(@PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
    public List<PersonVO> findAll(){

        return service.findAll();
    }
    @PostMapping(
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
    public PersonVO create(@RequestBody PersonVO person){
        return service.create(person);
    }

    @PostMapping(value = "/v2",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
    public PersonVOV2 createV2(@RequestBody PersonVOV2 person){
        return service.createV2(person);
    }

    @PutMapping(
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,MediaType.APPLICATION_YML})
    public PersonVO update(@RequestBody PersonVO person){
        return service.update(person);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
