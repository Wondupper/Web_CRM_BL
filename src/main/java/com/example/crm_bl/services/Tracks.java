package com.example.crm_bl.services;

import com.example.crm_bl.dtos.requests.RequestTrackDTO;
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

@Service
@EnableKafka
public class Tracks {

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private ResponseTrackDTO trackContainer;

    private ObjectMapper mapper = new ObjectMapper();

    private List<ResponseTrackDTO> tracksContainer = new ArrayList<>();

    @Autowired
    public Tracks(KafkaTemplate<String, String> kafkaTemplateMessage) {
        this.kafkaTemplateMessage = kafkaTemplateMessage;
    }

    public List<ResponseTrackDTO> getTracks(){
        tracksContainer.clear();
        kafkaTemplateMessage.send("get-tracksdal", "get-tracks");
        while (tracksContainer.isEmpty()){
            continue;
        }
        List<ResponseTrackDTO> tracks = new ArrayList<>(tracksContainer);
        return tracks;
    }

    public ResponseTrackDTO getTrack(Long id){
        trackContainer=null;
        kafkaTemplateMessage.send("get-trackdal", id.toString());
        while (trackContainer==null){
            continue;
        }
        ResponseTrackDTO track = trackContainer;
        return track;
    }


    @KafkaListener(topics = "get-tracksbl")
    public void listenTracks(String tracks) throws JsonProcessingException {
        tracksContainer = new ArrayList<>(mapper.readValue(tracks, new TypeReference<List<ResponseTrackDTO>>(){}));
    }

    @KafkaListener(topics = "get-trackbl")
    public void listenTrack(String track) throws JsonProcessingException {
        trackContainer = mapper.readValue(track, ResponseTrackDTO.class);
    }

    public void saveTrack(RequestTrackDTO track) throws JsonProcessingException {
        kafkaTemplateMessage.send("save-trackdal", mapper.writeValueAsString(track));
    }
}