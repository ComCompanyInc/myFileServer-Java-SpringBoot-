/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.service;

import com.mycompany.myfileserver.dto.FileDataDto;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Service
public class WorkFileService {
    private MultipartFile file;
    
    // Директория со всеми файлами
    Path uploadDir = Paths.get("uploads"); // базовая директория
    
    public void setCurrentUploadDir(String currentUserLogin) {
        this.uploadDir = Paths.get("uploads", currentUserLogin); // Устанавливаем путь пользователя
    }
    
    public String uploadFile() {
        try {
            // Проверяем размер файла
            if (file.getSize() > 5L * 1024 * 1024 * 1024) {
                return "File too large! Max size 5GB";
            }
        
            // Создаем папку если нет
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // Сохраняем файл
            Path filePath = uploadDir.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            return "File uploaded: " + file.getOriginalFilename() +
                    " Size: " + (file.getSize() / (1024 * 1024)) + "MB";
        } catch (Exception e) {
            return "Upload error: " + e.getMessage();
        }
    }
  
/**
 * Взятие всех файлов пользователя с пагинацией если
 * поисковая строка пустая, иначе сберутся файлы по
 * именованию с совпадением из поисковой строки.
 * @param page текущая страница.
 * @param size общее количество страниц.
 * @param searchField Поле для поиска совпадений названий файлов.
 * @return Список всех файлов.
 */
public List<FileDataDto> getUploadedFiles(int page, int size, String searchField) {
    List<FileDataDto> result = new ArrayList<>();
    
    try {
        // 1. Получаем все файлы и папки
        DirectoryStream<Path> contents = Files.newDirectoryStream(uploadDir);
        
        // 2. Счетчики для пагинации
        int currentIndex = 0;
        int startIndex = page * size;
        int endIndex = startIndex + size;
        int foundCount = 0;
        
        // 3. Перебираем всё в цикле
        for (Path item : contents) {
            // Проверяем поиск только если searchField не пустой
            boolean matchesSearch = searchField == null || searchField.isEmpty() || 
                                  item.getFileName().toString().toLowerCase()
                                     .contains(searchField.toLowerCase());
            
            // Если поиск не активен ИЛИ файл подходит под поиск
            if (matchesSearch) {
                // Проверяем попадает ли файл в текущую страницу
                if (currentIndex >= startIndex && currentIndex < endIndex) {
                    result.add(new FileDataDto(item));
                    foundCount++;
                }
                currentIndex++;
            }
            
            // Если собрали достаточно элементов для страницы - выходим
            if (foundCount >= size) {
                break;
            }
        }

        return result;
    } catch (Exception e) {
        System.out.println("Ой! Ошибка: " + e.getMessage());
        return result;
    }
}

    /**
     * Поиск файла по логину автора и названию самого файла.
     * @param login Логин автора.
     * @param fileName Имя файла.
     * @return Файл.
     */
    public Path findFileByName(String login, String fileName) {
        setCurrentUploadDir(login);
        Path filePath = uploadDir.resolve(fileName); // directoryPath.resolve(fileName) объединяет путь директории с именем файла
        
        // Проверяем существование файла
        if (Files.exists(filePath)) {
            // возвращаем
            return filePath;
        } else {
            return null;
        }
    }
    
    public String deleteFile(String login, String name) {
        try {
            // Удаляем файл
            if (findFileByName(login, name) != null) {
                Files.delete(findFileByName(login, name));
                return "Файл успешно удален: " + name;
            } else {
                return "Файл не найден - " + name;
            }
        } catch (IOException e) {
            return "Ошибка при удалении файла: " + e.getMessage();
        }
    }
    
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
