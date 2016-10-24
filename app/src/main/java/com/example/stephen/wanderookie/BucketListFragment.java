package com.example.stephen.wanderookie;

/**
 * Created by zpanter on 2016-Jul-15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

public class BucketListFragment extends Fragment {
    TextView errMessage;
    ImageButton profileButton;
    Button menuButton;
    ListAdapter bucketList;
    Button advancedBucketSearchButton;

    // NEED TO INSTANTIATE A LIST ADAPTER TO INTERFACE WITH THE DATABASE HELPER
    // TO POPULATE THE LIST

    public BucketListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View bucketlistView = inflater.inflate(R.layout.fragment_bucket_list, container, false);

        setupViews(bucketlistView);

        return bucketlistView;
    }

    private void setupViews(View v){
        profileButton = (ImageButton) v.findViewById(R.id.bucket_profile_button);
        menuButton = (Button) v.findViewById(R.id.menu_button);
        advancedBucketSearchButton = (Button) v.findViewById(R.id.bucket_advanced_search);

        if(profileButton != null && MainActivity.userLoggedIn){
            profileButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToProfileView();
                }
            });
        }

        if(menuButton != null){
            menuButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToMenuView();
                }
            });
        }


        if(advancedBucketSearchButton != null){
            advancedBucketSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToAdvancedSearchView();
                }
            });
        }

    }
    public void showMessage(String err){
        errMessage.setText(err);
    }
}
