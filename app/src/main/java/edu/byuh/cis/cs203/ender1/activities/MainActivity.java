package edu.byuh.cis.cs203.ender1.activities;

import android.app.Activity;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;

import edu.byuh.cis.cs203.ender1.ui.GameView;
import edu.byuh.cis.cs203.ender1.R;

public class MainActivity extends Activity {

    private GameView gv;
    private MediaPlayer music;

    /**
     * It all starts here
     * @param savedInstanceState - passed in by OS. Ignore for now.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        music = MediaPlayer.create(this, R.raw.zhaytee_microcomposer_1);
        if (OptionsOptions.getMusicOption(this)) {
            music.setLooping(true);
        }
        gv = new GameView(this);
        setContentView(gv);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (music != null && OptionsOptions.getMusicOption(this)) {
            music.start();
        }
    }

    @Override
    public  void onPause() {
        super.onPause();
        if (music != null  && OptionsOptions.getMusicOption(this)) {
            music.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (music != null) {
            music.release();
        }
    }

    /**
     * Helper method I made up one day, to work around the
     * lack of documentation about font sizes in Android.
     * This function is only "partially debugged" and I do not
     * guarantee its accuracy.
     * @param lowerThreshold how many pixels high the text should be
     * @return the font size that corresponds to the requested pixel height
     */
    public static float findThePerfectFontSize(float lowerThreshold) {
        float fontSize = 1;
        Paint p = new Paint();
        p.setTextSize(fontSize);
        while (true) {
            float asc = -p.getFontMetrics().ascent;
            if (asc > lowerThreshold) {
                break;
            }
            fontSize++;
            p.setTextSize(fontSize);
        }
        return fontSize;
    }

}
