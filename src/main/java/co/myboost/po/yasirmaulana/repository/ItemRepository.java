package co.myboost.po.yasirmaulana.repository;

import co.myboost.po.yasirmaulana.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<Item> findById(int itemId);
    Page<Item> findByNameLikeIgnoreCase(String itemName, Pageable pageable);
    List<Item> findAllByIdIn(List<Integer> itemIds);
    List<Item> findAll();
}
