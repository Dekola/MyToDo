package com.kola.mytodo.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kola.mytodo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFragment extends Fragment {


    public IntroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.fragment_intro, container, false);

        view.findViewById(R.id.frg_intro_login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.frame, new LoginFragment());
//                fragmentManager.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentManager.commit();
            }
        });

        view.findViewById(R.id.frg_intro_sign_up_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.frame, new SignUpFragment());
//                fragmentManager.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentManager.commit();
            }
        });

        return view;
    }

}
