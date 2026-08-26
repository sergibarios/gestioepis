package gestioepis.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "purchase_order")
@Getter
@Setter
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