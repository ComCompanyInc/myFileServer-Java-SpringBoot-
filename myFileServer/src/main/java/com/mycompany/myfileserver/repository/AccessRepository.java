/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.myfileserver.repository;

import com.mycompany.myfileserver.entity.Access;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author User
 */
@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {
    Optional<Access> findByLogin(String login);
    
    @Query("SELECT access FROM Access access WHERE access.login ILIKE %:searchFilter%")
    Page<Access> findByLogin(@Param("searchFilter") String searchFilter, Pageable page);
}
