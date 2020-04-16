package com.example.groupupplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaceHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = "place";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private GoogleSignInClient mGoogleSignInClient;
    ResponseStr responseStr = new ResponseStr();
    TextView hName;
    TextView hEmail;
    String name = "", id = "", email = "";
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_home);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.na_view);
        navigationView.setNavigationItemSelectedListener(PlaceHome.this);
        navigationView.bringToFront();
        View v = navigationView.getHeaderView(0);
        hName = v.findViewById(R.id.menu_name);
        hEmail = v.findViewById(R.id.menu_email);
        email = getIntent().getStringExtra("email");
        progressDialog = new ProgressDialog(PlaceHome.this);
        progressDialog.setMessage("กำลังโหลดข้อมูล....");
        progressDialog.setTitle("กรุณารอซักครู่");
        progressDialog.show();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(PlaceHome.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        new Thread(new Runnable() {
            @Override
            public void run() {
                getUser();
                deleteDateOldDay();
            }
        }).start();
        new CountDownTimer(800, 800) {
            public void onFinish() {
                getUser();
            }
            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

        //firebase signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        ImageButton btnNoti = findViewById(R.id.btn_notification);
        btnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(PlaceHome.this,HomeAlert.class);
                in.putExtra("id", id+"");
                in.putExtra("email", email+"");
                startActivity(in);
            }
        });
    }
    public void addplace(View v){
        Intent in = new Intent(PlaceHome.this, CreatePlace.class);
        in.putExtra("id", id+"");
        in.putExtra("name", name+"");
        in.putExtra("email", email+"");
        startActivity(in);
    }

    public void menuHambergerPlace(View v){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()){
            case R.id.menu_home:
                startActivity(getIntent());
                Log.d(TAG, "onNavigationItemSelected home: " + item.getTitle());
                break;
            case R.id.menu_account:
                goToManageAccount();
                Log.d(TAG, "onNavigationItemSelected account: " + item.getTitle());
                break;
            case R.id.menu_calendar:
                goToManageCalendar();
                Log.d(TAG,"onNavigationItemSelected calendar: " + item.getTitle());
                break;
            case R.id.menu_signout:
                signout();
                Log.d(TAG, "onNavigationItemSelected signout: " + item.getTitle());
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    public void goToManageAccount() {
        Intent intent = new Intent(PlaceHome.this, Register.class);
        intent.putExtra("email", email+"");
        intent.putExtra("name", name+"");
        startActivity(intent);
    }

    public void goToManageCalendar() {
        Intent intent = new Intent(PlaceHome.this, ManageCalendar.class);
        startActivity(intent);
    }
    public void signout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.revokeAccess();
        Intent intent = new Intent(PlaceHome.this, MainActivity.class);
        startActivity(intent);
    }
    public void getUser() {
        responseStr = new ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        Log.d("footer", "email " + email);
        String url = "http://www.groupupdb.com/android/getuser.php";
        url += "?sEmail=" + email;//รอเอาIdหรือ email จากfirebase
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map = null;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("user_id", c.getString("user_id"));
                                map.put("user_names", c.getString("user_names"));
                                map.put("user_email", c.getString("user_email"));
                                map.put("user_photo", c.getString("user_photo"));
                                MyArrList.add(map);
                            }
                            //set Header menu name email
//                            Log.d("footer", MyArrList.get(0).get("user_id"));
//                            Log.d("footer", MyArrList.get(0).get("user_names"));
//                            Log.d("footer", MyArrList.get(0).get("user_email"));
                            hName.setText(MyArrList.get(0).get("user_names"));
                            name = MyArrList.get(0).get("user_names");
                            id = MyArrList.get(0).get("user_id");
                            hEmail.setText(MyArrList.get(0).get("user_email"));
//                            writeFile(id,name,email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
        progressDialog.dismiss();
    }
    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }
    public void deleteDateOldDay() {
        responseStr = new ResponseStr();
        String url = "http://www.groupupdb.com/android/deleteDate.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("deleteDateOldDay", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Log", "Volley::onErrorResponse():" + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

}
