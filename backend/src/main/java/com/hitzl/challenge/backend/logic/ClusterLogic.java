package com.hitzl.challenge.backend.logic;

import com.hitzl.challenge.backend.observation.ObservationDto;


import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;


public class ClusterLogic {

    private final double radius;

    public ClusterLogic(double radius) {
        this.radius = radius;
    }

    public List<Cluster> cluster(List<ObservationDto> observations) {
        List<Cluster> clusters = new ArrayList<>();

        for (ObservationDto obs : observations) {
            boolean added = false;

            for (Cluster c : clusters) {
                if (c.canAccept(obs, radius)) {
                    c.add(obs);
                    added = true;
                    break;
                }
            }

            if (!added) {
                clusters.add(new Cluster(obs));
            }
        }

        return clusters;
    }

    public List<ClusterNearbyDto> clusterNearby(
        List<ObservationDto> observations,
        double clusterRadiusMeters,
        double vehicleLat,
        double vehicleLon,
        double searchRadiusMeters
    ) {
        ClusterLogic clustering = new ClusterLogic(clusterRadiusMeters);
        List<Cluster> clusters = clustering.cluster(observations);

        return clusters.stream()
            .map(c -> {
                double dist = GeoUtils.haversineMeters(vehicleLat, vehicleLon, c.getCenterLat(), c.getCenterLon());
                return new ClusterNearbyDto(
                        c.getCenterLat(),
                        c.getCenterLon(),
                        c.getType(),
                        c.getValue(),
                        c.size(),
                        dist
                );
            })
            .filter(dto -> dto.getDistanceMeters() <= searchRadiusMeters)
            .sorted(Comparator.comparingDouble(ClusterNearbyDto::getDistanceMeters))
            .toList();
}

}
