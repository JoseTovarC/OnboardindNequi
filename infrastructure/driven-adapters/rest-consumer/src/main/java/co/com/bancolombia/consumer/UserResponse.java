package co.com.bancolombia.consumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;


@Value
@Builder(toBuilder = true)
@Jacksonized
public class UserResponse {

        Integer id;
        @JsonProperty("first_name")
        String firstName;
        @JsonProperty("last_name")
        String lastName;
        String email;
        String avatar;

}