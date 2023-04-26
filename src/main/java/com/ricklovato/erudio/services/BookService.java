package com.ricklovato.erudio.services;


import com.ricklovato.erudio.controllers.BookController;
import com.ricklovato.erudio.data.vo.v1.BookVO;

import com.ricklovato.erudio.exceptions.RequiredObjectIsNullException;
import com.ricklovato.erudio.mapper.DozerMapper;
import com.ricklovato.erudio.model.Book;
import com.ricklovato.erudio.repositories.BookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

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

    @Autowired
    PagedResourcesAssembler<BookVO> assembler;




    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {
        logger.info("Finding all book");
        var bookPage = repository.findAll(pageable);
        var bookVOPage = bookPage.map(b -> DozerMapper.parseObject(b,BookVO.class));

        bookVOPage.map(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(),pageable.getPageSize(),
                "asc")).withSelfRel();
        return assembler.toModel(bookVOPage,link);
    }


    public BookVO findById(Long id) {
        logger.info("Finding one BookVO");

        var entity = repository.findById(id).orElseThrow(() -> new RuntimeException("No records found for this ID!"));

        BookVO vo = DozerMapper.parseObject(entity,BookVO.class);
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
