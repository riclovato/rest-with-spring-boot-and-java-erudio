package com.ricklovato.erudio.integrationtests.controller.withYaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.ricklovato.erudio.configs.TestConfigs;
import com.ricklovato.erudio.integrationtests.controller.withYaml.mapper.YMLMapper;
import com.ricklovato.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.ricklovato.erudio.integrationtests.vo.AccountCredentialsVO;
import com.ricklovato.erudio.integrationtests.vo.PersonVO;
import com.ricklovato.erudio.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest{


	private static RequestSpecification specification;
	private static YMLMapper mapper;
	private static PersonVO person;
	@BeforeAll
	public static void setUp(){
		mapper = new YMLMapper();



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
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.config(new RestAssuredConfig().config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.body(user,mapper)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(TokenVO.class,mapper)
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
		var createdPerson =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.config(new RestAssuredConfig().config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.body(person,mapper)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class,mapper);

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
		var createdPerson =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.config(new RestAssuredConfig().config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.body(person,mapper)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class,mapper);

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
		var createdPerson =
				given().spec(specification)
						.config(
								RestAssuredConfig
										.config()
										.encoderConfig(EncoderConfig.encoderConfig()
												.encodeContentTypeAs(
														TestConfigs.CONTENT_TYPE_YML,
														ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.pathParam("id", person.getId())
						.when()
						.patch("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class,mapper);

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
		var createdPerson =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.config(new RestAssuredConfig().config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
						.pathParam("id",person.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class,mapper);

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
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.config(new RestAssuredConfig().config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
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
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.config(new RestAssuredConfig().config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.when()
						.get()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonVO[].class,mapper);

						//.as(new TypeRef<List<PersonVO>>() {});


		List<PersonVO> people = Arrays.asList(content);
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

		PersonVO foundPerson4= people.get(3);

		assertNotNull(foundPerson4.getId());
		assertNotNull(foundPerson4.getFirstName());
		assertNotNull(foundPerson4.getLastName());
		assertNotNull(foundPerson4.getAddress());
		assertNotNull(foundPerson4.getGender());
		assertTrue(foundPerson4.getEnabled());

		assertEquals(8,foundPerson4.getId());

		assertEquals("Cloud", foundPerson4.getFirstName());
		assertEquals("Strife", foundPerson4.getLastName());
		assertEquals("Niflhaim", foundPerson4.getAddress());
		assertEquals("Male", foundPerson4.getGender());
	}
	@Order(7)
	@Test
	public void testFindAllWithoutToken() throws JsonProcessingException,JsonProcessingException {
		RequestSpecification specificationWithoutToken= new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

				given().spec(specificationWithoutToken)
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.config(new RestAssuredConfig().config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.when()
						.get()
						.then()
						.statusCode(403);


		//.as(new TypeRef<List<PersonVO>>() {});

	}

	private void mockPerson() {
		person.setFirstName("Richard");
		person.setLastName("Stallman");
		person.setAddress("New York City - New York, US");
		person.setGender("Male");
		person.setEnabled(true);
	}

}