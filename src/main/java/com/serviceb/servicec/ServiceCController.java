package com.serviceb.servicec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@RestController
public class ServiceCController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceCController.class);

    private static final String INSTANCE_ID = UUID.randomUUID().toString();
    private Random random = new Random();

    @Autowired
    BuildProperties buildProperties;
    @Value("${VERSION:v1}")
    private String version;

    @GetMapping("/ping")
    public String ping() {
        LOGGER.info("Ping: name={}, version={}", buildProperties.getName(), version);
        return "Inside Service C " + version;
    }

    @GetMapping("/ping-with-random-error")
    public ResponseEntity<String> pingWithRandomError() {
        int r = random.nextInt(100);
        if (r % 2 == 0) {
            LOGGER.info("Ping with random error: name={}, version={}, random={}, httpCode={}",
                    buildProperties.getName(), version, r, HttpStatus.GATEWAY_TIMEOUT);
            return new ResponseEntity<>("Surprise " + INSTANCE_ID + " " + version, HttpStatus.GATEWAY_TIMEOUT);
        } else {
            LOGGER.info("Ping with random error: name={}, version={}, random={}, httpCode={}",
                    buildProperties.getName(), version, r, HttpStatus.OK);
            return new ResponseEntity<>("Inside Service C" + INSTANCE_ID + " " + version, HttpStatus.OK);
        }
    }

    @GetMapping("/ping-with-random-delay")
    public String pingWithRandomDelay() throws InterruptedException {
        int r = new Random().nextInt(3000);
        LOGGER.info("Ping with random delay: name={}, version={}, delay={}", buildProperties.getName(), version, r);
        Thread.sleep(r);
        return "Inside Service C " + version;
    }

    // if version is 1 then return error and check the retry

    @GetMapping("/ping-error-v1")
    public ResponseEntity<String> errorforv1() {
        int r = random.nextInt(100);
        if (version.equalsIgnoreCase("v1")) {
            LOGGER.info("Ping with random error: name={}, version={}, random={}, httpCode={}",
                    buildProperties.getName(), version, r, HttpStatus.GATEWAY_TIMEOUT);
            return new ResponseEntity<>("Surprise " + INSTANCE_ID + " " + version, HttpStatus.GATEWAY_TIMEOUT);
        } else {
            LOGGER.info("Ping with random error: name={}, version={}, random={}, httpCode={}",
                    buildProperties.getName(), version, r, HttpStatus.OK);
            return new ResponseEntity<>("Inside Service C" + INSTANCE_ID + " " + version, HttpStatus.OK);
        }
    }
    @GetMapping("/ping-with-fixed-delay")
    public String pingWithFixedDelay() throws InterruptedException {
       // int r = new Random().nextInt(3000);
        LOGGER.info("Ping with random delay: name={}, version={}, delay={}", buildProperties.getName(), version, 3000);
        Thread.sleep(3000);
        return "Inside Service C " + version;
    }
}
