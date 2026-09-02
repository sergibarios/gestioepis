package gestioepis.models;

public enum TallaTipus {
    ROMAN("De XS a 5XL"),
    PANTALO("De 34 a 60"),
    CALCAT("Calçat"),
    UNICA("Talla única"),
    GUANTS("Guants");


    private final String label;

    TallaTipus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
