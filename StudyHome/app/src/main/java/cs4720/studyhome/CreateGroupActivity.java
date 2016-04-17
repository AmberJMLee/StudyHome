package cs4720.studyhome;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends Activity implements View.OnClickListener {

        // Progress Dialog
        private ProgressDialog pDialog;

        JSONParser jsonParser = new JSONParser();
        private EditText creator;
        private EditText gName;
        private EditText classname;
        private Button createGroup;
        private int success;//to determine JSON signal insert success/fail

        // url to insert new idiom (change accordingly)
        private static String url_insert_new = "http://localhost/Study_Home/init.php";

        // JSON Node names
        private static final String TAG_SUCCESS = "success";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_group);

            // Edit Text
            creator = (EditText) findViewById(R.id.editText);
            gName = (EditText) findViewById(R.id.editText2);
            classname = (EditText) findViewById(R.id.editText3);
            // Save button
            createGroup = (Button) findViewById(R.id.create);
            // button click event
            createGroup.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.create){
                //call the InsertNewIdiom thread
                new InsertNewGroup().execute();
                if (success==1){
                    Toast.makeText(getApplicationContext(), "New group saved...", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "New group FAILED to saved...", Toast.LENGTH_LONG).show();
                }
            }

        }

        /**
         * Background Async Task to Create new Idioms
         * */
        class InsertNewGroup extends AsyncTask<String, String, String> {
            //capture values from EditText
            String maker = creator.getText().toString();
            String groupName = gName.getText().toString();
            String className = classname.getText().toString().replace(" ", "");;
            String [] separate = className.split("[0-9]+", 2);
            String classId = separate[0];
            int classNum = Integer.parseInt(separate[1]);
            /**
             * Before starting background thread Show Progress Dialog
             * */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(CreateGroupActivity.this);
                pDialog.setMessage("Saving the new group ("+groupName+")...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            /**
             * Inserting the new idiom
             * */
            protected String doInBackground(String... args) {


                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("creator", maker));
                params.add(new BasicNameValuePair("gName", groupName));
                params.add(new BasicNameValuePair("classID", classId));
                params.add(new BasicNameValuePair("classNum", String.valueOf(classNum)));
                // getting JSON Object
                // Note that create product url accepts GET method
                JSONObject json = jsonParser.makeHttpRequest(url_insert_new,
                        "GET", params);

                // check log cat from response
                Log.d("Add New Group Response", json.toString());

                // check for success tag
                try {
                    success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // successfully save new idiom
                    } else {
                        // failed to add new idiom
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //return null;
                return null;
            }

            /**
             * After completing background task Dismiss the progress dialog
             * **/
            protected void onPostExecute(String file_url) {
                // dismiss the dialog once done
                pDialog.dismiss();
            }

        }

}
