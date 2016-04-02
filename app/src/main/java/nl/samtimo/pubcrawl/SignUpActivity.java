package nl.samtimo.pubcrawl;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText mUsernameView;
    TextInputEditText mPasswordView;
    TextInputEditText mPassword2View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mUsernameView = (TextInputEditText) findViewById(R.id.username);
        mPasswordView = (TextInputEditText) findViewById(R.id.password);
        mPassword2View = (TextInputEditText) findViewById(R.id.password_repeat);

        Button signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        Button simpleSignUpButton = (Button) findViewById(R.id.simple_sign_up_button);
        simpleSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Request request = new Request(RequestMethod.POST, "signup", "username=hitchhiker&password=password", null);
                new RequestTask(SignUpActivity.this).execute(request);
            }
        });
    }

    private void attemptSignUp(){
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mPassword2View.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String password2 = mPassword2View.getText().toString();

        boolean cancel = false;

        //TODO testing errors

        System.out.println(password.equals(password2));
        if(password.equals(password2)){
            Request request = new Request(RequestMethod.POST, "signup", "username="+username+"&password="+password, null);
            new RequestTask(SignUpActivity.this).execute(request);
        }
    }

    public void openMenu(){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
