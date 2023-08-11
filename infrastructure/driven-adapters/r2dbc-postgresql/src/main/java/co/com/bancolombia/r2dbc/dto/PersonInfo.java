package co.com.bancolombia.r2dbc.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("person")
public class PersonInfo {

    @Id
    Integer id;
    @Column
    String nombre;
    @Column
    Integer documento;
}
