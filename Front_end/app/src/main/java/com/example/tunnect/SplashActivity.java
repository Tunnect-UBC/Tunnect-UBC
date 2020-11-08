package com.example.tunnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    // https://m.youtube.com/watch?v=dQw4w9WgXcQ , the secret function...
    //private static final String CLIENT_ID = "35i4h34h5j69jk";
    private static final String CLIENT_ID = "b30cb6a307474da78191b84e475f90a6";
    private static final String REDIRECT_URI = "com.example.tunnect://callback";
    private static final int REQUEST_CODE = 1337;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private";

    // This activity starts when the app opens and checks spotify permissions and if we don't have them it requests them
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        authenticateSpotify();

    }
    private void authenticateSpotify() {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{SCOPES});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    SharedPreferences.Editor editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    Log.d("spotAuth", "GOT AUTH TOKEN");
                    editor.apply();
                    startMainActivity();
                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.d("spotAuth", "Auth Error");
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
                    Log.d("spotAuth", "Auth flow cancelled");
                    break;
            }
        }
    }

    // Transfers to the main activity
    private void startMainActivity() {
        Intent newIntent = new Intent(SplashActivity.this, MainActivity.class);
        newIntent.putExtra("USER_ID", CLIENT_ID);
        startActivity(newIntent);
    }

    // Transfers to the profile activity
    private void startProfileActivity() {
        Intent newIntent = new Intent(SplashActivity.this, ProfileActivity.class);
        newIntent.putExtra("FROM_MENU", false);
        newIntent.putExtra("USER_ID", CLIENT_ID);
        startActivity(newIntent);
    }
}

interface VolleyCallBack {

    void onSuccess();
}