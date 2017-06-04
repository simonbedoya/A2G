package com.monitoreosatelitalgps.a2g.Fragment.Interface;

import com.monitoreosatelitalgps.a2g.Models.VehiculoMap;

import java.util.List;

/**
 * Created by sbv23 on 10/05/2017.
 */

public interface MapInterface {

    void setTab(int tab);

    void setPlaca(String placa);

    void setListVehiculos(List<VehiculoMap> vehiculoMapList);

    void viewDetails(boolean state);

    void closeViewSearch(boolean state);
}
