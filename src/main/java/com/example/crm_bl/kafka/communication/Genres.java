package com.example.crm_bl.kafka.communication;

import com.example.crm_bl.dtos.requests.RequestGenreDTO;
import com.example.crm_bl.dtos.requests.RequestGenreDTO;
import com.example.crm_bl.dtos.responses.ResponseGenreDTO;
import com.example.crm_bl.dtos.responses.ResponseGenreDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@EnableKafka
public class Genres {

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private ResponseGenreDTO genreContainer;

    private ObjectMapper mapper = new ObjectMapper();

    private List<ResponseGenreDTO> genresContainer = new ArrayList<>();

    @Autowired
    public Genres(KafkaTemplate<String, String> kafkaTemplateMessage) {
        this.kafkaTemplateMessage = kafkaTemplateMessage;
    }

    public List<ResponseGenreDTO> getGenres() {
        genresContainer.clear();
        kafkaTemplateMessage.send("get-genresdal", "get-genres");
        while (genresContainer.isEmpty()) {
            continue;
        }
        List<ResponseGenreDTO> genres = new ArrayList<>(genresContainer);
        return genres;
    }

    public ResponseGenreDTO getGenre(Long id) {
        genreContainer = null;
        kafkaTemplateMessage.send("get-genredal", id.toString());
        while (genreContainer == null) {
            continue;
        }
        ResponseGenreDTO genre = genreContainer;
        return genre;
    }


    @KafkaListener(topics = "get-genresbl")
    public void listenGenres(String genres) throws JsonProcessingException {
        genresContainer = new ArrayList<>(mapper.readValue(genres, new TypeReference<List<ResponseGenreDTO>>(){}));
    }

    @KafkaListener(topics = "get-genrebl")
    public void listenGenre(String genre) throws JsonProcessingException {
        genreContainer = mapper.readValue(genre,ResponseGenreDTO.class);
    }

    public void saveGenre(RequestGenreDTO genre) throws JsonProcessingException {
        kafkaTemplateMessage.send("save-genredal", mapper.writeValueAsString(genre));
    }
}