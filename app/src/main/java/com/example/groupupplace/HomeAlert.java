package com.example.groupupplace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeAlert extends AppCompatActivity {
    //*******************************TextView with checkbox******************************************//
    class Item {
        String ItemTranPlaceId;
        String ItemPId;
        String ItemPName;
        String ItemDate;
        String ItemTime;
        String ItemEid;
        String ItemEName;
        String ItemuEmail;
        String ItemuName;
        String ItempPhoto;


        //        upid,pPrice,pPhone,pSeat,pDepo,pDate,pStartTime,pEndTime
        //        Item(ImageView drawable, String t, boolean b){
        Item(String tpid,String pid,String pName,String date,String time,String eid,String eName,String uName,String uEmail,String pPhoto) {
            ItemTranPlaceId =tpid;
            ItemPId = pid;
            ItemPName = pName;
            ItemDate = date;
            ItemTime = time;
            ItemEid = eid;
            ItemEName = eName;
            ItemuEmail = uEmail;
            ItemuName = uName;
            ItempPhoto = pPhoto;
        }

    }
    class ViewHolder {
        ImageView icon;
        TextView place;
        TextView time;
        TextView day;
        TextView nevent;
        TextView creater;
        TextView email;
    }
    public class ItemsListAdapter extends BaseAdapter {
        private ArrayList<HomeAlert.Item> arraylist;
        private Context context;
        private List<HomeAlert.Item> list;

        ItemsListAdapter(Context c, List<HomeAlert.Item> l) {
            context = c;
            list = l;
            arraylist = new ArrayList<HomeAlert.Item>();
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
            HomeAlert.ViewHolder viewHolder = new HomeAlert.ViewHolder();
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.layout_showalert_dialog, null);
                viewHolder.icon = rowView.findViewById(R.id.rowImageUser);
                viewHolder.place = rowView.findViewById(R.id.rowPlace);
                viewHolder.day = rowView.findViewById(R.id.rowDay);
                viewHolder.time = rowView.findViewById(R.id.rowTime);
                viewHolder.nevent = rowView.findViewById(R.id.rowNameEvent);
                viewHolder.creater = rowView.findViewById(R.id.rowCreater);
                viewHolder.email = rowView.findViewById(R.id.rowEmail);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (HomeAlert.ViewHolder) rowView.getTag();
            }
            new Extend_MyHelper.SendHttpRequestTask(list.get(position).ItempPhoto, viewHolder.icon, 250).execute();

            final String ItemPlace = list.get(position).ItemPName;
            final String ItemTime= list.get(position).ItemTime;
            final String Itemday= list.get(position).ItemDate;
            final String ItemNEvent = list.get(position).ItemEName;
            final String ItemCreater = list.get(position).ItemuName;
            final String ItemEmail = list.get(position).ItemuEmail;
            viewHolder.nevent.setText(ItemNEvent);
            viewHolder.email.setText(ItemEmail);
            viewHolder.creater.setText(ItemCreater);
            viewHolder.place.setText(ItemPlace);
            viewHolder.time.setText(ItemTime);
            viewHolder.day.setText(Itemday);
            return rowView;
        }

    }
    //*******************************TextView with checkbox******************************************//
    String id="",email="";
    ListView listViewInvite;
    List<HomeAlert.Item> items2 = new ArrayList<HomeAlert.Item>();
    ArrayList<HashMap<String, String>> placeArray;
    HomeAlert.ItemsListAdapter myItemsListAdapter;
    HomeAlert.ResponseStr responseStr = new HomeAlert.ResponseStr();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_alert);
        email = getIntent().getStringExtra("email");
        id = getIntent().getStringExtra("id");
        placeArray = new ArrayList<>();
        Extend_MyHelper.checkInternetLost(this);
        listViewInvite = findViewById(R.id.listView_placeAlert);
        getNoti();
    }
    public void backHome(View v) {
        Intent in = new Intent(HomeAlert.this, HomePlace .class);
        in.putExtra("id", id + "");
        in.putExtra("email", email + "");
        startActivity(in);
    }

    public class ResponseStr {
        private String str;
        JSONArray jsonArray;

        public void setValue(JSONArray jsonArr) {
            this.jsonArray = jsonArr;
        }

    }
    private void addNotification() {
        // Builds your notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_account_box_black_24dp)
                .setContentTitle("การเชิญเพื่อน")
                .setContentText("คุณนิวได้เชิญคุณเข้าร่วมนัดหมาย");

        // Creates the intent needed to show the notification
        Intent notificationIntent = new Intent(this, HomeAlert.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
    public void UpdateStateToDb(String transId,String statusId){
        String url = "http://www.groupupdb.com/android/acceptEvent.php";
        url += "?tId=" + transId;
        url += "&stId=" +statusId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("updatedb", response);
//                        Toast.makeText(Home_Alert.this, response, Toast.LENGTH_LONG).show();
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
        startActivity(getIntent());
    }
    public void getNoti() {
        placeArray.clear();
        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
        String url = "http://www.groupupdb.com/android/getalertplace.php";
        url += "?uId=" + id;
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
                                map.put("tpid", c.getString("tpid"));
                                map.put("place_id", c.getString("place_id"));
                                map.put("place_name", c.getString("place_name"));
                                map.put("time", c.getString("time"));
                                map.put("timerange", c.getString("timerange"));
                                map.put("events_id", c.getString("events_id"));
                                map.put("events_name", c.getString("events_name"));
                                map.put("user_names", c.getString("user_names"));
                                map.put("user_email", c.getString("user_email"));
                                map.put("place_photoShow", c.getString("place_photoShow"));
                                MyArrList.add(map);
                                placeArray.add(map);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initItems2();
                        Log.d("pathimage", "get alertArray " + placeArray.toString());
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

    private void initItems2() {
        items2 = new ArrayList<HomeAlert.Item>();
        Log.d("pathimage", "memberArray " + placeArray.toString());
        for (int i = 0; i < placeArray.size(); i++) {

            String tpid = placeArray.get(i).get("tpid").toString();
            String pid = placeArray.get(i).get("place_id").toString();
            String pname = placeArray.get(i).get("place_name").toString();
            String day = placeArray.get(i).get("time").toString();
            String time = placeArray.get(i).get("timerange").toString();
            String eid = placeArray.get(i).get("events_id").toString();
            String ename = placeArray.get(i).get("events_name").toString();
            String uname = placeArray.get(i).get("user_names").toString();
            String uemail = placeArray.get(i).get("user_email").toString();
            String pphoto = placeArray.get(i).get("place_photoShow").toString();
            HomeAlert.Item item = new HomeAlert.Item(tpid,pid,pname,day,time,eid,ename,uname,uemail,pphoto);
            items2.add(item);
        }
        myItemsListAdapter = new HomeAlert.ItemsListAdapter(this, items2);
        listViewInvite.setAdapter(myItemsListAdapter);
        Log.d("pathimage", items2.toString());
        listViewInvite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String tpid = placeArray.get(position).get("tpid").toString();
                final android.app.AlertDialog viewDetail = new android.app.AlertDialog.Builder(HomeAlert.this).create();
                viewDetail.setTitle("ไม่แสดงอีก");
                viewDetail.setButton(viewDetail.BUTTON_NEGATIVE, "ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                viewDetail.setButton(viewDetail.BUTTON_POSITIVE, "ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("closeNoti",tpid);
                        deleteShowNoti(tpid);
                    }
                });
                viewDetail.show();
                Button btnPositive = viewDetail.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = viewDetail.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);

            }
        });
    }
    public void deleteShowNoti(String tpid) {
        String url = "http://www.groupupdb.com/android/unshowAlert.php";
        url += "?tpId=" + tpid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("deleteDateOldDay", response);
                        startActivity(getIntent());
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
