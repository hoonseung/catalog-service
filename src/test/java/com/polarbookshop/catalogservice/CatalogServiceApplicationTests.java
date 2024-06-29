package com.polarbookshop.catalogservice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.polarbookshop.catalogservice.domain.book.Book;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("intergration")
@Testcontainers
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    private static KeycloakToken isabelleTokens;
    private static KeycloakToken bjornTokens;

    @Container
    private static final KeycloakContainer keycloakContainer
            = new KeycloakContainer("quay.io/keycloak/keycloak:24.0")
                    .withRealmImportFile("test-realm-config.json");

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry){
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", // 발행자 uri 가 테스트 키클록을 가르키도록
                () -> keycloakContainer.getAuthServerUrl() + "/realms/PolarBookshop");
    }

    @BeforeAll
    static void generateAccessToken(){
        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakContainer.getAuthServerUrl() + "/realms/PolarBookshop/protocol/openid-connect/token")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

         isabelleTokens = authenticationWith("isabelle", "password", webClient);
         bjornTokens = authenticationWith("bjorn", "password", webClient);
    }



    @Test
    void whenPostRequestThenBookCreated() {
        var expectBook = Book.of("1234567890", "title", "devjohn", 9.9, "polar");

        webTestClient
                .post()
                .uri("/books")
                .headers(headers -> headers.setBearerAuth(isabelleTokens.accessToken()))
                .bodyValue(expectBook)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Book.class).value(actualBook  -> {
                    assertThat(actualBook).isNotNull();
                    assertThat(actualBook.isbn()).isEqualTo(expectBook.isbn());
                });
    }


    @Test
    void whenPostRequestUnauthorizedThen403(){
        var expectBook = Book.of("1234567890", "title", "devjohn", 9.9, "polar");

        webTestClient
                .post()
                .uri("/books")
                .headers(headers -> headers.setBearerAuth(bjornTokens.accessToken()))
                .bodyValue(expectBook)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void whenPostRequestUnauthenticatedThen401(){
        var expectBook = Book.of("1234567890", "title", "devjohn", 9.9, "polar");

        webTestClient
                .post()
                .uri("/books")
                .bodyValue(expectBook)
                .exchange()
                .expectStatus().isUnauthorized();
    }







    // 폼 방식으로 로그인 후 accessToken 받아서 KeycloakToken 타입으로 역직렬화
    private static KeycloakToken authenticationWith(String username, String password, WebClient webClient){
        return webClient
                .post()
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "polar-test")
                        .with("username", username)
                        .with("password", password))
                .retrieve()
                .bodyToMono(KeycloakToken.class)
                .block();
    }


    private record KeycloakToken(

            String accessToken){

        @JsonCreator // 해당 생성자를 통해 json 역직렬화
        KeycloakToken(@JsonProperty("access_token") final String accessToken){
            this.accessToken = accessToken;
        }

    }






}
