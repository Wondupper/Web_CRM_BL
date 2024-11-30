package com.example.crm_bl.services;

import com.example.crm_bl.dtos.requests.RequestGenreDTO;
import com.example.crm_bl.dtos.responses.ResponseGenreDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@EnableKafka
public class Genres {

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private ObjectMapper mapper = new ObjectMapper();

    private CompletableFuture<List<ResponseGenreDTO>> genresContainer;

    private CompletableFuture<ResponseGenreDTO> genreContainer;

    @Autowired
    public Genres(KafkaTemplate<String, String> kafkaTemplateMessage) {
        this.kafkaTemplateMessage = kafkaTemplateMessage;
    }

    public List<ResponseGenreDTO> getGenres() {
        genresContainer = new CompletableFuture<>();
        kafkaTemplateMessage.send("get-genresdal", "get-genres");
        try {
            return genresContainer.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка получения", e);
        }
    }

    public ResponseGenreDTO getGenre(Long id) {
        genreContainer = new CompletableFuture<>();
        kafkaTemplateMessage.send("get-genredal", id.toString());
        try {
            return genreContainer.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка получения", e);
        }
    }


    @KafkaListener(topics = "get-genresbl")
    public void listenGenres(String genres) throws JsonProcessingException {
        List<ResponseGenreDTO> genreList = new ArrayList<>(mapper.readValue(genres, new TypeReference<List<ResponseGenreDTO>>() {
        }));
        genresContainer.complete(genreList);
    }

    @KafkaListener(topics = "get-genrebl")
    public void listenGenre(String genre) throws JsonProcessingException {
        ResponseGenreDTO genree = mapper.readValue(genre, ResponseGenreDTO.class);
        genreContainer.complete(genree);
    }

    public void saveGenre(RequestGenreDTO genre) throws JsonProcessingException {
        kafkaTemplateMessage.send("save-genredal", mapper.writeValueAsString(genre));
    }
}