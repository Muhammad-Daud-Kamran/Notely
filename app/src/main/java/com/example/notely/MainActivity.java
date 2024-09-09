package com.example.notely;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainsplash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView splashText = findViewById(R.id.splash_text);

        splashText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                splashText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ObjectAnimator textAnimator = ObjectAnimator.ofFloat(splashText, "translationX", -splashText.getWidth(), 0);
                textAnimator.setDuration(2000);
                textAnimator.setInterpolator(new OvershootInterpolator());
                textAnimator.start();
            }
        });


        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN_TIME_OUT);
    }
}