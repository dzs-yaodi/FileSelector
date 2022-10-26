package com.xw.file_selector.listener;

import com.xw.file_selector.data.FileInfos;

import java.util.List;

public interface FileSelectCallBack {

    void onSuccess(List<FileInfos> infos);

    void onFailer(String message);
}
