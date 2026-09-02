package gestioepis.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subcategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String brand;

    private String code;

    @Enumerated(EnumType.STRING)
    private TallaTipus tallaTipus;

    @ElementCollection(targetClass = Talla.class)
    @CollectionTable(name = "subcategory_allowed_sizes", joinColumns = @JoinColumn(name = "subcategory_id"))
    @Column(name = "talla")
    @Enumerated(EnumType.STRING)
    private Set<Talla> allowedSizes;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "subcategory")
    private List<ClothingItem> clothingItems;

    public String getAllowedSizesCsv() {
        return allowedSizes == null ? "" : allowedSizes.stream().map(Enum::name).collect(Collectors.joining(","));
    }
}
