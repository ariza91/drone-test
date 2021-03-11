package com.drone.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drone.back.rabbitmq.RabbitMQSender;

@RestController
@RequestMapping(value = "/main/")
public class MainController {

    @Autowired
    RabbitMQSender rabbitMQSender;

    @GetMapping(value = "/producer")
    public String producer(@RequestParam("msg") String msg) {
        rabbitMQSender.send(msg);
        return "Message sent to the RabbitMQ JavaInUse Successfully";
    }

}
