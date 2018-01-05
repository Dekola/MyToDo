package com.kola.mytodo.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kola.mytodo.AppDatabase;
import com.kola.mytodo.R;
import com.kola.mytodo.TaskDao;
import com.kola.mytodo.TaskDb;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddFragment extends Fragment {


    public AddFragment() {
        // Required empty public constructor
    }

    String task, note, date, time;

    TextView dateTv, timeTv;
    EditText taskEt, noteEt;
    CheckBox reminderChk;
    static TaskDao taskDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        AppDatabase appDatabase = Room.databaseBuilder(getActivity(), AppDatabase.class, TaskDb.DATABASE).build();

        taskDao = appDatabase.taskDao();

        note = null;

        taskEt = view.findViewById(R.id.taskEt);
        taskEt.setText("kola");
        noteEt = view.findViewById(R.id.noteEt);

        dateTv = view.findViewById(R.id.dateTv);
        timeTv = view.findViewById(R.id.timeTv);

        Button addBtn = view.findViewById(R.id.addBtn);

        reminderChk = view.findViewById(R.id.reminderChk);

        reminderChk.setChecked(true);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTAsk();
            }
        });

        reminderChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (reminderChk.isChecked()){
                    dateTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                    timeTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                    dateTv.setClickable(true);
                    timeTv.setClickable(true);
                }
                else {
                    dateTv.setTextColor(Color.GRAY);
                    timeTv.setTextColor(Color.GRAY);

                    dateTv.setClickable(false);
                    timeTv.setClickable(false);

                    dateTv.setError(null);
                    timeTv.setError(null);

                    date = null;
                    time = null;

                }
            }
        });

        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDateDialog();
            }
        });

        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog();
            }
        });

        return view;
    }

    private void showTimeDialog() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = hourOfDay+":"+minute;
                timeTv.setText(time);
                timeTv.setError(null);
                timeTv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            }
        }, hour, minute, true);

        timePickerDialog.show();

    }

    void showToast(String message){
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    void startDateDialog(){

        final GregorianCalendar gregorianCalendar = new GregorianCalendar();

        int day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
        int month = gregorianCalendar.get(Calendar.MONTH);
        int year = gregorianCalendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                date = day +"/"+(month+1)+"/"+year;
                dateTv.setText(date);
                dateTv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                dateTv.setError(null);
            }
        },year, month, day);

        datePickerDialog.show();
    }

    private void addTAsk() {

        task = taskEt.getText().toString();
        note = noteEt.getText().toString();

        if (task.equals("")) taskEt.setError("Input a task");
        else if (reminderChk.isChecked() && dateTv.getText().equals("Date")) dateTv.setError("set the date");
        else if (reminderChk.isChecked() && timeTv.getText().equals("Time"))timeTv.setError("set the time");
        else addToDatabase();

    }

    @SuppressLint("StaticFieldLeak")
    private  void addToDatabase() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS",  Locale.getDefault()).format(new Date());

                TaskDb taskDb = new TaskDb();
                taskDb.timeStamp = timeStamp;
                taskDb.task = task;
                taskDb.note = note;
                taskDb.time = time;
                taskDb.date = date;

                taskDao.insertAll(taskDb);

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                showToast("Task successfully added");


                FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
                fragmentManager.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                fragmentManager.replace(R.id.activity_drawer_frame, new TodoFragment());
                fragmentManager.commit();
            }

        }.execute();
    }

    public void backButtonWasPressed() {
        Toast.makeText(getContext(), "scascas", Toast.LENGTH_SHORT).show();
    }

//    private class addToDatabase extends AsyncTask<Void, Void, Void>{
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            TaskDb taskDb = new TaskDb();
//            taskDb.task = task;
//            taskDb.note = note;
//            taskDb.time = time;
//            taskDb.date = date;
//
//            taskDao.insertAll(taskDb);
//            return null;
//        }
//
//        protected void onPostExecute(Cursor al) {
//            Toast.makeText(context, al.getCount() + "", Toast.LENGTH_SHORT).show();
//        }
//    }


//    private void addToDatabase() {
//

//
//        showToast("Task successfully added");
//
//        FragmentTransaction fragmentManager = getFragmentManager().beginTransaction();
//        fragmentManager.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//        fragmentManager.replace(R.id.activity_drawer_frame, new TodoFragment());
//        fragmentManager.commit();
//
//    }

}

