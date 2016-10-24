package com.example.stephen.wanderookie;


import android.media.midi.MidiDevice;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapViewFragment extends Fragment {


    public MapViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_map_view, container, false);

        setupView(view);

        return view;
    }

    protected void setupView(View v){
        Button hikeListViewButton = (Button) v.findViewById(R.id.list_view_button);
        Button advancedSearchButton = (Button) v.findViewById(R.id.advanced_search_map_view_button);
        ImageButton mapViewProfileButton = (ImageButton) v.findViewById(R.id.map_view_profile_button);
        Button mapViewMenuButton = (Button)v.findViewById(R.id.map_view_menu_button);

        if(hikeListViewButton != null){
            hikeListViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToListView();
                }
            });
        }
        if(advancedSearchButton != null){
            advancedSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToAdvancedSearchView();
                }

            });
        }
        if(mapViewProfileButton != null){
            mapViewProfileButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToProfileView();
                }
            });
        }
        if(mapViewMenuButton != null){
            mapViewMenuButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToMenuView();
                }
            });
        }

    }

}
