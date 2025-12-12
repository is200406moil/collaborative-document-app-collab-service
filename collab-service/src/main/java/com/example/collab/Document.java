package com.example.collab;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Document {
    @Id
    private String id;
    
    // Используем @Column с columnDefinition для больших текстов в PostgreSQL
    @Column(columnDefinition = "TEXT") 
    private String content; 

    private long version = 0;

    public Document(String id, String content) {
        this.id = id;
        this.content = content;
    }
}
