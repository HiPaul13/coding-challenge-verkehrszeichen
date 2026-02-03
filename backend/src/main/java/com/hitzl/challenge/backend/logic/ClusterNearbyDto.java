package com.hitzl.challenge.backend.logic;

import com.hitzl.challenge.backend.observation.SignType;

public class ClusterNearbyDto {
    private final double centerLat;
    private final double centerLon;
    private final SignType type;
    private final String value;
    private final int size;
    private final double distanceMeters;

    public ClusterNearbyDto(double centerLat, double centerLon, SignType type, String value, int size, double distanceMeters) {
        this.centerLat = centerLat;
        this.centerLon = centerLon;
        this.type = type;
        this.value = value;
        this.size = size;
        this.distanceMeters = distanceMeters;
    }

    public double getCenterLat() { return centerLat; }
    public double getCenterLon() { return centerLon; }
    public SignType getType() { return type; }
    public String getValue() { return value; }
    public int getSize() { return size; }
    public double getDistanceMeters() { return distanceMeters; }
}

