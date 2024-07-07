package com.solvia.ecommerce.integration.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.solvia.ecommerce.model.basket.BasketItemRepository;
import com.solvia.ecommerce.model.order.OrderStatus;
import com.solvia.ecommerce.model.product.Product;
import com.solvia.ecommerce.model.product.ProductRepository;
import com.solvia.ecommerce.model.user.Role;
import com.solvia.ecommerce.model.user.User;
import com.solvia.ecommerce.model.user.UserRepository;
import graphql.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class OrderApiTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BasketItemRepository basketItemRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void userCanCreateOrder() throws Exception {

        Product product1 = productRepository.save(new Product("test prod", "", 40.00, false));
        Product product2 = productRepository.save(new Product("test prod 2", "", 80.00, false));
        User user = userRepository.save(new User(null, "test", "test@test.com", "test", Role.USER));
        int product1Quantity = 3;
        int product2Quantity = 2;

        ObjectNode basketItem1Payload = objectMapper.createObjectNode();
        basketItem1Payload.put("quantity", product1Quantity);
        basketItem1Payload.put("product", "/" + product1.getId());
        basketItem1Payload.put("user", "/" + user.getId());

        this.mockMvc.perform(
                        post("/basketItems")
                                .with(SecurityMockMvcRequestPostProcessors.jwt()
                                        .authorities(new SimpleGrantedAuthority("USER")))
                                .content(basketItem1Payload.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());

        ObjectNode basketItem2Payload = objectMapper.createObjectNode();
        basketItem2Payload.put("quantity", product2Quantity);
        basketItem2Payload.put("product", "/" + product2.getId());
        basketItem2Payload.put("user", "/" + user.getId());

        this.mockMvc.perform(
                        post("/basketItems")
                                .with(SecurityMockMvcRequestPostProcessors.jwt()
                                        .authorities(new SimpleGrantedAuthority("USER")))
                                .content(basketItem2Payload.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());

        Assert.assertTrue(!basketItemRepository.findAllByUser(user).isEmpty());

        ObjectNode paymentPayload = objectMapper.createObjectNode();
        paymentPayload.put("user", "/" + user.getId());
        paymentPayload.put("cardNumber", "12345");
        paymentPayload.put("expiryDate", "1225");
        paymentPayload.put("cvv", "123");

        this.mockMvc.perform(
                        post("/makePayment")
                                .with(SecurityMockMvcRequestPostProcessors.jwt()
                                        .authorities(new SimpleGrantedAuthority("USER")))
                                .content(paymentPayload.toString())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath(
                        "total",
                        equalTo(product1.getPrice()*product1Quantity + product2.getPrice()*product2Quantity)
                ))
                .andExpect(jsonPath(
                        "status", equalTo(OrderStatus.PROCESSING.toString())
                ));

        Assert.assertTrue(basketItemRepository.findAllByUser(user).isEmpty());

    }
}
