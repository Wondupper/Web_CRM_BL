package com.example.crm_bl.kafka.communication;

import com.example.crm_bl.dtos.requests.RequestTrackDTO;
import com.example.crm_bl.dtos.requests.RequestTrackDTO;
import com.example.crm_bl.dtos.responses.ResponseTrackDTO;
import com.example.crm_bl.dtos.responses.ResponseTrackDTO;
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
public class Tracks {

    private final KafkaTemplate<String, RequestTrackDTO> kafkaTemplate;

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private final KafkaTemplate<String, Long> kafkaTemplateId;

    private ResponseTrackDTO trackContainer;

    private List<ResponseTrackDTO> tracksContainer = new ArrayList<>();

    @Autowired
    public Tracks(KafkaTemplate<String, RequestTrackDTO> kafkaTemplate, KafkaTemplate<String, String> kafkaTemplateMessage, KafkaTemplate<String, Long> kafkaTemplateId) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateMessage = kafkaTemplateMessage;
        this.kafkaTemplateId = kafkaTemplateId;
    }

    public List<ResponseTrackDTO> getTracks(){
        tracksContainer.clear();
        kafkaTemplateMessage.send("get-tracks", "get-tracks");
        while (tracksContainer.isEmpty()){
            continue;
        }
        List<ResponseTrackDTO> tracks = new ArrayList<>(tracksContainer);
        return tracks;
    }

    public ResponseTrackDTO getTrack(Long id){
        trackContainer=null;
        kafkaTemplateId.send("get-tracks", id);
        while (trackContainer==null){
            continue;
        }
        ResponseTrackDTO track = trackContainer;
        return track;
    }


    @KafkaListener(topics = "get-tracks")
    public void listenTracks( List<ResponseTrackDTO> tracks) {
        tracksContainer = new ArrayList<>(tracks);
    }

    @KafkaListener(topics = "get-track")
    public void listenTrack(ResponseTrackDTO track) {
        trackContainer = track;
    }

    public void saveTrack(RequestTrackDTO track) {
        kafkaTemplate.send("save-track", track);
    }
}