package cs4720.studyhome;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
//import com.android.volley.RequestQueue;
//import com.android.volley;

public class HomeActivity extends FragmentActivity {
    private double temperature, humidity, pressure;
    String description;
    ActionBar actionbar;
    ViewPager viewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ViewGroup weatherwidget = (ViewGroup) getLayoutInflater().inflate(R.layout.weather_widget, null);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        actionbar = getActionBar();
        viewpager = (ViewPager) findViewById(R.id.view_pager);
        SwipeAdapter swipeadapter = new SwipeAdapter(getSupportFragmentManager());
        viewpager.setAdapter(swipeadapter);

//        JSONObject jsonobject = getJSON();
//        try {
//            Log.i("JSON object", jsonobject.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //retrieveWeather();






        final Intent i = new Intent(this, Launchalot.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(i);
            }
        });
    }

    public void onClick(View v){
        if(v.getId() == R.id.create_button){
            //handle the click here
            Intent intent = new Intent(this, CreateGroupActivity.class);
            startActivity(intent);
        }
    }
    private void retrieveWeather()  {
//        try {
            String url = "http://api.openweathermap.org/data/2.5/weather?q=Sacramento%2CUS&APPID=b9b4115aed280f077d476837f748b4a4";
            Asyncer weatherservice = new Asyncer(this);
            weatherservice.execute(url);
//        }
//        catch (IOException e) {
//            Log.e("ERROR:", "IOERRROOOOOORRRR!!!!!!!!!!");
//        }
    }

    public static JSONObject getJSON() {
        return null;
    }
    public void SetTemperature(double temperature) {
        TextView view = (TextView) this.findViewById(R.id.weather);

        DecimalFormat df = new DecimalFormat("###.##");
        String formattedTemperature = df.format(temperature);

        view.setText(formattedTemperature + " *F");
    }
    public void SetPressure(double pressure) {
        this.pressure = pressure;
    }
    public void SetDescription(String description) {
        this.description = description;
    }
    public void SetHumidity(double humidity) {
        this.humidity = humidity;
    }

//    @Override
//    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//    }
//
//    @Override
//    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//    }
//
//    @Override
//    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//    }
}


