package com.example.crm_bl.kafka.communication;

import com.example.crm_bl.dtos.requests.RequestArtistDTO;
import com.example.crm_bl.dtos.responses.ResponseArtistDTO;
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

    private final KafkaTemplate<String, RequestArtistDTO> kafkaTemplate;

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private final KafkaTemplate<String, Long> kafkaTemplateId;

    private ResponseArtistDTO artistContainer;

    private List<ResponseArtistDTO> artistsContainer = new ArrayList<>();

    @Autowired
    public Artists(KafkaTemplate<String, RequestArtistDTO> kafkaTemplate, KafkaTemplate<String, String> kafkaTemplateMessage, KafkaTemplate<String, Long> kafkaTemplateId) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateMessage = kafkaTemplateMessage;
        this.kafkaTemplateId = kafkaTemplateId;
    }

    public List<ResponseArtistDTO> getArtists() {
        artistsContainer.clear();
        kafkaTemplateMessage.send("get-artists", "get-artists");
        while (artistsContainer.isEmpty()) {
            continue;
        }
        List<ResponseArtistDTO> artists = new ArrayList<>(artistsContainer);
        return artists;
    }

    public ResponseArtistDTO getArtist(Long id) {
        artistContainer = null;
        kafkaTemplateId.send("get-artists", id);
        while (artistContainer == null) {
            continue;
        }
        ResponseArtistDTO artist = artistContainer;
        return artist;
    }


    @KafkaListener(topics = "get-artists")
    public void listenArtists(List<ResponseArtistDTO> artists) {
        artistsContainer = new ArrayList<>(artists);
    }

    @KafkaListener(topics = "get-artist")
    public void listenArtist(ResponseArtistDTO artist) {
        artistContainer = artist;
    }

    public void saveArtist(RequestArtistDTO artist) {
        kafkaTemplate.send("save-artist", artist);
    }
}
