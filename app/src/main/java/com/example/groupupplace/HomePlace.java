package com.example.groupupplace;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import java.util.List;

public class HomePlace extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //*******************************TextView with checkbox******************************************//
    public class Item {
        String ItemDrawable;
        String ItemName;
        String ItemDest;
        String ItemFaci;
        float Rating;

        //        Item(ImageView drawable, String t, boolean b){
        Item(String name, String desc, String faci, float rating, String drawable) {
            ItemDrawable = drawable;
            ItemName = name;
            ItemDest = desc;
            ItemFaci = faci;
            Rating = rating;
        }

    }

    static class ViewHolder {
        ImageView icon;
        TextView text;
        TextView description;
        TextView facility;
        RatingBar rating;
    }

    public class ItemsListAdapter extends BaseAdapter {
        private ArrayList<HomePlace.Item> arraylist;
        private Context context;
        private List<HomePlace.Item> list;

        ItemsListAdapter(Context c, List<HomePlace.Item> l) {
            context = c;
            list = l;
            arraylist = new ArrayList<HomePlace.Item>();
            arraylist.addAll(l);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            HomePlace.ViewHolder viewHolder = new HomePlace.ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.activity_place, null);
                viewHolder.icon = rowView.findViewById(R.id.roeImageView);
                viewHolder.text = rowView.findViewById(R.id.rowPlaceName);
                viewHolder.description = rowView.findViewById(R.id.rowDescription);
                viewHolder.facility = rowView.findViewById(R.id.rowFacility);
                viewHolder.rating = rowView.findViewById(R.id.rowRatingBar2);

                rowView.setTag(viewHolder);
            } else {
                viewHolder = (HomePlace.ViewHolder) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list.get(position).ItemDrawable, viewHolder.icon, 250).execute();

            final String itemStr = list.get(position).ItemName;
            final String itemDes = list.get(position).ItemDest;
            final String itemFaci = list.get(position).ItemFaci;
            final float itemRating = list.get(position).Rating;
            viewHolder.text.setText(itemStr);
            viewHolder.rating.setRating(itemRating);
            viewHolder.description.setText(itemDes);
            viewHolder.facility.setText(itemFaci);

            return rowView;
        }
    }


    //***********************************************************************************************//
    private static final String TAG = "place";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private GoogleSignInClient mGoogleSignInClient;
    static HomePlace.ItemsListAdapter myItemsListAdapter;
    List<HomePlace.Item> items = new ArrayList<HomePlace.Item>();
    ResponseStr responseStr = new ResponseStr();
    TextView hName;
    TextView hEmail;
    String name = "", id = "", email = "", photo = "";
    ListView placeList;
    ArrayList<HashMap<String, String>> placeArray,placeTheme,placeImage;
    ProgressDialog progressDialog;
    ImageButton btnAlert, btnCreatePlace;
    ImageView imgAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_home);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.na_view);
        btnAlert = findViewById(R.id.btn_notification);
        btnCreatePlace = findViewById(R.id.btn_newGroup);
        placeList = findViewById(R.id.placeList);
        navigationView.setNavigationItemSelectedListener(HomePlace.this);
        navigationView.bringToFront();
        placeArray = new ArrayList<>();
        placeTheme = new ArrayList<>();
        placeImage = new ArrayList<>();
        View v = navigationView.getHeaderView(0);
        hName = v.findViewById(R.id.menu_name);
        hEmail = v.findViewById(R.id.menu_email);
        imgAccount = v.findViewById(R.id.imageUser);
        email = getIntent().getStringExtra("email");
        photo = getIntent().getStringExtra("photo");
        Extend_MyHelper.checkInternetLost(this);
        new Extend_MyHelper.SendHttpRequestTask(photo, imgAccount, 250).execute();
        progressDialog = new ProgressDialog(HomePlace.this);
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
//                        Toast.makeText(HomePlace.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        getUser();
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteDateOldDay();
            }
        }).start();

