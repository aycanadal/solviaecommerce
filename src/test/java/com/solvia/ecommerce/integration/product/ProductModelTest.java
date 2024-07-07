package com.solvia.ecommerce.integration.product;

import com.solvia.ecommerce.model.product.Product;
import com.solvia.ecommerce.model.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductModelTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @WithMockUser(username = "admin", authorities={"ADMIN"})
    void shouldCreateNewProduct(){

        long initialCount = productRepository.count();
        Product product = new Product("product 1", "description 1", 50.00, false);
        productRepository.save(product);
        long newCount = productRepository.count();
        assertEquals("Product is not successfully added.", newCount,initialCount + 1);

    }

}
