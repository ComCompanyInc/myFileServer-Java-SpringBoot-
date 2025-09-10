/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.dto;

import com.mycompany.myfileserver.entity.Message;

/**
 *
 * @author User
 */
public class MessageDto {
    private Long id;
    private String description;
    private AccessDto receiver;
    private AccessDto sender;
    
    public MessageDto(Message message) {
        this.id = message.getId();
        this.description = message.getDescription();
        this.receiver = new AccessDto(message.getReceiver());
        this.sender = new AccessDto(message.getSender());
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccessDto getReceiver() {
        return receiver;
    }

    public void setReceiver(AccessDto receiver) {
        this.receiver = receiver;
    }

    public AccessDto getSender() {
        return sender;
    }

    public void setSender(AccessDto sender) {
        this.sender = sender;
    }
}
