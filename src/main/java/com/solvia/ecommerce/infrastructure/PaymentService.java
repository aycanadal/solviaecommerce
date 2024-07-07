package com.solvia.ecommerce.infrastructure;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentService {

    @Async
    public CompletableFuture<Boolean> charge(String cardNumber, String expiryDate, String cvv, Double amount) throws InterruptedException {

        //Thread.sleep(5000);
        Thread.sleep(5);

        if (Objects.equals(cardNumber, "12345"))
            return CompletableFuture.completedFuture(true);

        return CompletableFuture.completedFuture(false);
    }

}
