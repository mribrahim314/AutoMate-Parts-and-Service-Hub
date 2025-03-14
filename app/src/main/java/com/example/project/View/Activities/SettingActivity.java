package com.example.project.View.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.example.project.Controller.UserData;
import com.example.project.R;

public class SettingActivity extends BaseActivity {
    private ImageButton back;
    private AppCompatButton save, logout;
    private Spinner lang;
    private UserData userData;
    private AppCompatButton changePass;
    private TextView call, email;
    private AlertDialog Dialog;

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
        changePass = findViewById(R.id.reset_pass);
        call = findViewById(R.id.hasPhone);
        email = findViewById(R.id.hasEmail);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:+96170123456");
                Intent i = new Intent(Intent.ACTION_DIAL, number);
                startActivity(i);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:administrator@gmail.com"));
                startActivity(emailIntent);
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(i);
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedLanguage = preferences.getString("selected_language", "en");
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
                    editor.apply();
                    restartApp();
                } else if (language.equals("Arabic") || language.equals("العربية")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                    View DialogView = LayoutInflater.from(SettingActivity.this).inflate(R.layout.arabic_dialog, null);
                    final AppCompatButton yesButton = DialogView.findViewById(R.id.ch_yes_button);
                    final AppCompatButton noButton = DialogView.findViewById(R.id.ch_no_button);
                    builder.setView(DialogView);
                    final AlertDialog Dialog = builder.create();
                    yesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            editor.putString("selected_language", "ar");
                            Locale locale = new Locale("ar");
                            Locale.setDefault(locale);
                            Configuration config = new Configuration();
                            config.locale = locale;
                            SettingActivity.this.getResources().updateConfiguration(config, SettingActivity.this.getResources().getDisplayMetrics());
                            if (Dialog != null && Dialog.isShowing()) {
                                editor.apply();
                                restartApp();
                                Dialog.dismiss();
                            }
                        }
                    });
                    noButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Dialog != null && Dialog.isShowing()) {
                                Dialog.dismiss();
                            }
                        }
                    });
                    Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Dialog.show();
                }
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                View DialogView = LayoutInflater.from(SettingActivity.this).inflate(R.layout.logout_dialog, null);
                final AppCompatButton yesButton = DialogView.findViewById(R.id.lo_yes_button);
                final AppCompatButton noButton = DialogView.findViewById(R.id.lo_no_button);
                builder.setView(DialogView);
                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userData.logout();
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        intent.putExtra("tab", "1");
                        startActivity(intent);
                        if (Dialog != null && Dialog.isShowing()) {
                            Dialog.dismiss();
                            finish();
                        }
                    }
                });
                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Dialog != null && Dialog.isShowing()) {
                            Dialog.dismiss();
                        }
                    }
                });
                Dialog = builder.create();
                Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Dialog.show();
            }
        });

    }

    private void restartApp() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

}
