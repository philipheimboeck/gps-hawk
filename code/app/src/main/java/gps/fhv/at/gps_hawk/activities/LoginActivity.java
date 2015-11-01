package gps.fhv.at.gps_hawk.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import gps.fhv.at.gps_hawk.R;
import gps.fhv.at.gps_hawk.helper.TokenHelper;
import gps.fhv.at.gps_hawk.workers.DbSetup;
import gps.fhv.at.gps_hawk.tasks.CheckUserTask;
import gps.fhv.at.gps_hawk.tasks.IAsyncTaskCaller;
import gps.fhv.at.gps_hawk.tasks.LoginTask;
import gps.fhv.at.gps_hawk.tasks.RegisterTask;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private Button mSignInButton;

    enum LoginAction {
        LOGIN,
        REGISTRATION
    }

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AsyncTask<Void, Void, String> mAuthTask = null;
    private CheckUserTask mCheckUserTask = null;

    // Define the login Action
    private LoginAction mLoginAction;

    // UI references.
    private EditText mUserView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;
    private TextView mUserStatusView;
    private View mProgressView;
    private View mLoginFormView;
    private View mLoginPasswordsFormView;
    private Button mCheckUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Create Database
        dbSetup();

        // Check for valid token to skip login process
        String token = TokenHelper.getToken(this);
        if (token != null) {
            loginFinished();
        }

        // Set up the login form.
        mUserView = (EditText) findViewById(R.id.user);
        mUserView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mLoginPasswordsFormView.setVisibility(View.GONE);
                mCheckUserButton.setVisibility(View.VISIBLE);
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mPasswordConfirmView = (EditText) findViewById(R.id.password_confirm);
        mPasswordConfirmView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mCheckUserButton = (Button) findViewById(R.id.button_check);
        mCheckUserButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginPasswordsFormView = findViewById(R.id.user_login_passwords_form);
        mProgressView = findViewById(R.id.login_progress);
        mUserStatusView = (TextView) findViewById(R.id.user_status_text);
    }

    private void dbSetup() {
        try {
            DbSetup db = new DbSetup(this);
            db.getWritableDatabase();
        } catch (Exception e) {
            Log.e("FATAL", "Could not create Database", e);
        }
    }

    private void checkUser() {
        if (mCheckUserTask != null) {
            return;
        }

        View focusView = null;
        boolean cancel = false;

        // Check for a valid user
        if (TextUtils.isEmpty(mUserView.getText().toString())) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();

        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);


            mCheckUserTask = new CheckUserTask(mUserView.getText().toString(), new IAsyncTaskCaller<Void, Boolean>() {
                @Override
                public void onPostExecute(Boolean existing) {
                    mCheckUserTask = null;
                    showProgress(false);

                    // Set visibilities correctly
                    mLoginPasswordsFormView.setVisibility(View.VISIBLE);
                    mCheckUserButton.setVisibility(View.GONE);

                    if (existing) {
                        // If user exists, you may login
                        mLoginAction = LoginAction.LOGIN;
                        mPasswordConfirmView.setVisibility(View.GONE);
                        mUserStatusView.setText(getText(R.string.user_status_existing));
                        mSignInButton.setText(getText(R.string.action_sign_in));

                    } else {
                        // If user doesn't exist, you may register
                        mLoginAction = LoginAction.REGISTRATION;
                        mPasswordConfirmView.setVisibility(View.VISIBLE);
                        mUserStatusView.setText(getText(R.string.user_status_available));
                        mSignInButton.setText(getText(R.string.action_register));
                    }
                }

                @Override
                public void onCancelled() {
                    mCheckUserTask = null;
                    showProgress(false);
                    mLoginAction = null;
                }

                @Override
                public void onProgressUpdate(Void... progress) {

                }

                @Override
                public void onPreExecute() {

                }
            });
            mCheckUserTask.execute();
        }

    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);
        mPasswordConfirmView.setError(null);

        // Store values at the time of the login attempt.
        String user = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for the password confirmation
        if (mLoginAction == LoginAction.REGISTRATION && !password.equals(mPasswordConfirmView.getText().toString())) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordConfirmView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid user
        if (TextUtils.isEmpty(user)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            // Get the Android ID
            String androidId = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            // Start the action
            if (mLoginAction == LoginAction.LOGIN) {
                // Start the login task
                startLoginAction(user, password, androidId);
            } else {
                // Start the registration task
                startRegistrationAction(user, password, androidId);
            }

        }
    }

    private void startLoginAction(String user, String password, String androidId) {

        mAuthTask = new LoginTask(user, password, androidId, new IAsyncTaskCaller<Void, String>() {
            @Override
            public void onPostExecute(String token) {
                mAuthTask = null;
                showProgress(false);

                if (token != null) {
                    TokenHelper.setToken(getApplicationContext(), token);
                    loginFinished();
                } else {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            }

            @Override
            public void onCancelled() {
                mAuthTask = null;
                showProgress(false);
            }

            @Override
            public void onProgressUpdate(Void... progress) {

            }

            @Override
            public void onPreExecute() {

            }
        });

        mAuthTask.execute();
    }

    private void startRegistrationAction(String user, String password, String androidId) {
        mAuthTask = new RegisterTask(user, password, androidId, new IAsyncTaskCaller<Void, String>() {
            @Override
            public void onPostExecute(String token) {
                mAuthTask = null;
                showProgress(false);

                if (token != null) {
                    TokenHelper.setToken(getApplicationContext(), token);
                    loginFinished();
                }
            }

            @Override
            public void onCancelled() {
                mAuthTask = null;
                showProgress(false);
            }

            @Override
            public void onProgressUpdate(Void... progress) {

            }

            @Override
            public void onPreExecute() {

            }
        });

        mAuthTask.execute();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Call when login is finished.
     * Will close this activity and call the next one.
     */
    private void loginFinished() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivity(intent);
        finish();
    }

}

