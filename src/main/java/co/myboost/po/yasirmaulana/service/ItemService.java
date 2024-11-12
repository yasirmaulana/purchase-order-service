package co.myboost.po.yasirmaulana.service;

import co.myboost.po.yasirmaulana.domain.Item;
import co.myboost.po.yasirmaulana.domain.User;
import co.myboost.po.yasirmaulana.dto.*;

import java.util.List;

public interface ItemService {
    void createItem(ItemCreateUpdateRequestDTO dto);
    void updateItem(int itemId, ItemCreateUpdateRequestDTO dto);
    void deleteItem(int itemId);
    Item getItemById(int itemId);
    ResultPageResponseDTO<ItemListResponseDTO> getItemList(Integer pages, Integer limit, String sortBy,
                                                           String direction, String itemName);
    List<Item> findItemsByIds(List<Integer> itemIds);
    List<Item> findAllItem();
}
