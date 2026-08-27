package gestioepis.repositories;

import gestioepis.models.DeliveryNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote, Long> {
    List<DeliveryNote> findAllByOrderByDeliveryDateDesc();
    List<DeliveryNote> findAllByOrderByDeliveryDateAsc();
    List<DeliveryNote> findTop5ByOrderByDeliveryDateDesc();
    List<DeliveryNote> findByReferenceContainingIgnoreCase(String reference);
}