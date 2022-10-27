package com.xw.file_selector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xw.file_selector.ui.FileSelectActivity;

import java.lang.ref.WeakReference;
import java.util.List;

public class FileSelector {

    private WeakReference<Activity> mActivity;
    private WeakReference<Fragment> mFragment;

    private FileSelector(Activity activity) {
        this(activity,null);
    }

    private FileSelector(Fragment fragment) {
        this(fragment.getActivity(),fragment);
    }

    public FileSelector(Activity activity, Fragment fragment) {
        mActivity = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    public static FileSelector create(Context context) {
        return new FileSelector((Activity) context);
    }

    public static FileSelector create(Activity activity) {
        return new FileSelector(activity);
    }

    public static FileSelector create(Fragment fragment) {
        return new FileSelector(fragment);
    }

    public SelectCreator choose(String[] items) {
        return new SelectCreator(this,items);
    }

    public static List<String> obtainPathsResult(Intent intent) {
        return intent.getStringArrayListExtra(FileSelectActivity.REQUEST_SELECT_MEDIAS);
    }

    @Nullable
    Activity getActivity() {
        return mActivity.get();
    }

    @Nullable
    Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }
}
