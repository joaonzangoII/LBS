package tut.lbs.locationbasedsystem.adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tut.lbs.locationbasedsystem.MapsActivity;
import tut.lbs.locationbasedsystem.R;
import tut.lbs.locationbasedsystem.RecyclerClickListener;
import tut.lbs.locationbasedsystem.managers.Session;
import tut.lbs.locationbasedsystem.models.Trip;
import tut.lbs.locationbasedsystem.utils.Util;

public class TripsRecyclerAdapter extends RecyclerView.Adapter<TripsRecyclerAdapter.TripViewHolder> {
    private LayoutInflater inflater;
    private List<Trip> trips = new ArrayList<>();
    private RecyclerClickListener.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(final RecyclerClickListener.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public TripsRecyclerAdapter(final Context context,
                                final int rType,
                                final List<Trip> pTrips) {
        this.trips = pTrips;
        inflater = LayoutInflater.from(context);
    }

    public void setItems(final List<Trip> pTips) {
        trips.clear();
        trips.addAll(pTips);
        notifyDataSetChanged();
    }


    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        final View view = inflater.inflate(R.layout.trips_list_item, parent,
                false);
        return new TripViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final TripViewHolder holder,
                                 int position) {
        final Trip trip = getItem(position);
        holder.reference.setText(trip.reference);
        holder.from.setText(Util.getAddress(holder.itemView.getContext(),
                trip.from_latitude,
                trip.from_longitude));
        holder.to.setText(Util.getAddress(holder.itemView.getContext(),
                trip.to_latitude,
                trip.to_longitude));
        holder.date.setText(trip.created_at);
        holder.rider.setText("");
        if (trip.user != null) {
            final Session session = new Session(holder.itemView.getContext());
            if(session.getLoggedInUser().id != trip.user_id) {
                holder.rider.setText(trip.user.name);
            }
        }

        holder.status.setText(Util.capitalize(trip.status));
        holder.itemView.setOnClickListener(new RecyclerClickListener(position, onItemClickCallback));
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public Trip getItem(final int position) {
        return trips.get(position);
    }

    class TripViewHolder extends RecyclerView.ViewHolder {
        TextView reference;
        TextView from;
        TextView to;
        TextView date;
        TextView rider;
        TextView status;

        public TripViewHolder(View itemView) {
            super(itemView);
            reference = (TextView) itemView.findViewById(R.id.reference);
            from = (TextView) itemView.findViewById(R.id.from);
            to = (TextView) itemView.findViewById(R.id.to);
            date = (TextView) itemView.findViewById(R.id.date);
            rider = (TextView) itemView.findViewById(R.id.rider);
            status = (TextView) itemView.findViewById(R.id.status);
        }
    }
}
