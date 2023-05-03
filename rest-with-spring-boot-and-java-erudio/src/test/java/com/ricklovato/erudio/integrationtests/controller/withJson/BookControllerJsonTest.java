package com.ricklovato.erudio.integrationtests.controller.withJson;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricklovato.erudio.configs.TestConfigs;
import com.ricklovato.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.ricklovato.erudio.integrationtests.vo.AccountCredentialsVO;
import com.ricklovato.erudio.integrationtests.vo.BookVO;

import com.ricklovato.erudio.integrationtests.vo.TokenVO;
import com.ricklovato.erudio.integrationtests.vo.wrappers.WrapperBookVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerJsonTest extends AbstractIntegrationTest{


	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static BookVO book;
	@BeforeAll
	public static void setUp(){
		objectMapper = new ObjectMapper();
		//desabilita falhas quando as propriedades forem desconhecidas
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		book = new BookVO();
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
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();


	}
	@Order(1)
	@Test
	public void testCreate() throws JsonProcessingException,JsonProcessingException {
		mockBook();
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.body(book)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		BookVO createdBook = objectMapper.readValue(content,BookVO.class);
		book = createdBook;
		assertNotNull(createdBook.getAuthor());
		assertNotNull(createdBook.getLaunchDate());
		assertNotNull(createdBook.getPrice());
		assertNotNull(createdBook.getTitle());
		assertNotNull(createdBook.getId());

		assertTrue(createdBook.getId()>0);

		assertEquals("Richard",createdBook.getAuthor());
		assertEquals("Midnight Espresso",createdBook.getTitle());
		assertEquals(19.99,createdBook.getPrice());




	}
	@Order(2)
	@Test
	public void testUpdate() throws JsonProcessingException,JsonProcessingException {
		book.setAuthor("MorningStar");
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.body(book)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		BookVO createdBook = objectMapper.readValue(content,BookVO.class);
		book = createdBook;
		assertNotNull(createdBook.getId());
		assertNotNull(createdBook.getAuthor());
		assertNotNull(createdBook.getTitle());
		assertNotNull(createdBook.getPrice());
		assertNotNull(createdBook.getLaunchDate());

		assertEquals(book.getId(),createdBook.getId());

		assertEquals("MorningStar",createdBook.getAuthor());
		assertEquals("Midnight Espresso",createdBook.getTitle());
		assertEquals(19.99,createdBook.getPrice());
	}

	@Order(3)
	@Test
	public void testFindById() throws JsonProcessingException {
		mockBook();
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
						.pathParam("id",book.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		BookVO createdBook = objectMapper.readValue(content,BookVO.class);
		book = createdBook;
		assertNotNull(createdBook.getId());
		assertNotNull(createdBook.getAuthor());
		assertNotNull(createdBook.getTitle());
		assertNotNull(createdBook.getPrice());
		assertNotNull(createdBook.getLaunchDate());

		assertEquals(book.getId(),createdBook.getId());

		assertEquals("MorningStar",createdBook.getAuthor());
		assertEquals("Midnight Espresso",createdBook.getTitle());
		assertEquals(19.99,createdBook.getPrice());
	}

	@Order(4)
	@Test
	public void testDelete() throws JsonProcessingException {

				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_XML)
						.pathParam("id",book.getId())
						.when()
						.delete("{id}")
						.then()
						.statusCode(204);
	}

	@Order(5)
	@Test
	public void testFindAll() throws JsonProcessingException,JsonProcessingException {
		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.queryParams("page", 0 , "limit", 12, "direction", "asc")
						.when()
						.get()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

						//.as(new TypeRef<List<BookVO>>() {});


		WrapperBookVO wrapper = objectMapper.readValue(content,WrapperBookVO.class);

		List<BookVO> books = wrapper.getEmbedded().getBooks();
		BookVO foundBookOne = books.get(0);

		assertNotNull(foundBookOne.getId());
		assertNotNull(foundBookOne.getTitle());
		assertNotNull(foundBookOne.getAuthor());
		assertNotNull(foundBookOne.getPrice());
		assertTrue(foundBookOne.getId() > 0);
		assertEquals(12,foundBookOne.getId());
		assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação " +
				"cotidiana", foundBookOne.getTitle());
		assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", foundBookOne.getAuthor());
		assertEquals(54.0, foundBookOne.getPrice());

		BookVO foundBookFive = books.get(4);

		assertNotNull(foundBookFive.getId());
		assertNotNull(foundBookFive.getTitle());
		assertNotNull(foundBookFive.getAuthor());
		assertNotNull(foundBookFive.getPrice());
		assertTrue(foundBookFive.getId() > 0);

		assertEquals(8,foundBookFive.getId());
		assertEquals("Domain Driven Design", foundBookFive.getTitle());
		assertEquals("Eric Evans", foundBookFive.getAuthor());
		assertEquals(92.0, foundBookFive.getPrice());
	}
	@Order(6)
	@Test
	public void testFindAllWithoutToken() throws JsonProcessingException,JsonProcessingException {
		RequestSpecification specificationWithoutToken= new RequestSpecBuilder()
				.setBasePath("/api/book/v1")
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


		//.as(new TypeRef<List<BookVO>>() {});

	}

	private void mockBook() {
		book.setAuthor("Richard");
		book.setPrice(19.99);
		book.setTitle("Midnight Espresso");
		book.setLaunchDate(new Date());
	}

}