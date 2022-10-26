package com.xw.file_selector.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.xw.file_selector.R;
import com.xw.file_selector.SelectSpec;
import com.xw.file_selector.data.FileInfos;
import com.xw.file_selector.listener.FileSelectCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FileReaders {

    private static FileReaders instance = null;
    private static final String rootDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static FileReaders getInstance() {
        if (instance == null) {
            synchronized (FileReaders.class) {
                if (instance == null) {
                    instance = new FileReaders();
                }
            }
        }
        return instance;
    }

    @SuppressLint("CheckResult")
    public void getFolderList(String path,FileSelectCallBack callBack){
        File[] files = new File(path).listFiles();
        if (files == null) return;

        Observable.create((ObservableOnSubscribe<List<FileInfos>>) emitter -> {
            List<FileInfos> list = new ArrayList<>();

            if (!rootDirectory.equals(path)) {
                FileInfos fi = new FileInfos();
                fi.fileIcon = R.mipmap.ic_selector_folder;
                fi.fileName = "...";
                fi.filePath = null;

                list.add(fi);
            }

            List<File> fileList = Arrays.asList(files);
            //文件排序--按照名称排序
            Collections.sort(fileList, (o1, o2) -> {
                if (o1.isDirectory() && o2.isFile()) {
                    return -1;
                }

                if (o1.isFile() && o2.isDirectory()) {
                    return 1;
                }
                return o1.getName().compareTo(o2.getName());
            });

            for (File file : files) {

                if (file.isDirectory()) {
                    if (file.getName().indexOf(".") != 0) {
                        FileInfos fol = new FileInfos();
                        fol.fileIcon = R.mipmap.ic_selector_folder;
                        fol.fileName = file.getName();
                        fol.filePath = file.getPath();
                        list.add(fol);
                    }
                } else {
                    FileInfos info = getInfos(file);
                    if (info != null) {
                        list.add(info);
                    }
                }
            }
            emitter.onNext(list);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileInfos -> {
                    callBack.onSuccess(fileInfos);
                },throwable -> {
                    throwable.printStackTrace();
                    callBack.onFailer("请求异常");
                });
    }

    private FileInfos getInfos(File file) {

        if (SelectSpec.getInstance().typeItems == null) {
            if (file.getName().contains("xlsx") ||file.getName().contains("xls") ||
                    file.getName().contains("doc") || file.getName().contains("docx") ||
                    file.getName().contains("pdf") || file.getName().contains("txt") ||
                    file.getName().contains("ppt") ||file.getName().contains("pptx") ||
                    file.getName().contains("txt") ||file.getName().contains("zip") ||
                    file.getName().contains("rar")) {

                FileInfos fol = new FileInfos();
                fol.fileName = file.getName();
                fol.filePath = file.getPath();
                fol.fileIcon = getIconByType(fol.fileName);

//                if (file.getName().contains("xlsx") ||file.getName().contains("xls")) {
//                    fol.fileIcon = R.mipmap.ic_selector_xlsx;
//                } else if (file.getName().contains("doc") || file.getName().contains("docx")) {
//                    fol.fileIcon = R.mipmap.ic_selector_doc;
//                } else if (file.getName().contains("ppt") || file.getName().contains("pptx")) {
//                    fol.fileIcon = R.mipmap.ic_selector_ppt;
//                } else if (file.getName().contains("zip") || file.getName().contains("rar")) {
//                    fol.fileIcon = R.mipmap.ic_selector_zip;
//                } else if (file.getName().contains("pdf")) {
//                    fol.fileIcon = R.mipmap.ic_selector_pdf;
//                } else if (file.getName().contains("txt")) {
//                    fol.fileIcon = R.mipmap.ic_selector_txt;
//                }

                return fol;
            } else {
                return null;
            }
        } else {
            FileInfos fol = null;
            for (int i = 0; i < SelectSpec.getInstance().typeItems.length; i++) {
                if (file.getName().contains(SelectSpec.getInstance().typeItems[i])) {
                    fol = new FileInfos();
                    fol.fileName = file.getName();
                    fol.filePath = file.getPath();
                    fol.fileIcon = getIconByType(fol.fileName);

                    break;
                }
            }

            return fol;
        }
    }

    private int getIconByType(String fileName) {
        if (fileName.contains("xlsx") ||fileName.contains("xls")) {
            return R.mipmap.ic_selector_xlsx;
        } else if (fileName.contains("doc") || fileName.contains("docx")) {
            return R.mipmap.ic_selector_doc;
        } else if (fileName.contains("ppt") || fileName.contains("pptx")) {
            return R.mipmap.ic_selector_ppt;
        } else if (fileName.contains("zip") || fileName.contains("rar")) {
            return R.mipmap.ic_selector_zip;
        } else if (fileName.contains("pdf")) {
            return R.mipmap.ic_selector_pdf;
        } else if (fileName.contains("txt")) {
            return R.mipmap.ic_selector_txt;
        } else {
            return R.mipmap.ic_selector_txt;
        }
    }

    private static File getRootFile(Context context) {
        File file;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // /storage/emulated/0/Android/data/com.example.demo/files
            File externalFileDir = context.getExternalFilesDir(null);
            do {
                externalFileDir = Objects.requireNonNull(externalFileDir).getParentFile();
            } while(Objects.requireNonNull(externalFileDir).getAbsolutePath().contains("/Android"));
            file = Objects.requireNonNull(externalFileDir);
        } else {
            file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile().toURI());
        }
        return file;
    }

    public static String getRootPath(Context context) {
        File file = getRootFile(context);
        return file.getAbsolutePath();
    }
}
