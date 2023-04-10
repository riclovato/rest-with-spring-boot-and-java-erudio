package com.ricklovato.erudio.unittests.mapper;

import com.ricklovato.erudio.data.vo.v1.BookVO;
import com.ricklovato.erudio.mapper.DozerMapper;
import com.ricklovato.erudio.model.Book;
import com.ricklovato.erudio.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DozerConverterBookTest {

    //Criação de objeto que será utilizado nos testes
    MockBook inputObject;

    //Método que será executado antes de cada teste e que instancia o objeto inputObject
    @BeforeEach
    public void setUp() {
        inputObject = new MockBook();
    }

    //Teste para verificar se a conversão de uma entidade para um objeto Value Object (VO) está funcionando corretamente
    @Test
    public void parseEntityToVOTest() {
        BookVO output = DozerMapper.parseObject(inputObject.mockEntity(), BookVO.class);
        assertEquals(Long.valueOf(0L), output.getKey());
        assertEquals("Author Test0", output.getAuthor());
        assertNotNull(output.getLaunchDate());
        assertEquals(25D, output.getPrice());
        assertEquals("Title Test0", output.getTitle());
    }

    //Teste para verificar se a conversão de uma lista de entidades para uma lista de objetos Value Object (VO) está funcionando corretamente
    @Test
    public void parseEntityListToVOListTest() {
        List<BookVO> outputList = DozerMapper.parseListObject(inputObject.mockEntityList(), BookVO.class);
        BookVO outputZero = outputList.get(0);

        assertEquals(Long.valueOf(0L), outputZero.getKey());
        assertEquals("Author Test0", outputZero.getAuthor());
        assertNotNull(outputZero.getLaunchDate());
        assertEquals(25D, outputZero.getPrice());
        assertEquals("Title Test0", outputZero.getTitle());

        BookVO outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getKey());
        assertEquals("Author Test7", outputSeven.getAuthor());
        assertNotNull(outputSeven.getLaunchDate());
        assertEquals(25D, outputSeven.getPrice());
        assertEquals("Title Test7", outputSeven.getTitle());

        BookVO outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getKey());
        assertEquals("Author Test12", outputTwelve.getAuthor());
        assertNotNull(outputTwelve.getLaunchDate());
        assertEquals(25D, outputTwelve.getPrice());
        assertEquals("Title Test12", outputTwelve.getTitle());
    }

    //Teste para verificar se a conversão de um objeto Value Object (VO) para uma entidade está funcionando corretamente
    @Test
    public void parseVOToEntityTest() {
        Book output = DozerMapper.parseObject(inputObject.mockVO(), Book.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Author Test0", output.getAuthor());
        assertNotNull(output.getLaunchDate());
        assertEquals(25D, output.getPrice());
        assertEquals("Title Test0", output.getTitle());
    }

    //Teste para verificar se a conversão de uma lista de objetos Value Object (VO) para uma lista de entidades está funcionando corretamente
    @Test
    public void parserVOListToEntityListTest() {
        List<Book> outputList = DozerMapper.parseListObject(inputObject.mockVOList(), Book.class);
        Book outputZero = outputList.get(0);
        
        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Author Test0", outputZero.getAuthor());
        assertNotNull(outputZero.getLaunchDate());
        assertEquals(25D, outputZero.getPrice());
        assertEquals("Title Test0", outputZero.getTitle());
        
        Book outputSeven = outputList.get(7);
        
        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Author Test7", outputSeven.getAuthor());
        assertNotNull(outputSeven.getLaunchDate());
        assertEquals(25D, outputSeven.getPrice());
        assertEquals("Title Test7", outputSeven.getTitle());
        
        Book outputTwelve = outputList.get(12);
        
        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Author Test12", outputTwelve.getAuthor());
        assertNotNull(outputTwelve.getLaunchDate());
        assertEquals(25D, outputTwelve.getPrice());
        assertEquals("Title Test12", outputTwelve.getTitle());
    }
}
