package co.myboost.po.yasirmaulana.repository;

import co.myboost.po.yasirmaulana.domain.PoD;
import co.myboost.po.yasirmaulana.domain.PoH;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PoDRepository extends JpaRepository<PoD, Integer> {

    List<PoD> findByPoH(PoH poH);
    List<PoD> findAll();
}
