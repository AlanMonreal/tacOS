package com.disp_mov.tacos.data;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.disp_mov.tacos.R;
import com.disp_mov.tacos.data.model.LoggedInUser;
import com.disp_mov.tacos.util.MySingleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private static String token;
    private static String email;
    private LoggedInUser user;
    private static boolean isLogged;
    private JSONObject responseJson;

    public Result<LoggedInUser> login(String username, String password, final Context cx) {
        responseJson = loginRequest(username, password, cx);
        try {
            Log.d("dummy", "le valio verga");
            token = responseJson.getString("token");
            email = responseJson.getString("email");
            Log.d("dummy", "token " + token);
            Log.d("dummy", "email" + email);
            if(!token.equals("null")){
                user = new LoggedInUser(token, email);
                isLogged = true;
            }

        }catch(Exception je){
            Log.d("dummy", "JSONEXC " + je.toString());
        }
        if(isLogged){
            return new Result.Success<>(user);
        }
        return new Result.Error(new IOException("Error logging in"));
    }

    private JSONObject loginRequest(String username, String password, final Context cx) {
        Log.d("dummy", "entering loginRequest");
        final View rootView = ((Activity)cx).getWindow().getDecorView().findViewById(android.R.id.content);
        final String url = cx.getString(R.string.url_request);
        try {
            //responseJson = new JSONObject();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", username);
            jsonObject.put("password", password);
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response){
                    try{
                        responseJson = response;
                        Log.d("dummy", response.getString("token"));
                    } catch (JSONException je) {
                        Log.d("dummy", "ahhhh " + je.toString());
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar.make(rootView, "Intentalo de nuevo.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Log.d("dummy", "error try/catch " + error.toString());
                }
            });

            Log.d("dummy","sending request");
            MySingleton.getInstance(cx).addToRequestQueue(objectRequest);
            Thread.sleep(2500);

        } catch (Exception e) {
            Log.d("dummy", "exception " + e.toString());
            //return new Result.Error(new IOException("Error logging in", e));
        }
        Log.d("dummy", "saliendo");
        return responseJson;
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
