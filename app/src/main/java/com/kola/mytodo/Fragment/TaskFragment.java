package com.kola.mytodo.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kola.mytodo.database.AppDatabase;
import com.kola.mytodo.R;
import com.kola.mytodo.database.TaskDao;
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
    ImageView completeImg, deleteImg;
    String task, note, date, time, timeStamp;
    RelativeLayout reminderRl, taskRl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_task, container, false);

        timeStamp = getArguments().getString("timeStamp");

        taskDao = Room.databaseBuilder(getActivity(), AppDatabase.class, Constants.ONGOING_TASK_TABLE).build().taskDao();

        deleteImg = view.findViewById(R.id.deleteImg);
        completeImg = view.findViewById(R.id.completeImg);

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

        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "Do u want to delete Task: "+ task;
                showDialog(message);
            }
        });

        completeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableStringBuilder message = new SpannableStringBuilder("You have chosen " + task + " as your contact.");
                StyleSpan b = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold
                message.setSpan(b, 16, 16 + task.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold

//                String message = "Do u want to add Task: "+ task + " <b> to completed list";
                showDialog(String.valueOf(message));
            }
        });

        return view;
    }

    private void showDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();

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

    public void addTaskToDeletedTable(){

    }

    public void addTaskToCompletedTable(){

    }

    public void addTaskToOngoingTable(){

    }

    public void deleteTaskFromOngoingTable(){

    }

    public void DeleteTaskFromCompletedTable(){

    }

    public void DeleteTaskFromOngoingTable(){

    }
}
