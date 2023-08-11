package co.com.bancolombia.redis.template.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDataCache {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
}
