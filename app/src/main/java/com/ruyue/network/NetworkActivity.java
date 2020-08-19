package com.ruyue.network;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NetworkActivity extends AppCompatActivity {
    private Button getData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        getData = findViewById(R.id.get_data);
        setListeners();
    }

    private void setListeners() {
        OnClick onclick = new OnClick();
        getData.setOnClickListener(onclick);


    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.get_data:
                    break;
            }
        }
    }
}

