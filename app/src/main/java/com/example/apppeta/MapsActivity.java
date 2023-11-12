package com.example.apppeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.apppeta.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private static final int Request_CODE=101;
    private Boolean oke = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (oke) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                }

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        oke = true;
        setMapOnLongClick(googleMap);
        setPoiClick(googleMap);

        // Add a marker in indonesia  and move the camera
        LatLng Hotel1 = new LatLng(-7.758264112087919, 110.38118554870907);
        // Mengubah ukuran gambar
        Bitmap resizedBitmap = resizeBitmap(R.drawable.img, 70, 70); // Ganti ukuran sesuai kebutuhan
        // Menambahkan marker dengan gambar yang telah diubah ukurannya
        mMap.addMarker(new MarkerOptions()
                .position(Hotel1)
                .title("Hotel UTTARA")
                .icon(BitmapDescriptorFactory.fromBitmap(resizedBitmap)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Hotel1, 6));


        LatLng GSPUGM  = new LatLng( -7.771242483465723, 110.37768637717882);
        mMap.addMarker(new MarkerOptions().position(GSPUGM).title("Gedung GSP UGM"));


        LatLng TITIK0 = new LatLng(-7.773686053144239, 110.36826872656304);
        mMap.addMarker(new MarkerOptions().position(TITIK0).title("Hotel Tentrem Yogyakarta"));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.normal_map){
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (item.getItemId()== R.id.satellite_map) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        return true;
    }

    private void setMapOnLongClick(final GoogleMap googleMap) {
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng));

                String text = String.format(Locale.getDefault(),
                        "Lat:%1$.5f Long :%2$.5f",
                        latLng.latitude,
                        latLng.longitude)
                        ;
                googleMap.addMarker(new MarkerOptions().position(latLng)
                        .title("Drop Pin")
                        .snippet(text));
            }
        });
    }

    private void setPoiClicik(final GoogleMap map){
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(@NonNull PointOfInterest pointOfInterest) {
                Marker poiMarker = mMap.addMarker(new MarkerOptions().position(pointOfInterest.latLng).title(pointOfInterest.name));
                poiMarker.showInfoWindow();
            }
});
    }

    private void setPoiClick (final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(@NonNull PointOfInterest pointOfInterest) {
                Marker poiMarker = mMap.addMarker(new MarkerOptions().position(pointOfInterest.latLng).title(pointOfInterest.name));

                // Mengatur ikon marker POI menjadi ikon bawaan Google Maps
                BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                poiMarker.setIcon(icon);

                poiMarker.showInfoWindow();
            }
        });
    }

    private BitmapDescriptor bitmapDescriptor(Context context,int vectoreResId){
        Drawable vectorDrawabel = ContextCompat.getDrawable(context,vectoreResId);
        vectorDrawabel.setBounds(0,0,vectorDrawabel.getIntrinsicWidth(),vectorDrawabel.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawabel.getIntrinsicWidth(),vectorDrawabel.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);


        Canvas canvas=new Canvas(bitmap);
        vectorDrawabel.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private Bitmap resizeBitmap(int imageResource, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), imageResource);
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }
}