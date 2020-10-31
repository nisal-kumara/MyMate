package com.example.my_mate_01.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIservice {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAS95Brh0:APA91bE9gbluh6FtU_4V6xBu6Uh6N2WrnqqMoV-_tGum38ZVfmmFKykzxCHE0Ov_-NAFgf1gK5WW_4z9TczzaEv9wA17uYLZLh02BwUwqaeJxZY48RUpCaRJGUQ9ZPV0pQrcnijpKD7v"
    })
    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
