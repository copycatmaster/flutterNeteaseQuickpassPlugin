package com.loongwalk.flutter_neteasequickpass_example;

import io.flutter.embedding.android.FlutterActivity;
import android.os.Bundle;
import com.loongwalk.flutter_neteasequickpass.FlutterNeteasequickpassPlugin;

//FlutterActivity实现了.PluginRegistry
public class MainActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlutterNeteasequickpassPlugin.registerWith(this,getApplicationContext());
    }
}
