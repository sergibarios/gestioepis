package gestioepis.repositories;

import gestioepis.models.ClothingItem;
import gestioepis.models.Handover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HandoverRepository extends JpaRepository<Handover, Long> {
    List<Handover> findTop5ByOrderByHandoverDateDesc();
    List<Handover> findByPersonNameContainingIgnoreCase(String name);
    List<Handover> findAllByOrderByHandoverDateDesc();
    List<Handover> findAllByOrderByHandoverDateAsc();

}
