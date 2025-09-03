/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myfileserver.dto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

/**
 *
 * @author User
 */
public class FileDataDto {
    private String name;
    private String size;
    private Date date;
    
    public FileDataDto (Path file) throws IOException {
        this.setName(file.getFileName().toString());
        
        if (Files.isRegularFile(file)) {
            this.setSize((Files.size(file) / (1024 * 1024)) + " MB");
        } else if (Files.isDirectory(file)) {
            this.setSize("[DIR]");
        }
        
        this.setDate(new Date());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
