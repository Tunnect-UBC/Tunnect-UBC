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
    private static final int ADDITIONAL_RECEIVED_MESSAGE = 2;

    private Context context;
    private List<Message> messageList;
    private String currentUserId;
    //private static String lastId = "";
    private String otherUserId;
    // an instance of the recycler view must be kept if clicking functionality is added

    public MessageListAdaptor(Context context, List<Message> messageList, String currentUserId, String otherUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
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
            //lastId = "";
            return SENT_MESSAGE;
        } else if (message.getId().equals(otherUserId)){
            return RECEIVED_MESSAGE;

        } else {
            //lastId = otherUserId;
            return ADDITIONAL_RECEIVED_MESSAGE;
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
        } else if (viewType == RECEIVED_MESSAGE) {
            view = LayoutInflater.from(context).inflate(R.layout.received_message, parent, false);
            return new ReceivedMessageHolder(view);
        }

        view = LayoutInflater.from(context).inflate(R.layout.addtional_received_message, parent, false);
        return new ADReceivedMessageHolder(view);
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
                break;
            default:
                ((ADReceivedMessageHolder) holder).bind(message);
                break;
        }
    }

    // Class that presents the layout of a sent message
    private static class SentMessageHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView timestamp;

        SentMessageHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.sent_message);
            timestamp = itemView.findViewById(R.id.sent_time);
        }

        // Assigns values to instance of sent message layout
        private void bind(Message message) {
            MessageTime actualTime = new MessageTime(message.getTimestamp());
            this.message.setText(message.getMessage());
            this.timestamp.setText(actualTime.getTimeDate());
        }
    }

    // Class that presents the layout of a received message
    private static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView timestamp;
        private TextView name;
        private ImageView rowColour;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.received_message);
            timestamp = itemView.findViewById(R.id.received_time);
            name = itemView.findViewById(R.id.text_message_name);
            rowColour = itemView.findViewById(R.id.image_message_profile);
        }

        // Assigns values to instance of received message layout
        private void bind(Message message) {
            MessageTime actualTime = new MessageTime(message.getTimestamp());
            this.message.setText(message.getMessage());
            this.timestamp.setText(actualTime.getTimeDate());
            this.name.setText(message.getName());
            GradientDrawable background = (GradientDrawable) this.rowColour.getBackground().mutate();
            background.setColor(message.getColour());
        }
    }

    // Class that presents the layout of a subsequent received message
    private static class ADReceivedMessageHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView timestamp;

        ADReceivedMessageHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.additional_received_message);
            timestamp = itemView.findViewById(R.id.additional_received_time);
        }

        // Assigns values to instance of a subsequent received message layout
        private void bind(Message message) {
            MessageTime actualTime = new MessageTime(message.getTimestamp());
            this.message.setText(message.getMessage());
            this.timestamp.setText(actualTime.getTimeDate());
        }
    }
}