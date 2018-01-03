package com.kola.mytodo.adapter;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kola.mytodo.R;

import java.util.ArrayList;

/**
 * Created by Akano on 12/28/2017.
 */

public class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.ViewHolder>{


    Context context;
    ArrayList <String> title, message, time, date;
    LayoutInflater inflater;

    public DeleteAdapter(FragmentActivity activity, ArrayList<String> title, ArrayList<String> message, ArrayList<String> time, ArrayList<String> date) {

        this.context = activity;
        this.title = title;
        this.message = message;
        this.date = date;
        this.time = time;

        this.inflater = LayoutInflater.from(activity);

    }

    @Override
    public DeleteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.holder_todo, parent, false);

        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(DeleteAdapter.ViewHolder holder, int position) {

        holder.titleTv.setText(title.get(position));
        holder.messageTv.setText(message.get(position));
        holder.timeTv.setText(time.get(position));
        holder.dateTv.setText(date.get(position));

    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv, messageTv, timeTv, dateTv;
        public ViewHolder(View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.titleTv);
            messageTv  = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            dateTv = itemView.findViewById(R.id.dateTv);
        }
    }
}