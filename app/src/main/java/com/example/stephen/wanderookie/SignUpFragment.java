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
public class SignUpFragment extends Fragment {

    String[] userInput = new String[7];
    static String aboutMe;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View signUpView =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        setupViews(signUpView);

        return signUpView;
    }

    private void setupViews(View v){
        Button submitButtonCheck = (Button) v.findViewById(R.id.submit_button);

        final EditText email = (EditText) v.findViewById(R.id.email_field);
        final EditText username = (EditText) v.findViewById(R.id.username_field);
        final EditText password = (EditText) v.findViewById(R.id.password_field);
        final EditText confirmPassword = (EditText) v.findViewById(R.id.confirm_password);
        final EditText city = (EditText) v.findViewById(R.id.city_field);
        final EditText zip = (EditText) v.findViewById(R.id.zip_field);
        final EditText state = (EditText) v.findViewById(R.id.state_field);
        final EditText about = (EditText) v.findViewById(R.id.aboutme_field);

        if(submitButtonCheck != null){
            submitButtonCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    String[] input = InitializeArray(email.getText().toString(), username.getText().toString(),
                            password.getText().toString(), confirmPassword.getText().toString(), city.getText().toString(),
                            zip.getText().toString(), state.getText().toString());
                    String[] aboutArr = new String[]{about.getText().toString()};
                    boolean validSignUp = true;

                    MainActivity mainActivity = (MainActivity)getActivity();
                    MenuFragment menuFragment = new MenuFragment();

                    if(!mainActivity.checkUsername(input[1])){
                        for(int i = 0; i < 7; i++){
                            if(input[i] == null){
                                validSignUp = false;
                            }
                        }
                        if(aboutArr[0] != null && !(about.getText().toString().isEmpty())){
                            aboutMe = about.getText().toString();
                        }
                        if(!(input[2].equals(input[3]))){
                            validSignUp = false;
                            Toast toast = Toast.makeText(getActivity(), "The confirmation password does not match the password.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        if(validSignUp){

                            mainActivity.signUpCustomer(input[1], input[0], input[5], input[4], input[6],input[2]);
                            menuFragment.setLoginButtonText(input[1]);
                            mainActivity.setUserLoggedIn();
                            mainActivity.changeToMenuView();
                        }else{
                            Toast toast = Toast.makeText(getActivity(), "Must enter all fields", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                }
            });
        }

    }

    private String[] InitializeArray(String email, String username, String password,
                                        String confirmPassword, String city, String zip, String state){
        userInput[0] = email;
        userInput[1] = username;
        userInput[2] = password;
        userInput[3] = confirmPassword;
        userInput[4] = city;
        userInput[5] = zip;
        userInput[6] = state;
        return userInput;
    }
    private String getUsername(String username){
        return username;
    }

}
