package cs4720.studyhome;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Owner on 4/4/2016.
 */
public class Asyncer extends AsyncTask<String, Void, String> {
    private final HomeActivity WeatherActivity;

// this constructor takes the activity as the parameter.
// that way we can use the activity later to populate the weather value fields
// on the screen


    public Asyncer(HomeActivity weatherActivity) {
        this.WeatherActivity = weatherActivity;
    }
    @Override
    protected String doInBackground(String... strings) {
        String response = "";
        try {
            URL url;

            url = new URL("http://jsonplaceholder.typicode.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            connection.setRequestProperty("api_key", "b9b4115aed280f077d476837f748b4a4");
            Log.e("Status", "Connection set BALALALALA");
            Log.e("URL is: ", url.toString());

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            Log.e("Status", "Reader set");
            //Log.e("HELLO!", "HI!");
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";

            while ((tmp = reader.readLine()) != null) {
                response += tmp;
            }
            reader.close();
            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not successful
            if (data.getInt("cod") != 200) {
                return null;
            }

        } catch (Exception e) {
            //Log.i("ERROR!", e.getMessage());
            return null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        String test = result;
        Log.e("The test value is", test);
        try {
// parse the json result returned from the service
            JSONObject jsonResult;
            Log.e("Is JSON null?", "We don't know!");
            jsonResult = new JSONObject(test);

// parse out the temperature from the JSON result
            double temperature = jsonResult.getJSONObject("main").getDouble("temp");
            temperature = ConvertTemperatureToFarenheit(temperature);

            // parse out the pressure from the JSON Result
            double pressure = jsonResult.getJSONObject("main").getDouble("pressure");

// parse out the humidity from the JSON result
            double humidity = jsonResult.getJSONObject("main").getDouble("humidity");

// parse out the description from the JSON result
            String description = jsonResult.getJSONArray("weather").getJSONObject(0).getString("description");

// set all the fields in the activity from the parsed JSON
            this.WeatherActivity.SetDescription(description);
            this.WeatherActivity.SetTemperature(temperature);
            this.WeatherActivity.SetPressure(pressure);
            this.WeatherActivity.SetHumidity(humidity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private double ConvertTemperatureToFarenheit(double temperature) {
        return (temperature - 273) * (9/5) + 32;
    }
}

