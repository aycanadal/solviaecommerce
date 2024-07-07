package com.solvia.ecommerce.application.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class CustomRepositoryRestConfigurer implements RepositoryRestConfigurer {
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {

        // Fixes validation errors returning as 500.
        validatingListener.addValidator("beforeCreate", validator());
        validatingListener.addValidator("afterCreate", validator());
        validatingListener.addValidator("beforeSave", validator());
        validatingListener.addValidator("afterSave", validator());
    }
}