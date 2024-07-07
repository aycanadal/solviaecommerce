package com.solvia.ecommerce.model.order;

import com.solvia.ecommerce.model.basket.BasketItem;
import com.solvia.ecommerce.model.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Product product;

    @Min(value = 1)
    private Integer quantity;

    public OrderItem(BasketItem basketItem) {
        this.product = basketItem.getProduct();
        this.quantity = basketItem.getQuantity();
    }
}
