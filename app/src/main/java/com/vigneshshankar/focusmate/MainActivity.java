package com.vigneshshankar.focusmate;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private int sessionCounter;
    private Boolean isStudyPhase = true;
    private Boolean isTimerRunning = false;
    private Button sessionStartBtn;
    private TextView sessionCompletedTv;
    private TextView motivationalQuoteTv;

    private final String[] quotes = {
            "Believe you can and you're halfway there.",
            "Focus on being productive instead of busy.",
            "Don't watch the clock; do what it does. Keep going.",
            "Success is the sum of small efforts repeated daily.",
            "Push yourself, because no one else is going to do it for you."
    };


//    private final long STUDY_DURATION = 20 * 60 * 1000;
//    private final long BREAK_DURATION = 5 * 60 * 1000;

    private final long STUDY_DURATION = 10 * 1000; // 10 seconds study
    private final long BREAK_DURATION = 5 * 1000;  // 5 seconds break
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionStartBtn = findViewById(R.id.session_start_bt);
        sessionCompletedTv = findViewById(R.id.session_completed_tv);
        motivationalQuoteTv = findViewById(R.id.motivation_quote_tv);

        sessionCompletedTv.setVisibility(View.GONE);

        sessionStartBtn.setOnClickListener(v -> {
            if(isTimerRunning){
                return;
            }

            isTimerRunning = true;

            if(isStudyPhase){
                sessionStartBtn.setText("Starting Studying");
                startTimer(STUDY_DURATION);
                sessionStartBtn.setClickable(false);
            }else{
                sessionStartBtn.setText("Have a Snack");
                startTimer(BREAK_DURATION);
                sessionStartBtn.setClickable(false);
            }

        });
    }

    private void startTimer(long durationMillis){
        CountDownTimer countDownTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long l) {
                sessionStartBtn.setText(formatTime(l));
            }
            @Override
            public void onFinish() {
                if(isStudyPhase){
                    isStudyPhase = false;
                    sessionStartBtn.setText("Starting Break...");
                }else{
                    isStudyPhase = true;
                    sessionCounter++;

                    sessionStartBtn.setVisibility(View.GONE);
                    sessionCompletedTv.setVisibility(View.GONE);

                    int randomIndex = (int) Math.random() * quotes.length;
                    motivationalQuoteTv.setText(quotes[randomIndex]);
                    motivationalQuoteTv.setVisibility(View.VISIBLE);

                    new CountDownTimer(5000, 1000){
                        @Override
                        public void onTick(long l) {
                        }

                        @Override
                        public void onFinish() {
                            motivationalQuoteTv.setVisibility(View.GONE);

                            sessionCompletedTv.setText("Congratulations! Session " + sessionCounter + " Completed");
                            sessionCompletedTv.setVisibility(View.VISIBLE);

                            sessionStartBtn.setText("Start Next Session?");
                            sessionStartBtn.setVisibility(View.VISIBLE);
                        }
                    }.start();
                }
                isTimerRunning = false;
                sessionStartBtn.setClickable(true);
            }


        };
        countDownTimer.start();
    }

    private String formatTime(long millis){
        long totalSeconds = millis / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        if(hours > 0)
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else
            return String.format("%02d:%02d", minutes, seconds);

    }
}