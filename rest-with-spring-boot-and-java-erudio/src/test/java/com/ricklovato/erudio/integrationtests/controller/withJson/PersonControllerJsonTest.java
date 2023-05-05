package com.ricklovato.erudio.integrationtests.controller.withJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricklovato.erudio.configs.TestConfigs;
import com.ricklovato.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.ricklovato.erudio.integrationtests.vo.AccountCredentialsVO;
import com.ricklovato.erudio.integrationtests.vo.PersonVO;
import com.ricklovato.erudio.integrationtests.vo.TokenVO;
import com.ricklovato.erudio.integrationtests.vo.wrappers.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest{


	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static PersonVO person;
	@BeforeAll
	public static void setUp(){
		objectMapper = new ObjectMapper();
		//desabilita falhas quando as propriedades forem desconhecidas
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonVO();
	}
	@Order(0)
	@Test
	public void authorization() throws JsonProcessingException,JsonProcessingException {

		AccountCredentialsVO user = new AccountCredentialsVO("leandro","admin123");

		var accessToken =
				given()
						.basePath("auth/signin")
						.port(TestConfigs.SERVER_PORT)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.body(user)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(TokenVO.class)
						.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();


	}
	@Order(1)
	@Test
	public void testCreate() throws JsonProcessingException,JsonProcessingException {
		mockPerson();
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.body(person)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonVO createdPerson = objectMapper.readValue(content,PersonVO.class);
		person = createdPerson;
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());

		assertTrue(createdPerson.getId()>0);

		assertEquals("Richard",createdPerson.getFirstName());
		assertEquals("Stallman",createdPerson.getLastName());
		assertEquals("New York City - New York, US",createdPerson.getAddress());
		assertEquals("Male",createdPerson.getGender());

	}
	@Order(2)
	@Test
	public void testUpdate() throws JsonProcessingException,JsonProcessingException {
		person.setLastName("MorningStar");
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.body(person)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonVO createdPerson = objectMapper.readValue(content,PersonVO.class);
		person = createdPerson;
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());

		assertEquals(person.getId(),createdPerson.getId());

		assertEquals("Richard",createdPerson.getFirstName());
		assertEquals("MorningStar",createdPerson.getLastName());
		assertEquals("New York City - New York, US",createdPerson.getAddress());
		assertEquals("Male",createdPerson.getGender());
	}
	@Order(3)
	@Test
	public void testDisablePersonById() throws JsonProcessingException {
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
						.pathParam("id",person.getId())
						.when()
						.patch("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonVO createdPerson = objectMapper.readValue(content,PersonVO.class);
		person = createdPerson;
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		assertFalse(createdPerson.getEnabled());

		assertEquals(person.getId(),createdPerson.getId());

		assertEquals("Richard",createdPerson.getFirstName());
		assertEquals("MorningStar",createdPerson.getLastName());
		assertEquals("New York City - New York, US",createdPerson.getAddress());
		assertEquals("Male",createdPerson.getGender());
	}
	@Order(4)
	@Test
	public void testFindById() throws JsonProcessingException {
		mockPerson();
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
						.pathParam("id",person.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonVO createdPerson = objectMapper.readValue(content,PersonVO.class);
		person = createdPerson;
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		assertFalse(createdPerson.getEnabled());

		assertEquals(person.getId(),createdPerson.getId());

		assertEquals("Richard",createdPerson.getFirstName());
		assertEquals("MorningStar",createdPerson.getLastName());
		assertEquals("New York City - New York, US",createdPerson.getAddress());
		assertEquals("Male",createdPerson.getGender());
	}

	@Order(5)
	@Test
	public void testDelete() throws JsonProcessingException {

				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.pathParam("id",person.getId())
						.when()
						.delete("{id}")
						.then()
						.statusCode(204);
	}

	@Order(6)
	@Test
	public void testFindAll() throws JsonProcessingException,JsonProcessingException {
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.queryParams("page",3,"size",12,"direction","asc")
						.when()
						.get()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

						//.as(new TypeRef<List<PersonVO>>() {});


		WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
		var people = wrapper.getEmbedded().getPersons();
		PersonVO foundPersonOne = people.get(0);
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());
		assertTrue(foundPersonOne.getEnabled());

		assertEquals(742,foundPersonOne.getId());

		assertEquals("Anderson", foundPersonOne.getFirstName());
		assertEquals("Lumsdale", foundPersonOne.getLastName());
		assertEquals("010 Prairie Rose Circle", foundPersonOne.getAddress());
		assertEquals("Male", foundPersonOne.getGender());

		PersonVO foundPerson4= people.get(1);

		assertNotNull(foundPerson4.getId());
		assertNotNull(foundPerson4.getFirstName());
		assertNotNull(foundPerson4.getLastName());
		assertNotNull(foundPerson4.getAddress());
		assertNotNull(foundPerson4.getGender());
		assertFalse(foundPerson4.getEnabled());

		assertEquals(554,foundPerson4.getId());

		assertEquals("Angil", foundPerson4.getFirstName());
		assertEquals("Spinke", foundPerson4.getLastName());
		assertEquals("2886 Moose Point", foundPerson4.getAddress());
		assertEquals("Female", foundPerson4.getGender());
	}
	@Order(7)
	@Test
	public void testFindByName() throws JsonProcessingException,JsonProcessingException {
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.accept(TestConfigs.CONTENT_TYPE_JSON)
						.pathParam("firstName", "ayr")
						.queryParams("page",0,"size",6,"direction","asc")
						.when()
						.get("findPersonsByName/{firstName}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		//.as(new TypeRef<List<PersonVO>>() {});


		WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
		var people = wrapper.getEmbedded().getPersons();
		PersonVO foundPersonOne = people.get(0);
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());
		assertTrue(foundPersonOne.getEnabled());

		assertEquals(1,foundPersonOne.getId());

		assertEquals("Ayrthon", foundPersonOne.getFirstName());
		assertEquals("Senna", foundPersonOne.getLastName());
		assertEquals("São Paulo", foundPersonOne.getAddress());
		assertEquals("Male", foundPersonOne.getGender());

	}
	@Order(8)
	@Test
	public void testFindAllWithoutToken() throws JsonProcessingException,JsonProcessingException {
		RequestSpecification specificationWithoutToken= new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

				given().spec(specificationWithoutToken)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.when()
						.get()
						.then()
						.statusCode(403);


		//.as(new TypeRef<List<PersonVO>>() {});

	}

	@Order(9)
	@Test
	public void testHATEOAS() throws JsonProcessingException,JsonProcessingException {
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.accept(TestConfigs.CONTENT_TYPE_JSON)
						.queryParams("page",1,"size",20,"direction","asc")
						.when()
						.get()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();


		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/823\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/620\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/925\"}}}"));

		//assertTrue(content.contains("\"page\":{\"size\":12,\"totalElements\":1005,\"totalPages\":84,\"number\":0}}"));

		//last
		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/person/v1?limit=12&direction=asc&page=83&size=12&sort=firstName,asc\"}}"));
		//next
		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/person/v1?limit=12&direction=asc" +
				"&page=2&size=12&sort=firstName,asc\"}"));
		//self
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/person/v1?page=1&limit=12" +
				"&direction=asc\"}"));
		//prev
		assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8888/api/person/v1?limit=12&direction=asc" +
				"&page=0&size=12&sort=firstName,asc\"}"));
		//first
		assertTrue(content.contains("\"first\":{\"href\":\"http://localhost:8888/api/person/v1?limit=12&direction=asc" +
				"&page=0&size=12&sort=firstName,asc\"}"));
	}

	private void mockPerson() {
		person.setFirstName("Richard");
		person.setLastName("Stallman");
		person.setAddress("New York City - New York, US");
		person.setGender("Male");
		person.setEnabled(true);
	}

}