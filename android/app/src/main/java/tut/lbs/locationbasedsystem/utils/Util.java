package tut.lbs.locationbasedsystem.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.VolleyError;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Util {
    public static void logVolleyMessage(final VolleyError error,
                                        final String TAG) {
        Log.e(TAG, error.getMessage() != null ? error.getMessage() : error.toString());
    }

    public static String getVolleyMessage(final VolleyError error) {
        String body = "";
        //get status code here
        if (error.getMessage() != null) {
            body = error.getMessage();
            //  if (error.networkResponse != null) {
            //           final String statusCode = String.valueOf(error.networkResponse.statusCode);
            //            //get response body and parse with appropriate encoding
            //            if (error.networkResponse.data != null) {
            //                try {
            //                    body = new String(error.networkResponse.data, "UTF-8");
            //                } catch (final UnsupportedEncodingException e) {
            //                    e.printStackTrace();
            //                }
            //            }
        } else {
            body = error.toString();
        }

        return body;
    }

    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }


    public static String getAddress(final Context context,
                              @NonNull final double latitude,
                              @NonNull final double longitude) {
        final Geocoder geocoder;
        final List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            // If any additional address line present than only, check with max available address lines
            // by getMaxAddressLineIndex()
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                // Only if available else return NULL
                String knownName = addresses.get(0).getFeatureName();
                return address;
            }

            return "No Address found";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "No Address found";
    }
}
