package com.ricklovato.erudio.services;

import com.ricklovato.erudio.data.vo.v1.PersonVO;
import com.ricklovato.erudio.exceptions.RequiredObjectIsNullException;
import com.ricklovato.erudio.model.Person;
import com.ricklovato.erudio.repositories.PersonRepository;
import com.ricklovato.erudio.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

// Indica que a classe de teste manterá uma única instância para todos os testes,
// permitindo compartilhar estados entre eles
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

// Indica a utilização do MockitoExtension para executar os testes com o Mockito
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    // Criação de um objeto MockPerson para utilização nos testes
    MockPerson input;

    // Injeção de dependências utilizando o InjectMocks do Mockito
    @InjectMocks
    private PersonService service;

    // Criação de um objeto mock para o repositório de pessoas
    @Mock
    PersonRepository repository;

    // Método que é executado antes de cada teste
    @BeforeEach
    void setUpMocks() {
        input = new MockPerson();
        // Inicialização dos objetos mock
        MockitoAnnotations.openMocks(this);
    }

    // Teste para o método findAll, ainda não implementado


    // Teste para o método findById
    @Test
    void findById() {
        // Criação de uma entidade de pessoa com ID 1
        Person entity = input.mockEntity(1);
        entity.setId(1L);

        // Configuração do comportamento do repositório para quando o método findById é chamado com o ID 1
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        // Execução do método a ser testado
        var result = service.findById(1L);

        // Verificação dos resultados obtidos
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    // Teste para o método create
    @Test
    void create() {
        // Criação de uma entidade de pessoa com ID 1
        Person entity = input.mockEntity(2);
        Person persisted = entity;
        persisted.setId(1L);

        // Criação de um objeto VO de pessoa com ID 1
        PersonVO vo = input.mockVO(2);
        vo.setKey(1L);

        // Configuração do comportamento do repositório para quando o método save é chamado com a entidade
        when(repository.save(entity)).thenReturn(persisted);

        // Execução do método a ser testado
        var result = service.create(vo);

        // Verificação dos resultados obtidos
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Address Test2", result.getAddress());
        assertEquals("First Name Test2", result.getFirstName());
        assertEquals("Last Name Test2", result.getLastName());
        assertEquals("Male", result.getGender());
    }
@Test
    void createWithNullPerson() {
     Exception exception = assertThrows(RequiredObjectIsNullException.class,() -> {
         service.create(null);
     });
     String expectedMessage ="It is not allowed to persist a null object!";
     String actualMessage = exception.getMessage();
     assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void update() {

        // Criação de uma entidade de pessoa com ID 1
        Person entity = input.mockEntity(2);
        entity.setId(1L);
        Person persisted = entity;
        persisted.setId(1L);

        // Criação de um objeto VO de pessoa com ID 1
        PersonVO vo = input.mockVO(2);
        vo.setKey(1L);

        // Configuração do comportamento do repositório para quando o método save é chamado com a entidade
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        // Execução do método a ser testado
        var result = service.update(vo);

        // Verificação dos resultados obtidos
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Address Test2", result.getAddress());
        assertEquals("First Name Test2", result.getFirstName());
        assertEquals("Last Name Test2", result.getLastName());
        assertEquals("Male", result.getGender());
    }

    @Test
    void updateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,() -> {
            service.update(null);
        });
        String expectedMessage ="It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void delete() {
        // Criação de uma entidade de pessoa com ID 1
        Person entity = input.mockEntity(1);
        entity.setId(1L);

        // Configuração do comportamento do repositório para quando o método findById é chamado com o ID 1
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        // Execução do método a ser testado
         service.delete(1L);
    }
}