package co.com.bancolombia.consumer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder(toBuilder = true)
@Jacksonized
public class UserRequest {

        @JsonProperty("data")
        UserResponse data;

}
