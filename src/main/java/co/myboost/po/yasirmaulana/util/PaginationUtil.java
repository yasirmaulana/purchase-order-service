package co.myboost.po.yasirmaulana.util;

import co.myboost.po.yasirmaulana.dto.ResultPageResponseDTO;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PaginationUtil {
    public static <T> ResultPageResponseDTO<T> createResultPageDTO(List<T> dtos, Long totalElement, Integer pages) {
        ResultPageResponseDTO<T> result = new ResultPageResponseDTO<>();
        result.setPage(pages);
        result.setElement(totalElement);
        result.setResult(dtos);
        return result;
    }

    public static Sort.Direction getSortBy(String sortBy) {
        if(sortBy.equalsIgnoreCase("asc")) {
            return Sort.Direction.ASC;
        } else {
            return Sort.Direction.DESC;
        }
    }
}
