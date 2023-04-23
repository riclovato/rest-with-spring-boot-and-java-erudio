package com.ricklovato.erudio.integrationtests.controller.withYaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ricklovato.erudio.configs.TestConfigs;
import com.ricklovato.erudio.integrationtests.controller.withYaml.mapper.YMLMapper;
import com.ricklovato.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.ricklovato.erudio.integrationtests.vo.AccountCredentialsVO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerYamlTest extends AbstractIntegrationTest {



    private static TokenVO tokenVO;

    private static YMLMapper mapper;

    @BeforeAll
    public static void setup(){

        mapper = new YMLMapper();
    }
    @Order(1)
    @Test
    public void testSignin() throws JsonProcessingException,JsonProcessingException {

        AccountCredentialsVO user = new AccountCredentialsVO("leandro","admin123");

        RequestSpecification specification = new RequestSpecBuilder()
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();


        tokenVO =
                given().spec(specification)
                        .config(new RestAssuredConfig().config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                        .accept(TestConfigs.CONTENT_TYPE_YML)
                        .basePath("auth/signin")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .body(user, mapper)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenVO.class, mapper);

      assertNotNull(tokenVO.getAccessToken());
      assertNotNull(tokenVO.getRefreshToken());

    }

    @Order(2)
    @Test
    public void testRefresh() throws JsonProcessingException,JsonProcessingException {

        AccountCredentialsVO user = new AccountCredentialsVO("leandro","admin123");

        var newTokenVO =
                given()
                        .config(new RestAssuredConfig().config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                        .accept(TestConfigs.CONTENT_TYPE_YML)
                        .basePath("auth/refresh")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_YML)
                        .pathParam("username",tokenVO.getUsername())
                        .header(TestConfigs.HEADER_PARAM_AUTHORIZATION,"Bearer " + tokenVO.getRefreshToken())
                        .when()
                        .put("{username}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenVO.class, mapper);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());

    }

}
