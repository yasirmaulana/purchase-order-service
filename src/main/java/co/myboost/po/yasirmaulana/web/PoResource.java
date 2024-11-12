package co.myboost.po.yasirmaulana.web;

import co.myboost.po.yasirmaulana.dto.PoCreateUpdateRequestDTO;
import co.myboost.po.yasirmaulana.dto.PoResponseDTO;
import co.myboost.po.yasirmaulana.dto.ResultPageResponseDTO;
import co.myboost.po.yasirmaulana.service.PoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/po")
@AllArgsConstructor
public class PoResource {

    private final PoService poService;

    @PostMapping
    public ResponseEntity<Void> createNewPo(@RequestBody PoCreateUpdateRequestDTO dto) {
        poService.createPo(dto);
        return ResponseEntity.created(URI.create("/v1/po")).build();
    }

    @PutMapping("/{pohId}")
    public ResponseEntity<Void> editPo(@PathVariable int pohId, @RequestBody PoCreateUpdateRequestDTO dto) {
        poService.updatePo(pohId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{pohId}")
    public ResponseEntity<Void> deletePo(@PathVariable int pohId) {
        poService.deletePo(pohId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{pohId}")
    public ResponseEntity<PoResponseDTO> getPo(@PathVariable int pohId) {
        return ResponseEntity.ok().body(poService.getPo(pohId));
    }

    @GetMapping
    public ResponseEntity<ResultPageResponseDTO<PoResponseDTO>> findItemList(
            @RequestParam(name = "page", required = true, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = true, defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", required = true, defaultValue = "asc") String direction) {
        return ResponseEntity.ok().body(poService.getPoList(pages, limit, sortBy, direction));
    }
}
