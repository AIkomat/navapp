package com.studentproject.navigationapp;

import static android.provider.CalendarContract.CalendarCache.URI;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class SettingsActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    TextView email, name;
    ImageView userIcon;
    Button signOutButton, startNavigation;
    RadioButton currentLocation, specifiedLocation;
    EditText providedLocation, destination;
    private final int FINE_PERMISSION_CODE = 1;
    FusedLocationProviderClient fusedLocationProviderClient;
    Intent intent, intentCall;
    HashMap<String, String> routeMap = new HashMap<>();
    FloatingActionButton callButton, call997, call999, call112, call998;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        intent = new Intent(SettingsActivity.this, MapActivity.class);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        callButton = findViewById(R.id.floatingCallButton);
        call112 = findViewById(R.id.floating112Button);
        call997 = findViewById(R.id.floating997Button);
        call998 = findViewById(R.id.floating998Button);
        call999 = findViewById(R.id.floating999Button);

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Geocoder geocoder = new Geocoder(SettingsActivity.this, Locale.getDefault());

        name = findViewById(R.id.tv_name);
        email = findViewById(R.id.tv_email);
        signOutButton = findViewById(R.id.btn_logout);
        userIcon = findViewById(R.id.iv_user_icon);
        currentLocation = findViewById(R.id.radioButton);
        specifiedLocation = findViewById(R.id.radioButton2);
        providedLocation = findViewById(R.id.provided_location);
        startNavigation = findViewById(R.id.start_navigation_button);
        destination = findViewById(R.id.destination_text);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            String displayName = account.getDisplayName();
            String emailAddress = account.getEmail();
            Uri photo = account.getPhotoUrl();

            name.setText("Welcome " + displayName);
            email.setText(emailAddress);
            if (photo != null) {
                Picasso.get().load(photo).into(userIcon);
            }
        }

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call112.getVisibility() == View.GONE) {
                    call112.setVisibility(View.VISIBLE);
                    call997.setVisibility(View.VISIBLE);
                    call998.setVisibility(View.VISIBLE);
                    call999.setVisibility(View.VISIBLE);
                } else {
                    call112.setVisibility(View.GONE);
                    call997.setVisibility(View.GONE);
                    call998.setVisibility(View.GONE);
                    call999.setVisibility(View.GONE);
                }
            }
        });

        call112.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(URI.parse("tel:" + "112"));
                startActivity(intentCall);
            }
        });

        call997.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(URI.parse("tel:" + "997"));
                startActivity(intentCall);
            }
        });

        call998.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(URI.parse("tel:" + "998"));
                startActivity(intentCall);
            }
        });

        call999.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentCall = new Intent(Intent.ACTION_DIAL);
                intentCall.setData(URI.parse("tel:" + "999"));
                startActivity(intentCall);
            }
        });

        specifiedLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                providedLocation.setVisibility(View.VISIBLE);
            }
        });

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
                providedLocation.setVisibility(View.INVISIBLE);
            }
        });

        startNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((destination.getText().length() < 3) || (!specifiedLocation.isChecked() && !currentLocation.isChecked())) {
                    Toast.makeText(SettingsActivity.this, "PLEASE PROVIDE LOCATION AND DESTINATION", Toast.LENGTH_LONG).show();
                    return;
                }


                if (specifiedLocation.isChecked()) {

                    String specifiedlocation = providedLocation.getText().toString();
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(specifiedlocation, 1);
                        if (addresses.size() > 0) {
                            Address address = addresses.get(0);
                            intent.putExtra("lat", address.getLatitude());
                            intent.putExtra("lon", address.getLongitude());
                            intent.putExtra("locationName", address.getLocality());
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                String destinationLocation = destination.getText().toString();
                try {
                    List<Address> addresses = geocoder.getFromLocationName(destinationLocation, 1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);

                        Random random = new Random();
                        double min = 0.00005;
                        double max = 0.007;
                        double offsetLat = min + (max - min) * random.nextDouble();
                        double offsetLon = min + (max - min) * random.nextDouble();

                        intent.putExtra("destinationLat", address.getLatitude());
                        intent.putExtra("destinationLon", address.getLongitude());
                        intent.putExtra("destinationName", destinationLocation);
                        intent.putExtra("riotLat", (address.getLatitude()+offsetLat));
                        intent.putExtra("riotLon", (address.getLongitude()+offsetLon));
                        intent.putExtra("carCrashLat", (address.getLatitude()-(offsetLat*0.4)));
                        intent.putExtra("carCrashLon", (address.getLongitude()-(offsetLon*0.5)));
                        routeMap.put("destination", address.getLatitude() + " " + address.getLongitude());
                        routeMap.put("email", email.getText().toString());
                        routeMap.put("location", address.getLatitude() + " " + address.getLongitude());
                        routeMap.put("riot location", (address.getLatitude() + offsetLat) + " " +(address.getLongitude() + offsetLon));
                        routeMap.put("car crash location", (address.getLatitude() - (offsetLat*0.4)) + " " +(address.getLongitude() - (offsetLon*0.5)));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                try {
                    List<Address> addresses = geocoder.getFromLocationName("Gdansk okręgowy szpital kolejowy", 1);
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);
                        intent.putExtra("hospitalLat", address.getLatitude());
                        intent.putExtra("hospitalLon", address.getLongitude());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference reference = db.getReference("routes");

                String key = reference.push().getKey();
                routeMap.put("key", key);

                Calendar calendar = Calendar.getInstance();
                String date = calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR) +" " +calendar.get(Calendar.HOUR_OF_DAY)+":"+ calendar.get(Calendar.MINUTE)+":"+calendar.get(Calendar.SECOND) ;
                routeMap.put("date", date);



                reference.child(key).setValue(routeMap);
                routeMap.clear();

                startActivity(intent);
            }

        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }


    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    intent.putExtra("lat", location.getLatitude());
                    intent.putExtra("lon", location.getLongitude());
                    intent.putExtra("locationName", "Your current location");
                    routeMap.put("location", location.getLatitude() + " " + location.getLongitude());
                }
            }
        });
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(SettingsActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "LOCATION PERMISSION DENIED", Toast.LENGTH_LONG).show();
            }
        }
    }
}