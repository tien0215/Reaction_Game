package edu.byuh.cis.cs203.ender1.styles;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

import edu.byuh.cis.cs203.ender1.ui.NumberedSquare;

/**
 * Created by draperg on 11/6/17.
 */

public class AdditionGame implements GameStyle {

    private List<Point> problems;
    private Point currentProblem;
    private List<String> labels;
    private int currentDigitIndex;

    public AdditionGame() {
        problems = new ArrayList<>();
        labels = new ArrayList<>();
        makeProblemSet();
        prepare();
    }

    private void makeProblemSet() {
        for (int i=1; i <= 12; i++) {
            for (int j=1; j<=12; j++) {
                problems.add(new Point(i,j));
            }
        }
    }

    private void prepare() {
        labels.clear();
        currentDigitIndex = 0;
        int index = (int)(Math.random() * problems.size());
        currentProblem = problems.get(index);
        String answer = answer();
        for (int i=0; i<answer.length(); i++) {
            labels.add("" + answer.charAt(i));
        }
        for (int i=0; i<10-answer.length(); i++) {
            labels.add("" + (int)(Math.random()*10));
        }
    }

    @Override
    public String getNextLevelLabel() {
        return "What is " + currentProblem.x + " + " + currentProblem.y + "?";
    }

    private String answer() {
        return "" + (currentProblem.x + currentProblem.y);
    }

    @Override
    public String getTryAgainLabel() {
        return "Try \"" + answer().charAt(currentDigitIndex) + "\"";
    }

    @Override
    public List<String> getSquareLabels() {
        return labels;
    }

    @Override
    public TouchStatus getTouchStatus(NumberedSquare c) {
        String answer = answer();
        String digit = ""+answer.charAt(currentDigitIndex);
        if (digit.equals(c.toString())) {
            currentDigitIndex++;
            if (currentDigitIndex == answer.length()) {
                problems.remove(currentProblem);
                if (problems.isEmpty()) {
                    return TouchStatus.GAME_OVER;
                } else {
                    prepare();
                    return TouchStatus.LEVEL_COMPLETE;
                }
            }
            return TouchStatus.CONTINUE;
        } else {
            //user got it wrong, put the problem back in the set for future practice.
            problems.add(new Point(currentProblem));
            return TouchStatus.TRY_AGAIN;
        }
    }

    @Override
    public String toString() {
        return "Adding Game";
    }

}
