package com.solvia.ecommerce.integration.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.solvia.ecommerce.model.product.Product;
import com.solvia.ecommerce.model.product.ProductRepository;
import net.datafaker.Faker;
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

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class ProductApiTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void userShouldNotDeleteProduct() throws Exception {

        long productId = productRepository.save(
                new Product("test prod", "", 40.00, false)
        ).getId();

        this.mockMvc.perform(
                        delete("/products/" + productId)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()
                                        .authorities(new SimpleGrantedAuthority("USER")))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void adminShouldDeleteProduct() throws Exception {

        long productId = productRepository.save(
                new Product("test prod", "", 40.00, false)
        ).getId();

        this.mockMvc.perform(
                        delete("/products/" + productId)
                                .with(SecurityMockMvcRequestPostProcessors.jwt()
                                        .authorities(new SimpleGrantedAuthority("ADMIN")))
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void userShouldNotCreateProduct() throws Exception {

        ObjectNode productPayload = objectMapper.createObjectNode();
        productPayload.put("name", "test product name");
        productPayload.put("price", 33.00);

        this.mockMvc.perform(
                        post("/products")
                                .with(SecurityMockMvcRequestPostProcessors.jwt()
                                        .authorities(new SimpleGrantedAuthority("USER")))
                                .content(productPayload.toString())
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isForbidden());
    }

    @Test
    void adminShouldCreateProduct() throws Exception {

        ObjectNode productPayload = objectMapper.createObjectNode();
        productPayload.put("name", "test product name");
        productPayload.put("price", 33.00);

        this.mockMvc.perform(
                        post("/products")
                                .with(SecurityMockMvcRequestPostProcessors.jwt()
                                        .authorities(new SimpleGrantedAuthority("ADMIN")))
                                .content(productPayload.toString())
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void userShouldSearchProduct() throws Exception {

        productRepository.saveAll(
                Arrays.asList(
                        new Product("test product 1 xyzqq", "", 40.00, false),
                        new Product("test product 2 xyzqq", "", 40.00, false)
                )
        );

        this.mockMvc.perform(
                        get("/products?name=xyzqq")
                                .with(SecurityMockMvcRequestPostProcessors.jwt()
                                        .authorities(new SimpleGrantedAuthority("USER")))
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.products", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void adminShouldCreateBigProductData() throws Exception {

        ArrayList<Product> products = new ArrayList<>();
        Faker faker = new Faker();
        int maxNumberOfDecimals = 2;
        int minPrice = 1;
        int maxPrice = 1000;

        for (int i = 0; i < 1; i++) { // Replace 1 with 400000
            products.add(new Product(
                    faker.commerce().productName(),
                    faker.commerce().material(),
                    faker.number().randomDouble(maxNumberOfDecimals, minPrice, maxPrice),
                    faker.bool().bool()
            ));
        }

        products.add(new Product("test product 1 xyz", "", 40.00, false));

        for (int i = 0; i < 1; i++) { // Replace 1 with 400000
            products.add(new Product(
                    faker.commerce().productName(),
                    faker.commerce().material(),
                    faker.number().randomDouble(maxNumberOfDecimals, minPrice, maxPrice),
                    faker.bool().bool()
            ));
        }

        productRepository.saveAll(products);

        this.mockMvc.perform(
                        get("/products?name=product 1&isOnSale=true")
                                .with(SecurityMockMvcRequestPostProcessors.jwt()
                                        .authorities(new SimpleGrantedAuthority("USER")))
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.products", hasSize(0)));
    }

    @Test
    void userShouldSearchBigProductData() throws Exception {
        this.mockMvc.perform(
                        get("/products?name=product 1&isOnSale=true")
                                .with(SecurityMockMvcRequestPostProcessors.jwt()
                                        .authorities(new SimpleGrantedAuthority("USER")))
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.products", hasSize(0)));
    }

}
