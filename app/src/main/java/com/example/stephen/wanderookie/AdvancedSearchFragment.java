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
import android.widget.ListAdapter;
import android.widget.TextView;



public class AdvancedSearchFragment extends Fragment {
    TextView errMessage;
    Button profileButton;
    Button menuButton;
    Button listButton;
    Button mapButton;
    Button searchButton;
    ListAdapter difficultyList;
    //Distance entry
    //Elevation entry


    public AdvancedSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View advancedSearchView = inflater.inflate(R.layout.fragment_advanced_search, container, false);

        setupViews(advancedSearchView);

        return advancedSearchView;
    }

    private void setupViews(View v){
        profileButton = (Button) v.findViewById(R.id.advance_profile_button);
        menuButton = (Button) v.findViewById(R.id.menu_button);
        listButton = (Button) v.findViewById(R.id.find_hike_list_button);
        mapButton = (Button) v.findViewById(R.id.find_hike_map_button);
        searchButton = (Button) v.findViewById(R.id.perform_advanced_search_button);
        final EditText trailName = (EditText)v.findViewById(R.id.trail_name);
        final EditText trailLocale = (EditText)v.findViewById(R.id.trail_locale);
        final EditText trailCounty = (EditText)v.findViewById(R.id.trail_country);
        final EditText distance = (EditText)v.findViewById(R.id.distance);
        final EditText elevationGain = (EditText)v.findViewById(R.id.elevation_gain);

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


        if(listButton != null){
            listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToListView();
                }
            });
        }


        if(mapButton != null){
            mapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToMapView();
                }
            });
        }


        if(searchButton != null){
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] input = new String[5];
                    String name = assignParams(trailName.getText().toString());
                    String locale = assignParams(trailLocale.getText().toString());
                    String trailDistance = assignParams(distance.getText().toString());
                    String county = assignParams(trailCounty.getText().toString());
                    String elevation = assignParams(elevationGain.getText().toString());
                    input = insertSearchParams(name, locale, county, trailDistance, elevation);
//                    MainActivity mainActivity = (MainActivity)getActivity();
//
//                    // Need to implement a list parameter to pass into the search results view
//                    mainActivity.changeToSearchResultsView();
                }
            });
        }

    }
    public void showMessage(String err){
        errMessage.setText(err);
    }
    public String assignParams(String checkedParam){
        String tempString;
        if(checkedParam != null && !(checkedParam.isEmpty())){
            tempString = checkedParam;
        }else{
            tempString = "";
        }
        return  tempString;
    }
    public String[] insertSearchParams(String name, String locale, String county, String distance,
                                        String elevation){
        String[] output = new String[5];
        output[0] = name;
        output[1] = locale;
        output[2] = county;
        output[3] = distance;
        output[4] = elevation;

        return output;
    }
}