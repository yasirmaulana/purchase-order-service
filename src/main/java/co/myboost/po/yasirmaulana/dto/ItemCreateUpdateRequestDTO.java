package co.myboost.po.yasirmaulana.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.class)
public class ItemCreateUpdateRequestDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private int price;
    @NotBlank
    private int cost;

}
