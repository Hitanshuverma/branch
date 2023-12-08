package com.example.branchint.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.branchint.databinding.ItemContainerUserBinding;
import com.example.branchint.listeners.ThreadListener;
import com.example.branchint.models.ThreadsUser;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private final List<ThreadsUser> threadsUsers;
    private final ThreadListener threadListener;

    public UsersAdapter(List<ThreadsUser> threadsUsers, ThreadListener threadListener) {
        this.threadsUsers = threadsUsers;
        this.threadListener = threadListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContainerUserBinding itemContainerUserBinding = ItemContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false
        );
        return new UserViewHolder(itemContainerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(threadsUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return threadsUsers.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        ItemContainerUserBinding binding;
        UserViewHolder(ItemContainerUserBinding itemContainerUserBinding){
            super(itemContainerUserBinding.getRoot());
            binding = itemContainerUserBinding;

        }

        void setUserData(ThreadsUser threadsUser){
            binding.userId.setText(threadsUser.user);
            binding.threadId.setText("Thread: ");
            binding.threadId.append(threadsUser.thread);
            binding.lastMessage.setText(threadsUser.message);
            binding.time.setText(threadsUser.date);
            binding.getRoot().setOnClickListener(v -> threadListener.onThreadClicked(threadsUser));
        }
    }
}
