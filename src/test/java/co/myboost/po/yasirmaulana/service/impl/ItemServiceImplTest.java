package co.myboost.po.yasirmaulana.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import co.myboost.po.yasirmaulana.domain.Item;
import co.myboost.po.yasirmaulana.dto.ItemCreateUpdateRequestDTO;
import co.myboost.po.yasirmaulana.dto.ItemListResponseDTO;
import co.myboost.po.yasirmaulana.dto.ResultPageResponseDTO;
import co.myboost.po.yasirmaulana.exception.NotFoundException;
import co.myboost.po.yasirmaulana.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateItem_Success() {
        ItemCreateUpdateRequestDTO dto = new ItemCreateUpdateRequestDTO();
        dto.setName("Item A");
        dto.setDescription("Description A");
        dto.setPrice(100);
        dto.setCost(50);

        itemService.createItem(dto);

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void testUpdateItem_Success() {
        int itemId = 1;
        Item existingItem = new Item();
        existingItem.setId(itemId);
        existingItem.setName("Old Item");

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));

        ItemCreateUpdateRequestDTO dto = new ItemCreateUpdateRequestDTO();
        dto.setName("Updated Item");
        dto.setDescription("Updated Description");
        dto.setPrice(150);
        dto.setCost(75);

        itemService.updateItem(itemId, dto);

        verify(itemRepository, times(1)).save(existingItem);
        assertEquals("Updated Item", existingItem.getName());
        assertEquals("Updated Description", existingItem.getDescription());
        assertEquals(150.0, existingItem.getPrice());
        assertEquals(75.0, existingItem.getCost());
    }

    @Test
    void testUpdateItem_NotFound() {
        int itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        ItemCreateUpdateRequestDTO dto = new ItemCreateUpdateRequestDTO();

        NotFoundException exception = assertThrows(NotFoundException.class, () -> itemService.updateItem(itemId, dto));
        assertEquals("invalid.item.id", exception.getMessage());
    }

    @Test
    void testDeleteItem_Success() {
        int itemId = 1;
        Item existingItem = new Item();
        existingItem.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));

        itemService.deleteItem(itemId);

        verify(itemRepository, times(1)).delete(existingItem);
    }

    @Test
    void testDeleteItem_NotFound() {
        int itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> itemService.deleteItem(itemId));
        assertEquals("invalid.item.id", exception.getMessage());
    }

    @Test
    void testGetItemById_Success() {
        int itemId = 1;
        Item existingItem = new Item();
        existingItem.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));

        Item result = itemService.getItemById(itemId);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
    }

    @Test
    void testGetItemById_NotFound() {
        int itemId = 1;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> itemService.getItemById(itemId));
        assertEquals("invalid.item.id", exception.getMessage());
    }

    @Test
    void testGetItemList_Success() {
        int pages = 0;
        int limit = 10;
        String sortBy = "name";
        String direction = "asc";
        String itemName = "Item";

        Item item = new Item();
        item.setId(1);
        item.setName("Item A");
        item.setDescription("Description A");
        item.setPrice(100);
        item.setCost(50);

        Page<Item> pageResult = new PageImpl<>(List.of(item));
        when(itemRepository.findByNameLikeIgnoreCase(anyString(), any(Pageable.class))).thenReturn(pageResult);

        ResultPageResponseDTO<ItemListResponseDTO> result = itemService.getItemList(pages, limit, sortBy, direction, itemName);

        assertNotNull(result);
        assertEquals(1, result.getResult().size());
        assertEquals(item.getName(), result.getResult().getFirst().getName());
    }

    @Test
    void testFindItemsByIds_Success() {
        List<Integer> itemIds = List.of(1, 2);
        Item item1 = new Item();
        item1.setId(1);
        Item item2 = new Item();
        item2.setId(2);

        when(itemRepository.findAllByIdIn(itemIds)).thenReturn(List.of(item1, item2));

        List<Item> result = itemService.findItemsByIds(itemIds);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testFindAllItem_Success() {
        Item item1 = new Item();
        Item item2 = new Item();

        when(itemRepository.findAll()).thenReturn(List.of(item1, item2));

        List<Item> result = itemService.findAllItem();

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}