package com.monitoreosatelitalgps.a2g.Models;

/**
 * Created by sbv23 on 24/05/2017.
 */

public class DailyReportVehicle {

    /**
     * speed : 0
     * dailyTrack : 42945
     * averageSpeed : 26.723968216146414
     * lastTrip : 0
     * odometer : 18775600
     * startTime : 1495624237000
     * plate : SHT126
     * numberSpeeding : 0
     * maxSpeed : 0
     * lastTurnOff :
     * engineOnTime :
     * averageSpeedFormated : 26.72
     * maxSpeedFormated : 0
     * startTimeFormated : 06:10:37
     * lastTurnOffFormated : --ND--
     * dailyTrackFormated : 42.945
     * lastTripFormated : 0
     */

    private double speed;
    private int dailyTrack;
    private double averageSpeed;
    private double lastTrip;
    private int odometer;
    private long startTime;
    private String plate;
    private int numberSpeeding;
    private double maxSpeed;
    private String lastTurnOff;
    private String engineOnTime;
    private String averageSpeedFormated;
    private String maxSpeedFormated;
    private String startTimeFormated;
    private String lastTurnOffFormated;
    private double dailyTrackFormated;
    private double lastTripFormated;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDailyTrack() {
        return dailyTrack;
    }

    public void setDailyTrack(int dailyTrack) {
        this.dailyTrack = dailyTrack;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public double getLastTrip() {
        return lastTrip;
    }

    public void setLastTrip(double lastTrip) {
        this.lastTrip = lastTrip;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getNumberSpeeding() {
        return numberSpeeding;
    }

    public void setNumberSpeeding(int numberSpeeding) {
        this.numberSpeeding = numberSpeeding;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getLastTurnOff() {
        return lastTurnOff;
    }

    public void setLastTurnOff(String lastTurnOff) {
        this.lastTurnOff = lastTurnOff;
    }

    public String getEngineOnTime() {
        return engineOnTime;
    }

    public void setEngineOnTime(String engineOnTime) {
        this.engineOnTime = engineOnTime;
    }

    public String getAverageSpeedFormated() {
        return averageSpeedFormated;
    }

    public void setAverageSpeedFormated(String averageSpeedFormated) {
        this.averageSpeedFormated = averageSpeedFormated;
    }

    public String getMaxSpeedFormated() {
        return maxSpeedFormated;
    }

    public void setMaxSpeedFormated(String maxSpeedFormated) {
        this.maxSpeedFormated = maxSpeedFormated;
    }

    public String getStartTimeFormated() {
        return startTimeFormated;
    }

    public void setStartTimeFormated(String startTimeFormated) {
        this.startTimeFormated = startTimeFormated;
    }

    public String getLastTurnOffFormated() {
        return lastTurnOffFormated;
    }

    public void setLastTurnOffFormated(String lastTurnOffFormated) {
        this.lastTurnOffFormated = lastTurnOffFormated;
    }

    public double getDailyTrackFormated() {
        return dailyTrackFormated;
    }

    public void setDailyTrackFormated(double dailyTrackFormated) {
        this.dailyTrackFormated = dailyTrackFormated;
    }

    public double getLastTripFormated() {
        return lastTripFormated;
    }

    public void setLastTripFormated(double lastTripFormated) {
        this.lastTripFormated = lastTripFormated;
    }
}
