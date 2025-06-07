package com.example.englishlearn;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class kelime_ekle extends Fragment {


    public kelime_ekle() {
        // Required empty public constructor
    }


    public static kelime_ekle newInstance(String param1, String param2) {
        kelime_ekle fragment = new kelime_ekle();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kelime_ekle, container, false);

        EditText editTextTr = view.findViewById(R.id.inputtr);
        EditText editTextEn = view.findViewById(R.id.inputen);
        Button buttonEkle = view.findViewById(R.id.imageView3);

       editTextTr.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               ((TextView)view.findViewById(R.id.kelimeeklemestatus)).setText("");
               buttonEkle.setVisibility(View.VISIBLE);
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
        editTextEn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((TextView)view.findViewById(R.id.kelimeeklemestatus)).setText("");
                buttonEkle.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        buttonEkle.setOnClickListener(v -> {
            String kelimeTr = editTextTr.getText().toString().trim();
            String kelimeEn = editTextEn.getText().toString().trim();

            if (!kelimeTr.isEmpty() && !kelimeEn.isEmpty()) {
                sqlbaglanti.wordadd(kelimeTr, kelimeEn);
                editTextTr.setText("");
                editTextTr.setHint("yeni kelime ekleyin.");
                editTextEn.setText("");
                ((TextView)view.findViewById(R.id.kelimeeklemestatus)).setText(kelimeTr+ " Kelimesi Başarıyla eklendi.");
                buttonEkle.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Kelime eklendi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Lütfen boş bırakmayın", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


}