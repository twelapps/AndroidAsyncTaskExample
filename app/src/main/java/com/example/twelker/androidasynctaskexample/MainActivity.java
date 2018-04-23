package com.example.twelker.androidasynctaskexample;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //TODO to watch 4: local variables representing UI elements and a constant representing 5 seconds wait time
    private Button buttonWait;
    private Button buttonDummy;
    private ProgressBar progressBar;
    private static int SECONDS_TO_WAIT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] toastMessage = {"Wait button not yet clicked."};

        //Bring xml and Java code to eachother
        buttonWait  = (Button) findViewById(R.id.buttonWait);
        buttonDummy = (Button) findViewById(R.id.buttonDummy);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Stop the activity indicator
        progressBar.setVisibility(View.INVISIBLE);

        //TODO to watch 5: onClick listener of the WAIT button: wait on UI thread or wait on backgrund thread
        //What to do when "wait" button clicked (wait for SECONDS_TO_WAIT seconds)
        buttonWait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity ", "onClick: wait button clicked");

                //TODO to watch 6: wait on UI thread (uncomment one of the two options)
                /*****************************************************
                 * WAIT ON MAIN THREAD. UI WILL BE FROZEN.           *
                 * The activity indicator will not run while frozen! *
                 *****************************************************/
//                toastMessage[0] = "Dummy button clicked. If you see nothing for a while, UI was frozen!";
//
//                //Let current thread sleep for "waitSeconds" seconds
//                try { Thread.sleep(SECONDS_TO_WAIT * 1000); } catch (Exception e) { }

                //TODO to watch 7: wait on a background thread (uncomment one of the two options)
                /*****************************************************
                 * WAIT ON SEPARATE THREAD                           *
                 *****************************************************/
                toastMessage[0] = "Dummy button clicked. If the activity indicator is there, this is evidence of the UI not being frozen.";
                //Create a new task (thread)
                WaitASYNC task = new WaitASYNC();

                //And execure it
                task.execute(new Integer[] { SECONDS_TO_WAIT });

            }
        });

        //What to do when "dummy" button clicked (generate a Toast message, to show UI is not frozen while waiting)
        buttonDummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity ", "onClick: dummy button clicked.");

                Toast.makeText(MainActivity.this,
                        toastMessage[0], Toast.LENGTH_SHORT).show();
            }
        });
    }

    //TODO to watch 8: inner class WaitASYNC including the 3 mandatory methods
    private class WaitASYNC extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("MainActivity ", "onPreExecute ");

            //Let the activity indicator run
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... waitSeconds) {
            Log.d("MainActivity ", "doInBackground ");

            //Let current thread sleep for "waitSeconds" seconds
            try { Thread.sleep(waitSeconds[0] * 1000); } catch (Exception e) { }

            //WHY IS THE FOLLOWING STATEMENT INCORRECT HERE??
            //    progressBar.setVisibility(View.VISIBLE);

            return 99; //Will be logged by "onPostExecute" method
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.d("MainActivity ", "onPostExecute, result = " + result);

            //Stop the activity indicator
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
