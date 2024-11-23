package com.example.crm_bl.services;

import com.example.crm_bl.dtos.requests.RequestGroupDTO;
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

@Service
@EnableKafka
public class Groups {

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private ResponseGroupDTO groupContainer;

    private ObjectMapper mapper = new ObjectMapper();

    private List<ResponseGroupDTO> groupsContainer = new ArrayList<>();

    @Autowired
    public Groups(KafkaTemplate<String, String> kafkaTemplateMessage) {
        this.kafkaTemplateMessage = kafkaTemplateMessage;
    }

    public List<ResponseGroupDTO> getGroups() {
        groupsContainer.clear();
        kafkaTemplateMessage.send("get-groupsdal", "get-groups");
        while (groupsContainer.isEmpty()) {
            continue;
        }
        List<ResponseGroupDTO> groups = new ArrayList<>(groupsContainer);
        return groups;
    }

    public ResponseGroupDTO getGroup(Long id) {
        groupContainer = null;
        kafkaTemplateMessage.send("get-groupdal", id.toString());
        while (groupContainer == null) {
            continue;
        }
        ResponseGroupDTO group = groupContainer;
        return group;
    }


    @KafkaListener(topics = "get-groupsbl")
    public void listenGroups(String groups) throws JsonProcessingException {
        groupsContainer = new ArrayList<>(mapper.readValue(groups, new TypeReference<List<ResponseGroupDTO>>(){}));
    }

    @KafkaListener(topics = "get-groupbl")
    public void listenGroup(String group) throws JsonProcessingException {
        groupContainer = mapper.readValue(group, ResponseGroupDTO.class);
    }

    public void saveGroup(RequestGroupDTO group) throws JsonProcessingException {
        kafkaTemplateMessage.send("save-groupdal", mapper.writeValueAsString(group));
    }
}

