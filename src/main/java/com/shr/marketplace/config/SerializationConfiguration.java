package com.shr.marketplace.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shr.marketplace.mappers.CustomObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shruti.mishra
 */
@Configuration
public class SerializationConfiguration {

    /**
     * Allows the jackson ObjectMapper to
     * serialize the empty beans as well
     *
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new CustomObjectMapper();
    }
}
