package com.example.signin;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.signin.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;


public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    MarkerOptions marker;
    Vector<MarkerOptions> markerOptions;

    LatLng shahAlam;

    private String URL = "http://192.168.0.15/location/location.php";

    RequestQueue requestQueue;
    Gson gson;
    location[] locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gson = new GsonBuilder().create();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        markerOptions = new Vector<>();
        markerOptions.add(new MarkerOptions().title("You are here")
                .position(new LatLng(3.0697, 101.5037))
                .snippet("UiTM Shah Alam")
        );
        markerOptions.add(new MarkerOptions().title("SALAM Shah Alam Specialist Hospital")
                .position(new LatLng(3.052190, 101.534798))
                .snippet("Phone : 03-55434000")
        );

        markerOptions.add(new MarkerOptions().title("Klinik Pakar Hospital Shah Alam")
                .position(new LatLng(3.073680, 101.496680))
                .snippet("Phone : 0355263000")
        );

        markerOptions.add(new MarkerOptions().title("AVISENA Specialist Hospital")
                .position(new LatLng(3.080840, 101.509600
                ))
                .snippet("Phone : 03-55151888")
        );

        markerOptions.add(new MarkerOptions().title("KPJ Selangor Specialist Hospital")
                .position(new LatLng(3.053120, 101.537630))
                .snippet("Phone: 03-55431111")
        );

        markerOptions.add(new MarkerOptions().title("Pusat Kesihatan, UiTM Shah Alam")
                .position(new LatLng(3.065170, 101.495070))
                .snippet("Phone: 03-55443833")
        );

        markerOptions.add(new MarkerOptions().title("Columbia Asia Extended Care Hospital")
                .position(new LatLng(3.047550, 101.504750))
                .snippet("Phone: 03-55417833")
        );

        markerOptions.add(new MarkerOptions().title("MSU Medical Centre")
                .position(new LatLng(3.076480, 101.550970))
                .snippet("Phone: 03-55262600")
        );

        markerOptions.add(new MarkerOptions().title("Hospital Umra")
                .position(new LatLng(3.082250, 101.539680))
                .snippet("Phone: 03-55184319")
        );

        markerOptions.add(new MarkerOptions().title("Columbia Asia Hospital - Bukit Rimau")
                .position(new LatLng(2.999570, 101.529457))
                .snippet("Phone: 03-51259999")
        );

        markerOptions.add(new MarkerOptions().title("Ara Damansara Medical Centre")
                .position(new LatLng(3.114940, 101.565080))
                .snippet("Phone: 03-56391212")
        );

        markerOptions.add(new MarkerOptions().title("Hania Clinic Medical & Surgery")
                .position(new LatLng(3.076250, 101.550460))
                .snippet("Phone: 019-2165921")
        );

        markerOptions.add(new MarkerOptions().title("Klinik Dr Bazilah Shah Alam (Klinik Kulit / Skin Clinic)")
                .position(new LatLng(3.089980, 101.544800))
                .snippet("Phone: 03-50357359")
        );


        shahAlam = new LatLng(3.0697,101.5037);
        marker = new MarkerOptions().position(shahAlam).title("UiTM Shah Alam").snippet("You are here");


    }

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


        for (MarkerOptions mark : markerOptions){
            mMap.addMarker(mark);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shahAlam,12));
        enableMyLocation();
        sendRequest();

    }

    private void enableMyLocation() {

        String perms[] = {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_NETWORK_STATE"};
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                Log.d("hafizxx","permission granted");
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission

            Log.d("hafizxx","permission denied");
            ActivityCompat.requestPermissions(this,perms ,200);

        }
    }

    public void sendRequest() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,onSuccess,onError);
        requestQueue.add(stringRequest);
    }
    public Response.Listener<String> onSuccess = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            locations = gson.fromJson(response, location[].class);

            Log.d("Location", "List of Location :" + locations.length);

            if (locations.length <1){
                Toast.makeText(getApplicationContext(), "Problem retrieving JSON data",Toast.LENGTH_LONG).show();
                return;
            }

            for (location info: locations) {
                Double lat = Double.parseDouble(info.lat);
                Double lng = Double.parseDouble(info.lng);
                String title = info.name;
                String snippet = info.address;

                MarkerOptions marker = new MarkerOptions().position(new LatLng(lat,lng))
                        .title(title)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

                mMap.addMarker(marker);
            }

        }
    };
    public Response.ErrorListener onError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
}
