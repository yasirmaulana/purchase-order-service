package co.myboost.po.yasirmaulana.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.class)
public class UserListResponseDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
