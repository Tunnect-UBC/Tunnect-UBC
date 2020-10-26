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
 * This class takes information from a list of messages and populates a recycler view in the
 * given context with sent_message layouts and received_message layouts, based on the message ID
 */
public class MessageListAdaptor extends RecyclerView.Adapter {
    private static final int SENT_MESSAGE = 0;
    private static final int RECEIVED_MESSAGE = 1;

    private Context context;
    private List<Message> messageList;
    private String currentUserId;
    // an instance of the recycler view must be kept if clicking functionality is added

    public MessageListAdaptor(Context context, List<Message> messageList, String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);

        if (message.getId().equals(currentUserId)) {
            return SENT_MESSAGE;
        } else {
            return RECEIVED_MESSAGE;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == SENT_MESSAGE) {
            view = LayoutInflater.from(context).inflate(R.layout.sent_message, parent, false);
            return new SentMessageHolder(view);
        }

        view = LayoutInflater.from(context).inflate(R.layout.received_message, parent, false);
        return new ReceivedMessageHolder(view);
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        switch (holder.getItemViewType()) {
            case SENT_MESSAGE:
                ((SentMessageHolder) holder).bind(message);
                break;
            case RECEIVED_MESSAGE:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    // Class that presents the layout of a sent message
    private static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView message, timestamp;

        SentMessageHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.sent_message);
            timestamp = itemView.findViewById(R.id.sent_time);
        }

        // Assigns values to instance of sent message layout
        void bind(Message message) {
            this.message.setText(message.getMessage());
            this.timestamp.setText(message.getTimestamp());
        }
    }

    // Class that presents the layout of a received message
    private static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView message, timestamp, name;
        ImageView rowColour;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.received_message);
            timestamp = itemView.findViewById(R.id.received_time);
            name = itemView.findViewById(R.id.text_message_name);
            rowColour = itemView.findViewById(R.id.image_message_profile);
        }

        // Assigns values to instance of sent message layout
        void bind(Message message) {
            this.message.setText(message.getMessage());
            this.timestamp.setText(message.getTimestamp());
            this.name.setText(message.getName());
            GradientDrawable background = (GradientDrawable) this.rowColour.getBackground().mutate();
            background.setColor(message.getColour());
        }
    }
}