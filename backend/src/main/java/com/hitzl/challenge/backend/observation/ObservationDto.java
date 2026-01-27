package com.hitzl.challenge.backend.observation;

public class ObservationDto {

    private Double latitude;
    private Double longitude;
    private Integer heading;
    private SignType type;
    private String value; 


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getHeading() {
        return heading;
    }

    public void setHeading(Integer heading) {
        this.heading = heading;
    }

    public SignType getType() {
        return type;
    }


    public void setType(SignType type) {
        this.type = type; 
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSpeedLimit() {
        if (type != SignType.SPEED_LIMIT || value == null) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

