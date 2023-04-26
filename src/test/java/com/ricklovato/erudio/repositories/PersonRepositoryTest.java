package com.ricklovato.erudio.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ricklovato.erudio.configs.TestConfigs;
import com.ricklovato.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.ricklovato.erudio.integrationtests.vo.PersonVO;
import com.ricklovato.erudio.integrationtests.vo.wrappers.WrapperPersonVO;
import com.ricklovato.erudio.model.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    PersonRepository personRepository;

    private static Person person;

    @BeforeAll
    public static void setup(){
        person = new Person();

    }
@Test
@Order(0)
    public void testFindByName() throws JsonProcessingException,JsonProcessingException {

        Pageable pageable = PageRequest.of(0,6, Sort.by(Sort.Direction.ASC,"firstName"));
        person = personRepository.findPersonsByName("ayr",pageable).getContent().get(0);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.getEnabled());

        assertEquals(1,person.getId());

        assertEquals("Ayrthon", person.getFirstName());
        assertEquals("Senna", person.getLastName());
        assertEquals("São Paulo", person.getAddress());
        assertEquals("Male", person.getGender());

    }
    @Test
    @Order(1)
    public void disablePerson() throws JsonProcessingException,JsonProcessingException {
        personRepository.disablePerson(person.getId());
        Pageable pageable = PageRequest.of(0,6, Sort.by(Sort.Direction.ASC,"firstName"));
        person = personRepository.findPersonsByName("ayr",pageable).getContent().get(0);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertFalse(person.getEnabled());

        assertEquals(1,person.getId());

        assertEquals("Ayrthon", person.getFirstName());
        assertEquals("Senna", person.getLastName());
        assertEquals("São Paulo", person.getAddress());
        assertEquals("Male", person.getGender());

    }

}
