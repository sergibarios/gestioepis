package gestioepis.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "purchase_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String comments;

    private LocalDate orderDate;

    private String filePath;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<DeliveryNote> deliveryNotes;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<ClothingItem> items;
}