package com.polarbookshop.catalogservice.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@RequiredArgsConstructor
@EnableJdbcAuditing
@Configuration
public class DataConfig {


}
