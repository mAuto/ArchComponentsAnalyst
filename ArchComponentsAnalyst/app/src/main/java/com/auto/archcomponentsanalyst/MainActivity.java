package com.auto.archcomponentsanalyst;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.auto.archcomponentsanalyst.router.RoutPointer;
import com.auto.archcomponentsanalyst.router.Router;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickWorkManager(View view) {
        Router.jump(this, RoutPointer.WORKMANAGER);
    }
}
