package com.example.englishlearn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link loginpage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loginpage extends Fragment {



    public loginpage() {

    }

    public static loginpage newInstance(String param1, String param2) {
        loginpage fragment = new loginpage();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_loginpage, container, false);
        return  view;
    }


}