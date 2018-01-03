package com.kola.mytodo.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kola.mytodo.R;
import com.kola.mytodo.adapter.TodoAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {


    public TodoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        RecyclerView frg_todo_recyclerview = view.findViewById(R.id.frg_todo_recyclerview);


        ArrayList<String> title = new ArrayList<>();
        title.add("Buy groceries");
        title.add("go to school");
        title.add("call femi");
        title.add("wash clothes");

        ArrayList<String> message = new ArrayList<>();
        message.add("jam, butter, milk, toothpaste, bread, cornflakes, tomatoes, fries, ketchup, vinegar, baking soda ");
        message.add("");
        message.add("discuss about the school materials and his new stocks");
        message.add("bed sheet, towel, trousers");

        ArrayList<String> time = new ArrayList<>();
        time.add("12:52");
        time.add("1:35");
        time.add("16:32");
        time.add("22:07");

        ArrayList<String> date = new ArrayList<>();
        date.add("12/28/17");
        date.add("4/15/17");
        date.add("6/24/17");
        date.add("3/19/17");

        Context cc = getActivity();

        TodoAdapter adapter = new TodoAdapter(getActivity(), title, message, time, date);
        frg_todo_recyclerview.setLayoutManager(new LinearLayoutManager(cc));
        frg_todo_recyclerview.setAdapter(adapter);

        return view;
    }

}
