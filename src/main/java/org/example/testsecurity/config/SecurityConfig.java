package org.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("SecurityConfig.securityFilterChain");

        // 권한에 따라 경로 허용
        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                .requestMatchers("/", "/login", "/loginProc", "/join", "/joinProc").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
        );

        http.formLogin((form) -> form
                .loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .permitAll()
        );

        http.csrf(AbstractHttpConfigurer::disable);

        // 다중 로그인 설정
        http.sessionManagement((auth) -> auth
                        .maximumSessions(1) // 동일한 아이디로 최대 동시 로그인 가능 수
                        .maxSessionsPreventsLogin(true)
                        // 최대 동시 로그인 수를 초과했을 때 처리 방법
                        // true: 초과 시 새로운 로그인 차단
                        // false: 초과 시 기존 세션 하나 삭제
        );

        // 세션 고정 보호
        http.sessionManagement((auth) -> auth
                .sessionFixation().changeSessionId());

        return http.build();
    }
}
