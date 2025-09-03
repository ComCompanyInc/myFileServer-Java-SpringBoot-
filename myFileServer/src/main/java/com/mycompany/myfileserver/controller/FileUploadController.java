/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.controller;

import com.mycompany.myfileserver.dto.FileDataDto;
import com.mycompany.myfileserver.service.WorkFileService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */

@RestController
public class FileUploadController {
    WorkFileService workFileService;
    
    FileUploadController (WorkFileService workFileService) {
        this.workFileService = workFileService;
    }
    
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        workFileService.setFile(file);
        
        return workFileService.uploadFile();
    }
    
    @GetMapping("/getUploaded")
    public List<FileDataDto> getUploadedFiles(@RequestParam("page") int page, @RequestParam("size") int size) {
        return workFileService.getUploadedFiles(page, size);
    }
}
