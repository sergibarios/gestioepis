package gestioepis.repositories;

import gestioepis.models.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByNameContainingIgnoreCase(String name);
    List<PurchaseOrder> findAllByOrderByOrderDateDesc();
    List<PurchaseOrder> findAllByOrderByOrderDateAsc();
    long countByDeliveryNotesIsEmpty();
    List<PurchaseOrder> findTop5ByOrderByOrderDateDesc();
}
