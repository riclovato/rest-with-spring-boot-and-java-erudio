package com.ricklovato.erudio.services;


import com.ricklovato.erudio.controllers.BookController;
import com.ricklovato.erudio.data.vo.v1.BookVO;

import com.ricklovato.erudio.exceptions.RequiredObjectIsNullException;
import com.ricklovato.erudio.mapper.DozerMapper;
import com.ricklovato.erudio.model.Book;
import com.ricklovato.erudio.repositories.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service // Spring Boot encara como um objeto que vai ser injetado em runtime em outras classes
public class BookService {

    // cria um objeto Logger para registrar mensagens de log
    private Logger logger = Logger.getLogger(BookService.class.getName());

    // injeta um objeto BookRepository para fazer a comunicação com o banco de dados
    @Autowired
    BookRepository repository;

    // injeta um objeto BookMapper para fazer a conversão de objetos da entidade Book e objetos BookVO


    // método que busca todos os registros de Book no banco de dados
    public List<BookVO> findAll() {
        logger.info("Finding all book");
        // busca todos os registros de Book no banco de dados e converte cada um para um objeto BookVO
        var books = DozerMapper.parseListObject(repository.findAll(),BookVO.class);
        // adiciona links HATEOAS que permitem acessar cada registro individualmente
        books.stream().forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
        return books;
    }

    // método que busca um registro específico de Book no banco de dados a partir do seu ID
    public BookVO findById(Long id) {
        logger.info("Finding one BookVO");
        // busca um registro específico de Book no banco de dados a partir do seu ID
        var entity = repository.findById(id).orElseThrow(() -> new RuntimeException("No records found for this ID!"));
        // converte o registro encontrado para um objeto BookVO
        BookVO vo = DozerMapper.parseObject(entity,BookVO.class);
        // adiciona links HATEOAS que permitem acessar o registro
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    // método que cria um registro de Book a partir de um objeto BookVO
    public BookVO create(BookVO book) {
        if(book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one BookVO");
        // converte um objeto BookVO para um objeto da entidade Book
        var entity = DozerMapper.parseObject(book, Book.class);
        // salva o novo registro no banco de dados e converte para um objeto BookVO
        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        // adiciona links HATEOAS que permitem acessar o registro criado
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }
    public BookVO update(BookVO book) {
        if(book == null) throw new RequiredObjectIsNullException();

        // Busca no repositório a pessoa a ser atualizada pelo id recebido.
// Caso não encontre, lança uma exceção.
        var entity = repository.findById(book.getKey()).orElseThrow(() -> new RuntimeException("No records found for this ID!"));

// Atualiza as propriedades da pessoa encontrada com os valores recebidos no objeto BookVO.
        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

// Salva a pessoa atualizada no repositório e converte o resultado para o tipo BookVO.
        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);

// Adiciona um link HATEOAS para o endpoint que retorna a pessoa atualizada.
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());

// Retorna o objeto BookVO com a pessoa atualizada.
        return vo;

    }

    public void delete(Long id) {
        // Busca no repositório a pessoa a ser excluída pelo ‘id’ recebido.
        // Caso não encontre, lança uma exceção.
        var entity = repository.findById(id).orElseThrow(() -> new RuntimeException("No records found for this ID!"));

        // Remove a pessoa do repositório.
        repository.delete(entity);

    }
}
