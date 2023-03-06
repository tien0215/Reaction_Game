package edu.byuh.cis.cs203.ender1.styles;

import java.util.ArrayList;
import java.util.List;

import edu.byuh.cis.cs203.ender1.ui.NumberedSquare;

/**
 * Created by draperg on 10/25/17.
 */

public class SpellingGame implements GameStyle {

    private String[] words = {
            "DOG", "CAT", "CAR", "FISH", "FROG", "COIN",
            "LOOP", "CUBE", "BIG", "BLUE", "GREEN",
            "RED", "BROWN", "YELLOW", "WHITE", "GREY",
            "CORN", "SPAM", "RICE"
    };
    private int currentWordIndex;
    private List<String> labels;
    private int currentLetterIndex;

    public SpellingGame() {
        currentWordIndex = 0;
        labels = new ArrayList<>();
        prepare();
    }

    private void prepare() {
        labels.clear();
        currentLetterIndex = 0;
        String word = words[currentWordIndex];
        //first few squares have letters from the word
        for (int i=0; i<word.length(); i++) {
            labels.add(""+word.charAt(i));
        }
        //then make a few squares with random letters, to distract user
        for (int i=word.length(); i<10; i++) {
            int randomChar = 65 + (int)(Math.random() * 26);
            labels.add("" + (char)randomChar);
        }
    }
    @Override
    public String getNextLevelLabel() {
        return "Spell \"" + words[currentWordIndex] + "\"";
    }

    @Override
    public String getTryAgainLabel() {
        String word = words[currentWordIndex];
        char let = word.charAt(currentLetterIndex);
        return "Tap the \"" + let + "\" in \"" + word + "\"";
    }

    @Override
    public List<String> getSquareLabels() {
        return labels;
    }

    @Override
    public TouchStatus getTouchStatus(NumberedSquare c) {
        String word = words[currentWordIndex];
        String letter = ""+word.charAt(currentLetterIndex);
        if (letter.equals(c.toString())) {
            currentLetterIndex++;
            if (currentLetterIndex == word.length()) {
                currentWordIndex++;
                if (currentLetterIndex >= words.length) {
                    return TouchStatus.GAME_OVER;
                } else {
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
        return "Spelling Game";
    }

}
