package co.com.bancolombia.redis.template;

import co.com.bancolombia.model.person.gateways.RedisGateway;
import co.com.bancolombia.redis.template.helper.ReactiveTemplateAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import co.com.bancolombia.redis.template.dto.UserDataCache;
import co.com.bancolombia.model.person.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ReactiveRedisTemplateAdapter extends ReactiveTemplateAdapterOperations<User, String, UserDataCache>
implements RedisGateway
{

    private static final long EXPIRATION_TIME = 600000;

    public ReactiveRedisTemplateAdapter(ReactiveRedisConnectionFactory connectionFactory, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(connectionFactory, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> getUserById(String id) {
        return findById(generateKey(Integer.parseInt(id)))
                .doOnSubscribe(sb -> log.info("Buscando en cache el usuario con Id {}", id))
                .doOnNext(usr -> log.info("El usuario con Id {} fue encontrado en cache", usr.getId()))
                .onErrorResume(error -> Mono.empty());
    }

    @Override
    public Mono<User> saveUser(User user) {
        return save(generateKey(user.getId()), user, EXPIRATION_TIME)
                .doOnSubscribe(sb -> log.info("Guardando en cache el usuario con Id {}", user.getId()))
                .doOnNext(usr -> log.info("El usuario con Id {} fue guardado en cache", usr.getId()))
                .onErrorResume(error -> Mono.empty());
    }

    private String generateKey(Integer id) {
        return String.format("usuario:%d", id.hashCode());
    }
}
