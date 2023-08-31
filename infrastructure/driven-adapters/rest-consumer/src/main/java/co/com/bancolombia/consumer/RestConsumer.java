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
