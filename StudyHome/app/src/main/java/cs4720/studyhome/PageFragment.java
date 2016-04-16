package cs4720.studyhome;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends android.support.v4.app.Fragment {
    TextView textview;

    public PageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.page_layout, container, false);
        //textview = (TextView) view.findViewById(R.id.textview);
        Bundle bundle = getArguments();
        String page = Integer.toString(bundle.getInt("count"));
        //textview.setText("This is the message: "+message+ "Swipe view page");
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.layout);
        if (page.equals("1")) {
            final TextView view2 = new TextView(getContext());//(TextView) view.findViewById(R.id.weather);
            view2.setTextColor(Color.parseColor("#00CCFF"));
            view2.setGravity(Gravity.CENTER);
            view2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
            final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            //params.addRule(RelativeLayout.)
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = "http://api.openweathermap.org/data/2.5/weather?q=Sacramento%2CUS&APPID=b9b4115aed280f077d476837f748b4a4";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            //view.setText("Response is: "+ response);


                            JSONObject jsonobject;
                            JSONObject jsonweather;
                            try {
                                jsonobject = new JSONObject(response);
                                String weatherData = jsonobject.getString("weather");
                                weatherData = weatherData.replace("[", "");
                                weatherData = weatherData.replace("]", "");
                                jsonweather = new JSONObject(weatherData);
                                view2.setText(jsonweather.getString("main"));

                            } catch (JSONException e) {
                                view2.setText(response);
                                e.printStackTrace();
                            }

                            //.substring(0,50));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    view2.setText("That didn't work!");
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
            relativeLayout.addView(view2, params);
        }
        return view;
    }

}
