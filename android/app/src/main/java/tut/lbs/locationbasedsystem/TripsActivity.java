package tut.lbs.locationbasedsystem;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import tut.lbs.locationbasedsystem.adapters.TripsRecyclerAdapter;
import tut.lbs.locationbasedsystem.managers.Session;
import tut.lbs.locationbasedsystem.models.Trip;
import tut.lbs.locationbasedsystem.models.User;
import tut.lbs.locationbasedsystem.requests.GeneralRequests;
import tut.lbs.locationbasedsystem.utils.Constant;

public class TripsActivity extends BaseActivity {
    private Session sessionManager;
    private ProgressBar progress;
    private RecyclerView recyclerView;
    public static final int TYPE_LINEAR = 0;
    public static final int TYPE_GRID = 1;
    public static final int TYPE_STAGGERED_GRID = 2;
    final int mType = TYPE_LINEAR;
    private RecyclerView.LayoutManager layoutManager;
    private TripsRecyclerAdapter adapter;
    private List<Trip> trips = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        setTitle("My Trips");
        sessionManager = new Session(TripsActivity.this);
        progress = (ProgressBar) findViewById(R.id.progress);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        final User user = sessionManager.getLoggedInUser();
        trips = sessionManager.getTrips();
        GeneralRequests.getTrips(sessionManager,
                TripsActivity.this,
                String.valueOf(user.id),
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
        adapter = new TripsRecyclerAdapter(TripsActivity.this, mType, trips);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickCallback(onItemClickCallback);
    }

    private Handler requestHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            final Bundle data = message.getData();
            final boolean success = data.getBoolean(Constant.KEY_SUCCESS);
            if (success) {
                adapter.setItems(sessionManager.getTrips());
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


                }

                @Override
                public void onItemClicked(final View view,
                                          final int parentPosition,
                                          final int childPosition) {

                }
            };
}
