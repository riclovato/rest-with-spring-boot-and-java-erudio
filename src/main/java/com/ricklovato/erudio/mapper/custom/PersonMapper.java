package com.ricklovato.erudio.mapper.custom;

import com.ricklovato.erudio.data.vo.v1.PersonVO;
import com.ricklovato.erudio.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {


    public Person convertVOtoEntity(PersonVO person){
        Person entity = new Person();
        entity.setId(person.getKey());
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
       // entity.setBirthDay(new Date());
        return entity;
    }
}
