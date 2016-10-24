package com.example.stephen.wanderookie;


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
public class FindHikeListViewFragment extends Fragment {


    public FindHikeListViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_find_hike_list_view, container, false);

        setupViews(view);

        return view;
    }

    protected void setupViews(View v){
        Button mapViewButton = (Button) v.findViewById(R.id.map_view_button);
        Button advancedSearchButton = (Button) v.findViewById(R.id.advanced_search_list_view_button);
        ImageButton findHikeProfileBuutton = (ImageButton) v.findViewById(R.id.find_hike_profile_button);
        Button findHikeMenuButton = (Button) v.findViewById(R.id.find_hike_menu);

        if(mapViewButton != null){
            mapViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToMapView();
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
        if(findHikeProfileBuutton != null){
            findHikeProfileBuutton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToProfileView();
                }
            });
        }
        if(findHikeMenuButton != null){
            findHikeMenuButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToMenuView();
                }
            });
        }

    }

}
