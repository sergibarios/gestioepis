package gestioepis.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClothingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;

    private double price;

    @Enumerated(EnumType.STRING)
    private Talla itemSize;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "delivery_note_id")
    private DeliveryNote deliveryNote;

    @ManyToOne
    @JoinColumn(name = "handover_id")
    private Handover handover;
}
