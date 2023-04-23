package com.ricklovato.erudio.services;


import com.ricklovato.erudio.controllers.PersonController;
import com.ricklovato.erudio.data.vo.v1.PersonVO;
import com.ricklovato.erudio.exceptions.RequiredObjectIsNullException;
import com.ricklovato.erudio.mapper.DozerMapper;
import com.ricklovato.erudio.mapper.custom.PersonMapper;
import com.ricklovato.erudio.model.Person;
import com.ricklovato.erudio.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
@Service // Spring Boot encara como um objeto que vai ser injetado em runtime em outras classes
public class PersonService {

    // cria um objeto Logger para registrar mensagens de log
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    // injeta um objeto PersonRepository para fazer a comunicação com o banco de dados
    @Autowired
    PersonRepository repository;

    // injeta um objeto PersonMapper para fazer a conversão de objetos da entidade Person e objetos PersonVO
    @Autowired
    PersonMapper mapper;

    // método que busca todos os registros de Person no banco de dados
    public Page<PersonVO> findAll(Pageable pageable) {
        logger.info("Finding all people");
        // busca todos os registros de Person no banco de dados e converte cada um para um objeto PersonVO
        var personPage = repository.findAll(pageable);
        // cada p é uma entidade do tipo Person
        var personVOPage = personPage.map(p -> DozerMapper.parseObject(p,PersonVO.class));
        //adiciona os links hateoas
        personVOPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));

        return personVOPage;
    }

    // método que busca um registro específico de Person no banco de dados a partir do seu ID
    public PersonVO findById(Long id) {
        logger.info("Finding one PersonVO");
        // busca um registro específico de Person no banco de dados a partir do seu ID
        var entity = repository.findById(id).orElseThrow(() -> new RuntimeException("No records found for this ID!"));
        // converte o registro encontrado para um objeto PersonVO
        PersonVO vo = DozerMapper.parseObject(entity,PersonVO.class);
        // adiciona links HATEOAS que permitem acessar o registro
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    // método que cria um registro de Person a partir de um objeto PersonVO
    public PersonVO create(PersonVO person) {
        if(person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one PersonVO");
        // converte um objeto PersonVO para um objeto da entidade Person
        var entity = DozerMapper.parseObject(person, Person.class);
        // salva o novo registro no banco de dados e converte para um objeto PersonVO
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        // adiciona links HATEOAS que permitem acessar o registro criado
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }
    public PersonVO update(PersonVO person) {
        if(person == null) throw new RequiredObjectIsNullException();

        // Busca no repositório a pessoa a ser atualizada pelo id recebido.
// Caso não encontre, lança uma exceção.
        var entity = repository.findById(person.getKey()).orElseThrow(() -> new RuntimeException("No records found for this ID!"));

// Atualiza as propriedades da pessoa encontrada com os valores recebidos no objeto PersonVO.
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

// Salva a pessoa atualizada no repositório e converte o resultado para o tipo PersonVO.
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);

// Adiciona um link HATEOAS para o endpoint que retorna a pessoa atualizada.
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());

// Retorna o objeto PersonVO com a pessoa atualizada.
        return vo;

    }

    @Transactional
    public PersonVO disablePerson(Long id) {
        logger.info("Disabling one person!");
        repository.disablePerson(id);
        var entity = repository.findById(id).orElseThrow(() -> new RuntimeException("No records found for this ID!"));
        PersonVO vo = DozerMapper.parseObject(entity,PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
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
