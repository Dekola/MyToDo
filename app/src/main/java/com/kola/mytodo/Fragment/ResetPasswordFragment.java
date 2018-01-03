package com.kola.mytodo.Fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.kola.mytodo.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPasswordFragment extends Fragment {


    public ResetPasswordFragment() {
        // Required empty public constructor
    }
        EditText email_et;
    FirebaseAuth mAuth;
    String email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);

        email_et = view.findViewById(R.id.frg_reset_password_email_et);

        view.findViewById(R.id.frg_reset_password_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = email_et.getText().toString();
                if (!validateEmail(email))email_et.setError("invalid email");
                else reset_password();
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

        (view.findViewById(R.id.signUp_tv)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
//                fragmentManager.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentManager.replace(R.id.frame, new SignUpFragment());
                fragmentManager.commit();

            }
        });

        return view;
    }

    private void reset_password() {

        final android.app.AlertDialog dialog = new SpotsDialog(getActivity(), R.style.Custom);
        dialog.show();

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    showdialog();
                }
                else {
                    showToast("Failed to reset password");
                }
                dialog.dismiss();
            }
        });
    }

    private void showdialog() {

        AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());
        mDialog.setMessage("Password reset link has been sent to your email address");
        mDialog.setNeutralButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                email_et.setText("");

                FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
//                fragmentManager.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentManager.replace(R.id.frame, new LoginFragment());
                fragmentManager.commit();
            }
        });
        mDialog.show();

    }

    public static boolean validateEmail(String email) {
        String expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$";
        CharSequence inputString = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches()) return true;
        else return false;
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
