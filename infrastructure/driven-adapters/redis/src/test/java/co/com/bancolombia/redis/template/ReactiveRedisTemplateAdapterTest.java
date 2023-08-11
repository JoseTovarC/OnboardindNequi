package co.com.bancolombia.redis.template;


import co.com.bancolombia.model.person.User;
import co.com.bancolombia.redis.template.dto.UserDataCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ReactiveRedisTemplateAdapterTest {

    @Mock
    private ReactiveRedisConnectionFactory connectionFactory;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private ObjectMapper objectMapper;
    @InjectMocks
    private ReactiveRedisTemplateAdapter adapter;


    @Mock
    /* Type parameters:
     * <K> – the Redis key type against which the template works (usually a String)
     * <V> – the Redis value type against which the template works
     */
            ReactiveRedisTemplate<String, UserDataCache> reactiveRedisTemplate;

    @Mock
    ReactiveValueOperations<String, UserDataCache> reactiveValueOperations;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ReflectionTestUtils.setField(adapter, "mapper", objectMapper);
        ReflectionTestUtils.setField(adapter, "template", reactiveRedisTemplate);
        //ReflectionTestUtils.setField(adapter, "defaultExpirationTime", 10000);

        when(reactiveRedisTemplate.opsForValue()).thenReturn(reactiveValueOperations);
    }

    @Test
    void testSave() {
        User objetoModel = User.builder()
                .id(2039)
                .firstName("Pepita")
                .lastName("Perez")
                .email("tengo")
                .avatar("no tengo")
                .build();

        UserDataCache objectCache = UserDataCache.builder()
                .id(2039)
                .firstName("Pepita")
                .lastName("Perez")
                .email("tengo")
                .avatar("no tengo")
                .build();

        when(reactiveValueOperations.set(anyString(), any())).thenReturn(Mono.just(true));
        when(reactiveRedisTemplate.expire(anyString(), any())).thenReturn(Mono.just(true));
        when(objectMapper.map(any(), any())).thenReturn(objectCache);

        Mono<User> objectModelSaved = adapter.saveUser(objetoModel);

        StepVerifier.create(objectModelSaved)
                .expectNext(objetoModel)
                .verifyComplete();
    }

    @Test
    void testSaveError() {
        User objetoModel = User.builder()
                .id(2039)
                .firstName("Pepita")
                .lastName("Perez")
                .email("tengo")
                .avatar("no tengo")
                .build();

        UserDataCache objectCache = UserDataCache.builder()
                .id(2039)
                .firstName("Pepita")
                .lastName("Perez")
                .email("tengo")
                .avatar("no tengo")
                .build();

        when(reactiveValueOperations.set(anyString(), any())).thenReturn(Mono.error(new RuntimeException("Error en guardado")));
        when(reactiveRedisTemplate.expire(anyString(), any())).thenReturn(Mono.just(true));
        when(objectMapper.map(any(), any())).thenReturn(objectCache);

        Mono<User> objectModelSaved = adapter.saveUser(objetoModel);

        StepVerifier.create(objectModelSaved)
                .verifyComplete();
    }


    @Test
    void testFindById() {

        User objetoModel = User.builder()
                .id(2039)
                .firstName("Pepita")
                .lastName("Perez")
                .email("tengo")
                .avatar("no tengo")
                .build();

        UserDataCache objectCache = UserDataCache.builder()
                .id(2039)
                .firstName("Pepita")
                .lastName("Perez")
                .email("tengo")
                .avatar("no tengo")
                .build();

        when(reactiveRedisTemplate.opsForValue().get(anyString())).thenReturn(Mono.just(objectCache));
        when(objectMapper.map(any(), any())).thenReturn(objetoModel);

        Mono<User> objectModelGotten = adapter.getUserById("2039");

        StepVerifier.create(objectModelGotten)
                .expectNext(objetoModel)
                .verifyComplete();


    }



    @Test
    void testFindByIdEmpty() {

        when(reactiveRedisTemplate.opsForValue().get(anyString())).thenReturn(Mono.error(new RuntimeException("An error has been occurred")));
        //when(objectMapper.map(any(), any())).thenReturn(errorEc);

        Mono<User> objectModelGotten = adapter.getUserById("2039");

        StepVerifier.create(objectModelGotten)
                .verifyComplete();


    }

}