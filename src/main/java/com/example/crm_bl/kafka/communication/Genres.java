package com.example.crm_bl.kafka.communication;

import com.example.crm_bl.dtos.requests.RequestGenreDTO;
import com.example.crm_bl.dtos.requests.RequestGenreDTO;
import com.example.crm_bl.dtos.responses.ResponseGenreDTO;
import com.example.crm_bl.dtos.responses.ResponseGenreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableKafka
public class Genres {

    private final KafkaTemplate<String, RequestGenreDTO> kafkaTemplate;

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private final KafkaTemplate<String, Long> kafkaTemplateId;

    private ResponseGenreDTO genreContainer;

    private List<ResponseGenreDTO> genresContainer = new ArrayList<>();

    @Autowired
    public Genres(KafkaTemplate<String, RequestGenreDTO> kafkaTemplate, KafkaTemplate<String, String> kafkaTemplateMessage, KafkaTemplate<String, Long> kafkaTemplateId) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateMessage = kafkaTemplateMessage;
        this.kafkaTemplateId = kafkaTemplateId;
    }

    public List<ResponseGenreDTO> getGenres(){
        kafkaTemplateMessage.send("get-genres", "get-genres");
        List<ResponseGenreDTO> genres = new ArrayList<>(genresContainer);
        return genres;
    }

    public ResponseGenreDTO getGenre(Long id){
        kafkaTemplateId.send("get-genres", id);
        ResponseGenreDTO genre = genreContainer;
        return genre;
    }


    @KafkaListener(topics = "get-genres")
    public void listenGenres( List<ResponseGenreDTO> genres) {
        genresContainer = new ArrayList<>(genres);
    }

    @KafkaListener(topics = "get-genre")
    public void listenGenre(ResponseGenreDTO genre) {
        genreContainer = genre;
    }

    public void saveGenre(RequestGenreDTO genre) {
        kafkaTemplate.send("save-genre", genre);
    }
}