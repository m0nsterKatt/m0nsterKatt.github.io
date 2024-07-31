package com.tennis.scoreboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class LocaleHelper {

    public static void setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        // Guardar el idioma en las preferencias compartidas
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("language", language);
        editor.apply();
    }

    public static Context updateLocale(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String language = prefs.getString("language", "es"); // Idioma por defecto: español
        return setLocaleContext(context, language);
    }

    private static Context setLocaleContext(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }

        return context.createConfigurationContext(config);
    }
}
