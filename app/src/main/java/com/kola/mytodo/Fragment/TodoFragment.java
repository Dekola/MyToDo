package com.kola.mytodo.Fragment;


import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kola.mytodo.AppDatabase;
import com.kola.mytodo.R;
import com.kola.mytodo.TaskDao;
import com.kola.mytodo.TaskDb;
import com.kola.mytodo.adapter.CustomClickListener;
import com.kola.mytodo.adapter.TodoAdapter;
import com.kola.mytodo.other.SimpleDividerItemDecoration;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {


    public TodoFragment() {
        // Required empty public constructor
    }

    TaskDao taskDao;
    Context context;
    RecyclerView frg_todo_recyclerview;
    String section;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        context = getActivity();

        section = getArguments().getString("section");

        Toast.makeText(context, section+"", Toast.LENGTH_SHORT).show();

        frg_todo_recyclerview = view.findViewById(R.id.frg_todo_recyclerview);

        frg_todo_recyclerview.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));

        AppDatabase appDatabase = Room.databaseBuilder(getActivity(), AppDatabase.class, TaskDb.DATABASE).build();

        taskDao = appDatabase.taskDao();

        loadDatabase();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void loadDatabase() {
        new AsyncTask<Void, Void, Void>() {

            ArrayList<String> timeStamp = new ArrayList<>();
            ArrayList<String> task = new ArrayList<>();
            ArrayList<String> note = new ArrayList<>();
            ArrayList<String> date = new ArrayList<>();
            ArrayList<String> time = new ArrayList<>();

            Cursor cursor;
            @Override
            protected Void doInBackground(Void... voids) {

                cursor = taskDao.getAll();

                if (cursor.getCount()>0) {
                    cursor.moveToFirst();

                    do {
                        timeStamp.add(cursor.getString(cursor.getColumnIndex(TaskDb.TIMESTAMP)));
                        task.add(section+" "+cursor.getString(cursor.getColumnIndex(TaskDb.TASK)));
                        note.add(cursor.getString(cursor.getColumnIndex(TaskDb.NOTE)));
                        date.add(cursor.getString(cursor.getColumnIndex(TaskDb.DATE)));
                        time.add(cursor.getString(cursor.getColumnIndex(TaskDb.TIME)));

                    } while (cursor.moveToNext());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                TodoAdapter adapter = new TodoAdapter(getActivity(), task, note, time, date, timeStamp);

                adapter.registerListener(customClickListener);

                frg_todo_recyclerview.setLayoutManager(new LinearLayoutManager(context));
                frg_todo_recyclerview.setAdapter(adapter);

                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    CustomClickListener customClickListener = new CustomClickListener() {
        @Override
        public void onItemClicked(String timeStamp) {

            Bundle bundle = new Bundle();
            bundle.putString("timeStamp", timeStamp);

            TaskFragment taskFragment = new TaskFragment();

            taskFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.activity_drawer_frame, taskFragment).commit();
        }
    };

}