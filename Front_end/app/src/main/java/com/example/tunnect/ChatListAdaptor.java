package com.example.tunnect;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*
 * This class takes information from a list of chats and populates the given recycler view in the
 * given context with chat option layouts as defined in res/layout/user_chat.xml
 */
public class ChatListAdaptor extends RecyclerView.Adapter<ChatListAdaptor.ViewHolder> {
    private Context context;
    private List<Chat> chatList;
    private RecyclerView chatOptions;
    private final View.OnClickListener onClickListener = new OpenChat();

    // Class that presents the layout of a chat
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView rowName;
        private TextView rowLastMessage;
        private TextView rowTimestamp;
        private ImageView rowColour;

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
        this.chatList = chatList;
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
        Chat chat = chatList.get(position);
        MessageTime actualTime = new MessageTime(chat.getTimestamp());
        holder.rowName.setText(chat.getName());
        holder.rowLastMessage.setText(chat.getLastMessage());
        holder.rowTimestamp.setText(actualTime.getTimeDate());
        GradientDrawable background = (GradientDrawable) holder.rowColour.getBackground().mutate();
        background.setColor(chat.getColour());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    /*
     * This class makes each chat option clickable and able to open a new activity for a chat.
     */
    private class OpenChat implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = chatOptions.getChildLayoutPosition(v);
            String id = chatList.get(position).getId();
            String name = chatList.get(position).getName();
            int colour = chatList.get(position).getColour();

            if (context instanceof MessageListActivity) {
                ((MessageListActivity)context).openNewChat(id, name, colour, position);
            }
        }
    }
}
