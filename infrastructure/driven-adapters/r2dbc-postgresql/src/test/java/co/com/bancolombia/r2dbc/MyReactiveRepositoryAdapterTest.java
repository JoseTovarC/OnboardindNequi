package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.person.User;
import co.com.bancolombia.r2dbc.dto.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.xmlunit.util.Mapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    MyReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    MyReactiveUserRepository userRepository;
    @Mock
    MyReactivePersonRepository DumpRepository;

    @BeforeEach
    public void setUp() {

        repositoryAdapter = new MyReactiveRepositoryAdapter(userRepository, DumpRepository);
    }



    @Test
    void getUserByIdTest() {

        UserData usuario = UserData.builder()
                .idReal(454)
                .firstName("Pepito")
                .lastName("Perez")
                .email("pepipe")
                .avatar("curioso")
                .build();


        given(  userRepository.findByIdReal(454))
                .willReturn(Mono.just(usuario));

        Mono<User> objectModelResponse = repositoryAdapter.getUserById(String.valueOf(454));

        StepVerifier.create(objectModelResponse)
                .expectNextMatches(usuarito -> {
                            assertEquals(454, usuarito.getId());
                            assertEquals("Pepito", usuarito.getFirstName());
                            return true;
                        }
                )
                .verifyComplete();


    }

    @Test
    void saveUserIfExistsTest() {

        UserData usuario = UserData.builder()
                .idReal(7647)
                .firstName("Andres")
                .lastName("Guardado")
                .email("guardaito")
                .avatar("donde no llega la luz del sol")
                .build();


        given(userRepository.findByIdReal(7647))
                .willReturn(Mono.just(usuario));

        Mono<User> objectModelResponse = repositoryAdapter.saveUser(
                User.builder()
                        .id(usuario.getIdReal())
                        .firstName("otro")
                        .lastName("no guardado")
                        .email("pero con  igual id")
                        .avatar("a otro usuario en bd")
                        .build()
        );

        StepVerifier.create(objectModelResponse)
                .expectNextMatches(usuarito -> {
                            assertEquals(7647, usuarito.getId());
                            assertEquals("Guardado", usuarito.getLastName());
                            return true;
                        }
                )
                .verifyComplete();


    }


    @Test
    void saveNewUserTest() {

        UserData usuario = UserData.builder()
                .idReal(7647)
                .firstName("Andres")
                .lastName("Guardadon't")
                .email("dejame entrar")
                .avatar("a tu vida y a tu corazon")
                .build();


        given(userRepository.findByIdReal(7647))
                .willReturn(Mono.empty());
        given(userRepository.save(usuario))
                .willReturn(Mono.just(usuario));



        Mono<User> objectModelResponse = repositoryAdapter.saveUser(
                User.builder()
                        .id(usuario.getIdReal())
                        .firstName(usuario.getFirstName())
                        .lastName(usuario.getLastName())
                        .email(usuario.getEmail())
                        .avatar(usuario.getAvatar())
                        .build()
        );

        StepVerifier.create(objectModelResponse)
                .expectNextMatches(usuarito -> {
                            assertEquals(7647, usuarito.getId());
                            assertEquals("Guardadon't", usuarito.getLastName());
                            return true;
                        }
                )
                .verifyComplete();


    }

    @Test
    void mustFindByNameTest() {
        UserData usuario1 = UserData.builder()
                .idReal(9289)
                .firstName("Xiomara")
                .lastName("Motta")
                .email("jummm")
                .avatar("tachiquita")
                .build();
        User usuarioModelo1 = User.builder()
                .id(9289)
                .firstName("Xiomara")
                .lastName("Motta")
                .email("jummm")
                .avatar("tachiquita")
                .build();

        UserData usuario2 = UserData.builder()
                .idReal(8753)
                .firstName("Xiomara")
                .lastName("Mendoza")
                .email("xiomen")
                .avatar("patina")
                .build();
        User usuarioModelo2 = User.builder()
                .id(8753)
                .firstName("Xiomara")
                .lastName("Mendoza")
                .email("xiomen")
                .avatar("patina")
                .build();


        when(userRepository.findByFirstName("Xiomara")).thenReturn(Flux.just(usuario1, usuario2));

        Flux<User> result = repositoryAdapter.getUsersByName("Xiomara");

        StepVerifier.create(result)
                .expectNext(usuarioModelo1)
                .expectNext(usuarioModelo2)
                .verifyComplete();
    }



    @Test
    void getUsersTest() {
        UserData usuario1 = UserData.builder()
                .idReal(9289)
                .firstName("José")
                .lastName("Tovar")
                .email("jotovar")
                .avatar("voleibol")
                .build();
        User usuarioModelo1 = User.builder()
                .id(9289)
                .firstName("José")
                .lastName("Tovar")
                .email("jotovar")
                .avatar("voleibol")
                .build();

        UserData usuario2 = UserData.builder()
                .idReal(8753)
                .firstName("Xiomara")
                .lastName("Mendoza")
                .email("xiomen")
                .avatar("patina")
                .build();
        User usuarioModelo2 = User.builder()
                .id(8753)
                .firstName("Xiomara")
                .lastName("Mendoza")
                .email("xiomen")
                .avatar("patina")
                .build();


        given(userRepository.findAll()).willReturn(Flux.just(usuario1,usuario2));

        Flux<User> result = repositoryAdapter.getUsers();

        StepVerifier.create(result)
                .expectNext(usuarioModelo1)
                .expectNext(usuarioModelo2)
                .verifyComplete();
    }


    /*@Test
    void mustFindValueById() {

        when(repository.findById("1")).thenReturn(Mono.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<Object> result = repositoryAdapter.findById("1");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        when(repository.findAll()).thenReturn(Flux.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<Object> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<Object> result = repositoryAdapter.findByExample("test");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        when(repository.save("test")).thenReturn(Mono.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<Object> result = repositoryAdapter.save("test");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }*/
}
