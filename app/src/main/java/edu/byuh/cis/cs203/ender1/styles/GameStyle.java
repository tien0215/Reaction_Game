package edu.byuh.cis.cs203.ender1.styles;

import java.util.List;

import edu.byuh.cis.cs203.ender1.ui.NumberedSquare;

/**
 * Created by draperg on 10/25/17.
 */

public interface GameStyle {

    public enum TouchStatus {
        CONTINUE,
        LEVEL_COMPLETE,
        TRY_AGAIN,
        GAME_OVER,
    }

    String getNextLevelLabel();
    String getTryAgainLabel();
    List<String> getSquareLabels();
    TouchStatus getTouchStatus(NumberedSquare c);
}
