package tut.lbs.locationbasedsystem;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tut.lbs.locationbasedsystem.managers.Session;
import tut.lbs.locationbasedsystem.models.DirectionObject;
import tut.lbs.locationbasedsystem.models.Event;
import tut.lbs.locationbasedsystem.models.LegsObject;
import tut.lbs.locationbasedsystem.models.PolylineObject;
import tut.lbs.locationbasedsystem.models.RouteObject;
import tut.lbs.locationbasedsystem.models.StepsObject;
import tut.lbs.locationbasedsystem.models.Trip;
import tut.lbs.locationbasedsystem.models.User;
import tut.lbs.locationbasedsystem.network.GsonRequest;
import tut.lbs.locationbasedsystem.network.Helper;
import tut.lbs.locationbasedsystem.network.VolleySingleton;
import tut.lbs.locationbasedsystem.requests.GeneralRequests;
import tut.lbs.locationbasedsystem.utils.Constant;
import tut.lbs.locationbasedsystem.utils.Util;
import tut.lbs.locationbasedsystem.views.FragmentModalBottomSheet;

public class EventsMapsActivity extends BaseActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = EventsMapsActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private static final int PERMISSION_LOCATION_REQUEST_CODE = 100;
    private List<LatLng> latLngList;
    private MarkerOptions yourLocationMarker;
    private BottomNavigationView bottomNavigationView;
    private Session session;
    private User user;
    private LatLng current_location;
    private LatLng destination;
    private Event event;
    private Trip trip;
    private int mapZoom = 8;

    @Override
    protected void onResume() {
        super.onResume();
        session = new Session(EventsMapsActivity.this);
        if (!session.isLoggedIn()) {
            startActivity(new Intent(EventsMapsActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_maps);
        setTitle(getString(R.string.app_name));
        session = new Session(EventsMapsActivity.this);
        if (!session.isLoggedIn()) {
            logout(session);
            startActivity(new Intent(EventsMapsActivity.this, LoginActivity.class));
        }
        user = session.getLoggedInUser();
        event = (Event) getIntent().getSerializableExtra(Constant.KEY_EVENT);
        trip = (Trip) event.trip;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            final String channelId = getString(R.string.default_notification_channel_id);
            final String channelName = getString(R.string.default_notification_channel_name);
            final NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }

        latLngList = new ArrayList<LatLng>();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLocationRequest = createLocationRequest();
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        current_location = new LatLng(event.current_latitude, event.current_longitude);
        destination = new LatLng(trip.to_latitude, trip.to_longitude);

        if (mMap != null) {

        }
    }

    @Override
    public void onBackPressed() {
        returnToEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        Log.e("ID", id + "");
        if (id == android.R.id.home) {
            returnToEvents();
        }
        return super.onOptionsItemSelected(item);
    }

    public void returnToEvents() {
        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
       /* final Intent intent =
                new Intent(
                        EventsMapsActivity.this,
                        EventsActivity.class);
        intent.putExtra(Constant.KEY_TRACKED_TRIP, trip);
        finish();
        startActivity(intent);*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mLastLocation = new Location("");
        mLastLocation.setLatitude(current_location.latitude);
        mLastLocation.setLongitude(current_location.longitude);
        assignLocationValues(mLastLocation);
        setDefaultMarkerOption(current_location);

        drawPath(current_location, destination);
    }

    public void drawPath(final LatLng currentLocation,
                         final LatLng destinationLocation) {

        if (latLngList.size() > 0) {
            refreshMap(mMap);
            latLngList.clear();
        }

        latLngList.add(destinationLocation);
        Log.d(TAG, "Marker number " + latLngList.size());
        mMap.addMarker(yourLocationMarker);
        mMap.addMarker(new MarkerOptions()
                .title("Destination")
                .position(destinationLocation));

        //use Google Direction API to get the route between these Locations
        String directionApiPath = Helper.getUrl(String.valueOf(currentLocation.latitude),
                String.valueOf(currentLocation.longitude),
                String.valueOf(destinationLocation.latitude),
                String.valueOf(destinationLocation.longitude));
        Log.d(TAG, "Path " + directionApiPath);
        getDirectionFromDirectionApiServer(directionApiPath);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.d(TAG, "Connection method has been called");
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            mLastLocation = new Location("");
                            mLastLocation.setLatitude(current_location.latitude);
                            mLastLocation.setLongitude(current_location.longitude);
                            assignLocationValues(mLastLocation);
                            setDefaultMarkerOption(current_location);
                        } else {
                            ActivityCompat.requestPermissions(EventsMapsActivity.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                            Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSION_LOCATION_REQUEST_CODE);
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // permission was denied, show alert to explain permission
                    showPermissionAlert();
                } else {
                    //permission is granted now start a background service
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mLastLocation = new Location("");
                        mLastLocation.setLatitude(current_location.latitude);
                        mLastLocation.setLongitude(current_location.longitude);
                        assignLocationValues(mLastLocation);
                        setDefaultMarkerOption(current_location);
                    }
                }
            }
        }
    }

    private void assignLocationValues(final Location currentLocation) {
        if (currentLocation != null) {
            final double latitudeValue = currentLocation.getLatitude();
            final double longitudeValue = currentLocation.getLongitude();
            Log.d(TAG, "Latitude: " + latitudeValue + " Longitude: " + longitudeValue);
            LatLng currentLoc = new LatLng(latitudeValue, longitudeValue);
            markStartingLocationOnMap(mMap, currentLoc);
            addCameraToMap(currentLoc);
        }
    }

    private void addCameraToMap(LatLng latLng) {
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(mapZoom)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void showPermissionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_request_title);
        builder.setMessage(R.string.app_permission_notice);
        builder.create();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityCompat.checkSelfPermission(EventsMapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(EventsMapsActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EventsMapsActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST_CODE);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EventsMapsActivity.this,
                        R.string.permission_refused,
                        Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    private void markStartingLocationOnMap(GoogleMap mapObject,
                                           LatLng location) {
        mapObject.addMarker(new MarkerOptions().position(location).title("Current location"));
        mapObject.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    private void refreshMap(GoogleMap mapInstance) {
        mapInstance.clear();
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void setDefaultMarkerOption(LatLng location) {
        if (yourLocationMarker == null) {
            yourLocationMarker = new MarkerOptions();
        }
        yourLocationMarker.position(location);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    private void getDirectionFromDirectionApiServer(String url) {
        GsonRequest<DirectionObject> serverRequest = new GsonRequest<DirectionObject>(
                Request.Method.GET,
                url,
                DirectionObject.class,
                createRequestSuccessListener(),
                createRequestErrorListener());
        serverRequest.setRetryPolicy(new DefaultRetryPolicy(
                Helper.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(serverRequest);
    }

    private Response.Listener<DirectionObject> createRequestSuccessListener() {
        return new Response.Listener<DirectionObject>() {
            @Override
            public void onResponse(DirectionObject response) {
                try {
                    Log.d("JSON Response", response.toString());
                    if (response.getStatus().equals("OK")) {
                        List<LatLng> mDirections = getDirectionPolylines(response.getRoutes());
                        drawRouteOnMap(mMap, mDirections);
                    } else {
                        Toast.makeText(
                                EventsMapsActivity.this,
                                R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        };
    }

    private List<LatLng> getDirectionPolylines(List<RouteObject> routes) {
        List<LatLng> directionList = new ArrayList<LatLng>();
        for (RouteObject route : routes) {
            List<LegsObject> legs = route.getLegs();
            for (LegsObject leg : legs) {
                List<StepsObject> steps = leg.getSteps();
                for (StepsObject step : steps) {
                    PolylineObject polyline = step.getPolyline();
                    String points = polyline.getPoints();
                    List<LatLng> singlePolyline = decodePoly(points);
                    for (LatLng direction : singlePolyline) {
                        directionList.add(direction);
                    }
                }
            }
        }
        return directionList;
    }

    private Response.ErrorListener createRequestErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        };
    }

    private void drawRouteOnMap(GoogleMap map, List<LatLng> positions) {
        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        options.addAll(positions);
        Polyline polyline = map.addPolyline(options);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(positions.get(1).latitude, positions.get(1).longitude))
                .zoom(mapZoom)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}
