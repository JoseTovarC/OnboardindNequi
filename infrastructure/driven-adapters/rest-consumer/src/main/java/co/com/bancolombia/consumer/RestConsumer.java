package co.com.bancolombia.consumer;

import co.com.bancolombia.model.person.User;
import co.com.bancolombia.model.person.gateways.RestConsumerExternalAPI;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements RestConsumerExternalAPI {
    private final WebClient client;


    // these methods are an example that illustrates the implementation of WebClient.
    // You should use the methods that you implement from the Gateway from the domain.
    //@CircuitBreaker(name = "testGet" /*, fallbackMethod = "testGetOk"*/)
    /*public Mono<UserResponse> testGet() {
        return client
                .get()
                .retrieve()
                .bodyToMono(UserResponse.class);
    }*/

// Possible fallback method
//    public Mono<String> testGetOk(Exception ignored) {
//        return client
//                .get()  //TOD0: change for another endpoint or destination
//                .retrieve()
//                .bodyToMono(String.class);
//    }

    /*@CircuitBreaker(name = "testPost")
    public Mono<UserResponse> testPost() {
        UserRequest request = UserRequest.builder()
            .val1("exampleval1")
            .val2("exampleval2")
            .build();
        return client
                .post()
                .body(Mono.just(request), UserRequest.class)
                .retrieve()
                .bodyToMono(UserResponse.class);
    }*/

    @Override
    public Mono<User> getUserById(Integer id) {
        return client
                .get()
                .uri(uriBuilder -> uriBuilder.path("/{id}").build(id))
                .retrieve()
                .bodyToMono(UserRequest.class)
                .map(this::UserRequestToUser);

    }


    private User UserRequestToUser(UserRequest data) {
        return User
                .builder()
                .id(data.getData().getId())
                .firstName(data.getData().getFirstName())
                .lastName(data.getData().getLastName())
                .email(data.getData().getEmail())
                .avatar(data.getData().getAvatar())
                .build();
    }
}
