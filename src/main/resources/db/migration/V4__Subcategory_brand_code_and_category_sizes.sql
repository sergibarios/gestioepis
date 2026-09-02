-- Marca i codi passen de l'article individual a la subcategoria
ALTER TABLE subcategory ADD COLUMN brand VARCHAR(255);
ALTER TABLE subcategory ADD COLUMN code VARCHAR(255);

ALTER TABLE clothing_item DROP COLUMN brand;
ALTER TABLE clothing_item DROP COLUMN code;

-- Familia de talles de la subcategoria (decideix quin preset de talles es mostra)
ALTER TABLE subcategory ADD COLUMN talla_tipus VARCHAR(20);

-- Talles permeses per subcategoria (Enum Talla)
CREATE TABLE subcategory_allowed_sizes (
    subcategory_id BIGINT NOT NULL REFERENCES subcategory(id),
    talla VARCHAR(20) NOT NULL,
    PRIMARY KEY (subcategory_id, talla)
);
