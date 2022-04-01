package com.example.cardiaccorner;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The ApiClient Class is used to build a user service with
 * Retrofit2. This is used to make HTTP calls to the web API
 * Data server
 * @Author: David Casciano
 */
public class ApiClient {
    private static Retrofit getRetrofit(){

        //This is used for debugging the HTTP communication between the client and the API
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //This will build the HTTP client to make the call
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)   //If it cannot connect to the server in 10 seconds timeout
                .writeTimeout(10, TimeUnit.SECONDS)     //If it cannot write to the server in 10 seconds timeout
                .readTimeout(30, TimeUnit.SECONDS)      //If it does not get a response in 30 seconds timeout
                .build();

        //Create the Retrofit handler
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.2.69:3003") //Change to the address of the API Server
                .client(okHttpClient)
                .build();

        return retrofit;

    }

    //Get all the calls that will be made through the HTTP Client
    public static UserService getUserService() {
        UserService userService = getRetrofit().create(UserService.class);

        return userService;
    }
}
