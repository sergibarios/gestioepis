package gestioepis.repositories;

import gestioepis.models.ClothingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothingItemRepository extends JpaRepository<ClothingItem, Long> {
    long countByHandoverIsNull();
    List<ClothingItem> findByCodeContainingIgnoreCaseOrSubcategoryContainingIgnoreCase(String code, String subcategory);
    List<ClothingItem> findByLocationNameIgnoreCase(String locationName);
    List<ClothingItem> findByHandoverIsNull();
}
