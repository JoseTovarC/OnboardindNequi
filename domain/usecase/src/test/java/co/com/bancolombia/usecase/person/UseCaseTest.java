package co.com.bancolombia.usecase.person;

import co.com.bancolombia.model.person.User;
import co.com.bancolombia.model.person.gateways.PersonApiService;
import co.com.bancolombia.model.person.gateways.PostgreSQLGateway;
import co.com.bancolombia.model.person.gateways.RedisGateway;
import co.com.bancolombia.model.person.gateways.RestConsumerExternalAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UseCaseTest {

    @Mock
    private PersonApiService serviceGateway;
    @Mock
    private PostgreSQLGateway repositoryPostgresGateway;
    @Mock
    private RestConsumerExternalAPI externalAPI;
    @Mock
    private RedisGateway RedisCacheGateway;

    private PersonUseCase useCase;

    @BeforeEach
    public void setUp() {
        useCase= new PersonUseCase(serviceGateway, repositoryPostgresGateway, externalAPI, RedisCacheGateway);
    }

    @Test
    void getUserByIdFromCacheTest(){

        User usuarito = User.builder()
                .id(1193)
                .firstName("José")
                .lastName("Tovar")
                .email("jotovar")
                .avatar("tovar070201")
                .build();

        given(RedisCacheGateway.getUserById("1193"))
                .willReturn(Mono.just(usuarito));

        Mono<User> Respuesta = useCase
                .getUserById("1193");

        StepVerifier.create(Respuesta)
                .expectNextMatches(UserResult-> {
                    //Validaciones necesarias
                    UserResult.getId().equals(usuarito.getId());
                    UserResult.getFirstName().equals(usuarito.getFirstName());
                    return true;
                })
                .verifyComplete();

    }


    @Test
    void getUserByIdFromBDTest(){

        User usuarito = User.builder()
                .id(1193)
                .firstName("José")
                .lastName("Tovar")
                .email("jotovar")
                .avatar("tovar070201")
                .build();

        given(RedisCacheGateway.getUserById("1193"))
                .willReturn(Mono.empty());
        given(repositoryPostgresGateway.getUserById("1193"))
                .willReturn(Mono.just(usuarito));
        given(RedisCacheGateway.saveUser(usuarito))
                .willReturn(Mono.just(usuarito));

        Mono<User> Respuesta = useCase
                .getUserById("1193");

        StepVerifier.create(Respuesta)
                .expectNextMatches(UserResult-> {
                    //Validaciones necesarias
                    UserResult.getId().equals(usuarito.getId());
                    UserResult.getFirstName().equals(usuarito.getFirstName());
                    return true;
                })
                .verifyComplete();

    }


    @Test
    void createUserTest(){

        User usuarito = User.builder()
                .id(1193)
                .firstName("José")
                .lastName("Tovar")
                .email("jotovar")
                .avatar("tovar070201")
                .build();

        given(externalAPI.getUserById(1193))
                .willReturn(Mono.just(usuarito));
        given(repositoryPostgresGateway.saveUser(usuarito))
                .willReturn(Mono.just(usuarito));
        given(RedisCacheGateway.saveUser(usuarito))
                .willReturn(Mono.just(usuarito));



        Mono<User> Respuesta = useCase
                .createUser(1193);

        StepVerifier.create(Respuesta)
                .expectNextMatches(UserResult-> {
                    //Validaciones necesarias
                    UserResult.getId().equals(usuarito.getId());
                    UserResult.getFirstName().equals(usuarito.getFirstName());
                    return true;
                })
                .verifyComplete();

    }

    @Test
    void getUsersTest() {

        User usuario1 = User.builder()
                .id(9289)
                .firstName("José")
                .lastName("Tovar")
                .email("jotovar")
                .avatar("voleibol")
                .build();

        User usuario2 = User.builder()
                .id(8753)
                .firstName("Xiomara")
                .lastName("Mendoza")
                .email("xiomen")
                .avatar("patina")
                .build();


        given(repositoryPostgresGateway.getUsers()).willReturn(Flux.just(usuario1,usuario2));

        Flux<User> result = useCase.getUsers();

        StepVerifier.create(result)
                .expectNext(usuario1)
                .expectNext(usuario2)
                .verifyComplete();
    }

    @Test
    void getUsersByNameTest() {

        User usuario1 = User.builder()
                .id(9289)
                .firstName("Xiomara")
                .lastName("Motta")
                .email("jummm")
                .avatar("tachiquita")
                .build();

        User usuario2 = User.builder()
                .id(8753)
                .firstName("Xiomara")
                .lastName("Mendoza")
                .email("xiomen")
                .avatar("patina")
                .build();


        given(repositoryPostgresGateway.getUsersByName("Xiomara")).willReturn(Flux.just(usuario1,usuario2));

        Flux<User> result = useCase.getUsersByName("Xiomara");

        StepVerifier.create(result)
                .expectNext(usuario1)
                .expectNext(usuario2)
                .verifyComplete();
    }



}
