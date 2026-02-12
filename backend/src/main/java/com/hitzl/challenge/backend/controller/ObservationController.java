package com.hitzl.challenge.backend.controller;

import com.hitzl.challenge.backend.logic.Cluster;
import com.hitzl.challenge.backend.logic.ClusterDto;
import com.hitzl.challenge.backend.logic.ClusterLogic;
import com.hitzl.challenge.backend.logic.ClusterNearbyDto;
import com.hitzl.challenge.backend.observation.ObservationDto;
import com.hitzl.challenge.backend.observation.ObservationEntity;
import com.hitzl.challenge.backend.observation.ObservationRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    private final ObservationRepository repo;

    public ObservationController(ObservationRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public void receiveObservation(@RequestBody ObservationDto observation) {
        ObservationEntity e = toEntity(observation);
        repo.save(e);

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
    public List<ClusterDto> getClusters(@RequestParam(defaultValue = "30") double r) {
        List<ObservationDto> observations = repo.findAll().stream().map(this::toDto).toList();

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
    public long countObservations() {
        return repo.count();
    }

    @DeleteMapping
    public void deleteAllObservations() {
        repo.deleteAll();
    }

    @GetMapping("/clusters/nearby")
    public List<ClusterNearbyDto> getClustersNearby(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "200") double radius,
            @RequestParam(defaultValue = "30") double r
    ) {
        List<ObservationDto> observations = repo.findAll().stream().map(this::toDto).toList();
        return new ClusterLogic(r).clusterNearby(observations, r, lat, lon, radius);
    }

    private ObservationEntity toEntity(ObservationDto dto) {
        ObservationEntity e = new ObservationEntity();
        e.setLatitude(dto.getLatitude());
        e.setLongitude(dto.getLongitude());
        e.setHeading(dto.getHeading());
        e.setType(dto.getType());
        e.setValue(dto.getValue());
        return e;
    }

    private ObservationDto toDto(ObservationEntity e) {
        ObservationDto dto = new ObservationDto();
        dto.setLatitude(e.getLatitude());
        dto.setLongitude(e.getLongitude());
        dto.setHeading(e.getHeading());
        dto.setType(e.getType());
        dto.setValue(e.getValue());
        return dto;
    }
}