//        new CountDownTimer(2000, 1000) {
//            public void onFinish() {
//                class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
//                    @Override
//                    protected void onPreExecute() {
//                        super.onPreExecute();
//                        progressDialog = ProgressDialog.show(HomePlace.this, "Dowloading Place", "Please Wait", false, false);
//                    }
//
//                    @Override
//                    protected void onPostExecute(String string1) {
//                        super.onPostExecute(string1);
//                        showAllCheckboxClick();
//                        progressDialog.dismiss();
//                        Toast.makeText(HomePlace.this, string1, Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    protected String doInBackground(Void... params) {
//                        getEventInvitation();
//                        getPlacePhoto();
//                        getPlaceTheme();
//                        return "successful!!!";
//                    }
//                }
//                AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
//                AsyncTaskUploadClassOBJ.execute();
//            }
//
//            public void onTick(long millisUntilFinished) {
//                // millisUntilFinished    The amount of time until finished.
//            }
//        }.start();
        Log.d("footer", "email " + email + " name " + name + " id " + id);

        //firebase signin
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(HomePlace.this, HomeAlert.class);
                in.putExtra("id", id + "");
                in.putExtra("email", email + "");
                startActivity(in);
            }
        });
        btnCreatePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomePlace.this, CreatePlace.class);
                in.putExtra("id", id + "");
                in.putExtra("email", email + "");
                in.putExtra("create", "create");
                startActivity(in);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void menuHambergerPlace(View v) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
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
                Log.d(TAG, "onNavigationItemSelected calendar: " + item.getTitle());
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
        Intent in = new Intent(HomePlace.this, Manage_Account.class);
        in.putExtra("id", id + "");
        in.putExtra("name", name + "");
        in.putExtra("email", email + "");
        in.putExtra("photo", photo + "");
        startActivity(in);
    }

    public void goToManageCalendar() {
        Intent in = new Intent(HomePlace.this, ManageCalendar.class);
        in.putExtra("id", id + "");
        in.putExtra("email", email + "");
        startActivity(in);
    }

    public void signout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.revokeAccess();
        Intent intent = new Intent(HomePlace.this, Login.class);
        startActivity(intent);
    }

    public void getUser() {
        responseStr = new ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        Log.d("footer", "email " + email);
        String url = "http://www.groupupdb.com/android/getuserplace.php";
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
                                map.put("user_name", c.getString("user_name"));
                                map.put("user_email", c.getString("user_email"));
                                map.put("user_photo", c.getString("user_photo"));
                                MyArrList.add(map);
                            }
                            //set Header menu name email
//                            Log.d("footer", MyArrList.get(0).get("user_id"));
//                            Log.d("footer", MyArrList.get(0).get("user_names"));
//                            Log.d("footer", MyArrList.get(0).get("user_email"));
                            id = MyArrList.get(0).get("user_id");
                            email = MyArrList.get(0).get("user_email");
                            name = MyArrList.get(0).get("user_name");
                            photo = MyArrList.get(0).get("user_photo");
                            hName.setText(name);
                            hEmail.setText(email);
                            new Extend_MyHelper.SendHttpRequestTask(photo, imgAccount, 250).execute();
