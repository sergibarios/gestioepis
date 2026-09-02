package gestioepis.models;

public enum Talla {
    XS("XS", TallaTipus.ROMAN),
    S("S", TallaTipus.ROMAN),
    M("M", TallaTipus.ROMAN),
    L("L", TallaTipus.ROMAN),
    XL("XL", TallaTipus.ROMAN),
    XXL("2XL", TallaTipus.ROMAN),
    XXXL("3XL", TallaTipus.ROMAN),
    XXXXL("4XL", TallaTipus.ROMAN),
    XXXXXL("5XL", TallaTipus.ROMAN),
    C36("36", TallaTipus.CALCAT),
    C37("37", TallaTipus.CALCAT),
    C38("38", TallaTipus.CALCAT),
    C39("39", TallaTipus.CALCAT),
    C40("40", TallaTipus.CALCAT),
    C41("41", TallaTipus.CALCAT),
    C42("42", TallaTipus.CALCAT),
    C43("43", TallaTipus.CALCAT),
    C44("44", TallaTipus.CALCAT),
    C45("45", TallaTipus.CALCAT),
    C46("46", TallaTipus.CALCAT),
    C47("47", TallaTipus.CALCAT),
    C48("48", TallaTipus.CALCAT),
    C49("49", TallaTipus.CALCAT),
    C50("50", TallaTipus.CALCAT),
    UN("Talla única", TallaTipus.UNICA),
    T7("7", TallaTipus.GUANTS),
    T8("8", TallaTipus.GUANTS),
    T9("9", TallaTipus.GUANTS),
    T10("10", TallaTipus.GUANTS),
    P34("34", TallaTipus.PANTALO),
    P36("36", TallaTipus.PANTALO),
    P38("38", TallaTipus.PANTALO),
    P40("40", TallaTipus.PANTALO),
    P42("42", TallaTipus.PANTALO),
    P44("44", TallaTipus.PANTALO),
    P46("46", TallaTipus.PANTALO),
    P48("48", TallaTipus.PANTALO),
    P50("50", TallaTipus.PANTALO),
    P52("52", TallaTipus.PANTALO),
    P54("54", TallaTipus.PANTALO),
    P56("56", TallaTipus.PANTALO),
    P58("58", TallaTipus.PANTALO),
    P60("60", TallaTipus.PANTALO);

    private final String label;
    private final TallaTipus tipus;

    Talla(String label, TallaTipus tipus) {
        this.label = label;
        this.tipus = tipus;
    }

    public String getLabel() {
        return label;
    }

    public TallaTipus getTipus() {
        return tipus;
    }
}
