package co.com.bancolombia.usecase.person;

import co.com.bancolombia.model.exception.DataNotFoundException;
import co.com.bancolombia.model.person.User;
import co.com.bancolombia.model.person.gateways.RedisGateway;
import co.com.bancolombia.model.person.gateways.RestConsumerExternalAPI;
import lombok.RequiredArgsConstructor;
import co.com.bancolombia.model.person.gateways.PersonApiService;
import co.com.bancolombia.model.person.gateways.PostgreSQLGateway;
import co.com.bancolombia.model.person.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class PersonUseCase {
    private final PersonApiService serviceGateway;
    private final PostgreSQLGateway repositoryPostgresGateway;
    private final RestConsumerExternalAPI externalAPI;
    private final RedisGateway redisCacheGateway;

    public Mono<Person> getPerson(String id){
        return serviceGateway.getPerson(id);
    }

    public Mono<Float> getBalance(String id){
        return serviceGateway.getBalance(id);
    }

    public Mono<Person> getPersonById(String id) {

        return repositoryPostgresGateway.getPersonById(id);
    }

    public Flux<Person> getPeople() {

        return repositoryPostgresGateway.getPeople();
    }


    public Mono<User> getUserById(String id) {

        return redisCacheGateway.getUserById(id)
                        .switchIfEmpty(
                                Mono.defer(
                                        () -> repositoryPostgresGateway.getUserById(id)
                                                .switchIfEmpty(
                                                        Mono.defer(
                                                                () -> Mono.error(new DataNotFoundException("User not found"))
                                                        )
                                                )
                                                .flatMap(usuario ->
                                                    redisCacheGateway.saveUser(usuario).onErrorReturn(usuario))

                        ));
    }

    public Flux<User> getUsers() {

        return repositoryPostgresGateway.getUsers();
    }

    public Flux<User> getUsersByName(String nombre) {

        return repositoryPostgresGateway.getUsersByName(nombre)
                .switchIfEmpty( Flux.defer( () ->
                        this.getUsers()
                ));
    }

    public Mono<User> createUser(Integer id)
    {

            return externalAPI.getUserById(id)

                    .flatMap(repositoryPostgresGateway::saveUser)
                    .flatMap(usuario ->
                            redisCacheGateway.saveUser(usuario).onErrorReturn(usuario))
                    ;

    }


}
