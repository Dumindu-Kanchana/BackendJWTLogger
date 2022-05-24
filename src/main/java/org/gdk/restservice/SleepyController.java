package org.gdk.restservice;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SleepyController {
    private static final Logger logger = LoggerFactory.getLogger(SleepyController.class);

    @GetMapping("/sleepy-backend/{sleepTime}")
    public ResponseEntity<String> backendJWT(@PathVariable String sleepTime) throws InterruptedException {
        // code that uses the path param and make the server sleep
        logger.info("sleep time is " + sleepTime + " seconds.");
        int sleepT = Integer.parseInt(sleepTime);
        sleepT *= 1000;
        Thread.sleep(sleepT);
        logger.info("sleep is over!.");
        return new ResponseEntity<String>("server slept for " + sleepTime + " seconds.", HttpStatus.OK);
    }
}
