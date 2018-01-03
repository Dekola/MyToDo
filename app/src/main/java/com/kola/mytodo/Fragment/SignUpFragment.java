package com.kola.mytodo.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kola.mytodo.R;
import com.kola.mytodo.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
public class SignUpFragment extends Fragment {

    private FirebaseAuth mAuth;
    String username, password, email, confirmPassword;
    EditText username_et, password_et, email_et, confirmPassword_et;
    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        username_et = view.findViewById(R.id.frg_sign_up_username_et);
//        username_et.setText("kola");
        password_et = view.findViewById(R.id.frg_sign_up_password_et);
//        password_et.setText("muideen");
        email_et = view.findViewById(R.id.frg_sign_up_email_et);
//        email_et.setText("akano@gmail.com");
        confirmPassword_et = view.findViewById(R.id.frg_sign_up_confirm_password_et);
//        confirmPassword_et.setText("muideen");

        (view.findViewById(R.id.frg_sign_up_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = username_et.getText().toString();
                email = email_et.getText().toString();
                password = password_et.getText().toString();
                confirmPassword = confirmPassword_et.getText().toString();

                if (username.equals(""))username_et.setError("set a username");
                else if (username.length()<4)username_et.setError("username should be at least 4 characters");
                else if (!validateEmail(email))email_et.setError("invalid email");
                else if (password.isEmpty())password_et.setError("enter a password");
                else if (password.length()<6)password_et.setError("password should be at least 6 characters");
                else if (!password.equals(confirmPassword))confirmPassword_et.setError("passwords don't match");
                else signUp();
            }
        });

        (view.findViewById(R.id.forgot_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
//                fragmentManager.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentManager.replace(R.id.frame, new ResetPasswordFragment());
                fragmentManager.commit();
            }
        });
        (view.findViewById(R.id.login_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
//                fragmentManager.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentManager.replace(R.id.frame, new LoginFragment());
                fragmentManager.commit();

            }
        });

        return view;
    }

    private void signUp() {

            final AlertDialog dialog = new SpotsDialog(getActivity(), R.style.Custom);
            dialog.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                showToast("success");

                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                String userId = user.getUid();

                                Intent it = new Intent(getActivity(), Test.class);
                                it.putExtra("text",name+" "+email+" "+userId);
                                startActivity(it);
                            } else {
                                Log.w("email auth", "createUserWithEmail:failure", task.getException());
                                showToast("Authentication failed.");
                            }

                            dialog.dismiss();
                        }
                    });
    }

    public static boolean validateEmail(String email) {
        String expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$";
        CharSequence inputString = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches()) return true;
        else return false;
    }


    public void showToast(String message){

        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

    }

}

