package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import co.com.bancolombia.api.dto.UserIdRequest;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/person/{id}"), handler::getPerson)
                .and(route(GET("/api/personita/{id}"), handler::getPersonById))
                .and(route(GET("/api/personitas"), handler::getPeople))
                //Seccion Usuarios
                .and(route(GET("/api/usuario/{id}"), handler::getUserById))
                .and(route(GET("/api/usuarios/{name}"), handler::getUsersByName))
                .and(route(GET("/api/usuarios"), handler::getUsers))
                .and(route(POST("/api/usuarios"),
                        request -> request
                                .bodyToMono(UserIdRequest.class)
                                .flatMap(handler::createUser)));
                /*.andRoute(POST("/api/usecase/otherpath"), handler::listenPOSTUseCase)
                .and(route(GET("/api/otherusercase/path"), handler::listenGETOtherUseCase));*/
    }
}
