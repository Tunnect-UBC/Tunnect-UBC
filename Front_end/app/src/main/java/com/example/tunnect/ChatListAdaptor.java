package com.example.tunnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatListAdaptor extends RecyclerView.Adapter<ChatListAdaptor.ViewHolder> {
    Context context;
    List<Chat> chatList;
    RecyclerView chatOptions;
    final View.OnClickListener onClickListener = new OpenChat();
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rowName;
        TextView rowLastMessage;
        TextView rowTimestamp;
        ImageView rowColour;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowName = itemView.findViewById(R.id.user_chat_name);
            rowLastMessage = itemView.findViewById(R.id.user_chat_message);
            rowTimestamp = itemView.findViewById(R.id.user_chat_date);
            rowColour = itemView.findViewById(R.id.user_chat_icon);
        }
    }

    public ChatListAdaptor(Context context, List<Chat> chatList, RecyclerView chatOptions) {
        this.context = context;
        this. chatList = chatList;
        this.chatOptions = chatOptions;
    }

    @NonNull
    @Override
    public ChatListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_chat, parent, false);
        view.setOnClickListener(onClickListener);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdaptor.ViewHolder holder, int position) {
        Chat chat = chatOptions.get(position);
        holder.rowName.setText(chat.getName());
        holder.rowLastMessage.setText(chat.getLastMessage());
        holder.rowTimestamp.setText(chat.getTimestamp());
        holder.rowColour.setBackgroundColor(chat.getColour());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class OpenChat implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int itemPosition = chatOptions.getChildLayoutPosition(v);
            long
                    item = chatList.get(itemPosition).getId();
        }
    }
}
