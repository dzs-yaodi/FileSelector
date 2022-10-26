package com.xw.file_selector.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xw.file_selector.R;
import com.xw.file_selector.data.FileInfos;
import com.xw.file_selector.databinding.ItemFileLayoutBinding;
import com.xw.file_selector.listener.OnItemListener;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends  RecyclerView.Adapter<FileAdapter.FileViewHolder>{

    private List<FileInfos> fileInfosList = new ArrayList<>();
    private OnItemListener onItemListener;

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public FileAdapter(List<FileInfos> fileInfosList) {
        this.fileInfosList = fileInfosList;

    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFileLayoutBinding binding = ItemFileLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent,false);
        return new FileViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileInfos infos = fileInfosList.get(position);


        Glide.with(holder.itemView)
                .load(infos.fileIcon)
                .into(holder.binding.ivType);

        holder.binding.fileName.setText(infos.fileName);

        if (infos.isChecked) {
            holder.binding.imageSelect.setImageResource(R.mipmap.ic_checkbox_checked);
        } else {
            holder.binding.imageSelect.setImageResource(R.mipmap.ic_checkbox_unchecked);
        }
    }

    @Override
    public int getItemCount() {
        return fileInfosList.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder {

        private ItemFileLayoutBinding binding;

       public FileViewHolder(@NonNull ItemFileLayoutBinding binding) {
           super(binding.getRoot());
           this.binding = binding;

           itemView.setOnClickListener(view -> {
               if (onItemListener != null) {
                   onItemListener.onItemClick(getAdapterPosition());
               }
           });

           binding.imageSelect.setOnClickListener(view -> {
               if (onItemListener != null) {
                   onItemListener.onItemSelectListener(getAdapterPosition());
               }
           });
       }
   }
}
