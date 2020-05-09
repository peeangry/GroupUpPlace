package com.example.groupupplace;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.Locale;
import java.util.StringTokenizer;

public class HomePlace extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //*******************************TextView with checkbox******************************************//
    public class Item {
        String ItemId;
        String ItemDrawable;
        String ItemName;
        String ItemDest;
        String ItemFaci;
        float Rating;
        String ItemUserId;
        String ItemPrice;
        String ItemPhone;
        String ItemSeat;
        String ItemDeposite;
        String ItemDay;
        String ItemStartTime;
        String ItemEndTime;
        String ItemFaciString;

        //        upid,pPrice,pPhone,pSeat,pDepo,pDate,pStartTime,pEndTime
        //        Item(ImageView drawable, String t, boolean b){
        Item(String id, String name, String desc, String faci, float rating, String drawable, String uid, String price, String phone, String seat, String deposit, String day, String sTime, String eTime) {
            ItemId = id;
            ItemDrawable = drawable;
            ItemName = name;
            ItemDest = desc;
            ItemFaci = faci;
            Rating = rating;
            ItemUserId = uid;
            ItemPrice = price;
            ItemPhone = phone;
            ItemSeat = seat;
            ItemDeposite = deposit;
            ItemDay = day;
            ItemStartTime = sTime;
            ItemEndTime = eTime;
            ItemFaciString = showFacility(ItemFaci);
        }

    }

    static class ViewHolder {
        ImageView icon;
        TextView text;
        TextView description;
        TextView facility;
        RatingBar rating;
        Button confirm;
        Button Review;
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
                viewHolder.confirm = rowView.findViewById(R.id.btn_managePlace);
                viewHolder.Review = rowView.findViewById(R.id.btn_showReview);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (HomePlace.ViewHolder) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list.get(position).ItemDrawable, viewHolder.icon, 250).execute();

            final String ItemName = list.get(position).ItemName;
            final String ItemDest = list.get(position).ItemDest;
            final String ItemFaci = list.get(position).ItemFaci;
            final float ItemRating = list.get(position).Rating;
            final String ItemId = list.get(position).ItemId;
            final String ItemUserId = list.get(position).ItemUserId;
            final String ItemPrice = list.get(position).ItemPrice;
            final String ItemPhone = list.get(position).ItemPhone;
            final String ItemSeat = list.get(position).ItemSeat;
            final String ItemDeposite = list.get(position).ItemDeposite;
            final String ItemDay = list.get(position).ItemDay;
            final String ItemStartTime = list.get(position).ItemStartTime;
            final String ItemEndTime = list.get(position).ItemEndTime;
            viewHolder.text.setText(ItemName);
            viewHolder.rating.setRating(ItemRating);
            viewHolder.description.setText(ItemDest);

            viewHolder.facility.setText(showFacility(ItemFaci));
            viewHolder.confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ItemId,ItemDrawable,ItemName,ItemDest,ItemFaci,Rating,ItemUserId,ItemPrice,ItemPhone,ItemSeat,ItemDeposite,ItemDay,ItemStartTime,ItemEndTime,
                    Intent in = new Intent(HomePlace.this, Edit_place.class);
                    in.putExtra("ItemId", ItemId + "");
                    in.putExtra("ItemName", ItemName + "");
                    in.putExtra("ItemDest", ItemDest + "");
                    in.putExtra("ItemFaci", ItemFaci + "");
                    in.putExtra("Rating", ItemRating + "");
                    in.putExtra("ItemUserId", ItemUserId + "");
                    in.putExtra("ItemPrice", ItemPrice + "");
                    in.putExtra("ItemPhone", ItemPhone + "");
                    in.putExtra("ItemSeat", ItemSeat + "");
                    in.putExtra("ItemDeposite", ItemDeposite + "");
                    in.putExtra("ItemDay", ItemDay + "");
                    in.putExtra("ItemStartTime", ItemStartTime + "");
                    in.putExtra("ItemEndTime", ItemEndTime + "");
                    in.putExtra("ItemUserEmail", email + "");
                    startActivity(in);

                    Log.d("listclick", position + "  " + ItemId);
                }
            });
            viewHolder.Review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog viewDetail = new AlertDialog.Builder(HomePlace.this).create();
                    View mView = getLayoutInflater().inflate(R.layout.layout_showreview_dialog, null);
                    ImageButton btn_close = mView.findViewById(R.id.showbutton_btnClose);
                    ListView list = mView.findViewById(R.id.list_ShowReview);
                    getReview(ItemId,list);
                    btn_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewDetail.dismiss();
                        }
                    });
                    viewDetail.setView(mView);
                    viewDetail.show();
                }
            });
            return rowView;
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            list.clear();
            if (charText.length() == 0) {
                list.addAll(arraylist);
            } else {
                for (Item wp : arraylist) {
                    if (wp.ItemName.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list.add(wp);
                    }
                    else if (wp.ItemFaciString.toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        list.add(wp);
                    }

                }
            }
            notifyDataSetChanged();
        }
    }
    public class Item2 {
        //        String ItemDrawable;
        String ItemName;
        String ItemReview;
        String ItemScore;

        //        Item(ImageView drawable, String t, boolean b){
        Item2(String name, String review, String score) {
//            ItemDrawable = drawable;
            ItemName = name;
            ItemReview = review;
            ItemScore = score;
        }

    }
    static class ViewHolder2 {
        //        ImageView icon;
        TextView tName;
        TextView tReview;
        RatingBar rtScore;
    }
    public class ItemsListAdapter2 extends BaseAdapter {
        private ArrayList<HomePlace.Item2> arraylist2;
        private Context context;
        private List<HomePlace.Item2> list2;

        ItemsListAdapter2(Context c, List<HomePlace.Item2> l) {
            context = c;
            list2 = l;
            arraylist2 = new ArrayList<HomePlace.Item2>();
            arraylist2.addAll(l);
        }

        @Override
        public int getCount() {
            return list2.size();
        }

        @Override
        public Object getItem(int position) {
            return list2.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views

            HomePlace.ViewHolder2 viewHolder2 = new HomePlace.ViewHolder2();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.layout_review, null);
                viewHolder2.tName = rowView.findViewById(R.id.rowNameReview);
                viewHolder2.tReview = rowView.findViewById(R.id.rowRatingDetail);
                viewHolder2.rtScore = rowView.findViewById(R.id.rowRatingReview);
                rowView.setTag(viewHolder2);
            } else {
                viewHolder2 = (HomePlace.ViewHolder2) rowView.getTag();
            }
