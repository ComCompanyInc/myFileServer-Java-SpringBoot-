/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.controller;

import com.mycompany.myfileserver.dto.AccessDto;
import com.mycompany.myfileserver.entity.Access;
import com.mycompany.myfileserver.service.RegistrationService;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author User
 */
@RestController
public class RegistrationController {
    RegistrationService registrationService;
    
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
    
    @PostMapping("/signIn")
    public ResponseEntity<String> saveRegistrationAccess(@RequestBody Access access) {
        return registrationService.saveRegistrationAccess(access);
    }
    
    @GetMapping("admin/users")
    public List<AccessDto> saveRegistrationAccess(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "searchFilter") String searchFilter
    ) {
        Pageable pageable = PageRequest.of(page, size);
        
        return registrationService.showAccessAllUsers(pageable, searchFilter);
    }
    
    //Обновить запись доступа пользователя
    @PutMapping("admin/updateAccessData")
    public ResponseEntity updateAccess(@RequestBody Access access) {
        return registrationService.updateAccess(access);
    }
}
