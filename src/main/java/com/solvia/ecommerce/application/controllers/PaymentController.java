package com.solvia.ecommerce.application.controllers;

import com.solvia.ecommerce.infrastructure.PaymentService;
import com.solvia.ecommerce.model.basket.BasketItem;
import com.solvia.ecommerce.model.basket.BasketItemRepository;
import com.solvia.ecommerce.model.order.Order;
import com.solvia.ecommerce.model.order.OrderRepository;
import com.solvia.ecommerce.model.order.OrderStatus;
import com.solvia.ecommerce.model.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@BasePathAwareController
public class PaymentController {

    private final BasketItemRepository basketItemRepository;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    @Autowired
    public PaymentController(BasketItemRepository basketItemRepository, OrderRepository orderRepository, PaymentService paymentService) {
        this.basketItemRepository = basketItemRepository;
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    @RequestMapping(value = "/makePayment", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Order> makePayment(@Valid @RequestBody PaymentInfo paymentInfo) throws InterruptedException, ExecutionException {

        User user = paymentInfo.user;
        List<BasketItem> basket = this.basketItemRepository.findAllByUser(user);

        if (basket.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Basket cannot be empty.");

        Order order = new Order(user, basket);
        Double chargeAmount = order.getTotal();
        Future<Boolean> futurePaymentResult = paymentService.charge(paymentInfo.cardNumber, paymentInfo.expiryDate, paymentInfo.cvv, chargeAmount);
        Boolean isPaymentSuccess = futurePaymentResult.get();

        if (!isPaymentSuccess)
            throw new ResponseStatusException(
                    HttpStatus.PAYMENT_REQUIRED, "Payment failure.");

        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);
        basketItemRepository.deleteAll(basket);
        return new ResponseEntity<>(order, HttpStatus.CREATED);

    }
}
