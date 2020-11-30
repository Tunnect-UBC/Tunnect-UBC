package com.example.tunnect;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

// TODO: Delete this class (just kept here cause it has useful userstore functions to copy)
public class UserService {
    private User user;

    public void addUser(String userid, String username, String top_artist, Context context) {
        user.updateUserId(userid);
        user.updateUsername(username);
        RequestQueue queue = Volley.newRequestQueue(context);

        String add_url = "http://52.188.167.58:3000/userstore";
        JSONObject user = new JSONObject();
        try {
            user.put("_id", userid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            user.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            user.put("top_artist", top_artist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            user.put("icon_colour", 0xffffffff);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, add_url, user, response -> {
        }, error -> {
        });
        queue.add(jsonObjectRequest);
    }

    public void getUser(String userid, Context context) {
        String get_url = "http://52.188.167.58:3000/userstore/" + userid;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, get_url, null, response -> {
            JSONObject user_info = response;
            try {
                user.updateUserId((String) user_info.get("_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                user.updateUsername((String) user_info.get("username"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
        });
        queue.add(jsonObjectRequest);


    }

    public void deleteUser(String userid, Context context) {
        String delete_url = "http://52.188.167.58:3000/userstore/" + userid;
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, delete_url, null, response -> {
        }, error -> {
        });
        queue.add(jsonObjectRequest);
    }

    public void like(Context context) {
        Toast.makeText(context, "Likes not implemented yet", Toast.LENGTH_SHORT).show();
    }

    public void dislike(Context context) {
        Toast.makeText(context, "dislikes not implemented yet", Toast.LENGTH_SHORT).show();
    }
}
