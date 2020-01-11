package fr.univpau.kayu.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import fr.univpau.kayu.R;
import fr.univpau.kayu.db.AppDatabase;
import fr.univpau.kayu.db.DatabaseTask;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private Switch darkThemeSwitch;
    private Switch automaticSearchSwitch;
    private Button deleteHistoryBtn;


    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = layoutInflater.inflate(R.layout.fragment_settings, container, false);

        darkThemeSwitch = root.findViewById(R.id.darkThemeSwitch);
        automaticSearchSwitch = root.findViewById(R.id.automaticSearchSwitch);
        deleteHistoryBtn = root.findViewById(R.id.deleteHistoryBtn);

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
                getActivity().recreate();
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
                DatabaseTask.getInstance(getActivity().getApplication()).deleteAll();
            }
        });

        return root;
    }
}