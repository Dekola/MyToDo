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
import android.support.v4.app.FragmentTransaction;
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
import com.kola.mytodo.database.CompletedTaskDb;
import com.kola.mytodo.database.DeletedTaskDb;
import com.kola.mytodo.database.OngoingTaskDb;
import com.kola.mytodo.database.TaskDao;
import com.kola.mytodo.other.Constants;

import static com.kola.mytodo.other.Constants.TODO;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("StaticFieldLeak")
public class TaskFragment extends Fragment {


    public TaskFragment() {
        // Required empty public constructor
    }

    TaskDao taskDao;
    TextView taskTv, noteTv, dateTv, timeTv, textview2;
    ImageView completeImg, deleteImg;
    String task, note, date, time, timeStamp, section, DELETE = "delete", COMPLETE = "complete", RESTORE = "restore";
    RelativeLayout reminderRl, taskRl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_task, container, false);

        timeStamp = getArguments().getString("timeStamp");
        section = getArguments().getString("section");
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
                showDialog(message, DELETE);
            }
        });

        completeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpannableStringBuilder message = new SpannableStringBuilder("You have chosen " + task + " as your contact.");
                StyleSpan b = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold
                message.setSpan(b, 16, 16 + task.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold

//                String message = "Do u want to add Task: "+ task + " <b> to completed list";
                if (section.equals(Constants.TODO)) showDialog(String.valueOf(message), COMPLETE );
                    if (section.equals(Constants.DELETED)) showDialog(String.valueOf(message), RESTORE);

            }
        });

        return view;
    }

    private void showDialog(final String action, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (section.equals(Constants.TODO) && action.equals(DELETE)) addTaskToDeletedTable();
                if (section.equals(Constants.TODO) && action.equals(COMPLETE)) addTaskToCompletedTable();
                if (section.equals(Constants.COMPLETED)) deleteTaskFromCompletedTable();
                if (section.equals(Constants.DELETED) && action.equals(RESTORE)) addTaskToOngoingTable();
                if (section.equals(Constants.DELETED) && action.equals(DELETE)) deleteTaskFromDeletedTable();

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

    // TODO: 1/6/2018
    public void addTaskToOngoingTable(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                OngoingTaskDb ongoingTaskDb = new OngoingTaskDb();
                ongoingTaskDb.task = task;
                ongoingTaskDb.note = note;
                ongoingTaskDb.time = time;
                ongoingTaskDb.date = date;

                taskDao.insertAllintoOngoingTaskTable(ongoingTaskDb);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                deleteTaskFromDeletedTable();
            }
        }.execute();
    }

    public void addTaskToCompletedTable(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                CompletedTaskDb completedTaskDb = new CompletedTaskDb();
                completedTaskDb.task = task;
                completedTaskDb.note = note;
                completedTaskDb.time = time;
                completedTaskDb.date = date;

                taskDao.insertAllintoCompletedTaskTable(completedTaskDb);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                deleteTaskFromOngoingTable();
            }
        }.execute();
    }

    public void addTaskToDeletedTable(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                DeletedTaskDb deletedTaskDb = new DeletedTaskDb();
                deletedTaskDb.task = task;
                deletedTaskDb.note = note;
                deletedTaskDb.time = time;
                deletedTaskDb.date = date;

                taskDao.insertAllintoDeletedTaskTable(deletedTaskDb);

                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                deleteTaskFromOngoingTable();
            }
        }.execute();
    }

    public void deleteTaskFromOngoingTable(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                taskDao.deleteFromdeletedTaskTable(timeStamp);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dismissFragment();
            }
        }.execute();
    }

    public void deleteTaskFromCompletedTable(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                taskDao.deleteFromcompletedTaskTable(time);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dismissFragment();
            }
        }.execute();
    }

    public void deleteTaskFromDeletedTable(){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                taskDao.deleteFromdeletedTaskTable(timeStamp);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dismissFragment();
            }
        }.execute();
    }

    void dismissFragment(){

        Bundle bundle = new Bundle();
        bundle.putString("section", TODO);

        TodoFragment todoFragment = new TodoFragment();
        todoFragment.setArguments(bundle);

        FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
        fragmentManager.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentManager.replace(R.id.activity_drawer_frame, todoFragment);
        fragmentManager.commit();

    }
}
