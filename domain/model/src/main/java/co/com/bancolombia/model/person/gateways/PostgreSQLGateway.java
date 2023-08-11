package co.com.bancolombia.model.person.gateways;

import co.com.bancolombia.model.person.Person;
import co.com.bancolombia.model.person.User;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface PostgreSQLGateway {

    Mono<Person> getPersonById(String id);
    Flux<Person> getPersonas();

    Mono<User> getUserById(String id);
    Flux<User> getUsersByName(String nombre);

    Mono<User> saveUser(User user);
    Flux<User> getUsers();
}
