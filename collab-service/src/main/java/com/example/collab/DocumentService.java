package com.example.collab;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public Document getDocument(String id) {
        // Создаем документ, если он не существует (для MVP)
        return repository.findById(id).orElseGet(() -> repository.save(new Document(id, "Новый документ")));
    }

    public Document update(String id, String content) {
        Optional<Document> existing = repository.findById(id);
        
        // В реальном приложении здесь будет сложная логика трансформации/CRDT
        Document doc = existing.orElse(new Document(id, content));
        doc.setContent(content);
        doc.setVersion(doc.getVersion() + 1); 
        
        return repository.save(doc);
    }
}
