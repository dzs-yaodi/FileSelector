package com.xw.fileselector;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.xw.file_selector.FileSelector;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_xlsx).setOnClickListener(view -> {

            FileSelector.create(this)
                    .choose(new String[]{"xlsx","pdf"})
                    .maxSelect(1)
                    .start(1001);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            List<String> paths = FileSelector.obtainPathsResult(data);
            if (paths != null && paths.size() > 0) {
                for (int i = 0; i < paths.size(); i++) {
                    Log.d("info","==文件地址=" + paths.get(i));
                }
            }
        }
    }
}