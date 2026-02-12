package com.hitzl.challenge.backend.observation;

import jakarta.persistence.*;

@Entity
@Table(name = "observations")
public class ObservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    private Double longitude;
    private Integer heading;

    @Enumerated(EnumType.STRING)
    private SignType type;

    private String value;

    public Long getId() { return id; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Integer getHeading() { return heading; }
    public void setHeading(Integer heading) { this.heading = heading; }

    public SignType getType() { return type; }
    public void setType(SignType type) { this.type = type; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}

