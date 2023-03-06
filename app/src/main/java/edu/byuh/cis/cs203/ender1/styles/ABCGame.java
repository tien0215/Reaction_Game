package edu.byuh.cis.cs203.ender1.styles;

import java.util.List;
import java.util.Vector;

import edu.byuh.cis.cs203.ender1.ui.NumberedSquare;

/**
 * Created by draperg on 11/6/17.
 */

public class ABCGame implements GameStyle {
    private char level;
    private Vector<String> labels;
    private char nextExpectedChar;

    public ABCGame() {
        level = 'A';
        labels = new Vector<>();
        prepare();
    }

    private void prepare() {
        nextExpectedChar = 'A';
        labels.clear();
        for (char i='A'; i<=level; ++i) {
            labels.add(""+i);
        }
    }


    @Override
    public String getNextLevelLabel() {
        if (level == 'A') {
            return "Tap the A";
        } else if (level == 'B') {
            return "Tap A, then B";
        } else {
            return "Tap from A to " + level;
        }
    }

    @Override
    public String getTryAgainLabel() {
        return "Oops, tap the " + nextExpectedChar;
    }

    @Override
    public List<String> getSquareLabels() {
        return labels;
    }

    @Override
    public TouchStatus getTouchStatus(NumberedSquare c) {
        char id = c.toString().charAt(0);
        if (id <= nextExpectedChar) {
            if (id == nextExpectedChar) {
                nextExpectedChar++;
                if (id == labels.lastElement().charAt(0)) {
                    level++;
                    if (level > 'Z') {
                        return TouchStatus.GAME_OVER;
                    } else {
                        prepare();
                        return TouchStatus.LEVEL_COMPLETE;
                    }
                }
            }
            return TouchStatus.CONTINUE;
        } else {
            return TouchStatus.TRY_AGAIN;
        }
    }

    @Override
    public String toString() {
        return "ABC Game";
    }
}
