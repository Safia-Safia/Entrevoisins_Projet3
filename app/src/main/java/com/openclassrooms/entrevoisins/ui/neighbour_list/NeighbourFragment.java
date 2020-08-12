package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.DeleteFavoriteEvent;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


public class NeighbourFragment extends Fragment implements MyNeighbourRecyclerViewAdapter.onNeighbourClickListener {

    private NeighbourApiService mApiService;
    private List<Neighbour> mNeighbours;
    private RecyclerView mRecyclerView;
    public static final String KEY_NEIGHBOURS = "KEY_NEIGHBOURS";
    public static final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";

    /**
     * Create and return a new instance
     *
     * @return @{@link NeighbourFragment}
     */
    public static NeighbourFragment newInstance(int fragmentIndex) {
        NeighbourFragment fragment = new NeighbourFragment();
        Bundle args = new Bundle();
        args.putInt(CURRENT_FRAGMENT, fragmentIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getNeighbourApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int getFragmentIndex = getArguments().getInt(CURRENT_FRAGMENT, 0);
        View view;
        if (getFragmentIndex == 0) {
            view = inflater.inflate(R.layout.fragment_neighbour_list, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_favorite_neighbour_list, container, false);
        }
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        return view;
    }

    /**
     * Init the List of neighbours
     */
    private void initList() {
        int getFragmentIndex = getArguments().getInt(CURRENT_FRAGMENT, 0);
        if (getFragmentIndex == 0) {
            mNeighbours = mApiService.getNeighbours();
        } else {
            mNeighbours = mApiService.getFavoriteNeighbour();
        }
        mRecyclerView.setAdapter(new MyNeighbourRecyclerViewAdapter
                (mNeighbours, this, getFragmentIndex));
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Fired if the user clicks on a delete button
     *
     * @param event
     */
    @Subscribe
    public void onDeleteNeighbour(DeleteNeighbourEvent event) {
        mApiService.deleteNeighbour(event.neighbour);
        initList();
    }

    @Subscribe
    public void onDeleteFavoriteNeighbour(DeleteFavoriteEvent event) {
        mApiService.deleteFavoriteNeighbour(event.neighbour);
        initList();
    }

    @Override
    public void onNeighbourClick(int position) {
        Intent NeighbourDetailIntent = new Intent(NeighbourFragment.this.getActivity(),
                                           NeighbourDetailActivity.class);
        NeighbourDetailIntent.putExtra(KEY_NEIGHBOURS, mNeighbours.get(position));
        startActivity(NeighbourDetailIntent);
    }
}
