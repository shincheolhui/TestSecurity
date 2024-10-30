package org.example.testsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // RoleHierarchyImpl()  is deprecated!
//    @Bean
//    public RoleHierarchy roleHierarchy() {
//
//        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
//
//        hierarchy.setHierarchy("ROLE_C > ROLE_B\n" +
//                               "ROLE_B > ROLE_A");
//
//        return hierarchy;
//    }

    // Spring Security 6.3.x~
    // 변경된 RoleHierarchyImpl() 방식 사용 : fromHierarchy 메소드 활용
//    @Bean
//    public RoleHierarchy roleHierarchy() {
//
//        return RoleHierarchyImpl.fromHierarchy("""
//            ROLE_C > ROLE_B
//            ROLE_B > ROLE_A
//            """);
//    }

    // 메소드 형식 : 명시적으로 접두사("ROLE_") 작성
//    @Bean
//    public RoleHierarchy roleHierarchy() {
//
//        return RoleHierarchyImpl.withRolePrefix("ROLE_")
//                .role("C").implies("B")
//                .role("B").implies("A")
//                .build();
//    }

    // 메소드 형식 : 자동으로 ROLE_ 접두사 붙임
    @Bean
    public RoleHierarchy roleHierarchy() {

        return RoleHierarchyImpl.withDefaultRolePrefix()
                .role("C").implies("B")
                .role("B").implies("A")
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.csrf((auth) -> auth.disable());

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login").permitAll()
                .requestMatchers("/").hasAnyRole("A")
                .requestMatchers("/manager").hasAnyRole("B")
                .requestMatchers("/admin").hasAnyRole("C")
                .anyRequest().authenticated()
        );

        http.formLogin((auth) -> auth.loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .permitAll()
        );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user1 = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("1234"))
                .roles("C")
                .build();

        UserDetails user2 = User.builder()
                .username("user2")
                .password(passwordEncoder().encode("1234"))
                .roles("B")
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }
}
