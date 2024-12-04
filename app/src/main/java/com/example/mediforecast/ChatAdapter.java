package com.example.mediforecast;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    // Define view types for user and system messages
    private static final int TYPE_USER = 1;
    private static final int TYPE_SYSTEM = 2;

    @Override
    public int getItemViewType(int position) {
        // Return the appropriate view type based on whether it's a user or system message
        return chatMessages.get(position).isUserMessage() ? TYPE_USER : TYPE_SYSTEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the appropriate layout based on the view type
        if (viewType == TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_message, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_system_message, parent, false);
            return new SystemMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);

        if (holder instanceof UserMessageViewHolder) {
            UserMessageViewHolder userHolder = (UserMessageViewHolder) holder;
            userHolder.userMessageText.setText(chatMessage.getMessage());

            // Align user message to the right
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) userHolder.userMessageText.getLayoutParams();
            params.gravity = Gravity.END; // Right alignment
            userHolder.userMessageText.setLayoutParams(params);

        } else if (holder instanceof SystemMessageViewHolder) {
            SystemMessageViewHolder systemHolder = (SystemMessageViewHolder) holder;
            systemHolder.systemMessageText.setText(chatMessage.getMessage());

            // Align system message to the left
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) systemHolder.systemMessageText.getLayoutParams();
            params.gravity = Gravity.START; // Left alignment
            systemHolder.systemMessageText.setLayoutParams(params);
        }
        Log.d("ChatAdapter", "Position: " + position + ", ViewType: " + getItemViewType(position));

    }


    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    // ViewHolder for user message
    public static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView userMessageText;

        public UserMessageViewHolder(View itemView) {
            super(itemView);
            userMessageText = itemView.findViewById(R.id.userMessageText);
        }
    }

    // ViewHolder for system message
    public static class SystemMessageViewHolder extends RecyclerView.ViewHolder {
        TextView systemMessageText;

        public SystemMessageViewHolder(View itemView) {
            super(itemView);
            systemMessageText = itemView.findViewById(R.id.systemMessageText);
        }
    }
}