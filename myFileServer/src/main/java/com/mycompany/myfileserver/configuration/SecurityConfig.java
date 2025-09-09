/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Этот сервис - главное сердце безоапсности
 * Оно проверяет пользователя, его логин и пароль, внедряется в конфигах Security (SecurityConfig) для проверки
 * @author User
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    // Внедряем сервис с проверкой на авторизацию пользователя
    @Autowired
    private UserDetailsService userDetailsService;
    
    // Кодировщик паролей (обязательно строим этот бин для шифрации в базе данных!)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // Настройка бина с доступам к страницам
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/login", "/public/**", "/static/**")
                    .permitAll() // эти страницы доступны всем

                    .requestMatchers("/admin/**").hasAnyRole("ADMIN") // только для админов
                    .anyRequest().authenticated() // остальные только для вошедших
                )
                .formLogin(form -> form
                    .loginPage("/login.html") // страанаица входа
                    .loginProcessingUrl("/login") // КРИТИЧЕСКИ ВАЖНО - endpoint для обработки формы
                    .defaultSuccessUrl("/upload.html") // куда перейти после входа
                    .permitAll()
                )
                .logout(logout -> logout
                    .logoutUrl("/logout") // endpoint для выхода
                    .logoutSuccessUrl("/main") // куда перейти после выхода
                    .permitAll()
                ).userDetailsService(userDetailsService) // добавляем сам сервисный бин с проверкой для аутентификации
                .csrf(csrf -> csrf.disable()) //отключение csrf токенов для пост запросов (по умолчанию требует, иначе будет 403)
                .build();
    }
}
