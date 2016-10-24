package com.example.stephen.wanderookie;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SetGoalFragment extends Fragment {


    public SetGoalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_set_goal, container, false);

        setupViews(rootView);

        return rootView;
    }

    public void setupViews(View v){
        Button submitSetGoal = (Button)v.findViewById(R.id.set_goal_submit);
        final EditText amountToBeSet = (EditText)v.findViewById(R.id.set_goal_miles);

        if(submitSetGoal != null){
            submitSetGoal.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if(!(MainActivity.goalSet)){
                        MainActivity.goalSetting = Integer.parseInt(amountToBeSet.getText().toString());
                        MainActivity.goalSet = true;
                        MainActivity mainActivity = (MainActivity)getActivity();
                        mainActivity.changeToMenuView();
                    }else{
                        Toast toast = Toast.makeText(getActivity(), "User has goal already set", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });
        }
    }
}
