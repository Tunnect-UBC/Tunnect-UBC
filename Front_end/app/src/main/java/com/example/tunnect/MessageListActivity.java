package com.example.tunnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    DatabaseAdapter databaseAdapter;
    RecyclerView chatOptions;
    ChatListAdaptor chatListAdaptor;
    RecyclerView.LayoutManager layoutManager;
    List<Chat> chatsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
    }
}