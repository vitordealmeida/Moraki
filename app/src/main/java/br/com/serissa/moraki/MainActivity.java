package br.com.serissa.moraki;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToResults();
            }
        });
    }

    private void navigateToResults() {
        startActivity(ResultsActivity.getIntent(this,
                ((Spinner) findViewById(R.id.spinner_tipo)).getSelectedItem().toString(),
                ((Spinner) findViewById(R.id.spinner_cidade)).getSelectedItem().toString(),
                ((EditText) findViewById(R.id.input_bairro)).getText().toString(),
                ((EditText) findViewById(R.id.input_dormitorios)).getText().toString(),
                ((EditText) findViewById(R.id.input_vagas)).getText().toString()
        ));
    }
}
