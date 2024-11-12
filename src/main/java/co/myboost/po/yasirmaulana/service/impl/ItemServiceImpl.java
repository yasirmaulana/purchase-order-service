package co.myboost.po.yasirmaulana.service.impl;

import co.myboost.po.yasirmaulana.domain.Item;
import co.myboost.po.yasirmaulana.dto.ItemCreateUpdateRequestDTO;
import co.myboost.po.yasirmaulana.dto.ItemListResponseDTO;
import co.myboost.po.yasirmaulana.dto.ResultPageResponseDTO;
import co.myboost.po.yasirmaulana.exception.NotFoundException;
import co.myboost.po.yasirmaulana.repository.ItemRepository;
import co.myboost.po.yasirmaulana.service.ItemService;
import co.myboost.po.yasirmaulana.util.PaginationUtil;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private static final String INVALID_ITEMID = "invalid.item.id";

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void createItem(ItemCreateUpdateRequestDTO dto) {
        Item item = mapDtoToItem(dto);
        itemRepository.save(item);
    }

    @Override
    public void updateItem(int itemId, ItemCreateUpdateRequestDTO dto) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(INVALID_ITEMID));
        item.setName(Optional.ofNullable(dto.getName()).orElse(item.getName()));
        item.setDescription(Optional.ofNullable(dto.getDescription()).orElse(item.getDescription()));
        item.setPrice(Optional.of(dto.getPrice()).orElse(item.getPrice()));
        item.setCost(Optional.of(dto.getCost()).orElse(item.getCost()));

        itemRepository.save(item);
    }

    @Override
    public void deleteItem(int itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(INVALID_ITEMID));
        itemRepository.delete(item);
    }

    @Override
    public Item getItemById(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(INVALID_ITEMID));

    }

    @Override
    public ResultPageResponseDTO<ItemListResponseDTO> getItemList(Integer pages, Integer limit, String sortBy, String direction, String itemName) {
        itemName = StringUtils.isBlank(itemName)?"%":itemName+"%";
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<Item> pageResult = itemRepository.findByNameLikeIgnoreCase(itemName, pageable);
        List<ItemListResponseDTO> dtos = pageResult.stream().map(p -> {
            ItemListResponseDTO dto = new ItemListResponseDTO();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setDescription(p.getDescription());
            dto.setPrice(p.getPrice());
            dto.setCost(p.getCost());

            return dto;
        }).toList();
        return PaginationUtil.createResultPageDTO(dtos, pageResult.getTotalElements(), pageResult.getTotalPages());

    }

    @Override
    public List<Item> findItemsByIds(List<Integer> itemIds) {
        return itemRepository.findAllByIdIn(itemIds);
    }

    @Override
    public List<Item> findAllItem() {
        return itemRepository.findAll();
    }

    private Item mapDtoToItem(ItemCreateUpdateRequestDTO dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setCost(dto.getCost());

        return  item;
    }
}
