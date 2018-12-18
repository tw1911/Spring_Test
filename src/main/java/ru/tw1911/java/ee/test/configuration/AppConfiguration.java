package ru.tw1911.java.ee.test.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tw1911.java.ee.test.utils.DtoMapper;

@Configuration
public class AppConfiguration {
    @Bean
    public DtoMapper mapper(){
        return new DtoMapper();
    }
}
