package com.xw.file_selector;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.xw.file_selector.listener.OnActivityResult;
import com.xw.file_selector.ui.SelectActivity;

import java.util.List;

public class SelectCreator {

    private FileSelector fileSelector;
    private SelectSpec selectSpec;
    private Router router;

    public SelectCreator(FileSelector selector,String[] typeItems) {
        this.fileSelector = selector;
        selectSpec = SelectSpec.clearInstance();
        selectSpec.typeItems = typeItems;
    }

    public SelectCreator(Router router,String[] typeItems) {
        this.router = router;
        selectSpec = SelectSpec.clearInstance();
        selectSpec.typeItems = typeItems;
    }

    public SelectCreator maxSelect(int maxLength) {
        if (maxLength < 1) {
            throw new IllegalArgumentException("maxSelectable must be greater than or equal to one");
        }

        selectSpec.maxSelectable = maxLength;
        return this;
    }

    /**
     * 添加/移除 文件
     */
    public SelectCreator addStringToSelects(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            selectSpec.addStringToSelects(filePath);
        }

        return this;
    }

    /**
     * 添加选中集合
     */
    public SelectCreator addListToSelects(List<String> imagePaths) {
        if (imagePaths != null) {
            selectSpec.addListToSelects(imagePaths);
        }

        return this;
    }

    public void start(int requestCode) {
        Activity activity = fileSelector.getActivity();
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, SelectActivity.class);

        Fragment fragment = fileSelector.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent,requestCode);
        } else {
            activity.startActivityForResult(intent,requestCode);
        }
    }

    public void startLaincher(OnActivityResult activityResult) {
        if (router == null) {
            return;
        }

        router.setActivityResult(activityResult);
        Activity activity = router.getActivity();

        Intent intent = new Intent(activity,SelectActivity.class);
        router.getActivityResult().launch(intent);
    }
}
