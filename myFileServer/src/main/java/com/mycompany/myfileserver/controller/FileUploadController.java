/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.controller;

import com.mycompany.myfileserver.dto.FileDataDto;
import com.mycompany.myfileserver.service.WorkFileService;
import java.nio.file.Path;
import java.util.List;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер файлов
 * @author User
 */

@RestController
public class FileUploadController {
    WorkFileService workFileService;
    
    FileUploadController (WorkFileService workFileService) {
        this.workFileService = workFileService;
    }
    
    /**
     * Роут загрузки файлов.
     * @param file Файл для загрузки.
     * @param userDetails
     * Обьект текущего пользователя из модуля Security.
     * @return Результат выполнения.
     */
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal UserDetails userDetails) {
        workFileService.setCurrentUploadDir(userDetails.getUsername()); // задаем папку пользователя как путь в который будут храниться все его файлы
        workFileService.setFile(file);
        
        return workFileService.uploadFile();
    }
    
    /**
     * Взятие всех файлов пользователя с пагинацией
     * (с дополнительной сортировкой по имени searchField).
     * @param searchField
     * Поле для сортировки и вывода по совпадению в именах файлов.
     * @param page Текущая страница (пагинация).
     * @param size Количество страниц (пагинация).
     * @param userDetails
     * Обьект текущего пользователя из модуля Security.
     * @return Список файлов пользователя.
     */
    @GetMapping("/getUploaded")
    public List<FileDataDto> getUploadedFiles(@RequestParam("searchField") String searchField, @RequestParam("page") int page, @RequestParam("size") int size, @AuthenticationPrincipal UserDetails userDetails) {
        workFileService.setCurrentUploadDir(userDetails.getUsername()); // задаем папку пользователя как путь в который будут храниться все его файлы
        return workFileService.getUploadedFiles(page, size, searchField);
    }
    
    /**
     * Удаление файла.
     * @param login Логин текущего пользователя.
     * @param nameFile Имя текущего файла для удаления.
     * @param userDetails
     * Обьект текущего пользователя из модуля Security.
     * @return Результат выполнения.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFile(@RequestParam("login") String login, @RequestParam("nameFile") String nameFile, @AuthenticationPrincipal UserDetails userDetails){
        workFileService.setCurrentUploadDir(userDetails.getUsername()); // задаем папку пользователя как путь в который будут храниться все его файлы
        return ResponseEntity.ok(workFileService.deleteFile(login, nameFile));
    } 
    
    /**
     * Скачивание файла.
     * @param login Логин текущего пользователя.
     * @param nameFile Имя текущего файла для скачивания.
     * @param userDetails Обьект текущего пользователя из модуля Security.
     * @return Результат выполнения.
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("login") String login, @RequestParam("nameFile") String nameFile, @AuthenticationPrincipal UserDetails userDetails){
        workFileService.setCurrentUploadDir(userDetails.getUsername()); // задаем папку пользователя как путь в который будут храниться все его файлы
        Path filePath = workFileService.findFileByName(login, nameFile);
        
        // Создаем Resource из файла
        Resource resource = new FileSystemResource(filePath);
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename=\"" + nameFile + "\"") // header - говорит о том как обрабатывать файл в случае получения пользователем - что это не текст и его нужно скачать в нашем случае
            .body(resource);
    }
    
    /**
     * Получение логина текущего пользователя.
     * @param userDetails Обьект текущего пользователя из модуля Security.
     * @return Логин пользователя в формате строки.
     */
    @GetMapping("/currentLogin")
    public String getLogin(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails.getUsername();
    }
}
