package com.example.googlemapsdonor;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class SearchAddressMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Address address ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button btn =(Button) findViewById(R.id.ReturnAdd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String location [] = new String[2];
                location [0] = Double.toString(address.getLatitude());
                location [1] = Double.toString(address.getLongitude());
                Intent intent = new Intent();
                intent.putExtra("latitude",Double.toString(address.getLatitude()));
                intent.putExtra("longitude",Double.toString(address.getLongitude()));
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }
    public void onSearch(View view){
        EditText location_addr = (EditText) findViewById(R.id.address_field);
        String location = location_addr.getText().toString();
        List<Address> addressList = null;

        if(location != null || !location.equals("")){
            Geocoder geocoder = new Geocoder(this);
            try {
                 addressList = geocoder.getFromLocationName(location,1);


            } catch (IOException e) {
                e.printStackTrace();
            }
            address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Searched Location "));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            Log.i("Latitude",Double.toString(address.getLatitude()));
            Log.i("Longitude",Double.toString(address.getLongitude()));
        }
    }
//    public void onReturn(View view){


//    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng Delhi = new LatLng(28.6921151, 76.8104786);
        mMap.addMarker(new MarkerOptions().position(Delhi).title("Marker in Delhi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Delhi));
    }
}
