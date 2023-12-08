package com.example.branchint.activities;

import static com.example.branchint.utils.Constants.ROOT_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.branchint.R;
import com.example.branchint.adapters.ChatAdapter;
import com.example.branchint.api.Message;
import com.example.branchint.api.MessageRequestBody;
import com.example.branchint.api.interfaces.ApiServices;
import com.example.branchint.databinding.ActivityChatBinding;
import com.example.branchint.models.ChatMessage;
import com.example.branchint.models.ThreadsUser;
import com.example.branchint.utils.Constants;
import com.example.branchint.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private ThreadsUser threadsUser;

    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
        init();
    }

    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages, preferenceManager);
        binding.chatRecyclerView.setAdapter(chatAdapter);
        for(Message m : threadsUser.list){
            ChatMessage cm = new ChatMessage();
            if (m.getAgent_id() == null)
                cm.agentId = "";
            else
                cm.agentId = String.valueOf(m.getAgent_id());
            cm.message = m.getBody();
            cm.dateTime = m.getTimestamp();
            cm.userID = String.valueOf(m.getUser_id());
            chatMessages.add(cm);
        }
        Collections.reverse(chatMessages);
        int count = chatMessages.size();
        if(count == 0)
            chatAdapter.notifyDataSetChanged();
        else {
            chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
            binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);

        }
        binding.chatRecyclerView.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }
    private void sendMessage(){
        Retrofit chatRetrofit = new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServices messageService = chatRetrofit.create(ApiServices.class);

        String authToken = preferenceManager.getString(Constants.KEY_AUTH_TOKEN);
        String threadId = threadsUser.thread;
        String messageBody = binding.inputMessage.getText().toString();
        MessageRequestBody requestMessage = new MessageRequestBody(threadId, messageBody);

        Call<Message> call = messageService.createNewMessage(authToken, requestMessage);

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    ChatMessage cm = new ChatMessage();
                    Message newMessage = response.body();
                    preferenceManager.putString(Constants.KEY_AGENT_ID,newMessage.getAgent_id().toString());
                    cm.agentId = String.valueOf(newMessage.getAgent_id());
                    cm.message = newMessage.getBody();
                    cm.dateTime = newMessage.getTimestamp();
                    cm.userID = String.valueOf(newMessage.getUser_id());
                    chatMessages.add(cm);
                    int count = chatMessages.size();
                    if(count == 0)
                        chatAdapter.notifyDataSetChanged();
                    else {
                        chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                        binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);

                    }
                } else {
                    Log.d("newmessage", "onResponse: failed"+response.code() + response.body());
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
        binding.inputMessage.setText(null);
    }
    private void loadReceiverDetails(){
        threadsUser = (ThreadsUser) getIntent().getSerializableExtra(Constants.Key_THREAD);
        binding.textName.setText("Thread: ");
        binding.textName.append(threadsUser.thread);
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> {
            //implemented it like this rather than using onBackPressed in order to refresh the homepage im mainactivity
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }

}