package com.ricklovato.erudio.integrationtests.controller.withJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricklovato.erudio.configs.TestConfigs;
import com.ricklovato.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.ricklovato.erudio.integrationtests.vo.PersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

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
	@Order(1)
	@Test
	public void testCreate() throws JsonProcessingException {
		mockPerson();
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

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

		PersonVO creadtedPerson = objectMapper.readValue(content,PersonVO.class);
		person = creadtedPerson;
		assertNotNull(creadtedPerson.getId());
		assertNotNull(creadtedPerson.getFirstName());
		assertNotNull(creadtedPerson.getLastName());
		assertNotNull(creadtedPerson.getAddress());
		assertNotNull(creadtedPerson.getGender());

		assertTrue(creadtedPerson.getId()>0);

		assertEquals("Richard",creadtedPerson.getFirstName());
		assertEquals("Stallman",creadtedPerson.getLastName());
		assertEquals("New York City - New York, US",creadtedPerson.getAddress());
		assertEquals("Male",creadtedPerson.getGender());

	}


	@Order(2)
	@Test
	public void testCreateWithWrongOrigin() throws JsonProcessingException {
		mockPerson();
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SERUDIO)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.body(person)
						.when()
						.post()
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();


		assertNotNull(content);
		assertEquals("Invalid CORS request",content);


	}

	@Order(3)
	@Test
	public void testFindById() throws JsonProcessingException {
		mockPerson();
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.pathParam("id",person.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonVO creadtedPerson = objectMapper.readValue(content,PersonVO.class);
		person = creadtedPerson;
		assertNotNull(creadtedPerson.getId());
		assertNotNull(creadtedPerson.getFirstName());
		assertNotNull(creadtedPerson.getLastName());
		assertNotNull(creadtedPerson.getAddress());
		assertNotNull(creadtedPerson.getGender());

		assertTrue(creadtedPerson.getId()>0);

		assertEquals("Richard",creadtedPerson.getFirstName());
		assertEquals("Stallman",creadtedPerson.getLastName());
		assertEquals("New York City - New York, US",creadtedPerson.getAddress());
		assertEquals("Male",creadtedPerson.getGender());

	}
	@Order(4)
	@Test
	public void testFindByIdWithWrongOrigin() throws JsonProcessingException {
		mockPerson();
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SERUDIO)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.pathParam("id",person.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request",content);

	}

	private void mockPerson() {
		person.setFirstName("Richard");
		person.setLastName("Stallman");
		person.setAddress("New York City - New York, US");
		person.setGender("Male");
	}

}