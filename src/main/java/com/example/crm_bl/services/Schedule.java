package com.example.crm_bl.services;

import com.example.crm_bl.dtos.requests.RequestScheduleDTO;
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

@Service
@EnableKafka
public class Schedule {

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private ObjectMapper mapper = new ObjectMapper();

    private ResponseScheduleDTO scheduleContainer;

    private List<ResponseScheduleDTO> allScheduleContainer = new ArrayList<>();

    @Autowired
    public Schedule(KafkaTemplate<String, String> kafkaTemplateMessage) {
        this.kafkaTemplateMessage = kafkaTemplateMessage;
    }

    public List<ResponseScheduleDTO> getAllSchedule(){
        allScheduleContainer.clear();
        kafkaTemplateMessage.send("get-allSchedulesdal", "get-allSchedules");
        while(allScheduleContainer.isEmpty()){
            continue;
        }
        List<ResponseScheduleDTO> schedule = new ArrayList<>(allScheduleContainer);
        return schedule;
    }

    public ResponseScheduleDTO getSchedule(Long id){
        scheduleContainer=null;
        kafkaTemplateMessage.send("get-scheduledal", id.toString());
        while (scheduleContainer==null){
            continue;
        }
        ResponseScheduleDTO schedule = scheduleContainer;
        return schedule;
    }


    @KafkaListener(topics = "get-allSchedulebl")
    public void listenAllSchedule(String schedules) throws JsonProcessingException {
        allScheduleContainer = new ArrayList<>(mapper.readValue(schedules, new TypeReference<List<ResponseScheduleDTO>>(){}));
    }

    @KafkaListener(topics = "get-schedulebl")
    public void listenSchedule(String schedule) throws JsonProcessingException {
        scheduleContainer = mapper.readValue(schedule, ResponseScheduleDTO.class);
    }

    public void saveSchedule(RequestScheduleDTO schedule) throws JsonProcessingException {
        kafkaTemplateMessage.send("save-scheduledal", mapper.writeValueAsString(schedule));
    }
}