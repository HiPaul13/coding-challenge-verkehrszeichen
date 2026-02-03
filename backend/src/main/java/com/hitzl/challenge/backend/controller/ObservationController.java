package com.hitzl.challenge.backend.controller;
import com.hitzl.challenge.backend.observation.ObservationDto;
import com.hitzl.challenge.backend.logic.Cluster;
import com.hitzl.challenge.backend.logic.ClusterDto;
import com.hitzl.challenge.backend.logic.ClusterLogic;
import com.hitzl.challenge.backend.logic.ClusterNearbyDto;

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

    @GetMapping("/clusters")
    public List<ClusterDto> getClusters(
            @RequestParam(defaultValue = "30") double r
    ) {
        ClusterLogic logic = new ClusterLogic(r);
        List<Cluster> clusters = logic.cluster(observations);

        return clusters.stream()
                .map(c -> new ClusterDto(
                        c.getCenterLat(),
                        c.getCenterLon(),
                        c.getType(),
                        c.getValue(),
                        c.size()
                ))
                .toList();
    }

    @GetMapping("/count")
    public int countObservations() {
        return observations.size();
    }

    @DeleteMapping
    public void deleteAllObservations() {
        observations.clear();
    }

    @GetMapping("/clusters/nearby")
    public List<ClusterNearbyDto> getClustersNearby(
        @RequestParam double lat,
        @RequestParam double lon,
        @RequestParam(defaultValue = "200") double radius,
        @RequestParam(defaultValue = "30") double r
    ) {
        return new ClusterLogic(r).clusterNearby(observations, r, lat, lon, radius);
    }



}


