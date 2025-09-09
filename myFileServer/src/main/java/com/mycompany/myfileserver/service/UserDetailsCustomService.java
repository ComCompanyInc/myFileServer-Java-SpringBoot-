/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.service;

import com.mycompany.myfileserver.entity.Access;
import com.mycompany.myfileserver.repository.AccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class UserDetailsCustomService implements UserDetailsService {

    // Внедряем бин репозитория для поиска доступа по логину
    @Autowired
    AccessRepository accessRepository;
    
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // ищем пользователя по логину
        
        System.out.println("i: " + login);
        try {
        System.out.println("!!!!!!!!!!!!!!!!!!! " + accessRepository.findByLogin(login).get().getLogin());
        } catch(Exception e){
            System.out.println("????????????????? UGH!");
        }
        
        try {
            Access access = accessRepository.findByLogin(login).get();
            
            // проверяем, активен ли пользовавтель
            if (!access.getIsActive()) {
                throw new UsernameNotFoundException("Пользователь заблокирован!");
            }
            
            // создаем обьект для Spring Security
            return User.builder()
                    .username(access.getLogin())
                    .password(access.getPassword())
                    .roles(access.getRole().getName())
                    .build();
            
        } catch (UsernameNotFoundException ex) {
            throw new UsernameNotFoundException("Ошибка - пользователь не найден! " + ex);
        }
    }
    
}
