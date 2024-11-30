package com.example.crm_bl.services;

import com.example.crm_bl.dtos.requests.RequestTrackDTO;
import com.example.crm_bl.dtos.responses.ResponseTrackDTO;
import com.example.crm_bl.dtos.responses.ResponseTrackDTO;
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
public class Tracks {

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private ObjectMapper mapper = new ObjectMapper();

    private CompletableFuture<List<ResponseTrackDTO>> tracksContainer;

    private CompletableFuture<ResponseTrackDTO> trackContainer;

    @Autowired
    public Tracks(KafkaTemplate<String, String> kafkaTemplateMessage) {
        this.kafkaTemplateMessage = kafkaTemplateMessage;
    }

    public List<ResponseTrackDTO> getTracks() {
        tracksContainer = new CompletableFuture<>();
        kafkaTemplateMessage.send("get-tracksdal", "get-tracks");
        try {
            return tracksContainer.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка получения", e);
        }
    }

    public ResponseTrackDTO getTrack(Long id) {
        trackContainer = new CompletableFuture<>();
        kafkaTemplateMessage.send("get-trackdal", id.toString());
        try {
            return trackContainer.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка получения", e);
        }
    }


    @KafkaListener(topics = "get-tracksbl")
    public void listenTracks(String tracks) throws JsonProcessingException {
        List<ResponseTrackDTO> trackList = new ArrayList<>(mapper.readValue(tracks, new TypeReference<List<ResponseTrackDTO>>() {
        }));
        tracksContainer.complete(trackList);
    }

    @KafkaListener(topics = "get-trackbl")
    public void listenTrack(String track) throws JsonProcessingException {
        ResponseTrackDTO tracke = mapper.readValue(track, ResponseTrackDTO.class);
        trackContainer.complete(tracke);
    }

    public void saveTrack(RequestTrackDTO track) throws JsonProcessingException {
        kafkaTemplateMessage.send("save-trackdal", mapper.writeValueAsString(track));
    }
}