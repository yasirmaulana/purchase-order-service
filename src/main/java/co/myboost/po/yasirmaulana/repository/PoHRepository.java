package co.myboost.po.yasirmaulana.repository;

import co.myboost.po.yasirmaulana.domain.PoH;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PoHRepository extends JpaRepository<PoH, Integer> {
    Optional<PoH> findById(int pohId);
    Page<PoH> findAll(Pageable pageable);
}
