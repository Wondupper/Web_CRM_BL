package com.example.crm_bl.services;

import com.example.crm_bl.dtos.requests.RequestScheduleDTO;
import com.example.crm_bl.dtos.responses.ResponseScheduleDTO;
import com.example.crm_bl.dtos.responses.ResponseScheduleDTO;
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
public class Schedule {

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private ObjectMapper mapper = new ObjectMapper();

    private CompletableFuture<List<ResponseScheduleDTO>> schedulesContainer;

    private CompletableFuture<ResponseScheduleDTO> scheduleContainer;

    @Autowired
    public Schedule(KafkaTemplate<String, String> kafkaTemplateMessage) {
        this.kafkaTemplateMessage = kafkaTemplateMessage;
    }

    public List<ResponseScheduleDTO> getAllSchedule(){
        schedulesContainer = new CompletableFuture<>();
        kafkaTemplateMessage.send("get-allSchedulesdal", "get-allSchedules");
        try {
            return schedulesContainer.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка получения", e);
        }
    }

    public ResponseScheduleDTO getSchedule(Long id){
        scheduleContainer = new CompletableFuture<>();
        kafkaTemplateMessage.send("get-scheduledal", id.toString());
        try {
            return scheduleContainer.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Ошибка получения", e);
        }
    }


    @KafkaListener(topics = "get-allSchedulebl")
    public void listenAllSchedule(String schedules) throws JsonProcessingException {
        List<ResponseScheduleDTO> scheduleList = new ArrayList<>(mapper.readValue(schedules, new TypeReference<List<ResponseScheduleDTO>>() {}));
            schedulesContainer.complete(scheduleList);
    }

    @KafkaListener(topics = "get-schedulebl")
    public void listenSchedule(String schedule) throws JsonProcessingException {
        ResponseScheduleDTO schedulee = mapper.readValue(schedule, ResponseScheduleDTO.class);
            scheduleContainer.complete(schedulee);

    }

    public void saveSchedule(RequestScheduleDTO schedule) throws JsonProcessingException {
        kafkaTemplateMessage.send("save-scheduledal", mapper.writeValueAsString(schedule));
    }
}