package tut.lbs.locationbasedsystem.requests;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tut.lbs.locationbasedsystem.managers.Route;
import tut.lbs.locationbasedsystem.managers.Session;
import tut.lbs.locationbasedsystem.network.VolleySingleton;
import tut.lbs.locationbasedsystem.utils.Constant;
import tut.lbs.locationbasedsystem.utils.Function;
import tut.lbs.locationbasedsystem.utils.Util;

public class AuthRequest {
    private static final String TAG = AuthRequest.class.getSimpleName();

    public static void login(final Session session,
                             final Context context,
                             final String email,
                             final String password,
                             final Handler requestHandler) {
        final Message msg = requestHandler.obtainMessage();
        final Bundle bundle = new Bundle();
        final String tag_string_req = "req_login";
        Function.setLoading(context, "Logging  in...", true);
        final String url = session.getServerUrl() + Route.LOGIN;
        final StringRequest strReq = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.d(TAG, "Login Response: " + response);
                        // Function.setLoading(false);
                        try {
                            final JSONObject jObj = new JSONObject(response);
                            final boolean success = jObj.getBoolean(Constant.KEY_SUCCESS);
                            // Check for error node in json
                            if (success) {
                                session.setLogin(true);
                                session.setLoggedinUser(
                                        jObj.getJSONObject(Constant.KEY_USER).toString());
                                bundle.putBoolean(Constant.KEY_IS_LOGGED_IN, true);
                                bundle.putBoolean(Constant.KEY_SUCCESS, true);
                                msg.setData(bundle);
                                requestHandler.sendMessage(msg);
                            } else {
                                session.setLogin(false);
                                session.setLoggedinUser(null);
                                bundle.putBoolean(Constant.KEY_IS_LOGGED_IN, false);
                                bundle.putBoolean(Constant.KEY_SUCCESS, false);
                                msg.setData(bundle);
                                requestHandler.sendMessage(msg);
                                final String errorMsg = jObj.getString(Constant.KEY_MESSAGES);
                                Toast.makeText(Function.getApplicationContext(),
                                        errorMsg,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Function.getApplicationContext(),
                                    "Json error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        Function.setLoading(context, false);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(final VolleyError error) {
                Util.logVolleyMessage(error, "Login");
                Toast.makeText(Function.getApplicationContext(),
                        Util.getVolleyMessage(error),
                        Toast.LENGTH_LONG).show();
                Function.setLoading(context, false);
                session.setLogin(false);
                session.setLoggedinUser(null);
                bundle.putBoolean(Constant.KEY_IS_LOGGED_IN, false);
                bundle.putBoolean(Constant.KEY_SUCCESS, false);
                msg.setData(bundle);
                requestHandler.sendMessage(msg);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.KEY_EMAIL, email);
                params.put(Constant.KEY_PASSWORD, password);
                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(strReq);
    }

    public static void register(final Session session,
                                final Context context,
                                final String name,
                                final String id_number,
                                final String email,
                                final String address,
                                final String contact,
                                final String password,
                                final String confirmPassword,
                                final String encodedImage,
                                final Handler requestHandler) {
        final Message msg = requestHandler.obtainMessage();
        final Bundle bundle = new Bundle();
        final String tag_string_req = "req_gegister";
        Function.setLoading(context, "Signing Up...", true);
        final String url = session.getServerUrl() + Route.REGISTER;
        final StringRequest strReq = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.d(TAG, "Registration Response: " + response);
                        // Function.setLoading(false);
                        try {
                            final JSONObject jObj = new JSONObject(response);
                            final boolean success = jObj.getBoolean(Constant.KEY_SUCCESS);
                            // Check for error node in json
                            if (success) {
                                session.setLogin(false);
                                session.setLoggedinUser(null);
                                bundle.putBoolean(Constant.KEY_SUCCESS, true);
                                msg.setData(bundle);
                                requestHandler.sendMessage(msg);
                            } else {
                                session.setLogin(false);
                                session.setLoggedinUser(null);
                                bundle.putBoolean(Constant.KEY_SUCCESS, false);
                                msg.setData(bundle);
                                requestHandler.sendMessage(msg);
                                final String errorMsg = jObj.getString(Constant.KEY_MESSAGES);
                                Toast.makeText(Function.getApplicationContext(),
                                        errorMsg,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Function.getApplicationContext(),
                                    "Json error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        Function.setLoading(context, false);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Util.logVolleyMessage(error, "Register");
                Toast.makeText(Function.getApplicationContext(),
                        Util.getVolleyMessage(error),
                        Toast.LENGTH_LONG).show();
                Function.setLoading(context, false);
                session.setLogin(false);
                session.setLoggedinUser(null);
                bundle.putBoolean(Constant.KEY_SUCCESS, false);
                msg.setData(bundle);
                requestHandler.sendMessage(msg);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.KEY_NAME, name);
                params.put(Constant.KEY_CONTACT, contact);
                params.put(Constant.KEY_ID_NUMBER, id_number);
                params.put(Constant.KEY_ADDRESS, address);
                params.put(Constant.KEY_EMAIL, email);
                params.put(Constant.KEY_PASSWORD, password);
                params.put(Constant.KEY_CONFIRM_PASSWORD, confirmPassword);
                // params.put(Constant.KEY_IMAGE, encodedImage);
                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(strReq);
    }

    public static void update(final Session session,
                              final Context context,
                              final String name,
                              final String id_number,
                              final String email,
                              final String contact,
                              final String address,
                              final String user_id,
                              final Handler requestHandler) {
        final Message msg = requestHandler.obtainMessage();
        final Bundle bundle = new Bundle();
        final String tag_string_req = "req_update_information";
        Function.setLoading(context, "Updating Information...", true);
        final String url = session.getServerUrl() + Route.UPDATE_USER + "/" + user_id;
        final StringRequest strReq = new StringRequest(
                Request.Method.PATCH,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.d(TAG, "Updating Information: " + response);
                        try {
                            final JSONObject jObj = new JSONObject(response);
                            final boolean success = jObj.getBoolean(Constant.KEY_SUCCESS);
                            // Check for error node in json
                            if (success) {
                                session.setLoggedinUser(
                                        jObj.getJSONObject(Constant.KEY_USER).toString());
                                bundle.putBoolean(Constant.KEY_SUCCESS, true);
                                msg.setData(bundle);
                                requestHandler.sendMessage(msg);
                            } else {
                                bundle.putBoolean(Constant.KEY_SUCCESS, false);
                                msg.setData(bundle);
                                requestHandler.sendMessage(msg);
                                final String errorMsg = jObj.getString(Constant.KEY_MESSAGES);
                                Toast.makeText(Function.getApplicationContext(),
                                        errorMsg,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Function.getApplicationContext(),
                                    "Json error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        Function.setLoading(context, false);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Util.logVolleyMessage(error, "Register");
                Toast.makeText(Function.getApplicationContext(),
                        Util.getVolleyMessage(error),
                        Toast.LENGTH_LONG).show();
                Function.setLoading(context, false);
                bundle.putBoolean(Constant.KEY_SUCCESS, false);
                msg.setData(bundle);
                requestHandler.sendMessage(msg);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.KEY_NAME, name);
                params.put(Constant.KEY_CONTACT, contact);
                params.put(Constant.KEY_ID_NUMBER, id_number);
                params.put(Constant.KEY_ADDRESS, address);
                params.put(Constant.KEY_EMAIL, email);
                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(strReq);
    }
}
