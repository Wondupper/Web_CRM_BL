package com.example.crm_bl.controllers;

import com.example.crm_bl.dtos.requests.RequestTrackDTO;
import com.example.crm_bl.kafka.communication.Tracks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tracks")
public class TracksController {
    private final Tracks tracks;

    @Autowired
    public TracksController(Tracks tracks) {
        this.tracks = tracks;
    }

    @GetMapping()
    public ResponseEntity<?> getTracks(){
        return new ResponseEntity<>(tracks.getTracks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTracks(@PathVariable Long id){
        return new ResponseEntity<>(tracks.getTrack(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> getTracks(@RequestBody RequestTrackDTO track){
        tracks.saveTrack(track);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

