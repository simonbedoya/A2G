package com.monitoreosatelitalgps.a2g.Models;

/**
 * Created by sbv23 on 24/05/2017.
 */

public class DetailsVehicle {


    /**
     * idAutomovil : 32
     * lat : 2.4175
     * lng : -76.6975
     * spd : 0
     * alt : 0
     * trk : 0
     * plk : SHT126
     * telAuto : 3106525746
     * status : No Reporta
     * recDate : 2017-05-24 07:54:05.0
     * imei : 863286020712015
     * carStatus : Apagado
     * idPosition : 100
     * orientation : 90
     * iconUrl : NoAp90
     * codeCarStatus : OFF
     * eventCode : null
     */

    private int idAutomovil;
    private double lat;
    private double lng;
    private double spd;
    private int alt;
    private double trk;
    private String plk;
    private String telAuto;
    private String status;
    private String recDate;
    private String imei;
    private String carStatus;
    private int idPosition;
    private String orientation;
    private String iconUrl;
    private String codeCarStatus;
    private String eventCode;

    public int getIdAutomovil() {
        return idAutomovil;
    }

    public void setIdAutomovil(int idAutomovil) {
        this.idAutomovil = idAutomovil;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getSpd() {
        return spd;
    }

    public void setSpd(int spd) {
        this.spd = spd;
    }

    public int getAlt() {
        return alt;
    }

    public void setAlt(int alt) {
        this.alt = alt;
    }

    public double getTrk() {
        return trk;
    }

    public void setTrk(int trk) {
        this.trk = trk;
    }

    public String getPlk() {
        return plk;
    }

    public void setPlk(String plk) {
        this.plk = plk;
    }

    public String getTelAuto() {
        return telAuto;
    }

    public void setTelAuto(String telAuto) {
        this.telAuto = telAuto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public int getIdPosition() {
        return idPosition;
    }

    public void setIdPosition(int idPosition) {
        this.idPosition = idPosition;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCodeCarStatus() {
        return codeCarStatus;
    }

    public void setCodeCarStatus(String codeCarStatus) {
        this.codeCarStatus = codeCarStatus;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }
}
