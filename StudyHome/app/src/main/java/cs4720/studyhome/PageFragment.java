package cs4720.studyhome;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;


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
        int page = bundle.getInt("count");
        //textview.setText("This is the message: "+message+ "Swipe view page");
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.layout);
        //relativeLayout.setLeft(500);
        if (page == 1) {
            final TextView view2 = new TextView(getContext());//(TextView) view.findViewById(R.id.weather);
            view2.setTextColor(Color.parseColor("#00CCFF"));
            view2.setGravity(Gravity.CENTER);
            view2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);

            final TextView view3 = new TextView(getContext());
            view3.setTextColor(Color.parseColor("#00CCFF"));
            view3.setGravity(Gravity.CENTER);
            //view3.setGravity(Gravity.H)
            view3.setY(100);
            view3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 33);

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
                            JSONObject jsonlocation;
                            try {
                                jsonobject = new JSONObject(response);
                                String weatherData = jsonobject.getString("weather");
                                weatherData = weatherData.replace("[", "");
                                weatherData = weatherData.replace("]", "");
                                jsonweather = new JSONObject(weatherData);
                                view2.setText(jsonweather.getString("main"));
                                //String locationData = jsonobject.getString("sys");
                                view3.setText(jsonobject.getString("name"));

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

            //group.addView(view2);

            relativeLayout.addView(view2, params);
            relativeLayout.addView(view3, params);
        }
        if (page == 2) {
            loadLocations();
            loadLocationList();
            addClickListener();
        }


        return view;
    }
    private ArrayList<StudyGroup> studygroups = new ArrayList<StudyGroup>();
    private void loadLocations() {
        //Get study groups from a database
        //Add to studygroups arraylist
        //Sort by classes that student is taking
    }
    private GridView list;
    private void loadLocationList() {
        //Populate the screen with the items
    }
    private void addClickListener() {
        //When you click the item, it will take you to a map that gets you to the location
    }
}
