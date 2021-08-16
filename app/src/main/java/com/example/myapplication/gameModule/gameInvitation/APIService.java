package com.example.myapplication.gameModule.gameInvitation;

import retrofit2.*;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {"Content-Type:Application/json",
            "Authorization:key=AAAAEd-Sr5M:APA91bHDFDW2v3DUHdqosUpE-e-jyLA_ymWy4SYrTxKTWumKvVl67QO6Nh7urImRbY-kzARhbGFntsImVn4NrtA__uuFUaJdXMZlSueyQhH9_qifHkolBIH3EPoFdf2PEME4ddeaTCmV"}
    )
    @POST("fcm/send")
    Call<My_response> sendNotification(@Body Notification_sender body);
}
