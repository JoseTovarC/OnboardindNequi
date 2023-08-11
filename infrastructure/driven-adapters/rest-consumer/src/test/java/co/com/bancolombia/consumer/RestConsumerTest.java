package co.com.bancolombia.consumer;


import co.com.bancolombia.model.person.User;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.io.IOException;


public class RestConsumerTest {

    private static RestConsumer restConsumer;

    private static MockWebServer mockBackEnd;


    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

        String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());

        var webClient = WebClient.builder().baseUrl(baseUrl).build();
        restConsumer = new RestConsumer(webClient);
    }

    @AfterAll
    static void tearDown() throws IOException {

        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("Validate the function testGet.")
    void validateTestGet() {

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"data\":{\"id\":8,\"email\":\"lindsay.ferguson@reqres.in\",\"" +
                        "first_name\":\"Lindsay\",\"last_name\":\"Ferguson\",\"" +
                        "avatar\":\"https://reqres.in/img/faces/8-image.jpg\"},\"" +
                        "support\":{\"url\":\"https://reqres.in/#support-heading\",\"" +
                        "text\":\"To keep ReqRes free, contributions towards" +
                        " server costs are appreciated!\"}}"));
        Mono<User> response = restConsumer.getUserById(8);

        StepVerifier.create(response)
                .expectNextMatches(objectResponse -> {
                    objectResponse.getId().equals("8");
                    objectResponse.getFirstName().equals("Lindsay");
                    objectResponse.getLastName().equals("Ferguson");
                    objectResponse.getAvatar().equals("https://reqres.in/img/faces/8-image.jpg");
                    objectResponse.getAvatar().equals("lindsay.ferguson@reqres.in");
                    return true;
                })
                .verifyComplete();
    }

    /*@Test
    @DisplayName("Validate the function testPost.")
    void validateTestPost() {

        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"state\" : \"ok\"}"));
        var response = restConsumer.testPost();

        StepVerifier.create(response)
                .expectNextMatches(objectResponse -> objectResponse.getState().equals("ok"))
                .verifyComplete();
    }*/
}