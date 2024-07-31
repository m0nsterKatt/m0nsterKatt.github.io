package com.tennis.scoreboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Spinner languageSpinner;
    private Switch darkModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Aplicar el modo oscuro antes de establecer el contenido de la vista
        applyDarkMode();

        applyLocale(); // Aplicar el idioma antes de establecer el contenido de la vista

        setContentView(R.layout.activity_settings);

        languageSpinner = findViewById(R.id.languageSpinner);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);


        // Cargar la preferencia de idioma guardada
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String language = prefs.getString("language", "es"); // Por defecto, español
        languageSpinner.setSelection(getLanguagePosition(language));

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = getLanguageCode(position);
                if (!selectedLanguage.equals(getCurrentLanguage())) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("language", selectedLanguage);
                    editor.apply();
                    applyLocale(); // Aplicar el nuevo idioma
                    recreate(); // Recargar la actividad para aplicar el cambio de idioma
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                recreate(); // Recargar la actividad para aplicar el cambio de idioma
            }

        });

        // Cargar la preferencia de modo oscuro
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        darkModeSwitch.setChecked(isDarkMode);
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply();
            recreate(); // Recargar la actividad para aplicar el cambio de tema
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(newBase);
        String language = prefs.getString("language", "es");
        super.attachBaseContext(updateBaseContextLocale(newBase, language));
    }

    private Context updateBaseContextLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }

    private void applyLocale() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String language = prefs.getString("language", "es");
        setLocale(language);
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    private String getLanguageCode(int position) {
        String[] languageCodes = getResources().getStringArray(R.array.language_values);
        return languageCodes[position];
    }

    private int getLanguagePosition(String languageCode) {
        String[] languageCodes = getResources().getStringArray(R.array.language_values);
        for (int i = 0; i < languageCodes.length; i++) {
            if (languageCodes[i].equals(languageCode)) {
                return i;
            }
        }
        return 0; // Por defecto, la primera posición
    }

    private String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }

    private void applyDarkMode() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
