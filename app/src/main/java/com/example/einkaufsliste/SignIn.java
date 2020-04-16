package com.example.einkaufsliste;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignIn extends AppCompatActivity {
    private Button button;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        MyRequest.PASSWORD = getString(R.string.pwd);

        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        final View.OnClickListener defaultButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String nutzerName = editText.getText().toString();

                try {
                    NutzerListe.update();

                    int nutzerInt = NutzerListe.getIdFromName(nutzerName);
                    if (nutzerInt==-1){
                        Toast.makeText(getApplicationContext(), "den nutzer gibt es noch nicht, vertippt? dann korregier das, sonst: registrieren?", Toast.LENGTH_LONG).show();
                        button.setText("jetzt registrieren");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view){
                                String nutzerName = editText.getText().toString();
                                if (nutzerName!=""){
                                    int nutzerInt = 0;
                                    try {
                                        nutzerInt = NutzerListe.createNutzer(nutzerName);
                                    } catch (InternetProblemException e) {
                                        Toast.makeText(getApplicationContext(), "ohne Internet kann kein Nutzer angelegt werden", Toast.LENGTH_LONG).show();
                                    }
                                    if (nutzerInt==-2)
                                        Toast.makeText(getApplicationContext(), "name schon vergeben", Toast.LENGTH_LONG).show();
                                    if (nutzerInt==-1)
                                        Toast.makeText(getApplicationContext(), "da ist was ganz komisches passiert. mein fehler", Toast.LENGTH_LONG).show();
                                    if (nutzerInt>=0){
                                        Toast.makeText(getApplicationContext(), "willkommen "+nutzerName, Toast.LENGTH_LONG).show();
                                        next(nutzerInt);
                                    }
                                }else Toast.makeText(getApplicationContext(), "der name kann nicht leer sein", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        next(nutzerInt);
                    }
                } catch (InternetProblemException e) {
                    Toast.makeText(getApplicationContext(), "Internet Probleme..", Toast.LENGTH_LONG).show();
                }
            }
        };
        button.setOnClickListener(defaultButtonListener);
        /*/button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Animation animation = AnimationUtils.loadAnimation(SignIn.this, R.anim.correct);
                //button.startAnimation(animation);

                ValueAnimator anim = ValueAnimator.ofInt(button.getMeasuredWidth(), 150);

                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
                        layoutParams.width = val;
                        button.requestLayout();
                    }
                });

                anim.setDuration(250);
                anim.start();
                ImageView tick = findViewById(R.id.imageView4);
                tick.bringToFront();
                tick.setVisibility(View.VISIBLE);
                button.setText("");


            }
        });*/
        editText.setOnKeyListener(new View.OnKeyListener() {//bei aenderung des eingegebenen namens
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                button.setOnClickListener(defaultButtonListener);
                button.setText("sign in");
                return false;
            }
        });
    }
    private void next(int nutzerInt){
        final TextView layout_helper1 = findViewById(R.id.layout_helper1);

        int height = layout_helper1.getHeight();
        Toast.makeText(getApplicationContext(), "willkommen "+height, Toast.LENGTH_LONG).show();

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout_helper1.getLayoutParams();
        final int oldTopMargin = params.topMargin;
        final int newTopMargin = 176;
        Toast.makeText(this, ""+oldTopMargin, Toast.LENGTH_SHORT).show();

        Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout_helper1.getLayoutParams();
                params.topMargin = oldTopMargin + (int) ((newTopMargin - oldTopMargin) * interpolatedTime);
                layout_helper1.setLayoutParams(params);
            }
        };
        a.setDuration(1000); // in ms
        layout_helper1.startAnimation(a);
        NutzerListe.setCurrentNutzer(nutzerInt);
        final Intent intent = new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        new CountDownTimer(1000, 500) {
            public void onFinish() {
                startActivity(intent);
            }

            public void onTick(long millisUntilFinished) {}
        }.start();

    }

}
