package tut.lbs.locationbasedsystem.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import tut.lbs.locationbasedsystem.models.Event;
import tut.lbs.locationbasedsystem.models.Trip;
import tut.lbs.locationbasedsystem.models.User;
import tut.lbs.locationbasedsystem.utils.Constant;

public class Session {
    private static String TAG = Session.class.getSimpleName();
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    @SuppressLint("CommitPrefEdits")
    public Session(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Constant.PREF_NAME, Constant.PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(final boolean isLoggedIn) {
        editor.putBoolean(Constant.KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }

    public void setLoggedinUser(final String user) {
        editor.putString(Constant.KEY_LOGGED_IN_USER, user);
        editor.apply();
    }

    public User getLoggedInUser() {
        final Gson gson = new GsonBuilder().create();
        final Type type = new TypeToken<User>() {
        }.getType();

        final String data = pref.getString(Constant.KEY_LOGGED_IN_USER, null);
        return gson.fromJson(data, type);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(Constant.KEY_IS_LOGGED_IN, false);
    }

    public void setServerUrl(final String serverUrl) {
        editor.putString(Constant.KEY_SERVER_URL, serverUrl);
        editor.apply();
    }

    public String getServerUrl() {
        return pref.getString(Constant.KEY_SERVER_URL, Constant.KEY_DEFAULT_SERVER_URL);
    }

    public void setTrips(final String trips) {
        editor.putString(Constant.KEY_TRIPS, trips);
        editor.apply();
    }

    public List<Trip> getTrips() {
        final Gson gson = new GsonBuilder().create();
        final Type type = new TypeToken<List<Trip>>() {
        }.getType();
        return gson.fromJson(pref.getString(Constant.KEY_TRIPS, "[]"), type);
    }

    public void setEvents(final String trips) {
        editor.putString(Constant.KEY_EVENTS, trips);
        editor.apply();
    }

    public List<Event> getEvents() {
        final Gson gson = new GsonBuilder().create();
        final Type type = new TypeToken<List<Event>>() {
        }.getType();
        return gson.fromJson(pref.getString(Constant.KEY_EVENTS, "[]"), type);
    }

    public void setCurrentTrip(final String trip) {
        editor.putString(Constant.KEY_CURRENT_TRIP, trip);
        editor.apply();
    }

    public Trip getCurrentTrip() {
        final Gson gson = new GsonBuilder().create();
        final Type type = new TypeToken<Trip>() {
        }.getType();
        return gson.fromJson(pref.getString(Constant.KEY_CURRENT_TRIP, null), type);
    }


    public void setTrackedTrip(final String trip) {
        editor.putString(Constant.KEY_TRACKED_TRIP, trip);
        editor.apply();
    }

    public Trip getTrackedTrip() {
        final Gson gson = new GsonBuilder().create();
        final Type type = new TypeToken<Trip>() {
        }.getType();
        return gson.fromJson(pref.getString(Constant.KEY_TRACKED_TRIP, null), type);

    }

    public void trackTrip(final Context context,
                          final Trip trip) {
        final List<Trip> trackedTrips = getTrackedTrips();
        boolean isAdded = false;
        int foundAt = 0;
        for (int x = 0; x < trackedTrips.size(); x++) {
            final Trip trackedTrip = trackedTrips.get(x);
            if (trackedTrip.reference.equals(trip.reference)) {
                isAdded = true;
                foundAt = x;
            }
        }

        final Trip mTrip;
        if (isAdded) {
            mTrip = getTrackedTrips().get(foundAt);
            trackedTrips.set(foundAt, mTrip);
        } else {
            mTrip = trip;
            // mTrip.number = 1;
            trackedTrips.add(mTrip);
        }


        final Gson gson = new GsonBuilder().create();
        editor.putString(Constant.KEY_TRACKED_TRIPS, gson.toJson(trackedTrips));
        editor.apply();
    }

    public void setTrackedTrips(final String trip) {
        editor.putString(Constant.KEY_TRACKED_TRIPS, trip);
        editor.apply();
    }

    public List<Trip> getTrackedTrips() {
        final Gson gson = new GsonBuilder().create();
        final Type type = new TypeToken<List<Trip>>() {
        }.getType();
        return gson.fromJson(pref.getString(Constant.KEY_TRACKED_TRIPS, "[]"), type);
    }

}