//                            writeFile(id,name,email);
                            Log.d("footer", "email " + email + " name " + name + " id " + id);
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
        new CountDownTimer(500,500){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = ProgressDialog.show(HomePlace.this, "Dowloading Place", "Please Wait", false, false);
                    }

                    @Override
                    protected void onPostExecute(String string1) {
                        super.onPostExecute(string1);
                        new CountDownTimer(300, 300) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                showAllCheckboxClick();
                            }
                        }.start();

                        progressDialog.dismiss();
                        Toast.makeText(HomePlace.this, string1, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    protected String doInBackground(Void... params) {
                        getEventInvitation();
                        getPlacePhoto();
                        getPlaceTheme();
                        return "successful!!!";
                    }
                }
                AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
                AsyncTaskUploadClassOBJ.execute();
            }
        }.start();
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

    public void setItemsListView() {
        myItemsListAdapter = new HomePlace.ItemsListAdapter(this, items);
        placeList.setAdapter(myItemsListAdapter);
//        placeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
//                final AlertDialog viewDetail = new AlertDialog.Builder(Home_Alert.this).create();
//                View mView = getLayoutInflater().inflate(R.layout.layout_showalert_dialog, null);
//                final TextView title = mView.findViewById(R.id.alt_title_Invite);
//                final TextView detail = mView.findViewById(R.id.alt_detail_Invite);
//                final ImageView imgF = mView.findViewById(R.id.alt_image_invite);
//                final TextView nameE = mView.findViewById(R.id.alt_name_invite);
//                final TextView startEnd = mView.findViewById(R.id.alt_start_end);
//                final TextView state = mView.findViewById(R.id.alt_state_invite);
//                final String tid = placeArray.get(position).get("trans_id").toString();
//                final String eid = placeArray.get(position).get("events_id").toString();
//                final String ename = placeArray.get(position).get("events_name").toString();
//                String sSta = placeArray.get(position).get("events_month_start").toString();
//                String sEnd = placeArray.get(position).get("events_month_end").toString();
//                String sPrId = placeArray.get(position).get("pri_id").toString();
//                String priName = placeArray.get(position).get("pri_name").toString();
//                String event_creater = placeArray.get(position).get("event_creater").toString();
//                String user_photo = placeArray.get(position).get("user_photo").toString();
//                String events_detail = placeArray.get(position).get("events_detail").toString();
//                title.setText(ename);
//                detail.setText(events_detail);
//                nameE.setText(event_creater);
//                startEnd.setText(some_array[Integer.parseInt(sSta)] + " ถึง " + some_array[Integer.parseInt(sEnd)]);
//                state.setText(priName);
//                Log.d("pathimage", placeArray.toString());
//                new Extend_MyHelper.SendHttpRequestTask(user_photo, imgF, 250).execute();
//                Button btn_join = mView.findViewById(R.id.btn_invite_join);
//                Button btn_notJoin = mView.findViewById(R.id.btn_invite_notjoin);
//                Button btn_later = mView.findViewById(R.id.btn_invite_later);
//
//                btn_join.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
//                            @Override
//                            protected void onPreExecute() {
//                                super.onPreExecute();
//                            }
//
//                            @Override
//                            protected void onPostExecute(String string1) {
//                                super.onPostExecute(string1);
//                                Intent intent = new Intent(Home_Alert.this, MainAttendent.class);
//                                intent.putExtra("id", id + "");
//                                intent.putExtra("eid", eid + "");
//                                intent.putExtra("nameEvent", ename + "");
//                                intent.putExtra("email", email);
//                                intent.putExtra("tab", 0 + "");
//                                startActivity(intent);
//                                viewDetail.dismiss();
//                                Toast.makeText(Home_Alert.this, string1, Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            @Override
//                            protected String doInBackground(Void... params) {
//                                addEventFriend(uid, eid, ename);
//                                Extend_MyHelper.UpdateStateToDb(tid, 3 + "", Home_Alert.this);
//                                return "join successful!!!";
//                            }
//                        }
//                        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
//                        AsyncTaskUploadClassOBJ.execute();
//                    }
//                });
//                btn_notJoin.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
//                            @Override
//                            protected void onPreExecute() {
//                                super.onPreExecute();
//                            }
//
//                            @Override
//                            protected void onPostExecute(String string1) {
//                                super.onPostExecute(string1);
//                                viewDetail.dismiss();
//                                startActivity(getIntent());
//                                Toast.makeText(Home_Alert.this, string1, Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            @Override
//                            protected String doInBackground(Void... params) {
//                                deletetransID(tid);
//
//                                return "deete successful!!!";
//                            }
//                        }
//                        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
//                        AsyncTaskUploadClassOBJ.execute();
//                    }
//                });
//                btn_later.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        viewDetail.dismiss();
//                    }
//                });
//
//                viewDetail.setView(mView);
//                viewDetail.show();
//            }
//        });

        Log.d("friend", "size" + myItemsListAdapter.getCount() + "");
    }

    private void initItems() {
        items = new ArrayList<HomePlace.Item>();
        Log.d("pathimage", "placeArray " + placeArray.toString());
        Log.d("pathimage", "placeImage " + placeImage.toString());
        for (int i = 0; i < placeArray.size(); i++) {
            String pid = placeArray.get(i).get("place_id").toString();
            String upid = placeArray.get(i).get("place_upid").toString();
            String pName = placeArray.get(i).get("place_name").toString();
            String pDetail = placeArray.get(i).get("place_detail").toString();
            String pPrice = placeArray.get(i).get("place_price").toString();
            String pPhone = placeArray.get(i).get("place_phone").toString();
            String pSeat = placeArray.get(i).get("place_numofseat").toString();
            String pDepo = placeArray.get(i).get("place_deposit").toString();
            String pDate = placeArray.get(i).get("place_date").toString();
            String pStartTime = placeArray.get(i).get("place_stime").toString();
            String pEndTime = placeArray.get(i).get("place_etime").toString();
            String pFacility = placeArray.get(i).get("place_facility").toString();
            String pScore = placeArray.get(i).get("place_score").toString();
            String pVisit = placeArray.get(i).get("place_visit").toString();
            String pImage = placeArray.get(i).get("place_photoShow").toString();

//            String mystring = getResources().getString(R.string.mystring);
//            String s = event_creater + "ได้เชิญคุณเข้าร่วม " + ename + " เป็น " + priName + " โดยมีช่วงเวลาระหว่างเดือน " + some_array[Integer.parseInt(sSta)] + " ถึง " + some_array[Integer.parseInt(sEnd)];
            Log.d("pathimage","item Home : "+ pName+" / "+ pDetail+" / "+ pFacility+" / "+ pScore+" / "+ pImage);
            HomePlace.Item item = new HomePlace.Item(pName,pDetail,pFacility,Float.parseFloat(pScore),pImage);
            items.add(item);
            Log.d("pathimage","item : "+ items.toString());
        }

    }

    public void getEventInvitation() {
        responseStr = new HomePlace.ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getplace.php";
        url += "?sId=" + id;//รอเอาIdจากfirebase
        Log.d("position", "stringRequest  " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("place_id", c.getString("place_id"));
                                map.put("place_upid", c.getString("place_upid"));
                                map.put("place_name", c.getString("place_name"));
                                map.put("place_detail", c.getString("place_detail"));
                                map.put("place_price", c.getString("place_price"));
                                map.put("place_phone", c.getString("place_phone"));
                                map.put("place_numofseat", c.getString("place_numofseat"));
                                map.put("place_deposit", c.getString("place_deposit"));
                                map.put("place_date", c.getString("place_date"));
                                map.put("place_stime", c.getString("place_stime"));
                                map.put("place_etime", c.getString("place_etime"));
                                map.put("place_facility", c.getString("place_facility"));
                                map.put("place_score", c.getString("place_score"));
                                map.put("place_visit", c.getString("place_visit"));
                                map.put("place_photoShow", c.getString("place_photoShow"));
                                MyArrList.add(map);
                                placeArray.add(map);

                            }
                            Log.d("placeHome", "get placeArray " + placeArray.toString());
//                            Log.d("place", "get MyArrList " + MyArrList.toString());
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
    }

    public void showAllCheckboxClick() {
        initItems();
        setItemsListView();
    }
    public void getPlaceTheme() {
        responseStr = new HomePlace.ResponseStr();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getplacetheme.php";
        url += "?sId=" + id;
        Log.d("position", "stringRequest  " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("placetheme_id", c.getString("placetheme_id"));
                                map.put("theme_id", c.getString("theme_id"));
                                map.put("place_id", c.getString("place_id"));
                                map.put("place_upid", c.getString("place_upid"));
                                MyArrList.add(map);
                                placeTheme.add(map);
                            }
//                            Log.d("position", MyArrList.size() + "");
                            Log.d("placeHome", "get placeTheme " + placeTheme.toString());
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
    }
    public void getPlacePhoto() {
        responseStr = new HomePlace.ResponseStr();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getplacephoto.php";
        url += "?sId=" + id;
        Log.d("position", "stringRequest  " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            HashMap<String, String> map;
                            JSONArray data = new JSONArray(response.toString());
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject c = data.getJSONObject(i);
                                map = new HashMap<String, String>();
                                map.put("photoplace_id", c.getString("photoplace_id"));
                                map.put("place_id", c.getString("place_id"));
                                map.put("photoplace_path", c.getString("photoplace_path"));
                                map.put("place_upid", c.getString("place_upid"));
                                MyArrList.add(map);
                                placeImage.add(map);
                            }
//                            Log.d("position", MyArrList.size() + "");
                            Log.d("placeHome", "get placeImage " + placeImage.toString());
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
    }

}
