package co.myboost.po.yasirmaulana.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.class)
public class ItemListResponseDTO {
    private int id;
    private String name;
    private String description;
    private int price;
    private int cost;
}
