package com.troutee.dto.request;

/**
 * Created by vicente on 13/07/16.
 */
public class XCheckinRequest extends XToken{

    private Integer clientid;
    private Double lat;
    private Double lon;

    public XCheckinRequest(Integer clientid, Double lat, Double lon) {
        this.clientid = clientid;
        this.lat = lat;
        this.lon = lon;
    }

    public XCheckinRequest(String token, Integer clientid, Double lat, Double lon) {
        super(token);
        this.clientid = clientid;
        this.lat = lat;
        this.lon = lon;
    }

    public Integer getClientid() {
        return clientid;
    }

    public void setClientid(Integer clientid) {
        this.clientid = clientid;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
