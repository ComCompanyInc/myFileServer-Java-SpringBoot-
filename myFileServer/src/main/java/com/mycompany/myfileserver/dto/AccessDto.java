/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.dto;

import com.mycompany.myfileserver.entity.Access;

/**
 *
 * @author User
 */
public class AccessDto {
    private Long id;
    private String login;
    private RoleDto role;

    public AccessDto(Access access) {
       this.id = access.getId();
       this.login = access.getLogin();
       this.role = new RoleDto(access.getRole());
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }
}
