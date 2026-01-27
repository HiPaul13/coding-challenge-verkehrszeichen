package com.hitzl.challenge.backend.logic;

import com.hitzl.challenge.backend.observation.ObservationDto;

import java.util.ArrayList;
import java.util.List;


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
}
