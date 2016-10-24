package com.example.stephen.wanderookie;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Stephen on 7/2/2016.
 */
public class MainActivity extends AppCompatActivity {
    static boolean userLoggedIn;
    static String profileUsername;
    static int goalSetting;
    static boolean goalSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        goalSet = false;

        getFragmentManager().beginTransaction()
                .add(R.id.MainContainer, new MenuFragment(), "Menu")
                .commit();
    }

    public void setUserLoggedIn(){
        userLoggedIn = true;
    }

    public void changeToLogin(){
        getFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new LoginFragment())
                .addToBackStack(null).commit();
    }
    public void changeToSignUp(){
        getFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new SignUpFragment())
                .addToBackStack(null).commit();
    }
    public void changeToMapView(){
        getFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new MapViewFragment())
                .addToBackStack(null).commit();
    }
    public void changeToListView(){
        getFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new FindHikeListViewFragment())
                .addToBackStack(null).commit();
    }
    public void changeToProfileView(){
        getFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new PersonalProfileFragment())
                .addToBackStack(null).commit();
    }
    public void changeToMenuView(){
        getFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new MenuFragment())
                .addToBackStack(null).commit();
    }
    public void changeToAdvancedSearchView(){
        getFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new AdvancedSearchFragment())
                .addToBackStack(null).commit();
    }
    public void changeToBucketList(){
        getFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new BucketListFragment())
                .addToBackStack(null).commit();
    }
    public void changeToGoalPage(){
        getFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new GoalFragment())
                .addToBackStack(null).commit();
    }
    public void changeToSetGoalPage(){
        getFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new SetGoalFragment())
                .addToBackStack(null).commit();
    }

    public boolean logIn(String username, String password){
        DatabaseHelper dh = new DatabaseHelper(this.getBaseContext(),"USER_CREDENTIALS", null, 1);
        MenuFragment menuFragment = new MenuFragment();
        LoginFragment loginFragment = new LoginFragment();

        if(dh.UserExists(username)){
            UserCredentials uc = new UserCredentials(dh.testUser(username));
            if(uc.MatchPassword(password))
            {
                getFragmentManager().beginTransaction()
                        .replace(R.id.MainContainer, menuFragment)
                        .addToBackStack(null).commit();
                menuFragment.setLoginButtonText(username);
                setUserLoggedIn();
                profileUsername = username;
                //return true;
            }else{
                getFragmentManager().beginTransaction()
                        .replace(R.id.MainContainer, loginFragment)
                        .addToBackStack(null).commit();
                userLoggedIn = false;
                return false;
            }
        }else{
            getFragmentManager().beginTransaction()
                    .replace(R.id.MainContainer, loginFragment)
                    .addToBackStack(null).commit();
            userLoggedIn = false;
            return false;
        }
        return true;
    }
    public void signUpCustomer(String username, String email, String zip, String city, String state,
                               String password){
        DatabaseHelper dh = new DatabaseHelper(this.getBaseContext(),"USER_CREDENTIALS", null, 1);
        boolean validation =  dh.signUpInsert(username, email, zip, city, state, password);
        profileUsername = username;
    }
    public boolean checkUsername(String username){
        DatabaseHelper dh = new DatabaseHelper(this.getBaseContext(),"USER_CREDENTIALS", null, 1);
        if(dh.UserExists(username)){
            return true;
        }
        return false;
    }


}
