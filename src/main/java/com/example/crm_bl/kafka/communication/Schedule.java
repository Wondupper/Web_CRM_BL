package com.example.crm_bl.kafka.communication;

import com.example.crm_bl.dtos.requests.RequestScheduleDTO;
import com.example.crm_bl.dtos.requests.RequestScheduleDTO;
import com.example.crm_bl.dtos.responses.ResponseScheduleDTO;
import com.example.crm_bl.dtos.responses.ResponseScheduleDTO;
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
public class Schedule {

    private final KafkaTemplate<String, RequestScheduleDTO> kafkaTemplate;

    private final KafkaTemplate<String, String> kafkaTemplateMessage;

    private final KafkaTemplate<String, Long> kafkaTemplateId;

    private ResponseScheduleDTO scheduleContainer;

    private List<ResponseScheduleDTO> allScheduleContainer = new ArrayList<>();

    @Autowired
    public Schedule(KafkaTemplate<String, RequestScheduleDTO> kafkaTemplate, KafkaTemplate<String, String> kafkaTemplateMessage, KafkaTemplate<String, Long> kafkaTemplateId) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateMessage = kafkaTemplateMessage;
        this.kafkaTemplateId = kafkaTemplateId;
    }

    public List<ResponseScheduleDTO> getAllSchedule(){
        allScheduleContainer.clear();
        kafkaTemplateMessage.send("get-allSchedules", "get-allSchedules");
        while(allScheduleContainer.isEmpty()){
            continue;
        }
        List<ResponseScheduleDTO> schedule = new ArrayList<>(allScheduleContainer);
        return schedule;
    }

    public ResponseScheduleDTO getSchedule(Long id){
        scheduleContainer=null;
        kafkaTemplateId.send("get-schedules", id);
        while (scheduleContainer==null){
            continue;
        }
        ResponseScheduleDTO schedule = scheduleContainer;
        return schedule;
    }


    @KafkaListener(topics = "get-schedules")
    public void listenSchedule( List<ResponseScheduleDTO> schedules) {
        allScheduleContainer = new ArrayList<>(schedules);
    }

    @KafkaListener(topics = "get-schedule")
    public void listenSchedule(ResponseScheduleDTO schedule) {
        scheduleContainer = schedule;
    }

    public void saveSchedule(RequestScheduleDTO schedule) {
        kafkaTemplate.send("save-schedule", schedule);
    }
}