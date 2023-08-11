package co.com.bancolombia.nequiservice;

import co.com.bancolombia.model.person.Person;
import co.com.bancolombia.model.person.gateways.PersonApiService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ApiService implements PersonApiService{
    
    @Override
    public Mono<Person> getPerson(String id){
        return Mono.just(Person.builder().id(Integer.parseInt(id)).documento(1000000).build());
    }

    @Override
    public Mono<Float> getBalance(String id){
        return Mono.just(100000f);
    }
}
