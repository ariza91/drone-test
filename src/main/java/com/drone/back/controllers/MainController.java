package com.drone.back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drone.back.services.IMainService;

@RestController
@RequestMapping(value = "/api/")
public class MainController {

    @Autowired
    IMainService mainService;

    @PostMapping(value = "/start")
    public ResponseEntity<?> start() {
        try {

            mainService.start();

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);

        } catch (Exception e) {
            // error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
