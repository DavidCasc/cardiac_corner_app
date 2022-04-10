package com.example.cardiaccorner;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Create a list of the endpoints and their URLs
 */
public interface UserService {
    @GET("/fetchLogs/{user}")
    Call<LogsResponse> fetchLogs(@Path("user") String username);

    @POST("/login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @POST("/register")
    Call<RegisterResponse> userRegister(@Body RegisterRequest registerRequest);

    @DELETE("/logout/{token}")
    Call<LogoutResponse> logout(@Path("token") String token);

    @DELETE("/deleteAll/{user}")
    Call<ClearLogsResponse> deleteAll(@Path("user") String user);

    @POST("/addLog")
    Call<LogPostResponse> addLogs(@Body LogPostRequest logPostRequest);

    @DELETE("/deleteLog/{user}/{date}")
    Call<DeleteLogResponse> deleteLog(@Path("user") String user, @Path("date") String date);

}
