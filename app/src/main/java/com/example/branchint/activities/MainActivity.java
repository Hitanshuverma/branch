package com.example.branchint.activities;


import static com.example.branchint.utils.Constants.ROOT_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.branchint.adapters.UsersAdapter;
import com.example.branchint.api.Message;
import com.example.branchint.api.interfaces.ApiServices;
import com.example.branchint.databinding.ActivityMainBinding;
import com.example.branchint.listeners.ThreadListener;
import com.example.branchint.models.ThreadsUser;
import com.example.branchint.utils.Constants;
import com.example.branchint.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements ThreadListener {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;

    Map<Integer, List<Message>> threadsMap;

    Retrofit mainRetrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager((getApplicationContext()));
        mainRetrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiServices messageService = mainRetrofit.create(ApiServices.class);
        String authToken = preferenceManager.getString(Constants.KEY_AUTH_TOKEN);
        Call<List<Message>> call = messageService.getAllMessages(authToken);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()){
                    List<Message> messages = response.body();

                    threadsMap = cleanMessage(messages);
                    getUsers();
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                threadsMap = new HashMap<>();
            }
        });
        setListeners();
        new Handler().post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private void setListeners(){
        binding.imageSignOut.setOnClickListener(v -> {
            signOut();
        });
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void getUsers(){
        loading(true);
        List<ThreadsUser> threadsUsers = new ArrayList<>();
        if(threadsMap == null)
            showErrorMessage();
        else {
            for (List<Message> list : threadsMap.values()) {
                ThreadsUser threadsUser = new ThreadsUser();
                threadsUser.list = list;
                threadsUser.thread = String.valueOf(list.get(0).getThread_id());
                if (list.get(0).getAgent_id() == null)
                    threadsUser.user = "U: " + list.get(0).getUser_id();
                else
                    threadsUser.user = "A: " + list.get(0).getAgent_id();
                threadsUser.message = list.get(0).getBody();
                threadsUser.date = Constants.convertToNormalTime(list.get(0).getTimestamp());
                threadsUsers.add(threadsUser);
            }
        }
        if (threadsUsers.size() > 0){
            loading(false);
            UsersAdapter usersAdapter = new UsersAdapter(threadsUsers, this);
            binding.userRecyclerView.setAdapter(usersAdapter);
            binding.userRecyclerView.setVisibility(View.VISIBLE);

        }else {
            loading(false);
            showErrorMessage();
        }
    }
    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s", "No threads available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }
    private void loadUserDetails(){
//        binding.
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void signOut(){
        showToast("Signing out...");
        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, false);
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private Map<Integer, List<Message>> cleanMessage(List<Message> messages){
        Map<Integer, List<Message>> map = new HashMap<>();
        for (Message msg : messages){
            int id = msg.getThread_id();
            if (!map.containsKey(id)){
                map.put(id, new ArrayList<>());
            }
            Objects.requireNonNull(map.get(id)).add(msg);
        }
        //descending sorting
        for (List<Message> list : map.values()){
            list.sort((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
        }
        //ascending sorting
//        for (List<Message> list : map.values()) {
//            list.sort((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()));
//        }

        return map;
    }

    @Override
    public void onThreadClicked(ThreadsUser threadsUser) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra(Constants.Key_THREAD,threadsUser);
        startActivity(intent);
        finish();
    }
}