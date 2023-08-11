package co.com.bancolombia.model.person.gateways;

import co.com.bancolombia.model.person.Person;
import co.com.bancolombia.model.person.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RedisGateway {

    Mono<User> getUserById(String id);
    Mono<User> saveUser(User user);

}
