package com.hitzl.challenge.backend.logic;

import com.hitzl.challenge.backend.observation.ObservationDto;
import com.hitzl.challenge.backend.observation.SignType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cluster {

    private final List<ObservationDto> points = new ArrayList<>();

    private final SignType type;
    private final String value;

    private double centerLat;
    private double centerLon;

    public Cluster(ObservationDto first) {
        this.type = first.getType();
        this.value = first.getValue();
        add(first);
    }

    public boolean canAccept(ObservationDto o, double radiusMeters) {
    if (!Objects.equals(type, o.getType())) return false;
    if (!Objects.equals(value, o.getValue())) return false;

    double distM = GeoUtils.haversineMeters(centerLat, centerLon, o.getLatitude(), o.getLongitude());
    return distM <= radiusMeters;
}

    public void add(ObservationDto o) {
        points.add(o);

        int n = points.size();
        centerLat = centerLat + (o.getLatitude() - centerLat) / n;
        centerLon = centerLon + (o.getLongitude() - centerLon) / n;
    }

    public double getCenterLat() {
        return centerLat;
    }

    public double getCenterLon() {
        return centerLon;
    }

    public SignType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int size() {
        return points.size();
    }

    public List<ObservationDto> getPoints() {
        return points;
    }
}
