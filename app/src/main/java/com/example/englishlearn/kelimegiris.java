package com.example.englishlearn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class kelimegiris extends Fragment {


    public kelimegiris() {
        // Required empty public constructor

    }
    private  static kelimegiris fragment;
    public static kelimegiris  instance( ) {
         fragment = (fragment==null)?new kelimegiris():fragment ;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_kelimegiris, container, false);
    }
}