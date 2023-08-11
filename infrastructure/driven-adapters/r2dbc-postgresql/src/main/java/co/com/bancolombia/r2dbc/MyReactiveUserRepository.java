package co.com.bancolombia.r2dbc;

import co.com.bancolombia.r2dbc.dto.UserData;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface MyReactiveUserRepository extends ReactiveCrudRepository<UserData, Integer> {
    /*
    @Modifying
    @Query("insert into usuarios ( id, firstname, lastname, email, avatar) " +
            "values (:#{newUser.getId()} , :#{newUser.getFirstName()} , " +
            ":#{newUser.getLastName()} , :#{newUser.getEmail()} , :#{newUser.getAvatar()} )")
    Mono<UserData> save(UserData newUser);*/

    Mono<UserData> findByIdReal(Integer id);

    Flux<UserData> findByFirstName(String nombre);

}
