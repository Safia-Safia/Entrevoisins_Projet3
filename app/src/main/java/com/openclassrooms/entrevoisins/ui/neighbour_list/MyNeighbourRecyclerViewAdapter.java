package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.events.DeleteFavoriteEvent;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyNeighbourRecyclerViewAdapter extends RecyclerView.Adapter<MyNeighbourRecyclerViewAdapter.ViewHolder> {

    private final List<Neighbour> mNeighbours;
    private onNeighbourClickListener mOnNeighbourClickListener;
    private int mCurrentFragment;

    public MyNeighbourRecyclerViewAdapter(List<Neighbour> items, onNeighbourClickListener onNeighbourClickListener, int mCurrentFragment) {
        mNeighbours = items;
        this.mCurrentFragment = mCurrentFragment;
        this.mOnNeighbourClickListener = onNeighbourClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_neighbour, parent, false);
        return new ViewHolder(view, mOnNeighbourClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Neighbour neighbour = mNeighbours.get(position);
        holder.mNeighbourName.setText(neighbour.getName());
        Glide.with(holder.mNeighbourAvatar.getContext())
                .load(neighbour.getAvatarUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mNeighbourAvatar);

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentFragment == 0){
                    EventBus.getDefault().post(new DeleteNeighbourEvent(neighbour));
                    EventBus.getDefault().post(new DeleteFavoriteEvent(neighbour));
                }else{
                    EventBus.getDefault().post(new DeleteFavoriteEvent(neighbour));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNeighbours.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.item_list_avatar)
        public ImageView mNeighbourAvatar;
        @BindView(R.id.item_list_name)
        public TextView mNeighbourName;
        @BindView(R.id.item_list_delete_button)
        public ImageButton mDeleteButton;
        onNeighbourClickListener mNeighbourClickListener;

        public ViewHolder(View view, onNeighbourClickListener onNeighbourClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            this.mNeighbourClickListener = onNeighbourClickListener;
        }

        @Override
        public void onClick(View view) {
            mNeighbourClickListener.onNeighbourClick(getAdapterPosition());
        }
    }

    public interface onNeighbourClickListener {
        void onNeighbourClick(int position);
    }
}
