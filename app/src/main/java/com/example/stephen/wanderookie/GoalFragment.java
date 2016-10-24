package com.example.stephen.wanderookie;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoalFragment extends Fragment {


    public GoalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_goal, container, false);

        setupViews(rootview);

        return rootview;
    }

    public void setupViews(View v){
        ImageButton iconButton = (ImageButton) v.findViewById(R.id.goal_icon_button);
        Button menuButton = (Button)v.findViewById(R.id.goal_menu_button);
        Button submitButton = (Button)v.findViewById(R.id.goal_submit_button);
        final EditText goalUpdate = (EditText)v.findViewById(R.id.add_to_goal_number);
        final SeekBar seekBar = (SeekBar)v.findViewById(R.id.goal_seek_bar);
        TextView goalPresentation = (TextView) v.findViewById(R.id.goal_text_presentation);

        if(iconButton != null){
            iconButton.setOnClickListener(new View.OnClickListener(){
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
        if(submitButton != null){
            submitButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    try{
                        int tempUpdate = Integer.parseInt(goalUpdate.getText().toString());
                        goalUpdate.setText("");
                        int setProgress = 0;

                        if(seekBar.getProgress() + tempUpdate > seekBar.getMax()){
                            setProgress = (seekBar.getProgress() + tempUpdate) - seekBar.getMax();
                        }

                        seekBar.setProgress((seekBar.getProgress() + tempUpdate));

                        if(seekBar.getProgress() == seekBar.getMax()){
                            MainActivity.goalSet = false;
                            seekBar.setProgress(setProgress);
                        }
                    }
                    catch (Exception e){
                        Toast toast = Toast.makeText(getActivity(), "Not a valid input", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });
        }
        if(MainActivity.goalSetting != 0){
            seekBar.setMax(MainActivity.goalSetting);
            goalPresentation.setText(goalPresentation.getText().toString() + " " + MainActivity.goalSetting);
        }else{
            goalPresentation.setText(goalPresentation.getText().toString() + " 100");
        }
    }

}
