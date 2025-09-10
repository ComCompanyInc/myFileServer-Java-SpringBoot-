/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.myfileserver.repository;

import com.mycompany.myfileserver.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author User
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    //@Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    //List<User> findByRoleName(@Param("roleName") String roleName);
    
    @Query("SELECT messages FROM Message messages "
            + "JOIN messages.receiver reciver "
            + "JOIN messages.sender sender "
            + "WHERE (receiver.login = :login)") //OR (sender.login = :login)
    Page<Message> getMessagesByCurrentUserLogin(
        @Param("login") String login,
        Pageable pageable
    ); 
}
