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
import reactor.core.publisher.Mono;
import co.com.bancolombia.usecase.person.PersonUseCase;
import co.com.bancolombia.model.person.Person;

@Component
@RequiredArgsConstructor
public class Handler {
   
    private  final PersonUseCase useCasePerson;


    //Sección Person
    public Mono<ServerResponse> getPerson(ServerRequest serverRequest) {
        // usecase.logic()
        return useCasePerson.getPerson(serverRequest.pathVariable("id"))
                .map(Handler::PersonToPersonResponse)
                .flatMap(response -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));


    }

    public Mono<ServerResponse> getPersonById(ServerRequest serverRequest) {
        // usecase.logic();

        return useCasePerson.getPersonById(serverRequest.pathVariable("id"))
                .map(Handler::PersonToPersonResponse)
                .flatMap(response -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response));

    }

    public Mono<ServerResponse> getPeople(ServerRequest serverRequest) {
        // usecase.logic();

        return ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(useCasePerson.getPeople()
                                            .map(Handler::PersonToPersonResponse),
                                PersonResponse.class
                );

    }

     private static PersonResponse PersonToPersonResponse(Person personModel) {
        return PersonResponse
                .builder()
                .id(personModel.getId())
                .nombre(personModel.getNombre())
                .documento(personModel.getDocumento())
                .build();
    }

    //Seccion User
    public Mono<ServerResponse> getUserById(ServerRequest serverRequest) {
        // usecase.logic();

        return useCasePerson.getUserById(serverRequest.pathVariable("id"))
                .map(Handler::UserToUserResponse)
                .flatMap(response -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(errorResponse -> Mono.just("Error " + errorResponse.getMessage())
                        .flatMap(errorMessage -> ServerResponse.ok()
                                .contentType(MediaType.TEXT_PLAIN)
                                .bodyValue(errorMessage)));

    }

    public Mono<ServerResponse> getUsersByName(ServerRequest serverRequest) {
        // usecase.logic();

        String searchUserName = String.valueOf(serverRequest.pathVariable("name"));
        return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(
                                    useCasePerson.getUsersByName(searchUserName)
                                            .map(Handler::UserToUserResponse),
                                    UserResponse.class
                            );



    }



    public Mono<ServerResponse> getUsers(ServerRequest serverRequest) {
        // usecase.logic();

        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(useCasePerson.getUsers()
                                .map(Handler::UserToUserResponse),
                        UserResponse.class
                );

    }


    public Mono<ServerResponse> createUser(UserIdRequest serverRequest) {
        // usecase.logic();
        return  useCasePerson.createUser(serverRequest.getRequestId())
                .map(Handler::UserToUserResponse)
                .flatMap(userResponseCreated -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userResponseCreated))
                .onErrorResume(errorResponse -> Mono.just("Error " + errorResponse.getMessage())
                        .flatMap(errorMessage -> ServerResponse.ok()
                                .contentType(MediaType.TEXT_PLAIN)
                                .bodyValue(errorMessage)));
    }

    private static UserResponse UserToUserResponse(User localUser) {
        return UserResponse
                .builder()
                .id(localUser.getId())
                .firstName(localUser.getFirstName())
                .lastName(localUser.getLastName())
                .email(localUser.getEmail())
                .avatar(localUser.getAvatar())
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
