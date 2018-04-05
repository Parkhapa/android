package com.abdul.parkhapa;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    private DatabaseReference mDatabase;
    List<LatLng> mySlots;



    public ExploreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  rootView =  inflater.inflate(R.layout.fragment_explore, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference("parkings");

        mySlots = new ArrayList<>();
        mySlots.add( new LatLng(-1.3106919438892097, 36.81509971618652));
        mySlots.add( new LatLng(-1.3112496973323873, 36.814799308776855 ));
        mySlots.add( new LatLng( -1.3120005190788542 ,36.81394100189209 ));
        mySlots.add( new LatLng( -1.3132447374768275 ,36.817325949668884 ));
        mySlots.add( new LatLng( -1.3131589293313368 ,36.8163281679153449 ));
        mySlots.add( new LatLng( -1.3131482033129376,36.81606262922287 ));



        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                LatLng sydney = new LatLng(-1.2833300, 36.8166700);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(13).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                googleMap.clear();
                for ( DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Latilong slots = postSnapshot.getValue(Latilong.class);

                    assert slots != null;
                    googleMap.addMarker( new MarkerOptions()
                            .position( new LatLng(slots.latitude, slots.longitude))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.position)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        return rootView;

    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
