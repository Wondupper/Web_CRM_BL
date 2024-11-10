package com.example.crm_bl.controllers;

import com.example.crm_bl.dtos.requests.RequestArtistDTO;
import com.example.crm_bl.kafka.communication.Artists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artists")
public class ArtistsController {
    private final Artists artists;

    @Autowired
    public ArtistsController(Artists artists) {
        this.artists = artists;
    }

    @GetMapping()
    public ResponseEntity<?> getArtists(){
        return new ResponseEntity<>(artists.getArtists(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArtists(@PathVariable Long id){
        return new ResponseEntity<>(artists.getArtist(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> getArtists(@RequestBody RequestArtistDTO artist){
        artists.saveArtist(artist);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

