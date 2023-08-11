package co.com.bancolombia.api.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
public class PersonResponse {
    Integer id;
    String nombre;
    Integer documento;

}
