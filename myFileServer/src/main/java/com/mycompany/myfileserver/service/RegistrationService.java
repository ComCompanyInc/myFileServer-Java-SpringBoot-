/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.service;

import com.mycompany.myfileserver.dto.AccessDto;
import com.mycompany.myfileserver.entity.Access;
import com.mycompany.myfileserver.repository.AccessRepository;
import com.mycompany.myfileserver.repository.RoleRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class RegistrationService {
    AccessRepository accessRepository;
    RoleRepository roleRepository;
    
    public RegistrationService(AccessRepository accessRepository, RoleRepository roleRepository) {
        this.accessRepository = accessRepository;
        this.roleRepository = roleRepository;
    }
    
    public ResponseEntity<String> saveRegistrationAccess(Access access) {
        try {
            if (accessRepository.findByLogin(access.getLogin()).isPresent() == false) {
                if (roleRepository.findByName(access.getRole().getName()).isPresent() == true) {
                    access.setRole(roleRepository.findByName(access.getRole().getName()).get());
                    accessRepository.save(access);
                    return ResponseEntity.status(201).body("Пользователь успешно сохранен!");
                } else {
                    return ResponseEntity.status(400).body("Ошибка - в базе данных нет такой роли!");
                }
            } else {
                return ResponseEntity.status(400).body("Ошибка - пользователь с таким логином уже существует!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка сохранения сущности: " + e);
        }
    }
    
    public List<AccessDto> showAccessAllUsers(Pageable pageable, String searchFilter) {
        List<AccessDto> allUserAccess = accessRepository.findByLogin(searchFilter, pageable)
                .map((access) -> {
                        return new AccessDto(access);
                }).toList();
        
        return allUserAccess;
    }
    
    public ResponseEntity<String> updateAccess(Access access) {
        try {
            Access currentAccess = accessRepository.findByLogin(access.getLogin()).get();
            
            if (currentAccess != null) {
                currentAccess.setIsActive(access.getIsActive());
                
                accessRepository.save(access);
                return ResponseEntity.status(201).body("Пользователь успешно изменен!");
            } else {
                return ResponseEntity.status(400).body("Ошибка - пользователя с таким логином не существует!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка обновления сущности: " + e);
        }
    }
}
