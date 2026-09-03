package gestioepis.repositories;

import gestioepis.models.ClothingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothingItemRepository extends JpaRepository<ClothingItem, Long> {
    List<ClothingItem> findBySubcategory_CodeContainingIgnoreCaseOrSubcategory_NameContainingIgnoreCase(String code, String subcategoryName);
    List<ClothingItem> findByLocationNameIgnoreCase(String locationName);
    List<ClothingItem> findByHandoverIsNull();

}
