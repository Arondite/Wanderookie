package com.example.stephen.wanderookie;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    TextView errMessage;
    Button submitButton;
    EditText usernameText;
    EditText passwordText;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View loginView = inflater.inflate(R.layout.fragment_login, container, false);

        setupViews(loginView);

        return loginView;
    }

    private void setupViews(View v){
        Button guestLogIn = (Button) v.findViewById(R.id.guest_button);
        Button signUpButton = (Button) v.findViewById(R.id.sign_up_button);
        submitButton = (Button) v.findViewById(R.id.submit_log_in);
        errMessage = (TextView) v.findViewById(R.id.err_Message);
        usernameText = (EditText) v.findViewById(R.id.user_username);
        passwordText = (EditText) v.findViewById(R.id.user_password);

        if(signUpButton != null){
            signUpButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //startActivity(new Intent(LoginPage.this, SignUp.class));
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.changeToSignUp();
                }
            });
        }
        if(submitButton != null){
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity)getActivity();
                    if(!mainActivity.logIn(usernameText.getText().toString(), passwordText.getText().toString())){
                        Toast toast = Toast.makeText(getActivity(), "Username and Password do not match", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            });
        }
        if(guestLogIn != null){
            guestLogIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.logIn("guest", "guest");
                }
            });
        }

    }

}
