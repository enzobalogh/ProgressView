package com.example.avalicaon1_mobile;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ProgressView progressView;
    private int tarefasConcluidas = 7;
    private final int maxTarefas = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressView = findViewById(R.id.progressView);
        progressView.setMaxValue(maxTarefas);
        progressView.setProgress(tarefasConcluidas);

        Button btn = findViewById(R.id.btnIncrement);
        btn.setOnClickListener(v -> {
            if (tarefasConcluidas < maxTarefas) {
                tarefasConcluidas++;
                progressView.setProgress(tarefasConcluidas);
            }
        });
    }
}
