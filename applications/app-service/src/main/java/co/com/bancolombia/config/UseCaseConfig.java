package co.com.bancolombia.config;

import co.com.bancolombia.model.person.gateways.RedisGateway;
import co.com.bancolombia.usecase.person.PersonUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import co.com.bancolombia.model.person.gateways.PersonApiService;
import co.com.bancolombia.model.person.gateways.PostgreSQLGateway;
import co.com.bancolombia.model.person.gateways.RestConsumerExternalAPI;

@Configuration
public class UseCaseConfig
{
    @Bean
    public PersonUseCase getPersonUseCase(PersonApiService serviceGateway, PostgreSQLGateway repositoryPostgresGateway,
                                          RestConsumerExternalAPI externalAPI, RedisGateway RedisCacheGateway){
        return new PersonUseCase(serviceGateway, repositoryPostgresGateway, externalAPI, RedisCacheGateway);
    }
}
