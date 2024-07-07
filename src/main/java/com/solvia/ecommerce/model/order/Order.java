package com.solvia.ecommerce.model.order;

import com.solvia.ecommerce.model.basket.BasketItem;
import com.solvia.ecommerce.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name="`Order`", indexes = @Index(columnList = "user_id, status"))
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING_PAYMENT;

    public Order(User user, List<BasketItem> basket) {

        this.user = user;

        for (BasketItem basketItem : basket)
            items.add(new OrderItem(basketItem));

    }

    public Double getTotal(){

        return this.items.stream()
                .mapToDouble(item-> item.getProduct().getPrice() * item.getQuantity())
                .sum();

    }

}
