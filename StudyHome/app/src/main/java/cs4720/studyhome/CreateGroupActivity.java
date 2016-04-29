package cs4720.studyhome;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Permission;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class CreateGroupActivity extends Activity  implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener{

    Button btnShowLocation;
    GPSTracker gps;
    TextView latitudeText;
    TextView longitudeText;
    LocationManager locationmanager;
    private LocationRequest locationRequest;
    private Double myLatitude;
    private Double myLongitude;
    private FusedLocationProviderApi locationProvider=LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;

    public static final String PREFS_NAME = "PrefsFile";

    String [] PermissionsLocation =  { Manifest.permission.ACCESS_COARSE_LOCATION,
           Manifest.permission.ACCESS_FINE_LOCATION};

    final int RequestLocationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String editTextValue = settings.getString("editTextValue", "none");

        EditText editText = (EditText)findViewById(R.id.editText);
        editText.setText(editTextValue);
        latitudeText=(TextView) findViewById(R.id.latitude);
        longitudeText=(TextView) findViewById(R.id.longitude);

        googleApiClient=new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        locationRequest=new LocationRequest();
        locationRequest.setInterval(60 * 1000); //1min=60000ms
        locationRequest.setFastestInterval(15 * 1000);//15s
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        // Restore file
        String FILENAME = "hello_file";
        EditText editText2 = (EditText)findViewById(R.id.editText2);
        final EditText location = (EditText) findViewById(R.id.location);
        try {
            FileInputStream fis = openFileInput(FILENAME);
            StringBuilder builder = new StringBuilder();
            int ch;
            while((ch = fis.read()) != -1){
                builder.append((char)ch);
            }
            editText2.setText(builder.toString());
            fis.close();
        }catch(Exception e) {
            Log.e("StorageExample", e.getMessage());
        }

        btnShowLocation = (Button) findViewById(R.id.show_location);
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.e("clicking", "clicking");

                gps = new GPSTracker(CreateGroupActivity.this);

                if (gps.canGetLocation()) {
                    double longitude = gps.getLongitude();
                    double latitude = gps.getLatitude();
                    EditText addres = (EditText) findViewById(R.id.location);
                    Location location = new Location("newLoc");
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    onLocationChanged(location);
                    Geocoder geocoder;
                    List<Address> addresses; {
                    };
                    geocoder = new Geocoder(CreateGroupActivity.this, Locale.getDefault());
                    try {
                      //Toast.makeText(getApplicationContext(), "In here" +latitude, Toast.LENGTH_LONG).show();
                        addresses = geocoder.getFromLocation(myLatitude, myLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                        Toast.makeText(getApplicationContext(), "Your location is -\nCity: " + city +
                               "\nState: " + state, Toast.LENGTH_LONG).show();

                      addres.setText(address + "\n" + city + ", " + state);
                      //  System.out.println(city);

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        // Using Preferences
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        EditText editText = (EditText)findViewById(R.id.editText);
        editor.putString("editTextValue", editText.getText().toString());

        // Commit the edits!
        editor.commit();

        // Using a file
        String FILENAME = "hello_file";
        EditText editText2 = (EditText)findViewById(R.id.editText2);
        String string = editText2.getText().toString();
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(string.getBytes());
            fos.close();
        }catch(Exception e) {
            Log.e("StorageExample", e.getMessage());
        }
    }

    public void saveToDB(View view) {
        // Gets the data repository in write mode
        DatabaseHelper mDbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        EditText editText = (EditText)findViewById(R.id.editText);
        EditText editText2 = (EditText)findViewById(R.id.editText2);
        EditText editText3 = (EditText) findViewById(R.id.location);

        String compid = editText.getText().toString();
        String name = editText2.getText().toString();
        String location = editText3.getText().toString();

        values.put("compid", compid);
        values.put("name", name);
        values.put("location", location);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                "my_table",
                null,
                values);

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "compid",
                "name",
                "location"
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                "compid" + " DESC";

        Cursor cursor = db.query(
                "my_table",  // The table to query
                projection,  // The columns to return
                null,        // The columns for the WHERE clause
                null,        // The values for the WHERE clause
                null,        // don't group the rows
                null,        // don't filter by row groups
                sortOrder    // The sort order
        );

        //cursor.moveToFirst();
        while(cursor.moveToNext()) {
            String currID = cursor.getString(
                    cursor.getColumnIndexOrThrow("compid")
            );
            Log.i("DBData", currID);
        }

        googleApiClient.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        myLatitude=location.getLatitude();
        myLongitude=location.getLongitude();
        //set the textViews
        latitudeText.setText("Latitude :" + String.valueOf(myLatitude));
        longitudeText.setText("Longitude :" + String.valueOf(myLongitude));


    }

    @Override
    public void onConnected(Bundle bundle) {
        requestLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();//gets out client connected
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()){
            requestLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,
                (com.google.android.gms.location.LocationListener) this);
    }


    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,
                (com.google.android.gms.location.LocationListener) this);
    }

}