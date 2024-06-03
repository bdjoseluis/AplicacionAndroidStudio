package com.example.tfg.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.R;
import com.example.tfg.domain.MessageDTO;

import java.util.List;

public class MessageAdapter2 extends RecyclerView.Adapter<MessageAdapter2.MessageViewHolder> {
    private List<MessageDTO> messageList;
    private String currentUser;

    public MessageAdapter2(List<MessageDTO> messageList, String currentUser) {
        this.messageList = messageList;
        this.currentUser = currentUser;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageDTO message = messageList.get(position);
        Log.d("mandador: ", message.getSender());
        Log.d("usuario: ", currentUser);
        if (message.getSender().equals(currentUser)) {
            // Si el mensaje fue enviado por el usuario actual
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightChatUser.setText(message.getSender());
            holder.rightChatTextView.setText(message.getContent());
        } else {
            // Si el mensaje fue enviado por otro usuario
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatUser.setText(message.getSender());
            holder.leftChatTextView.setText(message.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatView;
        LinearLayout rightChatView;
        TextView leftChatUser;
        TextView leftChatTextView;
        TextView rightChatUser;
        TextView rightChatTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatView = itemView.findViewById(R.id.left_chat_view);
            rightChatView = itemView.findViewById(R.id.right_chat_view);
            leftChatUser = itemView.findViewById(R.id.left_chat_user);
            leftChatTextView = itemView.findViewById(R.id.left_chat_text_view);
            rightChatUser = itemView.findViewById(R.id.right_chat_user);
            rightChatTextView = itemView.findViewById(R.id.right_chat_text_view);
        }
    }
}
