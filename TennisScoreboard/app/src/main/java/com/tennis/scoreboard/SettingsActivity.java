package com.tennis.scoreboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

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
                    // Guardar el idioma seleccionado antes de reiniciar la aplicación
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("language", selectedLanguage);
                    editor.apply();
                    LocaleHelper.setLocale(getApplicationContext(), selectedLanguage);
                    restartApp(); // Reiniciar la aplicación para aplicar el cambio de idioma
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
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
        super.attachBaseContext(LocaleHelper.updateLocale(newBase));
    }

    private void applyLocale() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String language = prefs.getString("language", "es");
        LocaleHelper.setLocale(this, language);
    }

    private String getLanguageCode(int position) {
        switch (position) {
            case 0:
                return "es"; // Español
            case 1:
                return "en"; // Inglés
            case 2:
                return "fr"; // Francés
            case 3:
                return "ca"; // Catalán
            case 4:
                return "it"; // Italiano
            case 5:
                return "de"; // Alemán
            default:
                return "es"; // Por defecto, español
        }
    }

    private int getLanguagePosition(String languageCode) {
        switch (languageCode) {
            case "es":
                return 0;
            case "en":
                return 1;
            case "fr":
                return 2;
            case "ca":
                return 3;
            case "it":
                return 4;
            case "de":
                return 5;
            default:
                return 0; // Por defecto, la primera posición (español)
        }
    }

    private String getCurrentLanguage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString("language", "es");
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

    private void restartApp() {
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
