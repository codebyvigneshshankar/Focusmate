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

    private Button resetBtn;
    private TextView sessionCompletedTv;
    private TextView motivationalQuoteTv;

    private long timeLeftInMillis;
    private long currentDuration;

    private CountDownTimer countDownTimer;

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

        resetBtn = findViewById(R.id.reset_btn);

        sessionCompletedTv.setVisibility(View.GONE);

        sessionStartBtn.setOnClickListener(v -> {
            if(!isTimerRunning){
                if(timeLeftInMillis == 0){
                    currentDuration = isStudyPhase ? STUDY_DURATION : BREAK_DURATION;
                    timeLeftInMillis = currentDuration;
                }
                startTimer();
            }else{
                pauseTimer();
            }
        });

        resetBtn.setOnClickListener(view -> resetTimer());
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                sessionStartBtn.setText(formatTime(timeLeftInMillis));
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                timeLeftInMillis = 0;

                if (isStudyPhase) {
                    isStudyPhase = false;
                    sessionStartBtn.setText("Break Time!");
                } else {
                    isStudyPhase = true;
                    sessionCounter++;

                    resetBtn.setVisibility(View.GONE);
                    sessionStartBtn.setVisibility(View.GONE);

                    int randomIndex = (int) (Math.random() * quotes.length);
                    motivationalQuoteTv.setText(quotes[randomIndex]);
                    motivationalQuoteTv.setVisibility(View.VISIBLE);
                    sessionCompletedTv.setVisibility(View.GONE);



                    new CountDownTimer(5000, 1000){

                        @Override
                        public void onFinish() {
                            motivationalQuoteTv.setVisibility(View.GONE);

                            sessionCompletedTv.setText("Session's Completed: " + sessionCounter + " 🎉");
                            sessionCompletedTv.setVisibility(View.VISIBLE);

                            sessionStartBtn.setText("Start New Session?");
                            sessionStartBtn.setVisibility(View.VISIBLE);
                            resetBtn.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onTick(long l) {

                        }
                    }.start();


                }
            }
        }.start();

        isTimerRunning = true;
        sessionStartBtn.setText("Pause");
    }

    private void pauseTimer(){
        countDownTimer.cancel();
        isTimerRunning = false;
        sessionStartBtn.setText("Resume");
    }

    private void resetTimer(){
        if(countDownTimer != null){
            countDownTimer.cancel();
        }

        sessionCounter = 0;
        isTimerRunning = false;
        timeLeftInMillis = 0;
        isStudyPhase = true;

        sessionStartBtn.setText("Start Studying");
        motivationalQuoteTv.setVisibility(View.GONE);
        sessionCompletedTv.setVisibility(View.GONE);
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