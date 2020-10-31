package com.example.my_mate_01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class Registration_Form extends AppCompatActivity implements View.OnClickListener {

    EditText txtEmail, txtPass, txtName;
    Button btnSignup;
    //ProgressDialog mProgressDialog;
    TextView txtVreg;
    RadioGroup radioGroupGender;
    RadioButton radioButtonGender;

    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    private FusedLocationProviderClient fusedLocationProviderClient;

    String string_city;
    String string_state;
    String string_country;
    String string_location;
    String stringLatitude;
    String stringLongitude;
    String stringLooking;

    LocationManager locationManager;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_form);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        /*//Title and action bar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Create User");
        //setting back button
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);*/


        txtEmail = findViewById(R.id.txtEmail);
        txtPass = findViewById(R.id.txtPass);
        btnSignup = findViewById(R.id.btnSignup);
        txtVreg = findViewById(R.id.txtVregistered);
        txtName = findViewById(R.id.txtName);
        radioGroupGender = findViewById(R.id.radioGenderGroup);


        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

//        mProgressDialog = new ProgressDialog(this);
//        mProgressDialog.setMessage("Registering User...");

        txtVreg.setOnClickListener(this);

        dialog = new ProgressDialog(Registration_Form.this, R.style.DialogColor);
        dialog.setMessage("Registering User...");
        dialog.setCancelable(false);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationPremissionCheck();
        GooglePlayServiceCheck();
        GPSLocationServiceCheck();

        //Sign up button handle
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                String email = txtEmail.getText().toString().trim();
                String pass = txtPass.getText().toString().trim();
                //Email validation check
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    txtEmail.setError("Invalid Email address");
                    txtEmail.setFocusable(true);
                } else if (pass.length() < 6) {
                    txtPass.setError("Password length should more than 6 characters");
                    txtPass.setFocusable(true);
                } else RegisterUser(email, pass);
            }
        });
    }

    private void RegisterUser(String email, String pass) {*/

                if (radioButtonGender != null) {
                    if (string_city != null && !string_city.equals("city"))   {

                        final String stringName = txtName.getText().toString();
                        final String stringEmail = txtEmail.getText().toString();
                        final String stringPassword = txtPass.getText().toString();
                        final String stringGender = radioButtonGender.getText().toString();


                        if (stringGender.equals("Male")) {
                            stringLooking = "Woman";
                        } else {
                            stringLooking = "Man";
                        }

                        if (!TextUtils.isEmpty(stringName) &&
                                !TextUtils.isEmpty(stringEmail) &&
                                !TextUtils.isEmpty(stringPassword) &&
                                !TextUtils.isEmpty(stringGender)) {

                            dialog.show();

                            mAuth.createUserWithEmailAndPassword(stringEmail, stringPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        String currentUser = firebaseUser.getUid();

                                        Map<String, Object> userProfile = new HashMap<>();
                                        userProfile.put("user_uid", currentUser);
                                        userProfile.put("user_email", stringEmail);
                                        /*userProfile.put("user_epass", stringPassword);*/
                                        userProfile.put("user_name", stringName);
                                        userProfile.put("user_gender", stringGender);

                                        userProfile.put("user_city", string_city);
                                        /*userProfile.put("user_state", string_state);
                                        userProfile.put("user_country", string_country);*/
                                        userProfile.put("user_location", string_location);

                                        userProfile.put("user_image", "image");

                                        userProfile.put("user_looking", stringLooking);
                                        userProfile.put("user_about", "Hi! This is my about.");
                                        userProfile.put("user_latitude", stringLatitude);
                                        userProfile.put("user_longitude", stringLongitude);

                                        firebaseFirestore.collection("users")
                                                .document(currentUser)
                                                .set(userProfile)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            Intent intent = new Intent(Registration_Form.this, Select_Interests.class);
                                                            startActivity(intent);
                                                            finish();
                                                            dialog.dismiss();
                                                        } else {
                                                            Toast.makeText(Registration_Form.this, "Something went wrong! Please try again!", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                });

                                    } else {

                                        Toast.makeText(Registration_Form.this, "Please check errors to proceed!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }

                            });


                        } else {

                            Toast.makeText(Registration_Form.this, "Please Fill in all the details to proceed!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    } else {
                        Toast.makeText(Registration_Form.this, "Please turn on Location service to continue.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                } else {
                    Toast.makeText(Registration_Form.this, "Please Fill in all the details to proceed!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

});

}



    public void radioButtonGender(View view) {
        int radioId = radioGroupGender.getCheckedRadioButtonId();
        radioButtonGender = findViewById(radioId);
    }



    private void LocationPremissionCheck() {
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        String rationale = "Please provide location permission so that you can ...";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Location Permission")
                .setSettingsDialogTitle("Warning");
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                LocationRequest();

            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                super.onDenied(context, deniedPermissions);
                LocationPremissionCheck();
            }
        });
    }

    private void LocationRetreive(Double locationLatitude, Double locationLongitude) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(locationLatitude, locationLongitude, 1);
            if (addresses != null && addresses.size() > 0) {
                string_city = addresses.get(0).getSubAdminArea();
                string_state = addresses.get(0).getAdminArea();
//                string_country = addresses.get(0).getCountryName();
                string_location = addresses.get(0).getAddressLine(0);


/*                if (string_country == null) {
                    if (string_state != null) {
                        string_country = string_state;
                    } else if (string_city != null) {
                        string_country = string_city;
                    } else {
                        string_country = "null";
                    }
                }*/
                if (string_city == null) {
                    if (string_state != null) {
                        string_city = string_state;
                    } else {
                        string_city = "null";
                    }
                }
/*                if (string_state == null) {
                    if (string_city != null) {
                        string_state = string_city;
                    } else {
                        string_state = string_country;
                    }
                }*/
                if (string_location == null) {
                    string_location = "Null";
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Registration_Form.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void LocationRequest() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PermissionChecker.PERMISSION_GRANTED) {


            fusedLocationProviderClient = new FusedLocationProviderClient(this);

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {


                    if (location != null) {

                        Double locationLatitude = location.getLatitude();
                        Double locationLongitude = location.getLongitude();

                        stringLatitude = locationLatitude.toString();
                        stringLongitude = locationLongitude.toString();

                        if (!stringLatitude.equals("0.0") && !stringLongitude.equals("0.0")) {

                            LocationRetreive(locationLatitude, locationLongitude);

                        } else {

                            Toast.makeText(Registration_Form.this,
                                    "Please turn on any GPS or location service and restart to use the app", Toast.LENGTH_SHORT).show();

                        }


                    } else {
                        Toast.makeText(Registration_Form.this,
                                "Please turn on any GPS or location service and restart to use the app", Toast.LENGTH_SHORT).show();
                    }

                }

            });


        } else {

            LocationPremissionCheck();

        }
    }

    public boolean GooglePlayServiceCheck() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    private void GPSLocationServiceCheck() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, enable it to use this app?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                            Intent intent = new Intent(Registration_Form.this, Login_Form.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


        GPSLocationServiceCheck();


    }


    @Override
    public void onClick(View v) {
        if(v == txtVreg){
            startActivity(new Intent(Registration_Form.this,Login_Form.class));
        }
    }
}
