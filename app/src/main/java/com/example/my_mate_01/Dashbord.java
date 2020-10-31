package com.example.my_mate_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class
Dashbord extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);

        //Title and action bar
        actionbar = getSupportActionBar();
        actionbar.setTitle("Home");

        firebaseAuth = FirebaseAuth.getInstance();

        //bottom navigation
        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(mSelectedListener);

        //home fragment (set default on start)
        actionbar.setTitle("Home");
        Home_Fragment fragment1 = new Home_Fragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1, "");
        ft1.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //handle fragments clicks
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            //home fragment
                            actionbar.setTitle("Home");
                            Home_Fragment fragment1 = new Home_Fragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content, fragment1, "");
                            ft1.commit();
                            return true;
                        case R.id.nav_search:
                            //Search fragment
                            actionbar.setTitle("Search");
                            Search_Fragment fragment2 = new Search_Fragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content, fragment2, "");
                            ft2.commit();
                            return true;
                        case R.id.nav_account:
                            //Account fragment
                            actionbar.setTitle("Account");
                            Account_Fragment fragment3 = new Account_Fragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content, fragment3, "");
                            ft3.commit();
                            return true;
                    }

                    return false;
                }
            };

    private void userStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            //user is already in the account(signed in)

        }
        else {
            //if user not signed in, redirect to the login
            startActivity(new Intent(Dashbord.this, Login_Form.class));

            finish();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        //checking on start of the app
        userStatus();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //handle menu items clicks

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get items id
        int id = item.getItemId();
        if (id ==R.id.action_logout){
            firebaseAuth.signOut();
            userStatus();
        }
        return super.onOptionsItemSelected(item);
    }
}
