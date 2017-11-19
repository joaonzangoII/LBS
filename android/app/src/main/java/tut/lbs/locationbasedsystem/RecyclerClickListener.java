package tut.lbs.locationbasedsystem;

import android.view.View;

public class RecyclerClickListener implements View.OnClickListener {
    private int parentPosition;
    private int childPosition;
    private OnItemClickCallback onItemClickCallback;

    public RecyclerClickListener() {
    }

    public RecyclerClickListener(final int parentPosition,
                                 final OnItemClickCallback onItemClickCallback) {
        this.parentPosition = parentPosition;
        this.onItemClickCallback = onItemClickCallback;
    }

    public RecyclerClickListener(final int parentPosition,
                                 final int childPosition,
                                 final OnItemClickCallback onItemClickCallback) {
        this.parentPosition = parentPosition;
        this.childPosition = childPosition;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onClick(final View v) {
        onItemClickCallback.onItemClicked(v, parentPosition, childPosition);
        onItemClickCallback.onItemClicked(v, parentPosition);
    }

    public interface OnItemClickCallback {
        void onItemClicked(final View view,
                           final int parentPosition,
                           final int childPosition);

        void onItemClicked(final View view,
                           final int parentPosition);
    }
}
