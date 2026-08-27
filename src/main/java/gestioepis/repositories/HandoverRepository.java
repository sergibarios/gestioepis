package gestioepis.repositories;

import gestioepis.models.ClothingItem;
import gestioepis.models.Handover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HandoverRepository extends JpaRepository<Handover, Long> {
    long countByHandoverDate(LocalDate date);
    List<Handover> findTop5ByOrderByHandoverDateDesc();
    List<Handover> findByPersonNameContainingIgnoreCase(String name);
    List<Handover> findAllByOrderByHandoverDateDesc();

}
