/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.controller;

import com.mycompany.myfileserver.dto.MessageDto;
import com.mycompany.myfileserver.entity.Message;
import com.mycompany.myfileserver.repository.AccessRepository;
import com.mycompany.myfileserver.service.MessageService;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер сообщений.
 * @author User
 */
@RestController
public class MessageController {
    
    MessageService messageService;
    AccessRepository accessRepository;
    
    public MessageController(MessageService messageService, AccessRepository accessRepository) {
        this.messageService = messageService;
        this.accessRepository = accessRepository;
    }
    
    /**
     * Все полученные сообщения для текущего пользователя.
     * @param page Текущая страница (для пагинации).
     * @param size Количество всех страниц (для пагинации).
     * @param userDetails Обьект текущего пользователя из модуля Security.
     * @return Список сообщений пользователя.
     */
    @GetMapping("/messages")
    public List<MessageDto> getMessagesForCurrentUser(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        messageService.setUserLogin(userDetails.getUsername());
        
        // СОЗДАЕМ Pageable и передаем в репозиторий (для пагинации)
        Pageable pageable = PageRequest.of(page, size);
        
        return messageService.getUsersMessages(pageable);
    }
    
    /*
    Пример json:
    {
        "description": "Привет, вот файл который ты просил",
        "fileUrl": "/files/document.pdf",
        "sender": {
            "id": 1
        },
        "receiver": {
            "id": 1
        }
    }
    */
    /**
     * Отправка сообщений пользователем.
     * @param message Обьект сообщения (обычно в json формате).
     * @param userDetails Обьект текущего пользователя из модуля Security.
     * @return Результат выполнения.
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody Message message, @AuthenticationPrincipal UserDetails userDetails) {
        message.setSender(accessRepository.findByLogin(userDetails.getUsername()).get()); // берем текущего пользователя и помечаем его как отправителя в полученном обьекте сообщения
        message.setReceiver(accessRepository.findByLogin(message.getReceiver().getLogin()).get()); // берем из сообщения обьект о получателе из фронта и находим через репозиторий по логину
        
        messageService.setUserLogin(userDetails.getUsername());
        
        return messageService.sendMessage(message);
    }
}
