package tut.lbs.locationbasedsystem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import tut.lbs.locationbasedsystem.adapters.TripsRecyclerAdapter;
import tut.lbs.locationbasedsystem.managers.Session;
import tut.lbs.locationbasedsystem.models.Trip;
import tut.lbs.locationbasedsystem.models.User;
import tut.lbs.locationbasedsystem.requests.GeneralRequests;
import tut.lbs.locationbasedsystem.utils.Constant;

public class TrackedTripsActivity extends AppCompatActivity {
    private Session sessionManager;
    private ProgressBar progress;
    private RecyclerView recyclerView;
    public static final int TYPE_LINEAR = 0;
    public static final int TYPE_GRID = 1;
    public static final int TYPE_STAGGERED_GRID = 2;
    final int mType = TYPE_LINEAR;
    private RecyclerView.LayoutManager layoutManager;
    private TripsRecyclerAdapter adapter;
    private User user;
    private List<Trip> trips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracked_trips);
        setTitle("Tracked Trips");
        sessionManager = new Session(TrackedTripsActivity.this);
        progress = (ProgressBar) findViewById(R.id.progress);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        trips = sessionManager.getTrackedTrips();
        user = sessionManager.getLoggedInUser();

        switch (mType) {
            case TYPE_LINEAR:
                layoutManager = new LinearLayoutManager(this);
                break;
            case TYPE_GRID:
                layoutManager = new GridLayoutManager(this, 2);
                break;
            case TYPE_STAGGERED_GRID:
                layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
        }

        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TripsRecyclerAdapter(TrackedTripsActivity.this, mType, trips);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickCallback(onItemClickCallback);

        recyclerView.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_track, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        long id = item.getItemId();
        if (id == R.id.track_trip) {
            showDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(TrackedTripsActivity.this);
        final View view = getLayoutInflater()
                .inflate(R.layout.track_trip_dialog, null);
        builder.setTitle("Insert Reference")
                //.setMessage("Do you want to track this activity?")
                .setCancelable(false)
                .setPositiveButton("Track", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        final AutoCompleteTextView edtReference =
                                (AutoCompleteTextView) view.findViewById(R.id.reference);
                        final String reference = edtReference.getText().toString();
                        GeneralRequests.checkTrip(sessionManager,
                                TrackedTripsActivity.this,
                                reference,
                                String.valueOf(user.id),
                                requestHandler);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();
    }

    private Handler requestHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            final Bundle data = message.getData();
            final boolean success = data.getBoolean(Constant.KEY_SUCCESS);
            if (success) {
                final Trip trip = sessionManager.getTrackedTrip();
                sessionManager.trackTrip(TrackedTripsActivity.this, trip);
                trips = sessionManager.getTrackedTrips();
                adapter.setItems(trips);
            }

            recyclerView.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            return false;
        }
    });

    private RecyclerClickListener.OnItemClickCallback onItemClickCallback =
            new RecyclerClickListener.OnItemClickCallback() {
                @Override
                public void onItemClicked(final View view,
                                          final int position) {
                    final Intent intent =
                            new Intent(
                                    TrackedTripsActivity.this,
                                    EventsActivity.class);
                    intent.putExtra(Constant.KEY_TRACKED_TRIP, trips.get(position));
                    startActivity(intent);
                }

                @Override
                public void onItemClicked(final View view,
                                          final int parentPosition,
                                          final int childPosition) {

                }
            };
}
