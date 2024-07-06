package com.polarbookshop.catalogservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {



    @Bean
    public SecurityFilterChain config(HttpSecurity http)throws Exception{
        return http.authorizeHttpRequests(authRequest ->
                authRequest.requestMatchers("/actuator/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/", "/books/**")
                        .permitAll()
                        .anyRequest().hasRole("employee")
        ).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt) // jwt 기반한 기본 설정이 적용된 oauth2 리소스 서버 지원 활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    // BearerTokenAuthenticationFilter 토큰 인증 수행 필터 같음
    // BearerTokenAuthenticationToken 인증객체
    // JwtAuthenticationConverter : jwt 토큰을 -> 인증 객체로 변환하는 객체,
    // 내부에서 JwtAuthenticationConverter, JwtGrantedAuthoritiesConverter 객체를 사용함
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // roles 클레임에서 추출한다.
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // 각 권한에 접두사를 붙힌다

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter); //jwtAuthenticationConverter 에 추가
        return jwtAuthenticationConverter;
    }
}
