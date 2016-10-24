package com.example.stephen.wanderookie;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    static Button logInButton;
    static String Username;
    static Button bucketListButton;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_menu, container, false);

        setupViews(rootView);

        return rootView;
    }

    private void setupViews(View v){
        logInButton = (Button) v.findViewById(R.id.log_in_button);
        Button findHikeButton = (Button) v.findViewById(R.id.find_hike_button);
        bucketListButton = (Button) v.findViewById(R.id.bucket_list_button);
        Button setGoalButton = (Button) v.findViewById(R.id.set_goal_button);

        if (logInButton != null) {
            logInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //startActivity(new Intent(Menu_Screen.this, LoginPage.class));
                    MainActivity ma = (MainActivity)getActivity();
                    ma.changeToLogin();
                }
            });
        }
        if(findHikeButton != null){
            findHikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToListView();
                }
            });
        }
        if(bucketListButton != null){
            bucketListButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToBucketList();
                }
            });
        }
        if(setGoalButton != null){
            setGoalButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    MainActivity mainActivity = (MainActivity)getActivity();
                    if(MainActivity.userLoggedIn){
                        mainActivity.changeToSetGoalPage();
                    }else {
                        Toast toast = Toast.makeText(getActivity(), "User must be logged in", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });
        }

        if(Username != null && !Username.isEmpty()){
            logInButton.setText(Username);
            logInButton.setClickable(false);
        }
    }
    public void setLoginButtonText(String username) {
        Username = username;
    }
}
