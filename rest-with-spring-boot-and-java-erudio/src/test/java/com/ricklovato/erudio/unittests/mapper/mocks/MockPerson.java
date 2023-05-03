package com.ricklovato.erudio.unittests.mapper.mocks;

import com.ricklovato.erudio.data.vo.v1.PersonVO;
import com.ricklovato.erudio.model.Person;

import java.util.ArrayList;
import java.util.List;

// Classe responsável por criar objetos falsos para testes unitários
public class MockPerson {

    // Cria um objeto Person com valores fictícios
    public Person mockEntity() {
        return mockEntity(0); // Chama o método mockEntity passando o número 0 como parâmetro
    }

    // Cria um objeto PersonVO com valores fictícios
    public PersonVO mockVO() {
        return mockVO(0); // Chama o método mockVO passando o número 0 como parâmetro
    }

    // Cria uma lista de objetos Person com valores fictícios
    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<Person>(); // Cria uma nova lista vazia
        for (int i = 0; i < 14; i++) { // Itera 14 vezes
            persons.add(mockEntity(i)); // Adiciona um novo objeto Person à lista
        }
        return persons; // Retorna a lista de objetos Person
    }

    // Cria uma lista de objetos PersonVO com valores fictícios
    public List<PersonVO> mockVOList() {
        List<PersonVO> persons = new ArrayList<>(); // Cria uma nova lista vazia
        for (int i = 0; i < 14; i++) { // Itera 14 vezes
            persons.add(mockVO(i)); // Adiciona um novo objeto PersonVO à lista
        }
        return persons; // Retorna a lista de objetos PersonVO
    }

    // Cria um objeto Person com valores fictícios com base no número passado como parâmetro
    public Person mockEntity(Integer number) {
        Person person = new Person(); // Cria um objeto Person
        person.setAddress("Address Test" + number); // Define o endereço com base no número passado como parâmetro
        person.setFirstName("First Name Test" + number); // Define o primeiro nome com base no número passado como parâmetro
        person.setGender(((number % 2) == 0) ? "Male" : "Female"); // Define o gênero com base no número passado como parâmetro
        person.setId(number.longValue()); // Define o ID com base no número passado como parâmetro
        person.setLastName("Last Name Test" + number); // Define o sobrenome com base no número passado como parâmetro
        return person; // Retorna o objeto Person
    }

    public PersonVO mockVO(Integer number) {
        // Instancia um novo objeto PersonVO.
        PersonVO person = new PersonVO();
// Define o endereço do objeto com uma string concatenada com o parâmetro 'number'.
        person.setAddress("Address Test" + number);
// Define o primeiro nome do objeto com uma string concatenada com o parâmetro 'number'.
        person.setFirstName("First Name Test" + number);
// Define o gênero do objeto com base no parâmetro 'number'.
// Se 'number' for par, o gênero será "Male", caso contrário, será "Female".
        person.setGender(((number % 2) == 0) ? "Male" : "Female");
// Define a chave do objeto como o valor do parâmetro 'number' convertido para long.
        person.setKey(number.longValue());
// Define o sobrenome do objeto com uma string concatenada com o parâmetro 'number'.
        person.setLastName("Last Name Test" + number);
// Retorna o objeto PersonVO criado.
        return person;

    }

}
