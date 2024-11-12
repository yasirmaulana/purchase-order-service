package co.myboost.po.yasirmaulana.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.class)
@AllArgsConstructor
public class PodCreateRequestDTO {
    private int itemId;
    private int itemQty;
}
