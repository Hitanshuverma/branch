package com.example.branchint.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.branchint.databinding.ItemContainerReceivedMessageBinding;
import com.example.branchint.databinding.ItemContainerSentMessageBinding;
import com.example.branchint.models.ChatMessage;
import com.example.branchint.utils.Constants;
import com.example.branchint.utils.PreferenceManager;

import java.security.PublicKey;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private final List<ChatMessage> chatMessages;

    private final PreferenceManager preferenceManager;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<ChatMessage> chatMessages, PreferenceManager preferenceManager) {
        this.chatMessages = chatMessages;

        this.preferenceManager = preferenceManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
        else {
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()), parent, false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        }
        else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(Objects.equals(chatMessages.get(position).agentId, preferenceManager.getString(Constants.KEY_AGENT_ID))){
            return VIEW_TYPE_SENT;
        }
        else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    private final
    static class SentMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding){
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(Constants.convertToNormalTime(chatMessage.dateTime));
            if(chatMessage.agentId == ""){
                binding.senderId.setText("U: ");
                binding.senderId.append(chatMessage.userID);
            }
            else{
                binding.senderId.setText("A: ");
                binding.senderId.append(chatMessage.agentId);
            }
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding){
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message);
            binding.textDateTime.setText(Constants.convertToNormalTime(chatMessage.dateTime));
            if(chatMessage.agentId == ""){
                binding.senderId.setText("U: ");
                binding.senderId.append(chatMessage.userID);
            }
            else{
                binding.senderId.setText("A: ");
                binding.senderId.append(chatMessage.agentId);
            }

        }
    }
}
