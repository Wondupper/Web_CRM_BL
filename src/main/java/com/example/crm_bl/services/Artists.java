package com.example.crm_bl.services;

import com.example.crm_bl.dtos.requests.RequestArtistDTO;
import com.example.crm_bl.dtos.responses.ResponseArtistDTO;
import com.example.crm_bl.dtos.responses.ResponseArtistDTO;
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
public class Artists {

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private ObjectMapper mapper = new ObjectMapper();

    private CompletableFuture<List<ResponseArtistDTO>> artistsContainer;

    private CompletableFuture<ResponseArtistDTO> artistContainer;


    @Autowired
    public Artists(KafkaTemplate<String, String> kafkaTemplateMessage) {
        this.kafkaTemplateMessage = kafkaTemplateMessage;
    }

    public List<ResponseArtistDTO> getArtists() {
        artistsContainer = new CompletableFuture<>();
        kafkaTemplateMessage.send("get-artistsdal", "get-artists");
        try {
            return artistsContainer.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка получения", e);
        }
    }

    public ResponseArtistDTO getArtist(Long id) {
        artistContainer = new CompletableFuture<>();
        kafkaTemplateMessage.send("get-artistdal", id.toString());
        try {
            return artistContainer.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка получения", e);
        }
    }


    @KafkaListener(topics = "get-artistsbl")
    public void listenArtists(String artists) throws JsonProcessingException {
        List<ResponseArtistDTO> artistList = new ArrayList<>(mapper.readValue(artists, new TypeReference<List<ResponseArtistDTO>>() {
        }));
        artistsContainer.complete(artistList);
    }

    @KafkaListener(topics = "get-artistbl")
    public void listenArtist(String artist) throws JsonProcessingException {
        ResponseArtistDTO artiste = mapper.readValue(artist, ResponseArtistDTO.class);
        artistContainer.complete(artiste);
    }

    public void saveArtist(RequestArtistDTO artist) throws JsonProcessingException {
        kafkaTemplateMessage.send("save-artistdal", mapper.writeValueAsString(artist));
    }
}
