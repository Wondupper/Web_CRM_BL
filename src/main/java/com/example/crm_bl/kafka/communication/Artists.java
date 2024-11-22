package com.example.crm_bl.kafka.communication;

import com.example.crm_bl.dtos.requests.RequestArtistDTO;
import com.example.crm_bl.dtos.responses.ResponseArtistDTO;
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

@Service
@EnableKafka
public class Artists {

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private ResponseArtistDTO artistContainer;

    private ObjectMapper mapper = new ObjectMapper();

    private List<ResponseArtistDTO> artistsContainer = new ArrayList<>();

    @Autowired
    public Artists(KafkaTemplate<String, String> kafkaTemplateMessage) {
        this.kafkaTemplateMessage = kafkaTemplateMessage;
    }

    public List<ResponseArtistDTO> getArtists() {
        artistsContainer.clear();
        kafkaTemplateMessage.send("get-artistsdal", "get-artists");
        while (artistsContainer.isEmpty()) {
            continue;
        }
        List<ResponseArtistDTO> artists = new ArrayList<>(artistsContainer);
        return artists;
    }

    public ResponseArtistDTO getArtist(Long id) {
        artistContainer = null;
        kafkaTemplateMessage.send("get-artistdal", id.toString());
        while (artistContainer == null) {
            continue;
        }
        ResponseArtistDTO artist = artistContainer;
        return artist;
    }


    @KafkaListener(topics = "get-artistsbl")
    public void listenArtists(String artists) throws JsonProcessingException {
        artistsContainer = new ArrayList<>(mapper.readValue(artists, new TypeReference<List<ResponseArtistDTO>>(){}));
    }

    @KafkaListener(topics = "get-artistbl")
    public void listenArtist(String artist) throws JsonProcessingException {
        artistContainer = mapper.readValue(artist,ResponseArtistDTO.class);
    }

    public void saveArtist(RequestArtistDTO artist) throws JsonProcessingException {
        kafkaTemplateMessage.send("save-artistdal", mapper.writeValueAsString(artist));
    }
}
