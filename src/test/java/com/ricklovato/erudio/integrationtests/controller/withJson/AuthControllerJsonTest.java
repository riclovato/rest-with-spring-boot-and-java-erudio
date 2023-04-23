package com.ricklovato.erudio.integrationtests.controller.withJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ricklovato.erudio.configs.TestConfigs;
import com.ricklovato.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import com.ricklovato.erudio.integrationtests.vo.AccountCredentialsVO;
import com.ricklovato.erudio.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerJsonTest extends AbstractIntegrationTest {

    private static TokenVO tokenVO;

    @Order(1)
    @Test
    public void testSignin() throws JsonProcessingException,JsonProcessingException {

        AccountCredentialsVO user = new AccountCredentialsVO("leandro","admin123");

        tokenVO =
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
                        .as(TokenVO.class);

      assertNotNull(tokenVO.getAccessToken());
      assertNotNull(tokenVO.getRefreshToken());

    }

    @Order(2)
    @Test
    public void testRefresh() throws JsonProcessingException,JsonProcessingException {

        AccountCredentialsVO user = new AccountCredentialsVO("leandro","admin123");

        var newTokenVO =
                given()
                        .basePath("auth/refresh")
                        .port(TestConfigs.SERVER_PORT)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .pathParam("username",tokenVO.getUsername())
                        .header(TestConfigs.HEADER_PARAM_AUTHORIZATION,"Bearer " + tokenVO.getRefreshToken())
                        .when()
                        .put("{username}")
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenVO.class);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());

    }

}
