package edu.byuh.cis.cs203.ender1.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import edu.byuh.cis.cs203.ender1.R;

/**
 * Created by draperg on 10/17/17.
 */

public class SplashScreen extends Activity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iv = new ImageView(this);
        iv.setImageResource(R.drawable.splash);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(iv);
    }

    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();
            if (y > iv.getHeight() * 0.8) {
                if (x < iv.getWidth() * 0.2) {
                    Intent i = new Intent(this, OptionsOptions.class);
                    startActivity(i);
                } else if (x > iv.getWidth() * 0.8) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(this);
                    ab.setTitle("About \"Frozen Cubes\"");
                    ab.setMessage("This game was developed by the students of CS 203" +
                            " at Brigham Young University Hawaii during the Fall 2021" +
                            " semester. GO SEASIDERS!");
                    ab.setNeutralButton("OK", null);
                    AlertDialog box = ab.create();
                    ab.show();
                }
            } else {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                //finish();
            }
        }
        return true;
    }
}