//            new Extend_MyHelper.SendHttpRequestTask(list2.get(position).ItemDrawable, viewHolder2.icon, 250).execute();
            final String itemStr = list2.get(position).ItemName;
            final String itemRev = list2.get(position).ItemReview;
            float score = Float.parseFloat(list2.get(position).ItemScore);
            viewHolder2.tName.setText(itemStr);
            viewHolder2.tReview.setText(itemRev);
            viewHolder2.rtScore.setRating(score);
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
    ArrayList<HashMap<String, String>> placeArray, placeTheme, placeImage;
    List<HomePlace.Item2> items2 = new ArrayList<HomePlace.Item2>();
    ArrayList<HashMap<String, String>> placeReview;
    HomePlace.ItemsListAdapter2 myItemsListAdapter2;
    ProgressDialog progressDialog;
    ImageButton btnAlert, btnCreatePlace;
    ImageView imgAccount;
    String[] some_array;
    EditText edtSearch;
    TextView numNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_home);
        Extend_MyHelper.checkInternetLost(this);
        Extend_MyHelper.deleteCache(this);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.na_view);
        btnAlert = findViewById(R.id.btn_notification);
        btnCreatePlace = findViewById(R.id.btn_newGroup);
        placeList = findViewById(R.id.placeList);
        numNoti = findViewById(R.id.badge_notification_2);
        navigationView.setNavigationItemSelectedListener(HomePlace.this);
        navigationView.bringToFront();
        placeArray = new ArrayList<>();
        placeReview = new ArrayList<>();
        placeTheme = new ArrayList<>();
        placeImage = new ArrayList<>();
        some_array = getResources().getStringArray(R.array.facility);
        View v = navigationView.getHeaderView(0);
        hName = v.findViewById(R.id.menu_name);
        hEmail = v.findViewById(R.id.menu_email);
        imgAccount = v.findViewById(R.id.imageUser);
        edtSearch = findViewById(R.id.editTextSearch);
        email = getIntent().getStringExtra("email");
        photo = getIntent().getStringExtra("photo");
        Extend_MyHelper.checkInternetLost(this);
        new Extend_MyHelper.SendHttpRequestTask(photo, imgAccount, 250).execute();
        search();
        numNoti.setVisibility(View.INVISIBLE);
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
        deleteDateOldDay();
        getUser();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                startActivity(getIntent());
                getUser();
                getnumNotification();
