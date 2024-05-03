package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

public class SettingActivity extends BaseActivity {
    private ImageButton back;
    private AppCompatButton save,logout;
    private Spinner lang;
    private UserData userData;
    private AppCompatButton chagpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.themeColor));
        setContentView(R.layout.activity_setting);

        back = findViewById(R.id.back_arrow3);
        save = findViewById(R.id.changel);
        lang = findViewById(R.id.lang);
        logout = findViewById(R.id.logout);
        userData = new UserData(this);
        chagpass = findViewById(R.id.reset_pass);

        chagpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "");
        if (selectedLanguage.equals("en")) {
            lang.setSelection(0); // English
            back.setImageResource(R.drawable.ic_back_en);
        } else if (selectedLanguage.equals("ar")) {
            lang.setSelection(1); // Arabic
            back.setImageResource(R.drawable.ic_back_ar);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, BottomNavMenuActivity.class);
                i.putExtra("start_page", 1);
                startActivity(i);
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String language = lang.getSelectedItem().toString();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                if (language.equals("English") || language.equals("الإنجليزية")) {
                    editor.putString("selected_language", "en");

                    Locale locale = new Locale("en");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    SettingActivity.this.getResources().updateConfiguration(config, SettingActivity.this.getResources().getDisplayMetrics());
                } else if (language.equals("Arabic") || language.equals("العربية")) {
                    editor.putString("selected_language", "ar");

                    Locale locale = new Locale("ar");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    SettingActivity.this.getResources().updateConfiguration(config, SettingActivity.this.getResources().getDisplayMetrics());
                }
                editor.apply();
                restartApp();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    builder.setTitle("Logout");
                    builder.setMessage("Are you sure?");
                    builder.setIcon(R.drawable.logout_icon);
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        userData.logout();
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        intent.putExtra("tab", "1");
                        startActivity(intent);

                            finish();

                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }
        });

    }

    private void restartApp() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

}
