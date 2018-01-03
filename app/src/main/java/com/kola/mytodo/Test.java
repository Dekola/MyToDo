package com.kola.mytodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class Test extends AppCompatActivity {

    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        FirebaseAuth.getInstance();

        Bundle b = getIntent().getExtras();

        text = b.getString("text");

        ((TextView) findViewById(R.id.text)).setText(text);


        (findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                startActivity(new Intent(getApplicationContext(), AuthActivity.class));
            }
        });

    }
}
//
//
//
//    <?xml version="1.0" encoding="utf-8"?>
//<resources>
//<style name="Custom" parent="android:Theme.DeviceDefault.Dialog">
//<item name="DialogTitleAppearance">@android:style/TextAppearance.Medium</item>
//<item name="DialogTitleText">Updating…</item>
//<item name="DialogSpotColor">@android:color/holo_orange_dark</item>
//<item name="DialogSpotCount">4</item>
//</style>
//</resources>
//        Pass it into constuctor:
//

//    AlertDialog dialog = new SpotsDialog(context);
//dialog.show();
//        ...
//        dialog.dismiss();
//        new SpotsDialog(context, R.style.Custom).show();