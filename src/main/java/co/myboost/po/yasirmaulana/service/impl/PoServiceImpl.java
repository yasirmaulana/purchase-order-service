package co.myboost.po.yasirmaulana.service.impl;

import co.myboost.po.yasirmaulana.domain.Item;
import co.myboost.po.yasirmaulana.domain.PoD;
import co.myboost.po.yasirmaulana.domain.PoH;
import co.myboost.po.yasirmaulana.dto.*;
import co.myboost.po.yasirmaulana.exception.NotFoundException;
import co.myboost.po.yasirmaulana.repository.PoDRepository;
import co.myboost.po.yasirmaulana.repository.PoHRepository;
import co.myboost.po.yasirmaulana.service.ItemService;
import co.myboost.po.yasirmaulana.service.PoService;
import co.myboost.po.yasirmaulana.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PoServiceImpl implements PoService {
    private static final String INVALID_POHID = "invalid.poh.id";

    private final PoHRepository poHRepository;
    private final PoDRepository poDRepository;
    private final ItemService itemService;

    @Autowired
    public PoServiceImpl(PoHRepository poHRepository, PoDRepository poDRepository, ItemService itemService) {
        this.poHRepository = poHRepository;
        this.poDRepository = poDRepository;
        this.itemService = itemService;
    }

    @Transactional
    public void createPo(PoCreateUpdateRequestDTO dto) {
        int totalPrice = calculateTotalPrice(dto.getPoDetails());
        int totalCost = calculateTotalCost(dto.getPoDetails());

        PoH poH = buildPoH(dto, totalPrice, totalCost);
        PoH savedPoH = poHRepository.save(poH);

        List<PoD> poDetails = buildPoDetails(dto.getPoDetails(), savedPoH);
        poDRepository.saveAll(poDetails);
    }


    @Transactional
    public void updatePo(int pohId, PoCreateUpdateRequestDTO dto) {
        // delete detail
        PoH pohEdit = poHRepository.findById(pohId).orElseThrow(() -> new NotFoundException(INVALID_POHID));
        List<PoD> pods = poDRepository.findByPoH(pohEdit);
        poDRepository.deleteAll(pods);

        int totalPrice = calculateTotalPrice(dto.getPoDetails());
        int totalCost = calculateTotalCost(dto.getPoDetails());

        PoH poH = buildPohEdit(pohId, dto, totalPrice, totalCost);
        PoH savedPohEdit = poHRepository.save(poH);

        List<PoD> poDetails = buildPoDetails(dto.getPoDetails(), savedPohEdit);
        poDRepository.saveAll(poDetails);
    }

    @Transactional
    public void deletePo(int pohId) {
        PoH poh = poHRepository.findById(pohId).orElseThrow(() -> new NotFoundException(INVALID_POHID));
        List<PoD> pods = poDRepository.findByPoH(poh);
        poDRepository.deleteAll(pods);

        poHRepository.delete(poh);
    }

    @Override
    public PoResponseDTO getPo(int pohId) {
        PoH poh = poHRepository.findById(pohId).orElseThrow(() -> new NotFoundException(INVALID_POHID));
        List<PodResponseDTO> podList = mapItemToPod();

        List<PodResponseDTO> podL = new ArrayList<>();
        for (PodResponseDTO p : podList) {
            if ( p.getPohId()==poh.getId()) {
                PodResponseDTO dto = new PodResponseDTO();
                dto.setPohId(p.getPohId());
                dto.setItemId(p.getItemId());
                dto.setItemName(p.getItemName());
                dto.setItemDesc(p.getItemDesc());
                dto.setItemQty(p.getItemQty());
                dto.setItemPrice(p.getItemPrice());
                dto.setItemCost(p.getItemCost());
                podL.add(dto);
            }
        }

        PoResponseDTO response = new PoResponseDTO();
        response.setId(poh.getId());
        response.setDatetime(poh.getDatetime());
        response.setDescription(poh.getDescription());
        response.setTotalCost(poh.getTotalCost());
        response.setTotalPrice(poh.getTotalPrice());
        response.setPoDetails(podL);

        return response;
    }

    @Override
    public ResultPageResponseDTO<PoResponseDTO> getPoList(Integer pages, Integer limit, String sortBy, String direction) {
        Sort sort = Sort.by(new Sort.Order(PaginationUtil.getSortBy(direction), sortBy));
        Pageable pageable = PageRequest.of(pages, limit, sort);
        Page<PoH> pageResult = poHRepository.findAll(pageable);
        List<PodResponseDTO> podList = mapItemToPod();
        List<PoResponseDTO> dtos = pageResult.stream().map(p -> {
            PoResponseDTO dto = new PoResponseDTO();
            dto.setId(p.getId());
            dto.setDatetime(p.getDatetime());
            dto.setDescription(p.getDescription());
            dto.setTotalPrice(p.getTotalPrice());
            dto.setTotalCost(p.getTotalCost());

            List<PodResponseDTO> podL = new ArrayList<>();
            for (PodResponseDTO podR : podList) {
                if (podR.getPohId()==p.getId()) {
                    podL.add(podR);
                }
            }
            dto.setPoDetails(podL);

            return dto;
        }).toList();

        return PaginationUtil.createResultPageDTO(dtos, pageResult.getTotalElements(), pageResult.getTotalPages());
    }

    private List<PodResponseDTO> mapItemToPod() {
        List<Item> items = itemService.findAllItem();
        List<PoD> pods = poDRepository.findAll();
        return pods.stream().map(p -> {
            PodResponseDTO dto = new PodResponseDTO();
            dto.setPohId(p.getPoH().getId());
            dto.setItemId(p.getItemId());
            dto.setItemQty(p.getItemQty());
            dto.setItemCost(p.getItemCost());
            dto.setItemPrice(p.getItemPrice());
            for (Item item : items) {
                if(item.getId()==p.getItemId()){
                    dto.setItemName(item.getName());
                    dto.setItemDesc(item.getDescription());
                }
            }
            return dto;
        }).toList();
    }

    private int calculateTotalPrice(List<PodCreateRequestDTO> poDetails) {
        return poDetails.stream()
                .mapToInt(podDto -> {
                    Item item = itemService.getItemById(podDto.getItemId());
                    return item.getPrice() * podDto.getItemQty();
                })
                .sum();
    }

    private int calculateTotalCost(List<PodCreateRequestDTO> poDetails) {
        return poDetails.stream()
                .mapToInt(podDto -> {
                    Item item = itemService.getItemById(podDto.getItemId());
                    return item.getCost() * podDto.getItemQty();
                })
                .sum();
    }

    private PoH buildPoH(PoCreateUpdateRequestDTO dto, int totalPrice, int totalCost) {
        PoH poH = new PoH();
        poH.setDatetime(dto.getDatetime());
        poH.setDescription(dto.getDescription());
        poH.setTotalPrice(totalPrice);
        poH.setTotalCost(totalCost);
        return poH;
    }

    private PoH buildPohEdit(int pohId, PoCreateUpdateRequestDTO dto, int totalPrice, int totalCost) {
        PoH poH = poHRepository.findById(pohId).orElseThrow(() -> new RuntimeException("invalid.poh.id"));
        poH.setDatetime(dto.getDatetime());
        poH.setDescription(dto.getDescription());
        poH.setTotalPrice(totalPrice);
        poH.setTotalCost(totalCost);
        return poH;
    }

    private List<PoD> buildPoDetails(List<PodCreateRequestDTO> poDetailsDTO, PoH poH) {
        return poDetailsDTO.stream().map(p -> {
            Item item = itemService.getItemById(p.getItemId());

            PoD poD = new PoD();
            poD.setPoH(poH);
            poD.setItemId(p.getItemId());
            poD.setItemQty(p.getItemQty());
            poD.setItemPrice(item.getPrice());
            poD.setItemCost(item.getCost());
            return poD;
        }).toList();
    }

}
