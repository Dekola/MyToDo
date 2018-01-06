package com.kola.mytodo.worker;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.kola.mytodo.baas.BaseApplication;
import com.kola.mytodo.database.AppDatabase;
import com.kola.mytodo.database.TaskDao;
import com.kola.mytodo.database.TaskDb;
import com.kola.mytodo.listener.TaskListener;
import com.kola.mytodo.other.Constants;

/**
 * Created by ribads on 1/6/18.
 */

public class AddToDoEventWorker extends AsyncTask<TaskDb, Void, Exception> {

    TaskListener listener;
    TaskDao taskDao;

    public AddToDoEventWorker() {
        Context context = BaseApplication.getInstance().getApplicationContext();
        AppDatabase appDatabase = Room.databaseBuilder(context, AppDatabase.class, Constants.ONGOING_TASK_TABLE).build();
        taskDao = appDatabase.taskDao();
    }

    public void attachListener(TaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected Exception doInBackground(TaskDb... taskDbs) {
        try {
            for (TaskDb taskDb : taskDbs) {
                taskDao.insertAllintoOngoingTaskTable(taskDb);
            }
            return null;
        } catch (Exception ex) {
            return ex;
        }
    }


    @Override
    protected void onPostExecute(Exception e) {
        super.onPostExecute(e);
        if (listener != null)
            listener.onException(e);
    }
}
