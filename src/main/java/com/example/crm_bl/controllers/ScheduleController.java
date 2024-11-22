package com.example.crm_bl.controllers;

import com.example.crm_bl.dtos.requests.RequestScheduleDTO;
import com.example.crm_bl.kafka.communication.Schedule;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final Schedule schedules;

    @Autowired
    public ScheduleController(Schedule schedules) {
        this.schedules = schedules;
    }

    @GetMapping()
    public ResponseEntity<?> getSchedule(){
        return new ResponseEntity<>(schedules.getAllSchedule(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSchedule(@PathVariable Long id){
        return new ResponseEntity<>(schedules.getSchedule(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> getSchedule(@RequestBody RequestScheduleDTO schedule) throws JsonProcessingException {
        schedules.saveSchedule(schedule);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

