package nl.samtimo.pubcrawl;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

public class SettingsActivity extends ColorAppCompatActivity {
    private static final String DEFAULT_BACKGROUND_COLOR = "#FFFFF2";

    private String backgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnBackground = (Button) findViewById(R.id.button_background);
        //assert btnBackground != null;
        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        Button btnSave = (Button) findViewById(R.id.button_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        Button btnDefaults = (Button) findViewById(R.id.button_defaults);
        btnDefaults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetDefaults();
            }
        });
    }

    private void openColorPicker(){
        final ColorPicker cp = new ColorPicker(SettingsActivity.this, 255, 255, 255);

        cp.show();
        cp.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Button okColor = (Button)cp.findViewById(R.id.okColorButton);
        okColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* You can get single channel (value 0-255) */
                int selectedColorR = cp.getRed();
                int selectedColorG = cp.getGreen();
                int selectedColorB = cp.getBlue();

                backgroundColor = String.format("#%02x%02x%02x", selectedColorR, selectedColorG, selectedColorB);

                View pvBackground = findViewById(R.id.preview_background);
                pvBackground.setBackgroundColor(Color.parseColor(backgroundColor));

                cp.dismiss();
            }
        });
    }

    private void save(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("background", backgroundColor);
        editor.apply();
        getWindow().getDecorView().setBackgroundColor(Color.parseColor(backgroundColor));
    }

    private void resetDefaults(){
        backgroundColor = DEFAULT_BACKGROUND_COLOR;

        View pvBackground = findViewById(R.id.preview_background);
        pvBackground.setBackgroundColor(Color.parseColor(backgroundColor));
    }
}
