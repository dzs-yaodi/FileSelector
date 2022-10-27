package com.xw.file_selector.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xw.file_selector.R;
import com.xw.file_selector.SelectSpec;
import com.xw.file_selector.databinding.FileSelectorActivitySelectBinding;
import com.xw.file_selector.utils.FileReaders;
import com.xw.file_selector.adapter.FileAdapter;
import com.xw.file_selector.data.FileInfos;
import com.xw.file_selector.listener.FileSelectCallBack;
import com.xw.file_selector.listener.OnItemListener;
import com.xw.file_selector.utils.StatusBarHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSelectActivity extends AppCompatActivity  {

    private FileSelectorActivitySelectBinding binding;
    private List<FileInfos> folderlist = new ArrayList<>();
    private int checkPosition = 0;
    private FileAdapter fileAdapter;
    private String path = FileReaders.getRootPath(this);
    /**
     * 返回选择媒体资源
     */
    public static final String REQUEST_SELECT_MEDIAS = "select_medias";


    //https://blog.csdn.net/sinat_31062885/article/details/84567879

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FileSelectorActivitySelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarHelper.translucent(this);

        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        fileAdapter = new FileAdapter(folderlist);
        binding.recycler.setAdapter(fileAdapter);

        setClick();
        refreshListItems(path);
    }

    private void setClick() {

        binding.tvBack.setOnClickListener(view -> {
            finish();
        });
        binding.tvFinish.setOnClickListener(view -> {
            Intent intent = new Intent();
            ArrayList<String> list = new ArrayList<>(SelectSpec.getInstance().getSelectList());
            intent.putStringArrayListExtra(REQUEST_SELECT_MEDIAS,list);
            setResult(RESULT_OK,intent);
            finish();
        });

        fileAdapter.setOnItemListener(new OnItemListener() {
            @Override
            public void onItemClick(int position) {
                path = folderlist.get(position).filePath;
                if (path  == null) {
                    goToParent();
                } else {
                    File file = new File(path);
                    if (file.isDirectory()) {
                        refreshListItems(path);
                    }
                }

            }

            @Override
            public void onItemSelectListener(int position) {
                checkPosition = position;

                FileInfos infos = folderlist.get(position);
                int type = SelectSpec.getInstance().addStringToSelects(infos.filePath);
                if (type > 0) {
                    if (type == 1) {
                        infos.isChecked = true;
                    } else {
                        infos.isChecked = false;
                    }
                    fileAdapter.notifyDataSetChanged();
                } else {
                    String message =  String.format(getString(R.string.select_file_max),
                            SelectSpec.getInstance().maxSelectable);
                    Toast.makeText(FileSelectActivity.this,message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void refreshListItems(String path) {
        FileReaders.getInstance().getFolderList(path, new FileSelectCallBack() {
            @Override
            public void onSuccess(List<FileInfos> infos) {
                folderlist.clear();
                folderlist.addAll(infos);
                fileAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailer(String message) {
                Toast.makeText(FileSelectActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 返回上一级目录
     */
    private void goToParent() {
        if (!FileReaders.getRootPath(this).equals(path)) {
            File file = new File(path);
            File str_pa = file.getParentFile();
            if (str_pa.equals(FileReaders.getRootPath(this))) {
                Toast.makeText(this, "已经是根目录",Toast.LENGTH_SHORT).show();
                refreshListItems(path);
            } else {
                path = str_pa.getAbsolutePath();
                refreshListItems(path);
            }
        } else {
            Toast.makeText(this, "已经是根目录",Toast.LENGTH_SHORT).show();
            refreshListItems(path);
        }
    }
}
