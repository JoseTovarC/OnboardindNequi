package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.person.Person;
import co.com.bancolombia.model.person.User;
import co.com.bancolombia.model.person.gateways.PostgreSQLGateway;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import co.com.bancolombia.r2dbc.dto.PersonInfo;
import co.com.bancolombia.r2dbc.dto.UserData;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
@RequiredArgsConstructor
public class MyReactiveRepositoryAdapter implements PostgreSQLGateway
// implements ModelRepository from domain
{
    private final MyReactiveUserRepository myOtherRepository;
    private final MyReactivePersonRepository myLocalRepository;

   // Sección de personas
    @Override
    public Mono<Person> getPersonById(String id) {

            return myLocalRepository.findById(Integer.parseInt(id))
                    .map(MyReactiveRepositoryAdapter::PersonInfoToPerson);
    }

    @Override
    public Flux<Person> getPeople() {
        return myLocalRepository.findAll()
                .map(MyReactiveRepositoryAdapter::PersonInfoToPerson);
    }



    private static Person PersonInfoToPerson(PersonInfo humanito) {
        return Person
                .builder()
                .id(humanito.getId())
                .nombre(humanito.getNombre())
                .documento(humanito.getDocumento())
                .build();
    }


    //Sección de Usuarios
    @Override
    public Mono<User> getUserById(String id) {
        return myOtherRepository.findByIdReal(Integer.parseInt(id))
                .map(MyReactiveRepositoryAdapter::UserDataToUser);
    }
    @Override
    public Flux<User> getUsers() {
        return myOtherRepository.findAll()
                .map(MyReactiveRepositoryAdapter::UserDataToUser);
    }

    public Flux<User> getUsersByName(String nombre) {
        return myOtherRepository.findByFirstName(nombre)
                .map(MyReactiveRepositoryAdapter::UserDataToUser);
    }


    public Mono<User> saveUser(User user) {
        /*return myOtherRepository.save(
                        UserData.builder()
                                .id(user.getId())
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .email(user.getEmail())
                                .avatar(user.getAvatar())
                                .build()
                )*/
        return myOtherRepository.findByIdReal(user.getId())
                .switchIfEmpty(Mono.defer(

                        () -> myOtherRepository.save(
                            UserData.builder()
                                    .idReal(user.getId())
                                    .firstName(user.getFirstName())
                                    .lastName(user.getLastName())
                                    .email(user.getEmail())
                                    .avatar(user.getAvatar())
                                    .build()
                        )
                ))
                .map(MyReactiveRepositoryAdapter::UserDataToUser);

    }


    private static User UserDataToUser(UserData usuarito) {
        return User
                .builder()
                .id(usuarito.getIdReal())
                .firstName(usuarito.getFirstName())
                .lastName(usuarito.getLastName())
                .email(usuarito.getEmail())
                .avatar(usuarito.getAvatar())
                .build();
    }


}
