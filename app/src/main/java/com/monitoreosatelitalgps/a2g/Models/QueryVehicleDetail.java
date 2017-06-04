package com.monitoreosatelitalgps.a2g.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sbv23 on 24/05/2017.
 */

public class QueryVehicleDetail {

    @SerializedName("plate")
    @Expose
    private String plate;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
}
