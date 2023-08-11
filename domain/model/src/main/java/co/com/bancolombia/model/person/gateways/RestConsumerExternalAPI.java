package co.com.bancolombia.model.person.gateways;

import co.com.bancolombia.model.person.Person;
import co.com.bancolombia.model.person.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RestConsumerExternalAPI {

    Mono<User> getUserById(Integer id);

}
