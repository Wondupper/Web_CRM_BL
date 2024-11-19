package com.example.crm_bl.kafka.communication;

import com.example.crm_bl.dtos.requests.RequestGroupDTO;
import com.example.crm_bl.dtos.requests.RequestGroupDTO;
import com.example.crm_bl.dtos.responses.ResponseGroupDTO;
import com.example.crm_bl.dtos.responses.ResponseGroupDTO;
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
public class Groups {

    private final KafkaTemplate<String, RequestGroupDTO> kafkaTemplate;

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private final KafkaTemplate<String, Long> kafkaTemplateId;

    private ResponseGroupDTO groupContainer;

    private List<ResponseGroupDTO> groupsContainer = new ArrayList<>();

    @Autowired
    public Groups(KafkaTemplate<String, RequestGroupDTO> kafkaTemplate, KafkaTemplate<String, String> kafkaTemplateMessage, KafkaTemplate<String, Long> kafkaTemplateId) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateMessage = kafkaTemplateMessage;
        this.kafkaTemplateId = kafkaTemplateId;
    }

    public List<ResponseGroupDTO> getGroups() {
        groupsContainer.clear();
        kafkaTemplateMessage.send("get-groups", "get-groups");
        while (groupsContainer.isEmpty()) {
            continue;
        }
        List<ResponseGroupDTO> groups = new ArrayList<>(groupsContainer);
        return groups;
    }

    public ResponseGroupDTO getGroup(Long id) {
        groupContainer = null;
        kafkaTemplateId.send("get-groups", id);
        while (groupContainer == null) {
            continue;
        }
        ResponseGroupDTO group = groupContainer;
        return group;
    }


    @KafkaListener(topics = "get-groups")
    public void listenGroups(List<ResponseGroupDTO> groups) {
        groupsContainer = new ArrayList<>(groups);
    }

    @KafkaListener(topics = "get-group")
    public void listenGroup(ResponseGroupDTO group) {
        groupContainer = group;
    }

    public void saveGroup(RequestGroupDTO group) {
        kafkaTemplate.send("save-group", group);
    }
}

