package co.myboost.po.yasirmaulana.service;

import co.myboost.po.yasirmaulana.dto.PoCreateUpdateRequestDTO;
import co.myboost.po.yasirmaulana.dto.PoResponseDTO;
import co.myboost.po.yasirmaulana.dto.ResultPageResponseDTO;

public interface PoService {
    void createPo(PoCreateUpdateRequestDTO dto);
    void updatePo(int pohId, PoCreateUpdateRequestDTO dto);
    void deletePo(int pohId);
    PoResponseDTO getPo(int pohId);
    ResultPageResponseDTO<PoResponseDTO> getPoList(Integer pages, Integer limit, String sortBy,
                                                    String direction);
}
