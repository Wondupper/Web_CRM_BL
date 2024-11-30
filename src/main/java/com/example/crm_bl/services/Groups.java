package com.example.crm_bl.services;

import com.example.crm_bl.dtos.requests.RequestGroupDTO;
import com.example.crm_bl.dtos.responses.ResponseGroupDTO;
import com.example.crm_bl.dtos.responses.ResponseGroupDTO;
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
public class Groups {

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private ObjectMapper mapper = new ObjectMapper();

    private CompletableFuture<List<ResponseGroupDTO>> groupsContainer;

    private CompletableFuture<ResponseGroupDTO> groupContainer;

    @Autowired
    public Groups(KafkaTemplate<String, String> kafkaTemplateMessage) {
        this.kafkaTemplateMessage = kafkaTemplateMessage;
    }

    public List<ResponseGroupDTO> getGroups() {
        groupsContainer = new CompletableFuture<>();
        kafkaTemplateMessage.send("get-groupsdal", "get-groups");
        try {
            return groupsContainer.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка получения", e);
        }
    }

    public ResponseGroupDTO getGroup(Long id) {
        groupContainer = new CompletableFuture<>();
        kafkaTemplateMessage.send("get-groupdal", id.toString());
        try {
            return groupContainer.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка получения", e);
        }
    }


    @KafkaListener(topics = "get-groupsbl")
    public void listenGroups(String groups) throws JsonProcessingException {
        List<ResponseGroupDTO> groupList = new ArrayList<>(mapper.readValue(groups, new TypeReference<List<ResponseGroupDTO>>() {
        }));
        groupsContainer.complete(groupList);
    }

    @KafkaListener(topics = "get-groupbl")
    public void listenGroup(String group) throws JsonProcessingException {
        ResponseGroupDTO groupe = mapper.readValue(group, ResponseGroupDTO.class);
        groupContainer.complete(groupe);
    }

    public void saveGroup(RequestGroupDTO group) throws JsonProcessingException {
        kafkaTemplateMessage.send("save-groupdal", mapper.writeValueAsString(group));
    }
}

