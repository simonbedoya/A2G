package com.monitoreosatelitalgps.a2g.Models;

/**
 * Created by sbv23 on 30/04/2017.
 */

public class VehiculoMap {

  /**
   * idAutomovil : 3
   * lat : 3.00719
   * lng : -76.2838
   * trk : 60.19
   * plk : SHT028
   * codeCarStatus : IGN
   * codeDeviceStatus : ACT
   * eventCode : null
   */

  private int idAutomovil;
  private double lat;
  private double lng;
  private float trk;
  private String plk;
  private String codeCarStatus;
  private String codeDeviceStatus;
  private Object eventCode;

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

  public float getTrk() {
    return trk;
  }

  public void setTrk(float trk) {
    this.trk = trk;
  }

  public String getPlk() {
    return plk;
  }

  public void setPlk(String plk) {
    this.plk = plk;
  }

  public String getCodeCarStatus() {
    return codeCarStatus;
  }

  public void setCodeCarStatus(String codeCarStatus) {
    this.codeCarStatus = codeCarStatus;
  }

  public String getCodeDeviceStatus() {
    return codeDeviceStatus;
  }

  public void setCodeDeviceStatus(String codeDeviceStatus) {
    this.codeDeviceStatus = codeDeviceStatus;
  }

  public Object getEventCode() {
    return eventCode;
  }

  public void setEventCode(Object eventCode) {
    this.eventCode = eventCode;
  }
}
