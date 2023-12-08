package com.example.branchint.activities;

import static com.example.branchint.utils.Constants.ROOT_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.branchint.api.RequestPost;
import com.example.branchint.api.UserData;
import com.example.branchint.api.interfaces.ApiServices;
import com.example.branchint.databinding.ActivitySignInBinding;
import com.example.branchint.utils.Constants;
import com.example.branchint.utils.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    Retrofit loginRetrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(binding.getRoot());
        loginRetrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        setListeners();
    }

    private void setListeners(){
        binding.buttonSignIn.setOnClickListener(v -> {
            if(isValidSignInDetails()){
                signIn();
            }
        });
    }

    private void signIn(){
        loading(true);
        ApiServices apiServices = loginRetrofit.create(ApiServices.class);
        String email = binding.inputEmail.getText().toString();
        String key = binding.inputPassword.getText().toString();
        RequestPost requestPost = new RequestPost(email,key);
        Call<UserData> call = apiServices.postUser(requestPost);
        call.enqueue(new Callback<UserData>() {
             @Override
             public void onResponse(Call<UserData> call, Response<UserData> response) {

                 if(response.code() == 200){
                     preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                     preferenceManager.putString(Constants.KEY_AUTH_TOKEN, response.body().auth_token);
                     Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     startActivity(intent);
                 } else{
                     loading(false);
                     showToast("Unable to sign in");
                 }
             }

             @Override
             public void onFailure(Call<UserData> call, Throwable t) {

             }
            }
        );
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.buttonSignIn.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails(){
        String email = binding.inputEmail.getText().toString();
        if(email.trim().isEmpty()){
            showToast("Enter E-mail");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter valid E-mail");
            return false;
        }else if (email.trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        }else{
            return true;
        }
    }
}