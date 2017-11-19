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

import java.net.ResponseCache;
import java.util.HashMap;
import java.util.Map;

import tut.lbs.locationbasedsystem.managers.Route;
import tut.lbs.locationbasedsystem.managers.Session;
import tut.lbs.locationbasedsystem.network.VolleySingleton;
import tut.lbs.locationbasedsystem.utils.Constant;
import tut.lbs.locationbasedsystem.utils.Function;
import tut.lbs.locationbasedsystem.utils.Util;

public class GeneralRequests {
    private static final String TAG = AuthRequest.class.getSimpleName();

    public static void getTrips(final Session session,
                                final Context context,
                                final String user_id,
                                final Handler requestHandler) {
        Function.setLoading(context, "Getting trips", true);
        final Message msg = requestHandler.obtainMessage();
        final Bundle bundle = new Bundle();
        final String url = session.getServerUrl() + Route.USER_TRIPS + "/" + user_id;
        final String tag_string_req = "req_get_trips";
        final StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.e(tag_string_req, response);

                try {
                    final JSONObject jObj = new JSONObject(response);
                    final boolean success = jObj.getBoolean(Constant.KEY_SUCCESS);
                    // Check for error node in json
                    if (success) {
                        session.setTrips(jObj.getJSONArray(Constant.KEY_DATA).toString());
                        bundle.putBoolean(Constant.KEY_SUCCESS, true);
                        bundle.putString(Constant.KEY_TRIPS,
                                jObj.getJSONArray(Constant.KEY_DATA).toString());
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
                Util.logVolleyMessage(error, "Get My Trips");
                Toast.makeText(Function.getApplicationContext(),
                        Util.getVolleyMessage(error),
                        Toast.LENGTH_LONG).show();
                session.setTrips("[]");
                bundle.putBoolean(Constant.KEY_SUCCESS, false);
                msg.setData(bundle);
                requestHandler.sendMessage(msg);
                Function.setLoading(context, false);
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(strReq);
    }

    public static void requestRide(final Session session,
                                   final Context context,
                                   final String from_latitude,
                                   final String from_longitude,
                                   final String to_latitude,
                                   final String to_longitude,
                                   final String user_id,
                                   final Handler requestHandler) {
        final Message msg = requestHandler.obtainMessage();
        final Bundle bundle = new Bundle();
        final String tag_string_req = "req_request_rides";
        Function.setLoading(context, "Requesting Ride...", true);
        final String url = session.getServerUrl() + Route.REQUEST_RIDE;
        final StringRequest strReq = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.d(TAG, "Requesting Ride Response: " + response);
                        // Function.setLoading(false);
                        try {
                            final JSONObject jObj = new JSONObject(response);
                            final boolean success = jObj.getBoolean(Constant.KEY_SUCCESS);
                            // Check for error node in json
                            if (success) {
                                session.setCurrentTrip(
                                        jObj.getJSONObject(Constant.KEY_DATA).toString());
                                bundle.putBoolean(Constant.KEY_SUCCESS, true);
                                bundle.putBoolean(Constant.KEY_SUCCESS, true);
                                bundle.putString(Constant.KEY_OPTION, Constant.KEY_REQUEST_RIDE);
                                msg.setData(bundle);
                                requestHandler.sendMessage(msg);
                            } else {

                                bundle.putBoolean(Constant.KEY_SUCCESS, false);
                                bundle.putString(Constant.KEY_OPTION, Constant.KEY_REQUEST_RIDE);
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
                Util.logVolleyMessage(error, "Requesting Ride");
                Toast.makeText(Function.getApplicationContext(),
                        Util.getVolleyMessage(error),
                        Toast.LENGTH_LONG).show();
                Function.setLoading(context, false);
                bundle.putBoolean(Constant.KEY_SUCCESS, false);
                bundle.putString(Constant.KEY_OPTION, Constant.KEY_REQUEST_RIDE);
                msg.setData(bundle);
                requestHandler.sendMessage(msg);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.KEY_FROM_LATITUDE, from_latitude);
                params.put(Constant.KEY_FROM_LONGITUDE, from_longitude);
                params.put(Constant.KEY_TO_LATITUDE, to_latitude);
                params.put(Constant.KEY_TO_LONGITUDE, to_longitude);
                params.put(Constant.KEY_USER_ID, user_id);
                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(strReq);
    }

    public static void checkTrip(final Session session,
                                 final Context context,
                                 final String reference,
                                 final String user_id,
                                 final Handler requestHandler) {
        final Message msg = requestHandler.obtainMessage();
        final Bundle bundle = new Bundle();
        final String tag_string_req = "req_request_rides";
        Function.setLoading(context, "Requesting Ride info...", true);
        final String url = session.getServerUrl() + Route.CHECK_RIDE;
        final StringRequest strReq = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.d(TAG, "Requesting Ride Info Response: " + response);
                        // Function.setLoading(false);
                        try {
                            final JSONObject jObj = new JSONObject(response);
                            final boolean success = jObj.getBoolean(Constant.KEY_SUCCESS);
                            // Check for error node in json
                            if (success) {
                                session.setTrackedTrip(
                                        jObj.getJSONObject(Constant.KEY_DATA).toString());
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
            public void onErrorResponse(final VolleyError error) {
                Util.logVolleyMessage(error, "Requesting Ride");
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
                params.put(Constant.KEY_REFERENCE, reference);
                params.put(Constant.KEY_USER_ID, user_id);
                return params;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(strReq);
    }

    public static void getEvents(final Session session,
                                 final Context context,
                                 final String trip_id,
                                 final Handler requestHandler) {
        Function.setLoading(context, "Getting events", true);
        final Message msg = requestHandler.obtainMessage();
        final Bundle bundle = new Bundle();
        final String url = session.getServerUrl() + "/api/trips/" + trip_id + "/events";
        final String tag_string_req = "req_get_events";
        final StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                Log.e(tag_string_req, response);
                try {
                    final JSONObject jObj = new JSONObject(response);
                    final boolean success = jObj.getBoolean(Constant.KEY_SUCCESS);
                    if (success) {
                        session.setEvents(
                                jObj.getJSONArray(Constant.KEY_DATA).toString());
                        bundle.putBoolean(Constant.KEY_SUCCESS, true);
                        bundle.putString(Constant.KEY_EVENTS,
                                jObj.getJSONArray(Constant.KEY_DATA).toString());
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
                Util.logVolleyMessage(error, "Get Events");
                Toast.makeText(Function.getApplicationContext(),
                        Util.getVolleyMessage(error),
                        Toast.LENGTH_LONG).show();
                session.setEvents("[]");
                bundle.putBoolean(Constant.KEY_SUCCESS, false);
                msg.setData(bundle);
                requestHandler.sendMessage(msg);
                Function.setLoading(context, false);
            }
        });
        VolleySingleton.getInstance(context).

                addToRequestQueue(strReq);

    }

    public static void publishEvent(final Session session,
                                    final Context context,
                                    final String current_latitude,
                                    final String current_longitude,
                                    final String trip_id,
                                    final Handler requestHandler) {
        final Message msg = requestHandler.obtainMessage();
        final Bundle bundle = new Bundle();
        final String tag_string_req = "req_publish_event";
        Function.setLoading(context, "Publishing Event...", true);
        final String url = session.getServerUrl() + "/api/trips/" + trip_id + "/events";
        final StringRequest strReq = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.d(TAG, "Publish event: " + response);
                        // Function.setLoading(false);
                        try {
                            final JSONObject jObj = new JSONObject(response);
                            final boolean success = jObj.getBoolean(Constant.KEY_SUCCESS);
                            // Check for error node in json
                            if (success) {
                                session.setTrackedTrip(
                                        jObj.getJSONObject(Constant.KEY_DATA).toString());
                                bundle.putBoolean(Constant.KEY_SUCCESS, true);
                                bundle.putString(Constant.KEY_OPTION, Constant.KEY_PUBLISH_EVENT);
                                msg.setData(bundle);
                                requestHandler.sendMessage(msg);
                            } else {
                                bundle.putBoolean(Constant.KEY_SUCCESS, false);
                                bundle.putString(Constant.KEY_OPTION, Constant.KEY_PUBLISH_EVENT);
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
                }, new Response.ErrorListener()

        {

            @Override
            public void onErrorResponse(final VolleyError error) {
                Util.logVolleyMessage(error, "Publish event");
                Toast.makeText(Function.getApplicationContext(),
                        Util.getVolleyMessage(error),
                        Toast.LENGTH_LONG).show();
                Function.setLoading(context, false);
                bundle.putString(Constant.KEY_OPTION, Constant.KEY_PUBLISH_EVENT);
                bundle.putBoolean(Constant.KEY_SUCCESS, false);
                msg.setData(bundle);
                requestHandler.sendMessage(msg);
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.KEY_CURRENT_LATITUDE, current_latitude);
                params.put(Constant.KEY_CURRENT_LONGITUDE, current_longitude);
                params.put(Constant.KEY_TRIP_ID, trip_id);
                return params;
            }

        };

        VolleySingleton.getInstance(context).

                addToRequestQueue(strReq);
    }

}
