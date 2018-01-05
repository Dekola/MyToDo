package com.kola.mytodo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kola.mytodo.AuthActivity;
import com.kola.mytodo.Fragment.ResetPasswordFragment;
import com.kola.mytodo.Fragment.TodoFragment;
import com.kola.mytodo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Akano on 12/28/2017.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {


    Context context;
    ArrayList<String> title, message, time, date, timeStamp;
    LayoutInflater inflater;
    CustomClickListener listener;

    public TodoAdapter(FragmentActivity activity, ArrayList<String> title, ArrayList<String> message, ArrayList<String> time, ArrayList<String> date, ArrayList<String> timeStamp) {

        this.context = activity;
        this.title = title;
        this.message = message;
        this.time = time;
        this.date = date;
        this.timeStamp = timeStamp;
        this.inflater = LayoutInflater.from(activity);

    }

    public void registerListener(CustomClickListener listener){
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.holder_todo, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.titleTv.setText(title.get(position));
        holder.messageTv.setText(message.get(position));
        holder.dateTv.setText(date.get(position));
        holder.timeTv.setText(time.get(position));

        context = holder.itemView.getContext();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(timeStamp.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return title.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv, messageTv, dateTv, timeTv;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.titleTv);
            messageTv = itemView.findViewById(R.id.messageTv);
            dateTv = itemView.findViewById(R.id.dateTv);
            timeTv = itemView.findViewById(R.id.timeTv);

        }


    }
}
