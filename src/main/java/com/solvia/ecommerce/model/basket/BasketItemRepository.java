package com.solvia.ecommerce.model.basket;

import com.solvia.ecommerce.model.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BasketItemRepository extends CrudRepository<BasketItem, Long> {

    List<BasketItem> findAllByUser(User user);
}
