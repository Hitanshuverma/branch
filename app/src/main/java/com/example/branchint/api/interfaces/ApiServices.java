package com.example.branchint.api.interfaces;

import static com.example.branchint.utils.Constants.LOGIN_URL;
import static com.example.branchint.utils.Constants.MESSAGE_URL;

import com.example.branchint.api.Message;
import com.example.branchint.api.MessageRequestBody;
import com.example.branchint.api.RequestPost;
import com.example.branchint.api.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiServices {
    @POST(LOGIN_URL)
    Call<UserData> postUser(@Body RequestPost requestPost);

    @GET(MESSAGE_URL)
    Call<List<Message>> getAllMessages(@Header("X-Branch-Auth-Token") String authToken);

    @POST(MESSAGE_URL)
    Call<Message> createNewMessage(@Header("X-Branch-Auth-Token") String authToken,
                                   @Body MessageRequestBody messageRequestBody);

}