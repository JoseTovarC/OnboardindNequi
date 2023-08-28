package co.com.bancolombia.r2dbc.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("usuarios")
public class UserData {

    //@Id
    @Column("second_id")
    Integer secondId;
    @Column("id")
    Integer idReal;
    @Column("firstname")
    String firstName;
    @Column("lastname")
    String lastName;
    @Column
    String email;
    @Column
    String avatar;

}
