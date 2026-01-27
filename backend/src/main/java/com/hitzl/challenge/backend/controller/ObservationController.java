package com.hitzl.challenge.backend.controller;
import com.hitzl.challenge.backend.observation.ObservationDto;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    private final List<ObservationDto> observations = new ArrayList<>();
    

    @PostMapping
    public void receiveObservation(@RequestBody ObservationDto observation) {
        observations.add(observation);

        System.out.println("Received observation:");
        System.out.println(
                "lat=" + observation.getLatitude() +
                ", lon=" + observation.getLongitude() +
                ", heading=" + observation.getHeading() +
                ", type=" + observation.getType() +
                ", speedLimit=" + observation.getSpeedLimit()
        );
    }

}
