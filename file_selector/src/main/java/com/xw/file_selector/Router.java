package com.xw.file_selector;

import android.app.Activity;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.xw.file_selector.listener.OnActivityResult;

import java.lang.ref.WeakReference;

public class Router {

    private static Router instance = null;
    private static ActivityResultLauncher<Intent> activityResult;
    private WeakReference<Activity> mActivity;
    private WeakReference<Fragment> mFragment;
    private OnActivityResult onActivityResult;

    public static Router getInstance() {
        if (instance == null) {
            synchronized (Router.class) {
                if (instance == null) {
                    instance = new Router();
                }
            }
        }
        return instance;
    }

    public void init(AppCompatActivity activity) {
        add(activity,null);
        activityResult = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    onActivityResult.onActivityResult(result);
                }
        );
    }

    public void init(Fragment fragment) {
        add(fragment.getActivity(),fragment);
        activityResult = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    onActivityResult.onActivityResult(result);
                }
        );
    }

    public Router() {
    }

    public void add(Activity mActivity, Fragment mFragment) {
        this.mActivity = new WeakReference<>(mActivity);
        this.mFragment = new WeakReference<>(mFragment);
    }

    public SelectCreator choose(String[] items) {
        return new SelectCreator(this,items);
    }

    public ActivityResultLauncher<Intent> getActivityResult() {
        return activityResult;
    }

    @Nullable
    Activity getActivity() {
        return mActivity.get();
    }

    @Nullable
    Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }

    public void setActivityResult(OnActivityResult onActivityResult) {
        this.onActivityResult = onActivityResult;
    }
}
