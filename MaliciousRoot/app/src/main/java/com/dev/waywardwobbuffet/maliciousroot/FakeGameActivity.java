package com.dev.waywardwobbuffet.maliciousroot;

import android.app.Activity;
import android.os.Bundle;


public class FakeGameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_game);
    }

}
