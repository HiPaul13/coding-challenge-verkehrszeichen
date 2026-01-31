package com.hitzl.challenge.backend.logic;

import com.hitzl.challenge.backend.observation.SignType;

public record ClusterDto(
    double centerLat,
    double centerLon,
    SignType type,
    String value,
    int size
) {}

