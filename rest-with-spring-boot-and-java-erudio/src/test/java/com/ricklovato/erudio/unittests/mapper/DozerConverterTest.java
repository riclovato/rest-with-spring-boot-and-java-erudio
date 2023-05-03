package com.ricklovato.erudio.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.ricklovato.erudio.data.vo.v1.PersonVO;
import com.ricklovato.erudio.mapper.DozerMapper;
import com.ricklovato.erudio.model.Person;
import com.ricklovato.erudio.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DozerConverterTest {

    //Criação de objeto que será utilizado nos testes
    MockPerson inputObject;

    //Método que será executado antes de cada teste e que instancia o objeto inputObject
    @BeforeEach
    public void setUp() {
        inputObject = new MockPerson();
    }

    //Teste para verificar se a conversão de uma entidade para um objeto Value Object (VO) está funcionando corretamente
    @Test
    public void parseEntityToVOTest() {
        PersonVO output = DozerMapper.parseObject(inputObject.mockEntity(), PersonVO.class);
        assertEquals(Long.valueOf(0L), output.getKey());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals("Address Test0", output.getAddress());
        assertEquals("Male", output.getGender());
    }

    //Teste para verificar se a conversão de uma lista de entidades para uma lista de objetos Value Object (VO) está funcionando corretamente
    @Test
    public void parseEntityListToVOListTest() {
        List<PersonVO> outputList = DozerMapper.parseListObject(inputObject.mockEntityList(), PersonVO.class);
        PersonVO outputZero = outputList.get(0);

        assertEquals(Long.valueOf(0L), outputZero.getKey());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals("Address Test0", outputZero.getAddress());
        assertEquals("Male", outputZero.getGender());

        PersonVO outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getKey());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals("Address Test7", outputSeven.getAddress());
        assertEquals("Female", outputSeven.getGender());

        PersonVO outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getKey());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals("Address Test12", outputTwelve.getAddress());
        assertEquals("Male", outputTwelve.getGender());
    }

    //Teste para verificar se a conversão de um objeto Value Object (VO) para uma entidade está funcionando corretamente
    @Test
    public void parseVOToEntityTest() {
        Person output = DozerMapper.parseObject(inputObject.mockVO(), Person.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals("Address Test0", output.getAddress());
        assertEquals("Male", output.getGender());
    }

    //Teste para verificar se a conversão de uma lista de objetos Value Object (VO) para uma lista de entidades está funcionando corretamente
    @Test
    public void parserVOListToEntityListTest() {
        List<Person> outputList = DozerMapper.parseListObject(inputObject.mockVOList(), Person.class);
        Person outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals("Address Test0", outputZero.getAddress());
        assertEquals("Male", outputZero.getGender());
        
        Person outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals("Address Test7", outputSeven.getAddress());
        assertEquals("Female", outputSeven.getGender());
        
        Person outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals("Address Test12", outputTwelve.getAddress());
        assertEquals("Male", outputTwelve.getGender());
    }
}
