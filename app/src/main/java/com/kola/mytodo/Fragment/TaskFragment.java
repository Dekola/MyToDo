package com.kola.mytodo.Fragment;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kola.mytodo.database.AppDatabase;
import com.kola.mytodo.R;
import com.kola.mytodo.database.TaskDao;
import com.kola.mytodo.database.TaskDb;
import com.kola.mytodo.other.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment {


    public TaskFragment() {
        // Required empty public constructor
    }

    TaskDao taskDao;
    TextView taskTv, noteTv, dateTv, timeTv, textview2;

    String task, note, date, time;

    String timeStamp;

    RelativeLayout reminderRl, taskRl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_task, container, false);

        timeStamp = getArguments().getString("timeStamp");

        taskDao = Room.databaseBuilder(getActivity(), AppDatabase.class, Constants.ONGOING_TASK_TABLE).build().taskDao();

        reminderRl = view.findViewById(R.id.reminderRl);
        taskRl = view.findViewById(R.id.taskRl);

        taskTv = view.findViewById(R.id.taskTv);
        noteTv = view.findViewById(R.id.noteTv);
        dateTv = view.findViewById(R.id.dateTv);
        timeTv = view.findViewById(R.id.timeTv);
        textview2 = view.findViewById(R.id.textview2);

        noteTv.setMovementMethod(new ScrollingMovementMethod());
        noteTv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        loadTasks();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void loadTasks() {


        new AsyncTask<Void, Void, Void>() {

            Cursor cursor;

            @Override
            protected Void doInBackground(Void... voids) {

                cursor = taskDao.getAtTimeStampFromOngoingTaskTable(timeStamp);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                cursor.moveToFirst();

                task = cursor.getString(cursor.getColumnIndex(Constants.TASK));
                note = cursor.getString(cursor.getColumnIndex(Constants.NOTE));
                date = cursor.getString(cursor.getColumnIndex(Constants.DATE));
                time = cursor.getString(cursor.getColumnIndex(Constants.TIME));
//
//                Toast.makeText(getActivity(), task+" "+date+" "+time, Toast.LENGTH_SHORT).show();

                taskTv.setText(task);

                if (note.equals("")) {
                    taskRl.removeView(noteTv);
                    taskRl.removeView(textview2);
                }
                else {
                    noteTv.setText(note);
                }

                if (date == null) {
                    reminderRl.removeAllViews();
                }
                else {
                    timeTv.setText(time);
                    dateTv.setText(date);
                }



            }
        }.execute();

    }


}
