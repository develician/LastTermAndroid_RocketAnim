package com.rocketanim.killi8n.rocketanim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager manager;
    private SensorView sensorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorView = new SensorView(this);
        setContentView(sensorView);

        sensorView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.background));



        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            manager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        switch (sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                sensorView.move(event.values[0], event.values[1]);
//                Log.d("xValue: ", xValue + "");
//                Log.d("yValue: ", yValue + "");
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class SensorView extends View {

        float x, y;

        private static final int ROCKET_SIZE = 50;

        Display display = getWindowManager().getDefaultDisplay();
        float xMax = (float) display.getWidth() - 50;
        float yMax = (float) display.getHeight() - 50;

        private int w, h;

        Bitmap rocket;
        public SensorView(Context context) {
            super(context);

            rocket = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocket);

        }


        public void move(float xValue, float yValue) {
            this.x -= (xValue * 4f);
            this.y += (yValue * 4f);

            if(this.x < 0) {
                this.x = 0;
            }
            if(this.y < 0) {
                this.y = 0;
            }

            if(this.x > xMax) {
                this.x = xMax;
            }
            if(this.y > yMax) {
                this.y = yMax;
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(rocket, this.x, this.y, null);
            invalidate();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            this.w = w;
            this.h = h;
            this.x = (w - ROCKET_SIZE) / 2f;
            this.y = (h - ROCKET_SIZE) / 2f;
        }




    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.unregisterListener(this);
    }
}
