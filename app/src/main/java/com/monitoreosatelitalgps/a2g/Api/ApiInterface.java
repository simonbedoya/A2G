package com.monitoreosatelitalgps.a2g.Api;

import com.monitoreosatelitalgps.a2g.Models.Authenticate;
import com.monitoreosatelitalgps.a2g.Models.DailyReportVehicle;
import com.monitoreosatelitalgps.a2g.Models.DetailsVehicle;
import com.monitoreosatelitalgps.a2g.Models.Query;
import com.monitoreosatelitalgps.a2g.Models.QueryVehicleDetail;
import com.monitoreosatelitalgps.a2g.Models.VehiculoMap;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by sbv23 on 30/04/2017.
 */

public interface ApiInterface {

    @POST("tracking-position/api/location/markers")
    Observable<List<VehiculoMap>> getVehiculoMap(@Body Query query);

    @POST("tracking-position/api/location/details")
    Observable<DetailsVehicle> getDatailsVehicle(@Body QueryVehicleDetail query);

    @POST("tracking-position/api/location/daily-report")
    Observable<DailyReportVehicle> getDailyReportVehicle(@Body QueryVehicleDetail query);

    @FormUrlEncoded
    @POST("auth/realms/A2G_Realm/protocol/openid-connect/token")
    Observable<Authenticate> getAuth(@FieldMap HashMap<String, String> param);
}
