package com.example.note.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.note.Model.Settings;
import com.example.note.R;

public class PinActivity extends AppCompatActivity {
    Settings settings;
    EditText editTextPin;
    Button buttonPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        init();
    }

    private void init(){
        settings = new Settings(this);
        boolean pinactive = settings.getPinActive();

        if (pinactive){
            getIds();

            buttonPin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chechkPin();
                }
            });
        }
        else {
            goNotesActivity();
        }
    }
    private void getIds(){
        editTextPin = findViewById(R.id.editTextPin);
        buttonPin = findViewById(R.id.buttonPin);
    }

    private void chechkPin(){
        String pin = editTextPin.getText().toString();
        String truePin = settings.getPin();

        if (pin.equals(truePin)) {
            goNotesActivity();
        } else {
            Toast.makeText(this, "Wrong Pin", Toast.LENGTH_LONG).show();
        }
    }

    private void goNotesActivity(){
        startActivity(new Intent(PinActivity.this, MainActivity.class));
        finish();
    }

}
