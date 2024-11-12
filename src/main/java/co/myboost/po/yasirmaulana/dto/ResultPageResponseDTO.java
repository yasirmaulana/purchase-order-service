package co.myboost.po.yasirmaulana.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.class)
public class ResultPageResponseDTO<T> {
    private List<T> result;
    private Integer page;
    private Long element;
}
