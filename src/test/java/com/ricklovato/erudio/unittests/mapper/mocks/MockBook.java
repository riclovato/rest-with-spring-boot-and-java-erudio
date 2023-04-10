package com.ricklovato.erudio.unittests.mapper.mocks;

import com.ricklovato.erudio.data.vo.v1.BookVO;
import com.ricklovato.erudio.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Classe responsável por criar objetos falsos para testes unitários
public class MockBook {

    // Cria um objeto Book com valores fictícios
    public Book mockEntity() {
        return mockEntity(0); // Chama o método mockEntity passando o número 0 como parâmetro
    }

    // Cria um objeto BookVO com valores fictícios
    public BookVO mockVO() {
        return mockVO(0); // Chama o método mockVO passando o número 0 como parâmetro
    }

    // Cria uma lista de objetos Book com valores fictícios
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>(); // Cria uma lista vazia
        for (int i = 0; i < 14; i++) { // Itera 14 vezes
            books.add(mockEntity(i)); // Adiciona um novo objeto Book à lista
        }
        return books; // Retorna a lista de objetos Book
    }

    // Cria uma lista de objetos BookVO com valores fictícios
    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>(); // Cria uma lista vazia
        for (int i = 0; i < 14; i++) { // Itera 14 vezes
            books.add(mockVO(i)); // Adiciona um novo objeto BookVO à lista
        }
        return books; // Retorna a lista de objetos BookVO
    }

    // Cria um objeto Book com valores fictícios com base no número passado como parâmetro
    public Book mockEntity(Integer number) {
        Book book = new Book(); // Cria um objeto Book
        book.setId(number.longValue()); // Define o ID com base no número passado como parâmetro
        book.setAuthor("Author Test" + number); // Define o endereço com base no número passado como parâmetro
        book.setLaunchDate(new Date()); // Define o primeiro nome com base no número passado como parâmetro
        book.setPrice(25D); // Define o gênero com base no número passado como parâmetro
        book.setTitle("Title Test" + number);
        return book; // Retorna o objeto Book
    }

    public BookVO mockVO(Integer number) {
        BookVO book = new BookVO(); // Cria um objeto Book
        book.setKey(number.longValue()); // Define o ID com base no número passado como parâmetro
        book.setAuthor("Author Test" + number); // Define o endereço com base no número passado como parâmetro
        book.setLaunchDate(new Date()); // Define o primeiro nome com base no número passado como parâmetro
        book.setPrice(25D); // Define o gênero com base no número passado como parâmetro
        book.setTitle("Title Test" + number);
        return book; // Retorna o objeto Book

    }

}
