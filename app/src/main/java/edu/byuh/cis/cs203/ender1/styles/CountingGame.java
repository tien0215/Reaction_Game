package edu.byuh.cis.cs203.ender1.styles;

import java.util.ArrayList;
import java.util.List;

import edu.byuh.cis.cs203.ender1.ui.NumberedSquare;

/**
 * Created by draperg on 10/25/17.
 */

public class CountingGame implements GameStyle {

    private int level;
    private List<String> labels;
    private int nextExpectedID;

    public CountingGame() {
        level = 1;
        nextExpectedID = 1;
        labels = new ArrayList<>();
        prepare();
    }

    private void prepare() {
        nextExpectedID = 1;
        labels.clear();
        for (int i=1; i<=level; ++i) {
            labels.add(""+i);
        }
    }


    @Override
    public String getNextLevelLabel() {
        if (level == 1) {
            return "Tap the 1";
        } else if (level == 2) {
            return "Tap 1, then 2";
        } else {
            return "Tap from 1 to " + level;
        }
    }

    @Override
    public String getTryAgainLabel() {
        return "Oops, tap the " + nextExpectedID;
    }

    @Override
    public List<String> getSquareLabels() {
        return labels;
    }

    @Override
    public TouchStatus getTouchStatus(NumberedSquare c) {
        //for debugging, we set the "game over" limit to 5 levels.
        int id = c.getID();
        if (id <= nextExpectedID) {
            if (id == nextExpectedID) {
                nextExpectedID++;
                if (id == labels.size()) {
                    level++;
                    if (level > 5) {
                        return TouchStatus.GAME_OVER;
                    }
                    prepare();
                    return TouchStatus.LEVEL_COMPLETE;
                }
            }
            return TouchStatus.CONTINUE;
        } else {
            return TouchStatus.TRY_AGAIN;
        }
    }

    @Override
    public String toString() {
        return "Counting Game";
    }

}
