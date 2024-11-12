package co.myboost.po.yasirmaulana.web;

import co.myboost.po.yasirmaulana.domain.Item;
import co.myboost.po.yasirmaulana.dto.ItemCreateUpdateRequestDTO;
import co.myboost.po.yasirmaulana.dto.ItemListResponseDTO;
import co.myboost.po.yasirmaulana.dto.ResultPageResponseDTO;
import co.myboost.po.yasirmaulana.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/item")
@AllArgsConstructor
public class ItemResource {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<Void> createNewItem(@RequestBody ItemCreateUpdateRequestDTO dto) {
        itemService.createItem(dto);
        return ResponseEntity.created(URI.create("/v1/item")).build();
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Void> updateItem(@PathVariable int itemId, @RequestBody ItemCreateUpdateRequestDTO dto) {
        itemService.updateItem(itemId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable int itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ResultPageResponseDTO<ItemListResponseDTO>> findItemList(
            @RequestParam(name = "page", required = true, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = true, defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", required = true, defaultValue = "asc") String direction,
            @RequestParam(name = "name", required = false) String itemName) {
        return ResponseEntity.ok().body(itemService.getItemList(pages, limit, sortBy, direction, itemName));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> findItemById(@PathVariable int itemId) {
        return ResponseEntity.ok().body(itemService.getItemById(itemId));
    }

}
