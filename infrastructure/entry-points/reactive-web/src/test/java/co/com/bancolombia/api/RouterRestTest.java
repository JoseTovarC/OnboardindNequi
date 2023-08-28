package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.UserIdRequest;
import co.com.bancolombia.api.dto.UserResponse;
import co.com.bancolombia.model.person.User;
import co.com.bancolombia.usecase.person.PersonUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;

    private ObjectMapper objectMapper;


    @MockBean
    private PersonUseCase useCase;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        webTestClient = WebTestClient
                .bindToApplicationContext(context)
                .configureClient()
                .build();
    }

    @Test
    void testListenGetUserById() {
        given(useCase.getUserById("12345"))
                .willReturn(Mono.just(
                        User.builder()
                                .id(12345)
                                .firstName("Camilo")
                                .lastName("Ramirez")
                                .email("jummm")
                                .avatar("busquelo")
                                .build()
                ));

        webTestClient.get()
                .uri("/api/usuario/12345")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(userResponse -> {
                    Assertions.assertEquals(12345, userResponse.getId());
                        }
                );
    }

    @Test
    void testListenPOSTUseCase() {

        given(useCase.createUser(1234))
                .willReturn(Mono.just(
                        User.builder()
                                .id(1234)
                                .firstName("Camilo")
                                .lastName("Ramirez")
                                .email("jummm")
                                .avatar("busquelo")
                                .build()
                ));

        webTestClient.post()
                .uri("/api/usuarios")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(
                                UserIdRequest.builder()
                                        .requestId(1234)
                                        .build()
                        ) , UserIdRequest.class
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(userResponse -> {
                            Assertions.assertEquals(1234, userResponse.getId());
                        }
                );

    }


   @Test
   void testListenGetUsersByNameFound() {

       var usuarioAndres = User.builder()
                   .id(12345)
                   .firstName("Andres")
                   .lastName("Pedraza")
                   .email("jummm")
                   .avatar("busquelo")
                   .build();
       given(useCase.getUsersByName("Andres"))
               .willReturn(Flux.just(usuarioAndres));

       webTestClient.get()
               .uri("/api/usuarios/Andres")
               .accept(MediaType.APPLICATION_JSON)
               .exchange()
               .expectStatus().isOk()
               .expectBodyList(UserResponse.class)
               .value(userResponse -> {
                         var userResponseEsperado =  Stream.of(usuarioAndres).map(
                                 usuarito -> UserResponse.builder()
                                         .id(usuarito.getId())
                                         .firstName(usuarito.getFirstName())
                                         .lastName(usuarito.getLastName())
                                         .email(usuarito.getEmail())
                                         .avatar(usuarito.getAvatar())
                                         .build())
                                         .collect(Collectors.toList());

                         assertThat(userResponse, is(userResponseEsperado));
                         assertThat(userResponse, hasSize(1));
                   }
               );
   }

   /*
   @Test
   void testListenGetUsersByNameNotFound() {

       var usuarioAndres = User.builder()
                   .id(12345)
                   .firstName("Andres")
                   .lastName("Pedraza")
                   .email("jummm")
                   .avatar("busquelo")
                   .build();
       given(useCase.getUsersByName("Andres"))
               .willReturn(Flux.empty());
       given(useCase.getUsers())
                .willReturn(Flux.just(usuarioAndres));


       webTestClient.get()
               .uri("/api/usuarios/Andres")
               .accept(MediaType.APPLICATION_JSON)
               .exchange()
               .expectStatus().isOk()
               .expectBodyList(UserResponse.class)
               .value(userResponse -> {
                         var userResponseEsperado =  Stream.of(usuarioAndres).map(
                                 usuarito -> UserResponse.builder()
                                         .id(usuarito.getId())
                                         .firstName(usuarito.getFirstName())
                                         .lastName(usuarito.getLastName())
                                         .email(usuarito.getEmail())
                                         .avatar(usuarito.getAvatar())
                                         .build())
                                         .collect(Collectors.toList());

                         assertThat(userResponse, is(userResponseEsperado));
                         assertThat(userResponse, hasSize(1));
                   }
               );
   }


@Test
void testListenGetUsers() {

    var usuario1 = User.builder()
                .id(12345)
                .firstName("Andres")
                .lastName("Pedraza")
                .email("jummm")
                .avatar("busquelo")
                .build();

     var usuario2 = User.builder()
                 .id(6352)
                 .firstName("Marcos")
                 .lastName("Orejuela")
                 .email("jummm")
                 .avatar("busquelo")
                 .build();

    given(useCase.getUsers())
             .willReturn(Flux.just(usuario1,usuario2));


    webTestClient.get()
            .uri("/api/usuarios")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(UserResponse.class)
            .value(userResponse -> {
                      var userResponseEsperado =  Stream.of(usuario1, usuario2).map(
                              usuarito -> UserResponse.builder()
                                      .id(usuarito.getId())
                                      .firstName(usuarito.getFirstName())
                                      .lastName(usuarito.getLastName())
                                      .email(usuarito.getEmail())
                                      .avatar(usuarito.getAvatar())
                                      .build())
                                      .collect(Collectors.toList());

                      assertThat(userResponse, is(userResponseEsperado));
                      assertThat(userResponse, hasSize(2));
                }
            );
}


    @Test
    void testListenGETOtherUseCase() {
        webTestClient.get()
                .uri("/api/otherusercase/path")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isEmpty();
                        }
                );
    }

    @Test
    void testListenPOSTUseCase() {
        webTestClient.post()
                .uri("/api/usecase/otherpath")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue("")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isEmpty();
                        }
                );
    }*/
}
