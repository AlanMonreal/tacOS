package com.disp_mov.tacos.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import com.google.android.material.snackbar.Snackbar;
//import android.support.v4.app.ActivityCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.disp_mov.tacos.R;
import com.disp_mov.tacos.data.model.OrderInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Requests {

    /*public static List<OrderInfo> ITEMS;
    public static Map<Integer, OrderInfo> ITEM_MAP;
    public static List<NextMovementsInfo> MOVEMENTS;
    public static List<Integer> ASSIGNED;
    private static SharedPreferences preferences;
    public static int totalGuides;
    public static String responseMsg;
    private static String token;


    public static void dataInit(Activity activity,Context cx, View view, boolean hasRoute){
        ITEMS.clear();
        totalGuides = 0;
        ITEM_MAP.clear();
        getRequest(activity, cx, view, hasRoute);
    }


    private static void addItem(OrderInfo item) {
        if(item == null)
            return;
        totalGuides = totalGuides + 1;
        if(containsName(ITEMS, item.name, item.kind)){
            ITEMS.get(ITEMS.size() - 1).tracking.add(item.tracking.get(0));
            ITEMS.get(ITEMS.size() - 1).height.add(item.height.get(0));
            ITEMS.get(ITEMS.size() - 1).weight.add(item.weight.get(0));
            ITEMS.get(ITEMS.size() - 1).length.add(item.length.get(0));
            ITEMS.get(ITEMS.size() - 1).width.add(item.width.get(0));
        }else{
            ITEMS.add(item);
            ITEM_MAP.put(item.index, item);
        }
    }


    private static void populateAssigned(int item){
        ASSIGNED.add(item);
    }


    private static void populateMovements(NextMovementsInfo item){
        MOVEMENTS.add(item);
    }


    private static void getRequest (final Activity activity, final Context cx, final View view, final boolean hasRoute){
        preferences = cx.getSharedPreferences("com.envia.mapsdirection", Context.MODE_PRIVATE);
        String url = cx.getString(R.string.url_request) + "?token=" + preferences.getString("session", "");

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for(int i=0;i<response.length();i++){
                        JSONObject orders = response.getJSONObject(i);
                        if(orders == null)
                            continue;

                        int currentIndex = i;
                        int tracking = orders.getInt("tracking");
                        int id = orders.getInt("iden");
                        double x = orders.getDouble("x");
                        double y = orders.getDouble("y");
                        String kind = orders.getString("kind");
                        String name = orders.getString("name");
                        String phone = stringValidator(orders.getString("phone"));
                        String company = stringValidator(orders.getString("company"));

                        JSONObject address = orders.getJSONObject("address");
                        String postal = stringValidator(address.getString("postal"));
                        String line = stringValidator(address.getString("line"));
                        String neighborhood = stringValidator(address.getString("neighborhood"));
                        String city = stringValidator(address.getString("city"));
                        String reference = stringValidator(address.getString("reference"));

                        JSONObject package1 = orders.getJSONObject("package");
                        String content = package1.getString("content");
                        String length = package1.getString("length");
                        String width = package1.getString("width");
                        String height = package1.getString("height");
                        String weight = package1.getString("weight");
                        LatLng location = new LatLng(y,x);

                        addItem(new OrderInfo(currentIndex, id, tracking, location, kind, name,
                                phone, line, neighborhood, city, postal, reference, content, weight,
                                length, width, height, company));
                    }

                    String act = ""+ activity;
                    String[] sAct = act.split("@");
                    if(!sAct[0].equals("com.envia.mapsdirection.OrderListActivity") && !hasRoute){
                        Intent intent = new Intent(cx, AllFinished.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        cx.startActivity(intent);
                        activity.finish();
                    }else{
                        Intent intent = new Intent(cx, OrderListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        cx.startActivity(intent);
                        activity.finish();
                    }
                }catch (JSONException e){
                    Snackbar.make(view, "Intentalo de nuevo.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(view, "Error: " + error.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        MySingleton.getInstance(cx).addToRequestQueue(jsonArrayRequest);
    }


    public static void updateRequest(final Activity activity, final String status, String name,
                                     String comment, String fileName, final Context context ,
                                     ArrayList<String> arrayList, final View view){
        try{
            preferences = context.getSharedPreferences("com.envia.mapsdirection", Context.MODE_PRIVATE);
            String url = context.getString(R.string.url_request) + "?token=" + preferences.getString("session", "");
            String lkl = preferences.getString("LastKnownLocation", "");
            String[] last = lkl.split(",");
            String lat, lng;

            if(last.length >1){
                lat = last[0];
                lng = last[1];
            }else{
                lat = null;
                lng = null;
            }

            JSONObject location = new JSONObject();
            location.put("lat", lat);
            location.put("lng", lng);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("track", new JSONArray(arrayList));
            jsonObject.put("status", status);
            jsonObject.put("fileName", fileName);
            jsonObject.put("receiver", name);
            jsonObject.put("info", comment);
            jsonObject.put("lastLocation", location);

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        responseMsg = response.getString("response");
                        if (status.equals("consulta"))
                            inquiryPostProc(response);
                        else if (status.equals("assign") || status.equals("inquiry"))
                            assignPostProc(response);
                        else {
                            Requests.ITEMS = new ArrayList<>();
                            Requests.ITEM_MAP = new HashMap<>();
                            Requests.dataInit(activity, context, view, true);
                        }
                    }catch(JSONException e){
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar.make(view, "Intentalo de nuevo.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            MySingleton.getInstance(context).addToRequestQueue(objectRequest);
        } catch(JSONException e){
            Snackbar.make(view, "El servicio no esta disponible en este momento", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }


    public static void postRequest(String user, String pass, final Context context, final View view){
        try{
            String url = context.getString(R.string.url_request);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", user);
            jsonObject.put("password", pass);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response){
                    try {
                        token = response.getString("token");
                        if(!token.equals("null")){
                            preferences = context.getSharedPreferences("com.envia.mapsdirection", Context.MODE_PRIVATE);
                            preferences.edit().putString("session", token).apply();
                        }
                    }catch(JSONException je){
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar.make(view, "Intentalo de nuevo.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            MySingleton.getInstance(context).addToRequestQueue(objectRequest);
        } catch(JSONException e){
            Snackbar.make(view, "El servicio no esta disponible en este momento post", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }




    private static boolean containsName(List<OrderInfo> list, String name, String kind){
        if(list.size() == 0)
            return false;
        OrderInfo oi = list.get(list.size() - 1);
        return(oi.name.equals(name) && oi.kind.equals(kind));
    }


    private static String stringValidator(String data){
        if(data.equals("null"))
            return "";
        return data;
    }


    private static void assignPostProc(JSONObject response){
        try{
            Requests.ASSIGNED = new ArrayList<>();
            JSONArray resp = response.getJSONArray("nextMoves");
            for(int i = 0; i< resp.length(); i++){
                JSONObject object = resp.getJSONObject(i);
                int tracking = object.getInt("track");
                populateAssigned(tracking);
            }
        }catch(JSONException e){
        }
    }


    private static void inquiryPostProc(JSONObject response){
        try{
            Requests.MOVEMENTS = new ArrayList<>();
            JSONArray resp = response.getJSONArray("nextMoves");
            for (int i = 0; i< resp.length(); i++) {
                JSONObject object = resp.getJSONObject(i);
                int tracking = object.getInt("track");
                int unit = object.getInt("unit");
                populateMovements(new NextMovementsInfo(tracking, unit));
            }
        }catch(JSONException e){
        }
    }*/


    /*public static boolean hasPermissions(Context context, String... permissions) {
        if(context != null && permissions != null) {
            for(String permission : permissions) {
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }*/
}