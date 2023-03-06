package edu.byuh.cis.cs203.ender1.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.byuh.cis.cs203.ender1.activities.MainActivity;
import edu.byuh.cis.cs203.ender1.activities.OptionsOptions;
import edu.byuh.cis.cs203.ender1.styles.GameStyle;
import edu.byuh.cis.cs203.ender1.util.TickListener;
import edu.byuh.cis.cs203.ender1.util.Timer;

import edu.byuh.cis.cs203.ender1.R;

/**
 * Created by draperg on 8/21/17.
 */

@SuppressLint("AppCompatCustomView")
public class GameView extends ImageView implements TickListener {

    private boolean firstRun;
    private float w, h;
    private List<NumberedSquare> squares;
    private Timer timer;
    //private int nextExpectedID;
    private Toast toasty;
    private GameStyle gs;
    private String levelText;
    private Paint levelTextPaint;
    private int score;
    private float speedBonus;
    private float maxBonus;
    private Paint bonusBarPaint;
    private Paint scoreTextPaint;

    /**
     * GameView constructor. Just initializes a few variables
     *
     * @param context - required by API
     */
    public GameView(Context context) {
        super(context);
        firstRun = true;
        squares = new ArrayList<>();
        timer = Timer.getInstance();
        setImageResource(R.drawable.sea_ice_terrain);
        setScaleType(ScaleType.FIT_XY);
        levelText = "";
        resetGame();
    }

    private void resetGame() {
        gs = OptionsOptions.getGameStyle(getContext());
        score = 0;
    }

    /**
     * All the rendering happens here. The first time onDraw is called, we also do some
     * one-time initialization of the graphics objects (since the width and height of
     * the screen are not known until onDraw is called).
     *
     * @param c
     */
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (firstRun) {
            w = c.getWidth();
            h = c.getHeight();
            levelTextPaint = new Paint();
            levelTextPaint.setTextAlign(Paint.Align.CENTER);
            levelTextPaint.setTextSize(MainActivity.findThePerfectFontSize(h*0.05f));
            levelTextPaint.setColor(Color.rgb(248,159,18));
            levelTextPaint.setShadowLayer(5, 5, 5, Color.BLACK);
            scoreTextPaint = new Paint(levelTextPaint);
            scoreTextPaint.setTextAlign(Paint.Align.LEFT);
//            scoreTextPaint.setTextSize(MainActivity.findThePerfectFontSize(h*0.05f));
//            scoreTextPaint.setColor(Color.rgb(248,159,18));
//            scoreTextPaint.setShadowLayer(5, 5, 5, Color.BLACK);
            bonusBarPaint = new Paint();
            bonusBarPaint.setColor(Color.YELLOW);
            bonusBarPaint.setStyle(Paint.Style.FILL);
            createSquares();
            timer.subscribe(this);
            firstRun = false;
        }
        for (NumberedSquare ns : squares) {
            ns.draw(c);
        }
        c.drawText(levelText, w/2, h*0.9f, levelTextPaint);

        //draw "speed bonus bar"
        float barHeight = h * 0.01f;
        float barWidth = w * (speedBonus / maxBonus);
        c.drawRect(0, h-barHeight, barWidth, h, bonusBarPaint);

