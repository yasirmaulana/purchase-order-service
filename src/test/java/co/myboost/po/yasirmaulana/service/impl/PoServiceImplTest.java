package co.myboost.po.yasirmaulana.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import co.myboost.po.yasirmaulana.domain.Item;
import co.myboost.po.yasirmaulana.domain.PoH;
import co.myboost.po.yasirmaulana.dto.PoCreateUpdateRequestDTO;
import co.myboost.po.yasirmaulana.dto.PoResponseDTO;
import co.myboost.po.yasirmaulana.dto.PodCreateRequestDTO;
import co.myboost.po.yasirmaulana.exception.NotFoundException;
import co.myboost.po.yasirmaulana.repository.PoHRepository;
import co.myboost.po.yasirmaulana.repository.PoDRepository;
import co.myboost.po.yasirmaulana.service.ItemService;
import co.myboost.po.yasirmaulana.domain.PoD;
import co.myboost.po.yasirmaulana.dto.PodResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.*;

class PoServiceImplTest {
    @Mock
    private PoHRepository poHRepository;
    @Mock
    private PoDRepository poDRepository;
    @Mock
    private ItemService itemService;

    @InjectMocks
    private PoServiceImpl poService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePo_Success() {
        PoCreateUpdateRequestDTO dto = new PoCreateUpdateRequestDTO();
        dto.setDatetime(LocalDateTime.now());
        dto.setDescription("Test PO");
        dto.setPoDetails(List.of(new PodCreateRequestDTO(1, 5), new PodCreateRequestDTO(2, 3)));

        Item item = new Item();
        item.setId(1);
        item.setName("Item A");
        item.setDescription("Desc");
        item.setPrice(100);
        item.setCost(50);
        when(itemService.getItemById(anyInt())).thenReturn(item);
        when(poHRepository.save(any(PoH.class))).thenAnswer(invocation -> invocation.getArgument(0));

        poService.createPo(dto);

        verify(poHRepository, times(1)).save(any(PoH.class));
        verify(poDRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testUpdatePo_Success() {
        int pohId = 1;
        PoCreateUpdateRequestDTO dto = new PoCreateUpdateRequestDTO();
        dto.setDatetime(LocalDateTime.now());
        dto.setDescription("Updated PO");
        dto.setPoDetails(List.of(new PodCreateRequestDTO(1, 2), new PodCreateRequestDTO(2, 4)));

        PoH existingPoH = new PoH();
        existingPoH.setId(pohId);

        when(poHRepository.findById(pohId)).thenReturn(Optional.of(existingPoH));
        Item item = new Item();
        item.setId(1);
        item.setName("Item A");
        item.setDescription("Desc");
        item.setPrice(100);
        item.setCost(50);
        when(itemService.getItemById(anyInt())).thenReturn(item);

        poService.updatePo(pohId, dto);

        verify(poDRepository, times(1)).deleteAll(anyList());
        verify(poHRepository, times(1)).save(any(PoH.class));
        verify(poDRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testDeletePo_Success() {
        int pohId = 1;
        PoH existingPoH = new PoH();
        existingPoH.setId(pohId);

        when(poHRepository.findById(pohId)).thenReturn(Optional.of(existingPoH));
        when(poDRepository.findByPoH(existingPoH)).thenReturn(Collections.emptyList());

        poService.deletePo(pohId);

        verify(poDRepository, times(1)).deleteAll(anyList());
        verify(poHRepository, times(1)).delete(existingPoH);
    }

    @Test
    void testGetPo_Success() {
        int pohId = 1;
        PoH poH = new PoH();
        poH.setId(pohId);
        poH.setDatetime(LocalDateTime.now());
        poH.setDescription("Test PO");
        poH.setTotalPrice(500);
        poH.setTotalCost(300);

        when(poHRepository.findById(pohId)).thenReturn(Optional.of(poH));
        when(poDRepository.findAll()).thenReturn(Collections.emptyList());

        PoResponseDTO result = poService.getPo(pohId);

        assertEquals(pohId, result.getId());
        assertEquals("Test PO", result.getDescription());
        verify(poHRepository, times(1)).findById(pohId);
    }

    @Test
    void testGetPo_NotFound() {
        int pohId = 999;
        when(poHRepository.findById(pohId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> poService.getPo(pohId));
        verify(poHRepository, times(1)).findById(pohId);
    }

    @Test
    void testGetPoList_Success() {
        // Arrange
        int page = 0;
        int limit = 10;
        String sortBy = "datetime";
        String direction = "asc";

        PoH poH = new PoH();
        poH.setId(1);
        poH.setDatetime(LocalDateTime.now());
        poH.setDescription("Test PO");
        poH.setTotalPrice(1000);
        poH.setTotalCost(500);

        Page<PoH> pageResult = new PageImpl<>(List.of(poH));
        when(poHRepository.findAll(any(Pageable.class))).thenReturn(pageResult);

        PoD poD = new PoD();
        poD.setId(1);
        poD.setPoH(poH);
        poD.setItemId(1);
        poD.setItemQty(10);
        poD.setItemPrice(100);
        poD.setItemCost(50);

        when(poDRepository.findAll()).thenReturn(List.of(poD));

        Item item = new Item();
        item.setId(1);
        item.setName("Item A");
        item.setDescription("Desc");
        item.setPrice(100);
        item.setCost(50);

        when(itemService.findAllItem()).thenReturn(List.of(item));

        // Act
        var result = poService.getPoList(page, limit, sortBy, direction);

        // Assert
        assertEquals(1, result.getResult().size());
        PoResponseDTO responseDto = result.getResult().getFirst();
        assertEquals(poH.getId(), responseDto.getId());
        assertEquals(poH.getDescription(), responseDto.getDescription());
        assertEquals(poH.getTotalPrice(), responseDto.getTotalPrice());
        assertEquals(poH.getTotalCost(), responseDto.getTotalCost());

        List<PodResponseDTO> podResponseList = responseDto.getPoDetails();
        assertEquals(1, podResponseList.size());
        PodResponseDTO podResponseDto = podResponseList.getFirst();
        assertEquals(poD.getItemId(), podResponseDto.getItemId());
        assertEquals(poD.getItemQty(), podResponseDto.getItemQty());
        assertEquals(poD.getItemPrice(), podResponseDto.getItemPrice());
        assertEquals(poD.getItemCost(), podResponseDto.getItemCost());
        assertEquals(item.getName(), podResponseDto.getItemName());
        assertEquals(item.getDescription(), podResponseDto.getItemDesc());

        verify(poHRepository, times(1)).findAll(any(Pageable.class));
        verify(poDRepository, times(1)).findAll();
        verify(itemService, times(1)).findAllItem();
    }
}