package co.myboost.po.yasirmaulana.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.class)
public class PodResponseDTO {
    private int pohId;
    private int itemId;
    private String itemName;
    private String itemDesc;
    private int itemQty;
    private int itemPrice;
    private int itemCost;
}
