package com.example.collab;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Controller
public class DocumentController {
    
    private final DocumentService service;
    
    // Sinks для паттерна Publisher/Subscriber (GraphQL Subscriptions)
    private final Sinks.Many<Document> documentSink = Sinks.many().multicast().onBackpressureBuffer();

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @QueryMapping
    public Document getDocument(@Argument String id) {
        return service.getDocument(id);
    }

    @MutationMapping
    public Document updateDocument(@Argument String id, @Argument String content) {
        Document doc = service.update(id, content); 
        // Эмитируем обновленный документ всем подписчикам
        documentSink.tryEmitNext(doc); 
        return doc;
    }

    // Фильтруем поток, чтобы отправлять документ только тем, кто подписался на этот ID
    @SubscriptionMapping
    public Flux<Document> documentUpdated(@Argument String id) {
        return documentSink.asFlux().filter(doc -> doc.getId().equals(id));
    }
}
