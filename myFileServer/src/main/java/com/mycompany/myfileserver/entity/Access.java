/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

/**
 *
 * @author User
 */
@Entity
@Table(name = "access")
public class Access {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "login")
    private String login;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "is_active")
    private boolean isActive;
    
    @JoinColumn(
        name = "role_id"
    )
    @ManyToOne
    private Role role;
    
    @OneToMany(
        mappedBy = "sender",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Message> sendersMessage;
    
    @OneToMany(
        mappedBy = "receiver",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Message> receiversMessage;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Message> getSendersMessage() {
        return sendersMessage;
    }

    public void setSendersMessage(List<Message> sendersMessage) {
        this.sendersMessage = sendersMessage;
    }

    public List<Message> getReceiversMessage() {
        return receiversMessage;
    }

    public void setReceiversMessage(List<Message> receiversMessage) {
        this.receiversMessage = receiversMessage;
    }
    
    
}
