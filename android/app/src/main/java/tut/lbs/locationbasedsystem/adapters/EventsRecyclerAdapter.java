package tut.lbs.locationbasedsystem.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tut.lbs.locationbasedsystem.R;
import tut.lbs.locationbasedsystem.RecyclerClickListener;
import tut.lbs.locationbasedsystem.managers.Session;
import tut.lbs.locationbasedsystem.models.Event;
import tut.lbs.locationbasedsystem.models.Trip;
import tut.lbs.locationbasedsystem.utils.Util;

public class EventsRecyclerAdapter extends RecyclerView.Adapter<EventsRecyclerAdapter.TripViewHolder> {
    private LayoutInflater inflater;
    private List<Event> events = new ArrayList<>();
    private RecyclerClickListener.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(final RecyclerClickListener.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public EventsRecyclerAdapter(final Context context,
                                 final int rType,
                                 final List<Event> pEvents) {
        this.events = pEvents;
        inflater = LayoutInflater.from(context);
    }

    public void setItems(final List<Event> pEvents) {
        events.clear();
        events.addAll(pEvents);
        notifyDataSetChanged();
    }


    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        final View view = inflater.inflate(R.layout.events_list_item, parent,
                false);
        return new TripViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final TripViewHolder holder,
                                 int position) {
        final Event event = getItem(position);
        holder.current_latitude.setText(String.valueOf(event.current_latitude));
        holder.current_longitude.setText(String.valueOf(event.current_longitude));
        holder.date.setText(event.created_at);
        holder.itemView.setOnClickListener(new RecyclerClickListener(position, onItemClickCallback));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public Event getItem(final int position) {
        return events.get(position);
    }

    class TripViewHolder extends RecyclerView.ViewHolder {
        TextView current_latitude;
        TextView current_longitude;
        TextView date;

        public TripViewHolder(View itemView) {
            super(itemView);
            current_latitude = (TextView) itemView.findViewById(R.id.current_latitude);
            current_longitude = (TextView) itemView.findViewById(R.id.current_longitude);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
