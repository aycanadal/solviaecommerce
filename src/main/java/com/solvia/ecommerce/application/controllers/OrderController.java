package com.solvia.ecommerce.application.controllers;

import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RepositoryRestController
public class OrderController {

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<String> createOrder() {
        return new ResponseEntity<>("Use the /make-payment endpoint to create a new order.", HttpStatus.METHOD_NOT_ALLOWED);
    }

}