        //draw the current score
        c.drawText("SCORE: " + score, w*0.01f, h*0.1f, scoreTextPaint);
    }

    /**
     * Handle touch events
     *
     * @param me - the MotionEvent object that encapsulates all the data about this
     *           particular touch event. Supplied by OS.
     * @return true, always.
     */
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            float x = me.getX();
            float y = me.getY();
            for (NumberedSquare ns : squares) {
                if (ns.contains(x, y) && !ns.isFrozen()) {
                    GameStyle.TouchStatus status = gs.getTouchStatus(ns);
                    if (status == GameStyle.TouchStatus.CONTINUE) {
                        ns.freeze();
                        score += (int)speedBonus;
                    } else if (status == GameStyle.TouchStatus.TRY_AGAIN) {
                        showToast(gs.getTryAgainLabel());
                    } else if (status == GameStyle.TouchStatus.GAME_OVER) {
                        score += (int)speedBonus;
                        showEndgameDialog();
                    } else {
                        //LEVEL_COMPLETE
                        score += (int)speedBonus;
                        createSquares();
                        break;
                    }
                }
            }
        }
        return true;
    }

    private void showEndgameDialog() {
        String greeting = "GAME OVER!";
        int oldScore;
        //load the previous high score
        try {
            FileInputStream fis = getContext().openFileInput(gs.toString());
            Scanner s = new Scanner(fis);
            oldScore = s.nextInt();
            s.close();
        } catch (FileNotFoundException e) {
            //if high score file does not exist, assume a high score of zero.
            oldScore = 0;
        }
        if (score > oldScore) {
            greeting += " Congratulations! You got a new high score!";
            try {
                FileOutputStream fos = getContext().openFileOutput(gs.toString(), Context.MODE_PRIVATE);
                fos.write((""+score).getBytes());
                fos.close();
            } catch (IOException e) {
                Log.d("CS203", "this should never happen");
            }
        }
        greeting += " Play again?";
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle(gs.toString());
        ab.setMessage(greeting);
        ab.setPositiveButton("Play Again", (d,i) -> {
            resetGame();
            createSquares();
        });
        ab.setNegativeButton("Quit", (d,i) -> ((Activity)getContext()).finish());
        AlertDialog box = ab.create();
        box.show();
    }

    private void showToast(String t) {
        if (toasty != null) {
            toasty.cancel();
        }
        toasty = Toast.makeText(getContext(), t, Toast.LENGTH_LONG);
        toasty.show();
    }

    /**
     * Helper method to unsubscribe all squares from timer,
     * then delete the squares.
     */
    private void deleteAllSquares() {
        for (NumberedSquare ns : squares) {
            timer.unsubscribe(ns);
        }
        squares.clear();
        NumberedSquare.resetCounter();
    }

    /**
     * Helper method for creating the NumberedSquare objects
     */
    private void createSquares() {
        deleteAllSquares();
        final int MAX_ATTEMPTS = 20;
        List<String> labels = gs.getSquareLabels();
        NumberedSquare first = new NumberedSquare(this, labels.get(0));
        float size = first.getSize();
        squares.add(first);
        timer.subscribe(first);
        for (int i=1; i<labels.size(); i++) {
            boolean legal = false;
            int attempts = 0;
            //we give the computer MAX_ATTEMPTS chances to find an empty space on the
            //screen. If cannot find any space, assume there are too many squares
            //and go to Endgame.
            while (!legal && attempts < MAX_ATTEMPTS) {
                float candidateX = (float)(Math.random() * (w-size));
                float candidateY = (float)(Math.random() * (h-size));
                RectF candidate = new RectF(candidateX, candidateY, candidateX+size, candidateY+size);
                legal = true;
                for (NumberedSquare other : squares) {
                    if (other.overlaps(candidate)) {
                        legal = false;
                        break;
                    }
                }
                if (legal) {
                    NumberedSquare ns = new NumberedSquare(this, candidate, labels.get(i));
                    squares.add(ns);
                    timer.subscribe(ns);
                } else {
                    attempts++;
                }
            }
            if (!legal) {
                //Could not find any vacant space on screen for a new square,
                //so goto Endgame.
                showEndgameDialog();
                break;
            }
        }
        levelText = gs.getNextLevelLabel();
        maxBonus = squares.size() * 50;
        speedBonus = maxBonus;
    }

    @Override
    public void tick() {
        checkForCollisions();
        if (speedBonus > 1) {
            speedBonus--;
        }
        invalidate();
    }

    private void checkForCollisions() {
        for (NumberedSquare a : squares) {
            for (NumberedSquare b : squares) {
                a.checkForCollision(b);
            }
        }
    }
}
