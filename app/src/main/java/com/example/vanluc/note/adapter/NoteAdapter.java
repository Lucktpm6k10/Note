package com.example.vanluc.note.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vanluc.note.R;
import com.example.vanluc.note.activity.EditNoteActivity;
import com.example.vanluc.note.define.DefaultValues;
import com.example.vanluc.note.model.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    ArrayList<Note> noteList;
    Context context;
    public static String checkID = "CheckID";
    public static String idBundle = "idBundle";

    public NoteAdapter(ArrayList<Note> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_recyclerview_note, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Set text cho các text view
        holder.tv_Conttent.setText(noteList.get(position).getConttent());
        holder.tv_Tittle.setText(noteList.get(position).getTittle());
        holder.tv_Time.setText(noteList.get(position).getNowTime());
        holder.tv_Date.setText(noteList.get(position).getNowDate());
        //Set màu cho itemView
        if (noteList.get(position).getItemBackground() == DefaultValues.itemBackground2) {
            //set màu itemView bằng màu itemBackground2
            holder.ln_Item.setBackgroundColor(ContextCompat.getColor(context, R.color.backGroundItem2));
        } else if (noteList.get(position).getItemBackground() == DefaultValues.itemBackground3) {
            //set màu itemView bằng màu itemBackground3
            holder.ln_Item.setBackgroundColor(ContextCompat.getColor(context, R.color.backGroundItem3));
        } else if (noteList.get(position).getItemBackground() == DefaultValues.itemBackground4) {
            //set màu itemView bằng màu itemBackground4
            holder.ln_Item.setBackgroundColor(ContextCompat.getColor(context, R.color.backGroundItem4));
        } else {
            //set màu itemView bằng màu itemBackground1,mặc định
            holder.ln_Item.setBackgroundColor(ContextCompat.getColor(context, R.color.backGroundItem1));

        }
        //Set ẩn hiện icon clock
        if (noteList.get(position)!= null) {
            if (noteList.get(position).getClockTime().equals("null") == false) {
                holder.iv_Clock.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        if (noteList.size() == 0) {
            return 0;
        } else {
            return noteList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Tittle, tv_Conttent, tv_Date, tv_Time;
        LinearLayout ln_Item;
        ImageView iv_Clock;

        public ViewHolder(final View itemView) {
            super(itemView);
            tv_Tittle = itemView.findViewById(R.id.tv_Tittle);
            tv_Conttent = itemView.findViewById(R.id.tv_Conttent);
            tv_Time = itemView.findViewById(R.id.tv_Time);
            tv_Date = itemView.findViewById(R.id.tv_Date);
            ln_Item = itemView.findViewById(R.id.ln_Item);
            iv_Clock = itemView.findViewById(R.id.iv_Clock);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context = itemView.getContext();
                    Intent intent  = new Intent(context,EditNoteActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(idBundle,getAdapterPosition());
                    intent.putExtra(checkID,bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
