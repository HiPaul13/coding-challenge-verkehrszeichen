package com.hitzl.challenge.backend.controller;

import com.hitzl.challenge.backend.logic.Cluster;
import com.hitzl.challenge.backend.logic.ClusterDto;
import com.hitzl.challenge.backend.logic.ClusterLogic;
import com.hitzl.challenge.backend.logic.ClusterNearbyDto;
import com.hitzl.challenge.backend.observation.ObservationDto;
import com.hitzl.challenge.backend.observation.ObservationEntity;
import com.hitzl.challenge.backend.observation.ObservationRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    private final ObservationRepository repo;
    private static final Logger log = LoggerFactory.getLogger(ObservationController.class);

    public ObservationController(ObservationRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<?> receiveObservation(@RequestBody ObservationDto observation) {

        String error = validateObservation(observation);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        ObservationEntity entity = toEntity(observation);
        repo.save(entity);

        log.info("Received observation lat={}, lon={}, heading={}, type={}, value={}",
                observation.getLatitude(),
                observation.getLongitude(),
                observation.getHeading(),
                observation.getType(),
                observation.getValue()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/clusters")
    public List<ClusterDto> getClusters(@RequestParam(defaultValue = "30") double r) {

        List<ObservationDto> observations =
                repo.findAll().stream().map(this::toDto).toList();

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


    @GetMapping("/clusters/nearby")
    public List<ClusterNearbyDto> getClustersNearby(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "200") double radius,
            @RequestParam(defaultValue = "30") double r
    ) {

        List<ObservationDto> observations =
                repo.findAll().stream().map(this::toDto).toList();

        return new ClusterLogic(r)
                .clusterNearby(observations, r, lat, lon, radius);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countObservations() {
        return ResponseEntity.ok(repo.count());
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteAllObservations() {
        repo.deleteAll();
        return ResponseEntity.noContent().build();
    }

    private String validateObservation(ObservationDto o) {

        if (o == null) return "Request body is missing";

        if (o.getLatitude() == null || o.getLongitude() == null)
            return "latitude and longitude are required";

        if (o.getType() == null)
            return "type is required";

        if (o.getValue() == null || o.getValue().isBlank())
            return "value is required";

        double lat = o.getLatitude();
        double lon = o.getLongitude();

        if (lat < -90 || lat > 90)
            return "latitude out of range (-90..90)";

        if (lon < -180 || lon > 180)
            return "longitude out of range (-180..180)";

        if (o.getHeading() != null) {
            int h = o.getHeading();
            if (h < 0 || h > 359)
                return "heading out of range (0..359)";
        }

        return null;
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
