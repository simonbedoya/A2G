package com.monitoreosatelitalgps.a2g.Api;

import android.support.annotation.AnyThread;

import com.monitoreosatelitalgps.a2g.Models.Authenticate;
import com.monitoreosatelitalgps.a2g.Models.DailyReportVehicle;
import com.monitoreosatelitalgps.a2g.Models.DetailsVehicle;
import com.monitoreosatelitalgps.a2g.Models.InfoPerson;
import com.monitoreosatelitalgps.a2g.Models.Query;
import com.monitoreosatelitalgps.a2g.Models.QueryVehicleDetail;
import com.monitoreosatelitalgps.a2g.Models.VehiculoMap;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by sbv23 on 30/04/2017.
 */

public interface ApiInterface {

    @POST("tracking-position-v2/api/location/markers")
    Observable<List<VehiculoMap>> getVehiculoMap(@Header("Authorization") String token,@Body Query query);
    //Observable<List<VehiculoMap>> getVehiculoMap(@Body Query query);

    @POST("tracking-position-v2/api/location/details")
    Observable<DetailsVehicle> getDatailsVehicle(@Header("Authorization") String token,@Body QueryVehicleDetail query);
    //Observable<DetailsVehicle> getDatailsVehicle(@Body QueryVehicleDetail query);

    @POST("tracking-position-v2/api/location/daily-report")
    Observable<DailyReportVehicle> getDailyReportVehicle(@Header("Authorization") String token,@Body QueryVehicleDetail query);
    //Observable<DailyReportVehicle> getDailyReportVehicle(@Body QueryVehicleDetail query);

    @FormUrlEncoded
    @POST("auth/realms/A2G_Realm/protocol/openid-connect/token")
    Observable<Authenticate> getAuth(@FieldMap HashMap<String, String> param);

    @GET
    Observable<InfoPerson> getInfoPerson(@Url String url, @Header("Authorization") String token);

    @PUT("tracking-position-v2/api/account/")
    Observable<InfoPerson> updateInfoPerson(@Header("Authorization") String token, @Body InfoPerson infoPerson);
}
