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
   
    private  final PersonUseCase useCaseperson;
    //private  final UseCase2 useCase2;

    //Sección Person
    public Mono<ServerResponse> getPerson(ServerRequest serverRequest) {
        // usecase.logic()
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

    public Mono<ServerResponse> getPeople(ServerRequest serverRequest) {
        // usecase.logic();

        return ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(useCaseperson.getPeople()
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

        String searchUserName = String.valueOf(serverRequest.pathVariable("nombre"));
        return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(
                                    useCaseperson.getUsersByName(searchUserName)
                                            .map(Handler::UserToUserResponse),
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
                .bodyValue(respuestaCreado));
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
