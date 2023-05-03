package com.ricklovato.erudio.services;

import com.ricklovato.erudio.data.vo.v1.BookVO;
import com.ricklovato.erudio.exceptions.RequiredObjectIsNullException;
import com.ricklovato.erudio.model.Book;

import com.ricklovato.erudio.repositories.BookRepository;
import com.ricklovato.erudio.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Indica que a classe de teste manterá uma única instância para todos os testes,
// permitindo compartilhar estados entre eles
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

// Indica a utilização do MockitoExtension para executar os testes com o Mockito
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    // Criação de um objeto MockBook para utilização nos testes
    MockBook input;

    // Injeção de dependências utilizando o InjectMocks do Mockito
    @InjectMocks
    private BookService service;

    // Criação de um objeto mock para o repositório de pessoas
    @Mock
    BookRepository repository;

    // Método que é executado antes de cada teste
    @BeforeEach
    void setUpMocks() {
        input = new MockBook();
        // Inicialização dos objetos mock
        MockitoAnnotations.openMocks(this);
    }

    // Teste para o método findAll, ainda não implementado
  /*  @Test
    void findAll() {

        List<Book> list = input.mockEntityList();

        when(repository.findAll()).thenReturn(list);
        var books = service.findAll();

        // Verificação dos resultados obtidos
        assertNotNull(books);
        assertEquals(14,books.size());

        var bookOne = books.get(1);

        assertNotNull(bookOne);
        assertNotNull(bookOne.getKey());
        assertNotNull(bookOne.getLinks());
        assertTrue(bookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Test1", bookOne.getAuthor());
        assertNotNull(bookOne.getLaunchDate());
        assertEquals(25D, bookOne.getPrice());
        assertEquals("Title Test1", bookOne.getTitle());

        var bookFour = books.get(4);

        assertNotNull(bookFour);
        assertNotNull(bookFour.getKey());
        assertNotNull(bookFour.getLinks());
        assertTrue(bookFour.toString().contains("links: [</api/book/v1/4>;rel=\"self\"]"));
        assertEquals("Author Test4", bookFour.getAuthor());
        assertNotNull(bookFour.getLaunchDate());
        assertEquals(25D, bookFour.getPrice());
        assertEquals("Title Test4", bookFour.getTitle());

        var bookSeven = books.get(7);

        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getKey());
        assertNotNull(bookSeven.getLinks());
        assertTrue(bookSeven.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]"));
        assertEquals("Author Test7", bookSeven.getAuthor());
        assertNotNull(bookSeven.getLaunchDate());
        assertEquals(25D, bookSeven.getPrice());
        assertEquals("Title Test7", bookSeven.getTitle());
    }
*/
    // Teste para o método findById
    @Test
    void findById() {
        // Criação de uma entidade de pessoa com ID 1
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        // Configuração do comportamento do repositório para quando o método findById é chamado com o ID 1
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        // Execução do método a ser testado
        var result = service.findById(1L);

        // Verificação dos resultados obtidos
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Test1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals(25D, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
    }

    // Teste para o método create
    @Test
    void create() {
        // Criação de uma entidade de pessoa com ID 1
        Book entity = input.mockEntity(1);
        entity.setId(1L);
        Book persisted = entity;
        persisted.setId(1L);

        // Criação de um objeto VO de pessoa com ID 1
        BookVO vo = input.mockVO(1);
        vo.setKey(1L);


        doReturn(persisted).when(repository).save(Mockito.any(Book.class));


        var result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Test1", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals(25D, result.getPrice());
        assertEquals("Title Test1", result.getTitle());
    }
@Test
    void createWithNullBook() {
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
        Book entity = input.mockEntity(2);
        entity.setId(1L);
        Book persisted = entity;
        persisted.setId(1L);

        // Criação de um objeto VO de pessoa com ID 1
        BookVO vo = input.mockVO(2);
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

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author Test2", result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertEquals(25D, result.getPrice());
        assertEquals("Title Test2", result.getTitle());
    }

    @Test
    void updateWithNullBook() {
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
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        // Configuração do comportamento do repositório para quando o método findById é chamado com o ID 1
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        // Execução do método a ser testado
         service.delete(1L);
    }
}