//                refreshData(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

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
                            getplace();
                            getnumNotification();
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
//        new CountDownTimer(500, 500) {
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
//                    @Override
//                    protected void onPreExecute() {
//                        super.onPreExecute();
//                    }
//
//                    @Override
//                    protected void onPostExecute(String string1) {
//                        super.onPostExecute(string1);
//                        new CountDownTimer(300, 300) {
//                            @Override
//                            public void onTick(long millisUntilFinished) {
//
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                showAllCheckboxClick();
//                            }
//                        }.start();
//
//
//                        Toast.makeText(HomePlace.this, string1, Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    @Override
//                    protected String doInBackground(Void... params) {
//                        getplace();
////                        getPlacePhoto();
////                        getPlaceTheme();
//                        return "successful!!!";
//                    }
//                }
//                AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
//                AsyncTaskUploadClassOBJ.execute();
//            }
//        }.start();
    }

    public void getUserThread() {
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
//                            writeFile(id,name,email);
                            Log.d("footer", "thread email " + email + " name " + name + " id " + id);
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
            Log.d("pathimage", "item Home : " + pName + " / " + pDetail + " / " + pFacility + " / " + pScore + " / " + pImage);
            HomePlace.Item item = new HomePlace.Item(pid, pName, pDetail, pFacility, Float.parseFloat(pScore), pImage, upid, pPrice, pPhone, pSeat, pDepo, pDate, pStartTime, pEndTime);
            items.add(item);
            Log.d("pathimage", "item : " + items.toString());
        }

    }

    public void getplace() {
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
                            showAllCheckboxClick();
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
        progressDialog.dismiss();
    }

    public String showFacility(String d) {
        StringTokenizer st = new StringTokenizer(d, ":");
        String s = "";
        ArrayList<Integer> array = new ArrayList<>();
        while (st.hasMoreTokens()) {
            array.add(Integer.parseInt(st.nextToken()) - 1);
        }
        for (int i = 0; i < array.size(); i++) {
            if (i != array.size() - 1) {
                s += "- " + some_array[array.get(i)] + "\n";
            } else {
                s += "- " + some_array[array.get(i)];
            }
        }
        return s;
    }

    public void search() {
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = edtSearch.getText().toString().toLowerCase(Locale.getDefault());
                myItemsListAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });

    }
    public void getnumNotification() {
        responseStr = new ResponseStr();

        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        Log.d("footer", "id getnumNotification" + id);
        String url = "http://www.groupupdb.com/android/getnumnotificationplace.php";
        url += "?pId=" + id;
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
                                map.put("num", c.getString("num"));
                                MyArrList.add(map);
                            }
                            //set Header menu name email
                            Log.d("footer", MyArrList.get(0).get("num"));
                            String numSn =MyArrList.get(0).get("num");
                            int numIn = Integer.parseInt(numSn);
                            if (numIn>0){
                                numNoti.setVisibility(View.VISIBLE);
                                numNoti.setText(numSn);
                            }


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
    public void getReview(String pid, final ListView listView) {
        placeReview.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getreviewdetail.php";
        url += "?pId=" + pid;//รอเอาIdจากfirebase
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
                                map.put("user_id", c.getString("user_id"));
                                map.put("user_names", c.getString("user_names"));
                                map.put("review_detail", c.getString("review_detail"));
                                map.put("review_score", c.getString("review_score"));
                                MyArrList.add(map);
                                placeReview.add(map);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initItems2(listView);
                        Log.d("pathimage", "get alertArray " + placeReview.toString());
                        Log.d("pathimage", "get MyArrList " + MyArrList.toString());
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

    private void initItems2(ListView list) {
        items2 = new ArrayList<HomePlace.Item2>();
        Log.d("pathimage", "memberArray " + placeReview.toString());
        for (int i = 0; i < placeReview.size(); i++) {
            String uid = placeReview.get(i).get("user_id").toString();
            String name = placeReview.get(i).get("user_names").toString();
            String detail = placeReview.get(i).get("review_detail").toString();
            String score = placeReview.get(i).get("review_score").toString();
            String pId = placeReview.get(i).get("place_id").toString();
            HomePlace.Item2 item2 = new HomePlace.Item2(name,detail,score);
            items2.add(item2);
        }
        myItemsListAdapter2 = new HomePlace.ItemsListAdapter2(this, items2);
        list.setAdapter(myItemsListAdapter2);
        Log.d("pathimage", items2.toString());
    }
}
