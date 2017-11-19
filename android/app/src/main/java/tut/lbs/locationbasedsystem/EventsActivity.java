package tut.lbs.locationbasedsystem;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import tut.lbs.locationbasedsystem.adapters.EventsRecyclerAdapter;
import tut.lbs.locationbasedsystem.adapters.TripsRecyclerAdapter;
import tut.lbs.locationbasedsystem.managers.Session;
import tut.lbs.locationbasedsystem.models.Event;
import tut.lbs.locationbasedsystem.models.Trip;
import tut.lbs.locationbasedsystem.models.User;
import tut.lbs.locationbasedsystem.requests.GeneralRequests;
import tut.lbs.locationbasedsystem.utils.Constant;

public class EventsActivity extends AppCompatActivity {
    private Session session;
    private ProgressBar progress;
    private RecyclerView recyclerView;
    public static final int TYPE_LINEAR = 0;
    public static final int TYPE_GRID = 1;
    public static final int TYPE_STAGGERED_GRID = 2;
    final int mType = TYPE_LINEAR;
    private List<Event> events = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private EventsRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        setTitle("Events");
        session = new Session(EventsActivity.this);
        progress = (ProgressBar) findViewById(R.id.progress);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        events = session.getEvents();
        final User user = session.getLoggedInUser();
        final Trip trip = (Trip) session.getTrackedTrip();

        GeneralRequests.getEvents(session,
                EventsActivity.this,
                String.valueOf(trip.id),
                requestHandler);
        switch (mType) {
            case TYPE_LINEAR:
                layoutManager = new LinearLayoutManager(this);
                break;
            case TYPE_GRID:
                layoutManager = new GridLayoutManager(this, 2);
                break;
            case TYPE_STAGGERED_GRID:
                layoutManager = new StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.VERTICAL);
                break;
        }

        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new EventsRecyclerAdapter(EventsActivity.this, mType, events);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickCallback(onItemClickCallback);
    }

    private Handler requestHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            final Bundle data = message.getData();
            final boolean success = data.getBoolean(Constant.KEY_SUCCESS);
            if (success) {
                events = session.getEvents();
                adapter.setItems(events);
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
                                    EventsActivity.this,
                                    EventsMapsActivity.class);
                    final Gson gson = new GsonBuilder().create();
                    session.setTrackedTrips(gson.toJson(events.get(position)));
                    intent.putExtra(Constant.KEY_EVENT, events.get(position));
                    startActivity(intent);
                }

                @Override
                public void onItemClicked(final View view,
                                          final int parentPosition,
                                          final int childPosition) {

                }
            };
}
