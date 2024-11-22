package com.example.crm_bl.controllers;

import com.example.crm_bl.dtos.requests.RequestGroupDTO;
import com.example.crm_bl.kafka.communication.Groups;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupsController {
    private final Groups groups;

    @Autowired
    public GroupsController(Groups groups) {
        this.groups = groups;
    }

    @GetMapping()
    public ResponseEntity<?> getGroups(){
        return new ResponseEntity<>(groups.getGroups(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroups(@PathVariable Long id){
        return new ResponseEntity<>(groups.getGroup(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> getGroups(@RequestBody RequestGroupDTO group) throws JsonProcessingException {
        groups.saveGroup(group);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

