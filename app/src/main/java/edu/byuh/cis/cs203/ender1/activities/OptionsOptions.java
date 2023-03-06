package edu.byuh.cis.cs203.ender1.activities;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import edu.byuh.cis.cs203.ender1.styles.ABCGame;
import edu.byuh.cis.cs203.ender1.styles.AdditionGame;
import edu.byuh.cis.cs203.ender1.styles.CountingGame;
import edu.byuh.cis.cs203.ender1.styles.GameStyle;
import edu.byuh.cis.cs203.ender1.styles.MultiplicationGame;
import edu.byuh.cis.cs203.ender1.styles.SpellingGame;


/**
 * Created by draperg on 10/18/17.
 */

public class OptionsOptions extends PreferenceActivity {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        PreferenceScreen ps = getPreferenceManager().createPreferenceScreen(this);
        CheckBoxPreference music = new CheckBoxPreference(this);
        music.setTitle("Toggle background music");
        music.setSummaryOn("Background music will play");
        music.setSummaryOff("Silent mode");
        music.setKey("MUSIC_PREF");
        music.setDefaultValue(true);
        ps.addPreference(music);
        ListPreference speedPref = new ListPreference(this);
        speedPref.setTitle("Cube Speed");
        speedPref.setSummary("How fast should the cubes fly?");
        speedPref.setKey("FLY_SPEED");
        String[] entries = {"Slow", "Medium", "Fast"};
        speedPref.setEntries(entries);
        String[] values = {"1", "2", "3"};
        speedPref.setEntryValues(values);
        speedPref.setDefaultValue("1"); //slow
        ps.addPreference(speedPref);

        ListPreference stylePref = new ListPreference(this);
        stylePref.setTitle("Game Styla");
        stylePref.setSummary("Which game do you want to play");
        stylePref.setKey("game_style");
        String[] entries2 = {"Counting Game", "ABC Game", "Adding Game", "Times Tables", "Spelling Game"};
        stylePref.setEntries(entries2);
        String[] values2 = {"1", "2", "3", "4", "5"};
        stylePref.setEntryValues(values2);
        stylePref.setDefaultValue("1"); //counting
        ps.addPreference(stylePref);

        setPreferenceScreen(ps);
    }

    //this is a "facade" method to hide the nastiness
    //of Android's preferences API.
    public static boolean getMusicOption(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c)
                .getBoolean("MUSIC_PREF", true);
    }

    public static int getCubeSpeed(Context c) {
        String speed = PreferenceManager.getDefaultSharedPreferences(c)
                .getString("FLY_SPEED", "1");
        return Integer.parseInt(speed);
    }

    public static GameStyle getGameStyle(Context c) {
        GameStyle i;
        String foo = PreferenceManager.getDefaultSharedPreferences(c)
                .getString("game_style", "1");
        switch (foo) {
            case "1":
            default:
                i = new CountingGame();
                break;
            case "2":
                i = new ABCGame();
                break;
            case "3":
                i = new AdditionGame();
                break;
            case "4":
                i = new MultiplicationGame();
                break;
            case "5":
                i = new SpellingGame();
                break;
        }
        return i;
    }

}
