package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.PersonResponse;
import co.com.bancolombia.api.dto.UserIdRequest;
import co.com.bancolombia.api.dto.UserResponse;
import co.com.bancolombia.model.person.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import co.com.bancolombia.usecase.person.PersonUseCase;
import co.com.bancolombia.model.person.Person;

@Component
@RequiredArgsConstructor
public class Handler {
   
    private  final PersonUseCase useCaseperson;
    //private  final UseCase2 useCase2;

    //Sección Person
    public Mono<ServerResponse> getPerson(ServerRequest serverRequest) {
        // usecase.logic();
        useCaseperson.getPerson(serverRequest.pathVariable("id")).subscribe(data -> System.out.println(data.getId()));

        return useCaseperson.getPerson(serverRequest.pathVariable("id"))
                .map(Handler::PersonToPersonResponse)
                .flatMap(Respuestac -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Respuestac));

    }

    public Mono<ServerResponse> getPersonById(ServerRequest serverRequest) {
        // usecase.logic();

        return useCaseperson.getPersonById(serverRequest.pathVariable("id"))
                .map(Handler::PersonToPersonResponse)
                .flatMap(Respuestac -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Respuestac));

    }

    public Mono<ServerResponse> getPersonas(ServerRequest serverRequest) {
        // usecase.logic();

        return ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(useCaseperson.getPersonas()
                                            .map(Handler::PersonToPersonResponse),
                                PersonResponse.class
                );

    }

     private static PersonResponse PersonToPersonResponse(Person humanito) {
        return PersonResponse
                .builder()
                .id(humanito.getId())
                .nombre(humanito.getNombre())
                .documento(humanito.getDocumento())
                .build();
    }

    //Seccion User
    public Mono<ServerResponse> getUserById(ServerRequest serverRequest) {
        // usecase.logic();

        return useCaseperson.getUserById(serverRequest.pathVariable("id"))
                .map(Handler::UserToUserResponse)
                .flatMap(Respuestac -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Respuestac));

    }

    public Mono<ServerResponse> getUsersByName(ServerRequest serverRequest) {
        // usecase.logic();

        String nombre = String.valueOf(serverRequest.pathVariable("nombre"));
        return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(
                                    useCaseperson
                                            .getUsersByName(nombre)
                                            .switchIfEmpty( Flux.defer( () ->
                                                            useCaseperson.getUsers()
                                                            ))
                                            .map(Handler::UserToUserResponse)

                                    ,
                                    UserResponse.class
                );



    }



    public Mono<ServerResponse> getUsers(ServerRequest serverRequest) {
        // usecase.logic();

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(useCaseperson.getUsers()
                                .map(Handler::UserToUserResponse),
                        UserResponse.class
                );

    }


    public Mono<ServerResponse> createUser(UserIdRequest serverRequest) {
        // usecase.logic();
        return  useCaseperson.createUser(serverRequest.getRequestId())
                .map(Handler::UserToUserResponse)
                .flatMap(respuestaCreado -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(respuestaCreado))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private static UserResponse UserToUserResponse(User usuarito) {
        return UserResponse
                .builder()
                .id(usuarito.getId())
                .firstName(usuarito.getFirstName())
                .lastName(usuarito.getLastName())
                .email(usuarito.getEmail())
                .avatar(usuarito.getAvatar())
                .build();
    }






/*
    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // usecase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // usecase.logic();
        return ServerResponse.ok().bodyValue("");
    }

 */
}
