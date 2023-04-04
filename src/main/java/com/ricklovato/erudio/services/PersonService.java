package com.ricklovato.erudio.services;


import com.ricklovato.erudio.data.vo.v1.PersonVO;
import com.ricklovato.erudio.data.vo.v2.PersonVOV2;
import com.ricklovato.erudio.mapper.DozerMapper;
import com.ricklovato.erudio.mapper.custom.PersonMapper;
import com.ricklovato.erudio.model.Person;
import com.ricklovato.erudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service //Spring Boot encara como um objeto que vai ser injetado em runtime em outras classes
public class PersonService {

    private Logger logger = Logger.getLogger(PersonService.class.getName());
    @Autowired
    PersonRepository repository;
    @Autowired
    PersonMapper mapper;

    public List<PersonVO> findAll() {

        logger.info("Finding all people");
        return DozerMapper.parseListObject(repository.findAll(),PersonVO.class);
    }


    public PersonVO findById(Long id) {
        logger.info("Finding one PersonVO");

        var entity = repository.findById(id).orElseThrow(() -> new RuntimeException("No records found for this ID!"));
        return DozerMapper.parseObject(entity,PersonVO.class);
    }

    public PersonVO create(PersonVO person) {
        logger.info("Creating one PersonVO");
        var entity = DozerMapper.parseObject(person, Person.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        return vo;
    }
    public PersonVOV2 createV2(PersonVOV2 person) {
        logger.info("Creating one person with V2");
        var entity = mapper.convertVOtoEntity(person);
        var vo = mapper.convertEntityToVo(repository.save(entity));
        return vo;
    }
    public PersonVO update(PersonVO person) {
        logger.info("Updating one PersonVO");
        var entity = repository.findById(person.getId()).orElseThrow(() -> new RuntimeException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting person");
        var entity = repository.findById(id).orElseThrow(() -> new RuntimeException("No records found for this ID!"));
        repository.delete(entity);

    }
}
