package fr.univpau.kayu.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import fr.univpau.kayu.R;
import fr.univpau.kayu.db.DatabaseTask;

public class SettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = layoutInflater.inflate(R.layout.fragment_settings, container, false);

        Switch darkThemeSwitch = root.findViewById(R.id.darkThemeSwitch);
        Switch automaticSearchSwitch = root.findViewById(R.id.automaticSearchSwitch);
        Button deleteHistoryBtn = root.findViewById(R.id.deleteHistoryBtn);
        Button checkThisProjectButton = root.findViewById(R.id.checkThisProjectButton);
        Button checkOtherProjectButton = root.findViewById(R.id.checkOtherProjectButton);

        if(getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("preferences", 0);
            final SharedPreferences.Editor editor = prefs.edit();

            boolean isDarkThemeOn = prefs.getBoolean("isDarkThemeOn", false);
            boolean isAutomaticSearchOn = prefs.getBoolean("isAutomaticSearchOn", true);

            darkThemeSwitch.setChecked(isDarkThemeOn);
            automaticSearchSwitch.setChecked(isAutomaticSearchOn);

            darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    editor.putBoolean("isDarkThemeOn", isChecked);
                    if(isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    editor.apply();
                    if(getActivity() != null) {
                        getActivity().recreate();
                    }
                }
            });

            automaticSearchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    editor.putBoolean("isAutomaticSearchOn", isChecked);
                    editor.apply();
                }
            });

            deleteHistoryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity() != null)
                        DatabaseTask.getInstance(getActivity().getApplication()).deleteAll();
                }
            });

            checkThisProjectButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/nicotitine/yuka_project"));
                    startActivity(browserIntent);
                }
            });

            checkOtherProjectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/nicotitine/csl_project"));
                    startActivity(browserIntent);
                }
            });
        }










        return root;
    }
}