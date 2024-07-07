package com.solvia.ecommerce.model.basket;

import com.solvia.ecommerce.model.product.Product;
import com.solvia.ecommerce.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(indexes = @Index(columnList = "user_id"))
public class BasketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @Min(value = 1)
    private Integer quantity;

    @NotNull
    @ManyToOne
    private User user;

}
