package com.example.mapwithmarker;
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener {

    //added
    public static final String MMA = "MapsMarkerActivity";
    private GoogleApiClient gac;
    private Location location;
    private TextView locationTV;
    private GoogleMap googleMap;
    private final static int REQUEST_CODE = 100;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    FusedLocationProviderClient flpa;

    //end added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //added
        gac = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        flpa = LocationServices.getFusedLocationProviderClient(this);
    }
    //end added

//    //added
//    public void onLocationChanged(Location location){
//        float accuracy = location.getAccuracy();
//        long nanos = location.getElapsedRealtimeNanos();
//        Log.w("M)
//    }
    //edn added

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.w(MMA, "connection suspended");
        //getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(MMA, "connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(MMA, "connection failed");
//        if (connectionResult.hasResolution()) {
//            try {
//                connectionResult.startResolutionForResult(this, REQUEST_CODE);
//            } catch (IntentSender.SendIntentException sie) {
//                Toast.makeText(this, "Google Play services problem, exiting", Toast.LENGTH_LONG).show();
//                finish();
//            }
//        }
    }

//    public void OnActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE && requestCode == RESULT_OK) {
//            gac.connect();
//        }
//    }

    protected void onStart() {
        super.onStart();
        if (gac != null)
            gac.connect();
    }
    //end added

    //to get current location and put a marker on it
    //https://gist.github.com/enginebai/adcae1f17d3b2114590c

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */

    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        LatLng myHouse = new LatLng(41.265539, -76.950677);
        LatLng pennCollege = new LatLng(41.234716, -77.021041);
        LatLng fountain = new LatLng(41.234350,-77.026114);
        googleMap.addMarker(new MarkerOptions().position(pennCollege)
                .title("Marker in PCT, Willaimsport, PA"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pennCollege));
        googleMap.addMarker(new MarkerOptions().position(myHouse)
                .title("Marker in at house, Montoursville, PA"));
        googleMap.addMarker(new MarkerOptions().position(fountain)
                .title("Marker in at Fountain, Williamsport, PA"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myHouse));
        googleMap.setOnMyLocationButtonClickListener(this);
        //added
        float zoomLevel = 16.0f;
        enableMyLocation(googleMap);
        //LatLng myLoc = new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pennCollege, zoomLevel));
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setOnMarkerClickListener(this);
        //googleMap.

        // end added
    }

    private void enableMyLocation(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (googleMap != null) {
            Log.d("Permission Status:", "enableMyLocation: Success");
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("LOCATION", marker.getPosition().toString());
        try{
            Task<Location> locationTask = flpa.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.d("ON SUCCESS" , Double.toString(location.getLatitude()));
                }
            });
            locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Log.d("RESULT", task.getResult().toString());
                        TravelManager.setCurrent(task.getResult());
                    }
                }
            });
        }catch (SecurityException se){
            Log.d("Security Exception", se.getMessage());
        }
        Location loc = new Location("marker");
        loc.setLatitude(marker.getPosition().latitude);
        loc.setLongitude(marker.getPosition().longitude);
        StringBuilder str = new StringBuilder();
        String[] temp = TravelManager.milesToDestination(loc);
        str.append(temp[0]);
        str.append('\n');
        str.append(temp[1]);
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
        return false;
    }
}
