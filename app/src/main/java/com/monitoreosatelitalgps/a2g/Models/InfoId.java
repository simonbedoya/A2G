package com.monitoreosatelitalgps.a2g.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sbv23 on 7/08/2017.
 */

public class InfoId {

    @SerializedName("idUser")
    @Expose
    private String idUser;

    @SerializedName("uuidCellphone")
    @Expose
    private String uuidCellphone;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUuidCellphone() {
        return uuidCellphone;
    }

    public void setUuidCellphone(String uuidCellphone) {
        this.uuidCellphone = uuidCellphone;
    }
}
