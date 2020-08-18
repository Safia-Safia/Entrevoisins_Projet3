package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NeighbourDetailActivity extends AppCompatActivity {
    private Neighbour mNeighbours;
    private NeighbourApiService mApiService;
    @BindView(R.id.textView_subTitle_name)
    TextView nameView;
    @BindView(R.id.textView_phoneNumber)
    TextView phoneView;
    @BindView(R.id.textView_detail_address)
    TextView addressView;
    @BindView(R.id.textView_website)
    TextView networkView;
    @BindView(R.id.textView_aboutMe_detail)
    TextView descriptionView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbarImage)
    ImageView avatarView;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.fab_favorite)
    FloatingActionButton mFavorite_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbour_detail);
        setUpView();
        setUpToolbar();
        initNeighbourDetail();
        setFabBtn();
        setFavoriteStatus();
    }

    private void initNeighbourDetail() {
        nameView.setText(mNeighbours.getName());
        Glide.with(this).load(mNeighbours.getAvatarUrl()).into(avatarView);
        addressView.setText(mNeighbours.getAddress());
        phoneView.setText(mNeighbours.getPhoneNumber());
        networkView.setText(String.format("%s%s", getString(R.string.txt_website), mNeighbours.getName()));
        descriptionView.setText(mNeighbours.getAboutMe());
    }

    public void setUpView() {
        mApiService = DI.getNeighbourApiService();
        mNeighbours = getIntent().getParcelableExtra(NeighbourFragment.KEY_NEIGHBOURS);//Get the neighbour detail from the Neighbour Fragment
        ButterKnife.bind(this);
    }

    public void setUpToolbar() {
        mCollapsingToolbar.setTitle(mNeighbours.getName());
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        mToolbar.setTitle(mNeighbours.getName());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //For back Btn
        mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFabBtn() {
        mFavorite_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnFavorite()) {
                    Snackbar.make(view, R.string.already_in_list, Snackbar.LENGTH_LONG).show();
                    deleteFromFavorite();
                } else {
                    addToFavorite();
                    Snackbar.make(view, R.string.add_to_favorite, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    public void addToFavorite() {
        //Add the neighbour in the favorite list
        mApiService.addFavoriteNeighbour(mNeighbours);
        mFavorite_fab.setImageResource(R.drawable.ic_star_yellow);
    }

    public boolean isOnFavorite() {
        //Check if the neighbour is already in the list
        return mApiService.getFavoriteNeighbour().contains(mNeighbours);
    }

    public void setFavoriteStatus() {
        //If the neighbour is in the list, the star stay full
        if (isOnFavorite()) {
            mFavorite_fab.setImageResource(R.drawable.ic_star_yellow);
        }
    }

    public void deleteFromFavorite() {
        //If the user click on the favorite btn again, the neighbour is removed from the lis
        isOnFavorite();
        mFavorite_fab.setImageResource(R.drawable.ic_star_border_24dp);
        mApiService.getFavoriteNeighbour().remove(mNeighbours);
    }

}
