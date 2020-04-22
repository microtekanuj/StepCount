package com.example.stepcount;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private TextView textView;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private static final String TEXT_CALO = "Number of Calories: ";
    private int numSteps;
    private int calorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TextView TvSteps = (TextView) findViewById(R.id.tv_steps);
        Button BtnStart = (Button) findViewById(R.id.btn_start);
        Button BtnStop = (Button) findViewById(R.id.btn_stop);
        //Button signin = (Button)findViewById(R.id.sign_in_button);

        /*signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        TextView name=(TextView)findViewById(R.id.name);
        TextView email=(TextView)findViewById(R.id.email);
        TextView id=(TextView)findViewById(R.id.id);


        String spname;
        String spmail;
        String spid;
        String na;
        String em;
        String i;
        if (getIntent().getStringExtra("name")!=null)
        {
        na = getIntent().getStringExtra("name");
        em = getIntent().getStringExtra("email");
        i = getIntent().getStringExtra("id");

            name.setText(na);
            email.setText(em);
            id.setText(i);
        }
        else {
            SharedPreferences sharedPreferences = getSharedPreferences("mydata",0);


            spname = sharedPreferences.getString("name",null);
            spmail = sharedPreferences.getString("email",null);
            spid = sharedPreferences.getString("id",null);
            name.setText(spname);
            email.setText(spmail);
            id.setText(spid);
        }


        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                numSteps = 0;
                calorie = 0;
                sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

            }
        });


        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                sensorManager.unregisterListener(MainActivity.this);

            }
        });

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.iconfit);
        builder.setContentTitle("StepCounter");
        builder.setContentText("Steps Walked "+ String.valueOf(numSteps));
        builder.setContentText("Calories "+ String.valueOf(calorie));
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1011, builder.build());



    }


  /*  @Override
    public void onStart(){
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    private void updateUI(GoogleSignInAccount account) {
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }*/

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        TextView TvSteps = (TextView) findViewById(R.id.tv_steps);
        TvSteps.setText(TEXT_NUM_STEPS + numSteps);
        calo();
    }

    public void calo()
    {
        calorie=0;
        calorie = calorie + (int)(0.05*numSteps);
        TextView cal = (TextView)findViewById(R.id.calorie);
        cal.setText(TEXT_CALO+String.valueOf(calorie));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
