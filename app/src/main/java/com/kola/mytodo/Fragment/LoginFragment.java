package com.kola.mytodo.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    EditText email_et, password_et;
    String email, password;
    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email_et = view.findViewById(R.id.frg_login_email_et);
//        email_et.setText("akano@gmail.com");
        password_et = view.findViewById(R.id.frg_login_password_et);
//        password_et.setText("muideen");

        (view.findViewById(R.id.frg_login_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = email_et.getText().toString();
                password = password_et.getText().toString();

                if (!validateEmail(email))email_et.setError("invalid email");
                else if (password.isEmpty())password_et.setError("enter a password");
                else if (password.length()<6)password_et.setError("password should be at least 6 characters");
                else login();
            }
        });

        (view.findViewById(R.id.forgot_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.frame, new ResetPasswordFragment());
//                fragmentManager.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentManager.commit();
            }
        });
        (view.findViewById(R.id.signUp_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
//                fragmentManager.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentManager.replace(R.id.frame, new SignUpFragment());
                fragmentManager.commit();

            }
        });
//
        return view;
    }

    private void login() {

        final AlertDialog dialog = new SpotsDialog(getActivity(), R.style.Custom);
        dialog.show();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            String name = user.getDisplayName();
                            String email = user.getEmail();
                            String userId = user.getUid();

                            Intent it = new Intent(getActivity(), Test.class);
                            it.putExtra("text",name+" "+email+" "+userId);
                            startActivity(it);
                        } else {

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

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

}
