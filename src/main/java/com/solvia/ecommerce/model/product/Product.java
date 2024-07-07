package com.solvia.ecommerce.model.product;

import com.solvia.ecommerce.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(indexes = @Index(columnList = "name, description, price, is_on_sale"))
public class Product extends BaseEntity {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Min(value = 0)
    private Double price;

    @NotNull
    @Builder.Default
    @ColumnDefault("false")
    private Boolean isOnSale = false;

}
