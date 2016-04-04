package cs4720.studyhome;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ViewGroup weatherwidget = (ViewGroup) getLayoutInflater().inflate(R.layout.weather_widget, null);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        JSONObject jsonobject = getJSON();
        
        final Intent i = new Intent(this, AppListActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(i);
            }
        });
    }
    public static JSONObject getJSON(){
        try{
            URL url;

            url = new URL("http://api.openweathermap.org/data/2.5/weather?q=Sacramento%2CUS&APPID=b9b4115aed280f077d476837f748b4a4");
            URLConnection connection = (URLConnection)url.openConnection();
            connection.addRequestProperty("x-api-key", "11111");
            Log.e("Status", "Connection set");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Log.e("Status", "Reader set");

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null){
                json.append(tmp).append("\n");
            }
            reader.close();
            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not successful
            if(data.getInt("cod") != 200){
                return null;
            }
            return data;
        }
        catch(Exception e){
            return null;
        }
    }
    public void showApps(View v){

    }
}
