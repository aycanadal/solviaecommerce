package com.solvia.ecommerce.application.controllers;

import com.solvia.ecommerce.model.user.User;
import jakarta.validation.constraints.NotNull;

public class PaymentInfo {
    @NotNull
    public User user;
    public String cardNumber;
    public String expiryDate;
    public String cvv;
}
