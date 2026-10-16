package com.moez.splashscreen;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Patterns;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showSplashScreen();

        new Handler(Looper.getMainLooper()).postDelayed(
                this::showLoginScreen,
                1800
        );
    }

    private void showSplashScreen() {
        LinearLayout splash = new LinearLayout(this);
        splash.setGravity(Gravity.CENTER);
        splash.setOrientation(LinearLayout.VERTICAL);

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{
                        Color.rgb(23, 105, 255),
                        Color.rgb(68, 56, 232),
                        Color.rgb(154, 27, 250)
                }
        );
        splash.setBackground(gradient);

        LinearLayout logoBackground = new LinearLayout(this);
        logoBackground.setGravity(Gravity.CENTER);
        logoBackground.setBackground(
                makeRoundedBackground(Color.rgb(36, 107, 253), 28)
        );

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.logo_bag);
        logo.setScaleType(ImageView.ScaleType.FIT_CENTER);

        logoBackground.addView(
                logo,
                new LinearLayout.LayoutParams(dp(92), dp(92))
        );

        splash.addView(
                logoBackground,
                new LinearLayout.LayoutParams(dp(120), dp(120))
        );

        TextView appName = new TextView(this);
        appName.setText("SwiftShop");
        appName.setTextColor(Color.WHITE);
        appName.setTextSize(34);
        appName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        appName.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        nameParams.topMargin = dp(20);
        splash.addView(appName, nameParams);

        TextView tagline = new TextView(this);
        tagline.setText("SHOP SMART. LIVE EASY.");
        tagline.setTextColor(Color.rgb(230, 233, 255));
        tagline.setTextSize(12);
        tagline.setLetterSpacing(0.12f);
        tagline.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams taglineParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        taglineParams.topMargin = dp(8);
        splash.addView(tagline, taglineParams);

        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(800);
        splash.startAnimation(fadeIn);

        setContentView(splash);
    }

    private GradientDrawable makeRoundedBackground(int color, int radiusDp) {
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(color);
        shape.setCornerRadius(dp(radiusDp));
        return shape;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private void showLoginScreen() {
        setContentView(R.layout.activity_main);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        findViewById(R.id.loginButton).setOnClickListener(v -> login());

        findViewById(R.id.googleButton).setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Connect Firebase to enable Google sign-in.",
                        Toast.LENGTH_LONG
                ).show()
        );

        findViewById(R.id.signUpLink).setOnClickListener(v -> showSignUpForm());

        findViewById(R.id.forgotPassword).setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Enter your email to reset your password.",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email address");
            emailInput.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            passwordInput.requestFocus();
            return;
        }

        Toast.makeText(
                this,
                "Login form validated. Connect Firebase for real login.",
                Toast.LENGTH_LONG
        ).show();
    }

    private void showSignUpForm() {
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(dp(24), dp(8), dp(24), dp(4));

        EditText nameInput = createField(
                "Full name",
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS
        );

        EditText signupEmail = createField(
                "Email address",
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        );

        EditText signupPassword = createField(
                "Password (at least 6 characters)",
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        EditText confirmPassword = createField(
                "Confirm password",
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        form.addView(nameInput);
        form.addView(signupEmail);
        form.addView(signupPassword);
        form.addView(confirmPassword);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Create your SwiftShop account")
                .setView(form)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Create account", null)
                .create();

        dialog.setOnShowListener(unused ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                    String name = nameInput.getText().toString().trim();
                    String email = signupEmail.getText().toString().trim();
                    String password = signupPassword.getText().toString();
                    String confirm = confirmPassword.getText().toString();

                    if (name.length() < 2) {
                        nameInput.setError("Enter your name");
                    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        signupEmail.setError("Enter a valid email address");
                    } else if (password.length() < 6) {
                        signupPassword.setError("Use at least 6 characters");
                    } else if (!password.equals(confirm)) {
                        confirmPassword.setError("Passwords do not match");
                    } else {
                        Toast.makeText(
                                this,
                                "Form validated. Connect Firebase to create the account.",
                                Toast.LENGTH_LONG
                        ).show();
                        dialog.dismiss();
                    }
                })
        );

        dialog.show();
    }

    private EditText createField(String hint, int inputType) {
        EditText field = new EditText(this);
        field.setHint(hint);
        field.setInputType(inputType);
        field.setSingleLine(true);
        return field;
    }
}