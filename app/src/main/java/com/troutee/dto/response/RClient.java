package com.troutee.dto.response;

/**
 * Created by vicente on 28/06/16.
 */
public class RClient {

    private Integer id;
    private String code;
    private String name;
    private String phone;
    private ClientStatus status;
    private Double lat;
    private Double lon;
    private Integer version;

    public RClient() {
    }

    public RClient(Integer id, String code, String name, String phone, ClientStatus status, Double lat, Double lon, Integer version) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.lat = lat;
        this.lon = lon;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
