package com.example.vanluc.note.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vanluc.note.R;
import com.example.vanluc.note.model.ImageNote;
import com.example.vanluc.note.model.Note;
import com.example.vanluc.note.ulti.ChangeData;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    ArrayList<ImageNote> arrImageBitmap;
    Context context;

    public ImageAdapter(ArrayList<ImageNote> arrImageBitmap, Context context) {
        this.arrImageBitmap = arrImageBitmap;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_list_image,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String string = arrImageBitmap.get(position).getBitmapImageNote();
        Bitmap bitmap = ChangeData.StringToBitMap(string);
        holder.iv_NoteImage.setImageBitmap(bitmap);

        holder.ib_CloseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImage(position);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (arrImageBitmap.size() == 0)
        {
            return 0;
        } else {
            return arrImageBitmap.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_NoteImage;
        ImageButton ib_CloseImage;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_NoteImage = itemView.findViewById(R.id.iv_NoteImage);
            ib_CloseImage = itemView.findViewById(R.id.ib_CLoseNoteImage);


        }
    }
    private void removeImage(int i)
    {
        arrImageBitmap.remove(i);
        notifyDataSetChanged();
    }
    }
