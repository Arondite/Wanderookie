package com.example.stephen.wanderookie;

/**
 * Created by zpanter on 2016-Jul-15.
 */

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PersonalProfileFragment extends Fragment {
    static TextView usernameDisplay;
    static TextView aboutDisplay;

    public PersonalProfileFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_personal_profile, container, false);

        setupViews(rootView);

        return rootView;
    }
    public void setupViews(View v){
        Button findHikes = (Button)v.findViewById(R.id.profile_findHike_button);
        Button bucketList = (Button)v.findViewById(R.id.profile_bucketList_button);
        Button goalButton = (Button)v.findViewById(R.id.profile_goals_button);
        Button completedButton = (Button)v.findViewById(R.id.profile_completed_button);
        aboutDisplay = (TextView) v.findViewById(R.id.aboutme_text);
        usernameDisplay = (TextView)v.findViewById(R.id.profile_username_text);

        if(findHikes != null){
            findHikes.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToListView();
                }
            });
        }
        if(goalButton != null){
            goalButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToGoalPage();
                }
            });
        }
        if(MainActivity.profileUsername != null && !(MainActivity.profileUsername.isEmpty())){
            usernameDisplay.setText(MainActivity.profileUsername.toString());
            aboutDisplay.setText(SignUpFragment.aboutMe);
        }

    }
}
