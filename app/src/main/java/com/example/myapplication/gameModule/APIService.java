package com.example.myapplication.gameModule;

import retrofit2.*;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {"Contetn-Type:Application/json",
            "Authorization:key=AAAAEd-Sr5M:APA91bHDFDW2v3DUHdqosUpE-e-jyLA_ymWy4SYrTxKTWumKvVl67QO6Nh7urImRbY-kzARhbGFntsImVn4NrtA__uuFUaJdXMZlSueyQhH9_qifHkolBIH3EPoFdf2PEME4ddeaTCmV"}
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
