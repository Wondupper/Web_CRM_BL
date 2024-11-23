package com.example.crm_bl.controllers;

import com.example.crm_bl.dtos.requests.RequestGenreDTO;
import com.example.crm_bl.services.Genres;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genres")
public class GenresController {
    private final Genres genres;

    @Autowired
    public GenresController(Genres genres) {
        this.genres = genres;
    }

    @GetMapping()
    public ResponseEntity<?> getGenres(){
        return new ResponseEntity<>(genres.getGenres(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGenres(@PathVariable Long id){
        return new ResponseEntity<>(genres.getGenre(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> getGenres(@RequestBody RequestGenreDTO genre) throws JsonProcessingException {
        genres.saveGenre(genre);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

