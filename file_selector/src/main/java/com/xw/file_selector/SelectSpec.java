package com.xw.file_selector;

import java.util.ArrayList;
import java.util.List;

public class SelectSpec {

    /**
     * 选择文件类型数组
     */
    public String[] typeItems;

    /**
     * 最大选择数量
     */
    public int maxSelectable;

    /**
     * 已经选中数据集合
     */
    private ArrayList<String> mediaPathList = new ArrayList<>();

    public static SelectSpec getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static final class InstanceHolder {
        private static final SelectSpec INSTANCE = new SelectSpec();
    }

    public static SelectSpec clearInstance() {
        SelectSpec selectSpec = getInstance();
        selectSpec.reset();
        return selectSpec;
    }

    private void reset() {
        typeItems = null;
        maxSelectable = 1;
        mediaPathList = new ArrayList<>();
    }

    public int getSelectLength() {
        return mediaPathList.size();
    }

    public ArrayList<String> getSelectList() {
        return mediaPathList;
    }

    /**
     * 添加、移除选择的文件
     */
    public int addStringToSelects(String filePath) {
        if (mediaPathList.contains(filePath)) {
            mediaPathList.remove(filePath);
            return 2;
        } else {
            if (getSelectLength() < maxSelectable) {
                mediaPathList.add(filePath);
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * 添加选中的集合
     */
    public void addListToSelects(List<String> strings) {
        if (strings != null) {
            for (int i = 0; i < strings.size(); i++) {
                String path = strings.get(i);
                if (!mediaPathList.contains(path) && getSelectLength() < maxSelectable) {
                    mediaPathList.add(path);
                }
            }
        }
    }
}
