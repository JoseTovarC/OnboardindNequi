package co.com.bancolombia.model.person;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;

}
