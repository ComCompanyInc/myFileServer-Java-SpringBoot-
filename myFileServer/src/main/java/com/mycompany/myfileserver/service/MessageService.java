/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.service;

import com.mycompany.myfileserver.dto.MessageDto;
import com.mycompany.myfileserver.entity.Message;
import com.mycompany.myfileserver.repository.MessageRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 *
 * @author User
 */
@Service
public class MessageService {
    private String currentUserLogin;
            
    MessageRepository messageRepository;
    
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    
    // для установки логина текущего пользователя
    public void setUserLogin(String currentLogin) {
        this.currentUserLogin = currentLogin;
    }
    
    public List<MessageDto> getUsersMessages(Pageable pageable) {
        // получаем все сообщения
        List<MessageDto> messages = messageRepository.getMessagesByCurrentUserLogin(currentUserLogin, pageable)
                .stream()
                .map((message) -> {
                    return new MessageDto(message);
                })
                .collect(Collectors.toList());
        
        return messages;
    }
    
    public ResponseEntity<?> sendMessage(Message message) {
        try {
            messageRepository.save(message);
            return ResponseEntity.status(201).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error saving message: " + ex.getMessage());
        }
    }
}
