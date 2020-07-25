package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;


public class NeighbourDetailActivity extends AppCompatActivity {
    private Neighbour mNeighbours;
    private ImageView avatarView;
    private NeighbourApiService mApiService;
    private TextView nameView, addressView, phoneView, networkView, descriptionView;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private FloatingActionButton mFavorite_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbour_detail);
        setUpView();
        setUpToolbar();
        initNeighbourDetail();
        setFabBtn();
        //TODO récupérer le statut du voisin pour afficher l'étoile pleine ou vide (setFavoriteStatus)

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
        mNeighbours = getIntent().getParcelableExtra(NeighbourFragment.KEY_NEIGHBOURS);
        mFavorite_fab = findViewById(R.id.fab_favorite);
        mToolbar = findViewById(R.id.toolbar);
        mCollapsingToolbar = findViewById(R.id.collapsingToolbar);
        avatarView = findViewById(R.id.toolbarImage);
        nameView = findViewById(R.id.textView_subTitle_name);
        addressView = findViewById(R.id.textView_detail_address);
        phoneView = findViewById(R.id.textView_phoneNumber);
        networkView = findViewById(R.id.textView_website);
        descriptionView = findViewById(R.id.textView_aboutMe_detail);
    }

    public void setUpToolbar() {
        mCollapsingToolbar.setTitle(mNeighbours.getName());
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.white));
        mToolbar.setTitle(mNeighbours.getName());
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setFabBtn() {
        mFavorite_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnFavorite()) {
                    Toast.makeText(NeighbourDetailActivity.this,
                            "Neighbour already in the list", Toast.LENGTH_LONG).show();
                    //TODO ajouter une méthode DeleteFromFavorite
                    //TODO mettre tous les strings qu'on afffiche à l'utilisateur dans le fichier string.xml
                } else {
                    addToFavorite();
                    Snackbar.make(view, "Add on favorite", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


    }

    public boolean isOnFavorite(){
        return mApiService.favoriteNeighbour().contains(mNeighbours);
    }

    public void addToFavorite(){
        mApiService.addFavoriteNeighbour(mNeighbours);
        mFavorite_fab.setImageResource(R.drawable.ic_star_yellow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
