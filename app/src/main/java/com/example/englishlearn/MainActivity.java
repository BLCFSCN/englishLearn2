package com.example.englishlearn;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private EditText promptEditText;
    private Button generateButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        sqlbaglanti.connectToSQL(this);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, loginpage.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();


    }
    String textContainer="";
    private String mod="def";
    private void kelimeyerlestir() {

        currentw=new sqlbaglanti().ogrenilecekKelimeler(1);
        ((TextView)findViewById(R.id.nextclickbubles)).setText("info : "+currentw.get(0).kelimeen+" / "+currentw.get(0).kelimetr);

        ((TextView)findViewById(R.id.trtext)).setText(currentw.get(0).kelimetr);
        textContainer="";
        ((TextView)findViewById(R.id.entext)).setText("");
        ArrayList<Integer> random = new ArrayList<>();
        for (ImageButton button1: buton)
        {
            button1.setImageResource(R.drawable.buttonblue);
            button1.setTag("x");
        }
        for(TextView t: textViews)
        {
            t.setGravity(Gravity.CENTER);
            t.setPadding(0, 0, 0, 14); // Negatif padding yerine 0 kullan
        }
        for (int a = 0; a < 12; a++) {
            random.add(a);
        }
        Collections.shuffle(random);
        for (int l = currentw.get(0).kelimeen.length(); l<12 ; l++) {
            textViews[random.get(l)].setVisibility(View.INVISIBLE);
            buton[random.get(l)].setVisibility(View.INVISIBLE);
        }
        for (int l = 0; l < currentw.get(0).kelimeen.length(); l++) {
            textViews[random.get(l)].setText(String.valueOf(currentw.get(0).kelimeen.charAt(l)));
            textViews[random.get(l)].setVisibility(View.VISIBLE);
            buton[random.get(l)].setVisibility(View.VISIBLE);
        }


    }

    ImageButton[] buton;
    TextView[] textViews;
    private void loadButton() {
        kelimegiris fragment = (kelimegiris) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);

        if (fragment != null && fragment.getView() != null) {
            View view = fragment.getView();
            buton = new ImageButton[12];
            textViews = new TextView[12];


            for (int i = 0; i < 12; i++) {
                int buttonId = getResources().getIdentifier("button" + (i + 1), "id", getPackageName());
                int textViewId = getResources().getIdentifier("t" + (i + 1), "id", getPackageName());

                buton[i] = view.findViewById(buttonId);
                textViews[i] = view.findViewById(textViewId);
            }
        }
    }
    EditText editText;
    private void loadButton2() {
        wordle fragment = (wordle) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);

        if (fragment != null && fragment.getView() != null) {
            View view = fragment.getView();

            buton = new ImageButton[25];
            textViews = new TextView[25];

            for (int i = 0; i < 25; i++) {
                int buttonId = getResources().getIdentifier("button" + (i + 1), "id", getPackageName());
                int textViewId = getResources().getIdentifier("t" + (i + 1), "id", getPackageName());

                buton[i] = view.findViewById(buttonId);
                textViews[i] = view.findViewById(textViewId);

                // TextView'leri sıfırla
                textViews[i].setText("");
            }

            editText = findViewById(R.id.worldletext);

            editText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_GO ||
                        actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                    setWordlrowclick(editText.getText().toString());
                    return true;
                }
                return false;
            });
        }
    }

    ArrayList<kelimedata>currentw;
    public  void playbalon(View view)
    {
        view.setVisibility(View.GONE);
        findViewById(R.id.ballongameparent).setVisibility(View.VISIBLE);
        loadButton();
        kelimeyerlestir();
    }
    public  void retryclick(View view)
    {
        loadButton();
        kelimeyerlestir();
    }
    int wordlrow=0;

    public  void  returnmenuClick(View view)
    {
        if(view.getId()==R.id.rtkelimeadd||view.getId()==R.id.quizreturn)
        {
            getSupportFragmentManager().popBackStack();

        }
        else if(view.getId()==R.id.quizrlogin)
        {
            if((findViewById(R.id.createacountlayout)).getVisibility()==View.VISIBLE)
            {
                (findViewById(R.id.createacountlayout)).setVisibility(View.GONE);
                (findViewById(R.id.loginpagelayoutu)).setVisibility(View.VISIBLE);
            }

        }
        else if(view.getId()==R.id.quick) {

            getSupportFragmentManager().popBackStack();
        }
        else
        (findViewById(R.id.optionmenu)).setVisibility(View.GONE);
    }
    public  void  acountcreateloginpageclick(View view)
    {
        (findViewById(R.id.createacountlayout)).setVisibility(View.VISIBLE);
        (findViewById(R.id.loginpagelayoutu)).setVisibility(View.GONE);
      }
    public  void  playmenuClick(View view)
    {
        (findViewById(R.id.optionmenu)).setVisibility(View.VISIBLE);
    }
    public  void playclick(View view)
    {
        view.setVisibility(View.GONE);
        findViewById(R.id.Wordleparent).setVisibility(View.VISIBLE);
        wordlrow=0;
        setwordleword();
        loadButton2();

    }
     String  wordleword  = " ";kelimedata kelimedat;
     private void setwordleword()
     {
         kelimedat =new sqlbaglanti().getLearnedWords(1).get(0);
     }
    public void onLoginClick(View view)
    {

        String a,b;
        a=((TextView) findViewById(R.id.inusername)).getText().toString().toLowerCase();
        b=((TextView) findViewById(R.id.inpasword)).getText().toString().toLowerCase();
        if(sqlbaglanti.getuserLogin(a,b))
        {
            ((TextView) findViewById(R.id.errorloginpagetext)).setText("başarılı giriş");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, anamenu.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack("name")
                    .commit();
        }
        else {
            ((TextView) findViewById(R.id.errorloginpagetext)).setVisibility(View.VISIBLE);

        ((TextView) findViewById(R.id.errorloginpagetext)).setText("kulanıcı adı yada şifre yanlış");}
    }

    public void onAccountCreateClick(View view)
    {
        String u,p,p1;
        u=  ((TextView) findViewById(R.id.createusername)).getText().toString().toLowerCase();
        p=   ((TextView) findViewById(R.id.createpasword)).getText().toString().toLowerCase();
        p1=   ((TextView) findViewById(R.id.createpasword1)).getText().toString().toLowerCase();
        if(p.equals(p1)&&sqlbaglanti.getusersConfirm(u))
        {
            sqlbaglanti.accountcreate(u,p);
            ((TextView) findViewById(R.id.infopasword)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.infopasword)).setText("Hesabınız başarıyla oluştrulumuştur!");
        }
        else if(p.equals(p1)&&!sqlbaglanti.getusersConfirm(u))
        {
            ((TextView) findViewById(R.id.infopasword)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.infopasword)).setText("Kulanıcı Adınız Daha Önce Kulanılmış!");
        }
        else if(!p.equals(p1)&&sqlbaglanti.getusersConfirm(u))
        {
            ((TextView) findViewById(R.id.infopasword)).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.infopasword)).setText("Şifreniz birbiriyle uyuşmuyor!");
        }



    }
    public  void setWordlrowclick(String wordlrowText)
    {

           wordleword= kelimedat.kelimeen;

           if(wordlrowText.length()>=5)
           {

               InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
               StringBuilder builder = new StringBuilder(wordleword);
                 if(wordleword.equals(wordlrowText))
                 {
                     sqlbaglanti.learnWorldUpdate(kelimedat);
                     sqlbaglanti.deleteLocalWorld(kelimedat.kelimeid);
                     Dialog dialog = new Dialog(MainActivity.this);
                     dialog.setContentView(R.layout.wordlesuccest);
                     ((TextView)dialog.findViewById(R.id.EN)).setText(kelimedat.kelimeen);
                     ((TextView)dialog.findViewById(R.id.TR)).setText(kelimedat.kelimetr);
                     dialog.findViewById(R.id.wordlecomplateonay).setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             getSupportFragmentManager().popBackStack();
                             dialog.cancel();
                         }
                     });
                     dialog.setCancelable(true);
                     dialog.show();
                 }

                    String combined = builder.toString();

                   for (int i = 0; i < 5; i++) {
                       if (wordleword.charAt(i) == wordlrowText.charAt(i)) {
                           buton[i + wordlrow * 5].setBackgroundColor(ContextCompat.getColor(this, R.color.trueStatewordl));
                           textViews[i + wordlrow * 5].setText(wordlrowText.charAt(i) + "");
                       } else if (combined.contains(wordlrowText.charAt(i)+"")) {
                           buton[i + wordlrow * 5].setBackgroundColor(ContextCompat.getColor(this, R.color.medStatewordl));
                           textViews[i + wordlrow * 5].setText(wordlrowText.charAt(i) + "");
                       } else {
                           textViews[i + wordlrow * 5].setText(wordlrowText.charAt(i) + "");
                           buton[i + wordlrow * 5].setBackgroundColor(ContextCompat.getColor(this, R.color.black));
                       }

                  }
                  wordlrow++;
                   if (wordlrow>=5)
                   {

                       Dialog dialog = new Dialog(MainActivity.this);
                       dialog.setContentView(R.layout.wordlesuccest);
                       ((TextView)dialog.findViewById(R.id.textView7)).setText("Kelimeyi bilemedin.");
                       ((TextView)dialog.findViewById(R.id.EN)).setText("Şansını sonra tekrar dene.");
                       ((TextView)dialog.findViewById(R.id.TR)).setText("");
                       dialog.setCancelable(true);
                       dialog.findViewById(R.id.wordlecomplateonay).setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               getSupportFragmentManager().popBackStack();
                               dialog.cancel();
                           }
                       });
                       dialog.show();
                   }
               ((TextView)findViewById(R.id.info)).setText("info : "+"userid "+sqlbaglanti.getcurrentuserid()+"/n text : "+wordleword+" : "+ wordlrow );

           }


    }

    kelimedata quizcontainer;
    public  void playQuizClick(View view)
    {



        mod="quiz";
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, quiz.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();

    }

    public  void playWordleClick(View view)
    {

        mod="wordl";
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, wordle.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();

    }
    public  void playezberClick(View view)
    {
        mod="def";
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, kelimegiris.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }
    public  void settingClick(View view)
    {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, kelime_ekle.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();

    }


    public void exitClick(View view)
    {
           finish();
    }

    private  int quizcontainercounter=0;
    public  void  QuiznextClick(View view)
    {
        quizcontainer=   sqlbaglanti.ogrenilecekKelimeler(1).get(0);
        ((ViewGroup)view.getParent().getParent()).findViewById(R.id.onayquiz).setVisibility(View.VISIBLE);
        ((ViewGroup)view.getParent().getParent()).findViewById(R.id.nextquizcbutton).setVisibility(View.GONE);
         if(((ViewGroup)view.getParent().getParent()).findViewById(R.id.startquizbuton).getVisibility()==View.VISIBLE)
        {
            ((ViewGroup)view.getParent().getParent()).findViewById(R.id.startquizbuton).setVisibility(View.GONE);
            ((ViewGroup)view.getParent().getParent()).findViewById(R.id.quizviev).setVisibility(View.VISIBLE);
        }
        EditText t= ((ViewGroup)view.getParent().getParent()).findViewById(R.id.editTextText);
        t.setTextColor(ContextCompat.getColor(this, R.color.defultStatewordl));
        TextView d1 = ((ViewGroup)view.getParent().getParent()).findViewById(R.id.textView14);
        TextView d2 = ((ViewGroup)view.getParent().getParent()).findViewById(R.id.textView16);
        TextView st = ((ViewGroup)view.getParent().getParent()).findViewById(R.id.textView18);
        TextView ed= ((ViewGroup)view.getParent().getParent()).findViewById(R.id.textView12);

        if(quizcontainercounter%2==0)
        {  ed.setText(quizcontainer.kelimeen);
            d1.setText(" ingilizce");
            d2.setText(" Türkçe");
            ((TextView)((ViewGroup)view.getParent().getParent()).findViewById(R.id.infotextquiz)).setText(" info : "+quizcontainer.kelimetr);

        }
        else
        {  ed.setText(quizcontainer.kelimetr);
            d2.setText(" ingilizce");
            d1.setText(" Türkçe");
            ((TextView)((ViewGroup)view.getParent().getParent()).findViewById(R.id.infotextquiz)).setText(" info : "+quizcontainer.kelimeen);

        }
    }
    int quizcounter=0;
    int quiztrueansver=0;
    public  void  QuizClick(View view)
    {

        EditText t= ((ViewGroup)view.getParent().getParent()).findViewById(R.id.editTextText);
        TextView d1 = ((ViewGroup)view.getParent().getParent()).findViewById(R.id.textView14);
        TextView d2 = ((ViewGroup)view.getParent().getParent()).findViewById(R.id.textView16);
        TextView st = ((ViewGroup)view.getParent().getParent()).findViewById(R.id.textView18);
        TextView ed= ((ViewGroup)view.getParent().getParent()).findViewById(R.id.textView12);
        quizcounter++;
        if(quizcontainercounter%2==0)
        {
            if(quizcontainer.kelimetr.toLowerCase().equals(t.getText().toString().toLowerCase()))
            {
                sqlbaglanti.learnWorldUpdate(quizcontainer);
                st.setText(st.getText().toString()+"✅");
                quiztrueansver++;
                sqlbaglanti.learnWorldUpdate(quizcontainer);
                ((ViewGroup)view.getParent().getParent()).findViewById(R.id.nextquizcbutton).setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.trueStatewordl)));
                t.setTextColor(ContextCompat.getColor(this, R.color.trueStatewordl));
            }
            else {
                st.setText(st.getText().toString()+"❌");
                ((ViewGroup)view.getParent().getParent()).findViewById(R.id.nextquizcbutton).setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.wrongtextcolor)));
                t.setTextColor(ContextCompat.getColor(this, R.color.wrongtextcolor));
            }
        }
        else
        {
            if(quizcontainer.kelimeen.equals(t.getText().toString().toLowerCase()))
            {
                sqlbaglanti.learnWorldUpdate(quizcontainer);
                st.setText(st.getText().toString()+"✅");
                quiztrueansver++;
                sqlbaglanti.learnWorldUpdate(quizcontainer);
                ((ViewGroup)view.getParent().getParent()).findViewById(R.id.nextquizcbutton).setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.trueStatewordl)));
                t.setTextColor(ContextCompat.getColor(this, R.color.trueStatewordl));
            }
            else {
                ((ViewGroup)view.getParent().getParent()).findViewById(R.id.nextquizcbutton).setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.wrongtextcolor)));
                st.setText(st.getText().toString()+"❌");
                t.setTextColor(ContextCompat.getColor(this, R.color.wrongtextcolor));
            }
        }
        ((ViewGroup)view.getParent().getParent()).findViewById(R.id.onayquiz).setVisibility(View.GONE);
        ((ViewGroup)view.getParent().getParent()).findViewById(R.id.nextquizcbutton).setVisibility(View.VISIBLE);
        quizcontainercounter++;
        if(quizcounter>=8)
        {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.wordlesuccest);
            ((TextView)dialog.findViewById(R.id.textView7)).setText("Aferim Quizi Tamamladın ");
            ((TextView)dialog.findViewById(R.id.EN)).setText("Doğru Cevap Sayısı : "+quiztrueansver);
            ((TextView)dialog.findViewById(R.id.TR)).setText("Toplam Soru Sayısı : "+quizcounter);
            dialog.setCancelable(true);
            ((TextView)(dialog.findViewById(R.id.textView9))).setText("Tamamla");
            dialog.findViewById(R.id.wordlecomplateonay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSupportFragmentManager().popBackStack();
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }
    int counter=0;
    public void buttonclick(View view) {
        int index = -1;
        if(mod.equals("def"))
        {
            counter++;
            for (int i = 0; i < buton.length; i++) {
                if (buton[i] == view) {
                    index = i;
                    break;
                }
            }

            if (index == -1) return;


            if ("x".equals(view.getTag())) {
                textViews[index].setGravity(Gravity.CENTER);
                textViews[index].setPadding(20, 0, 0, 0);
                ((ImageButton) view).setImageResource(R.drawable.buttonred);
                view.setTag("z");
                textContainer += textViews[index].getText().toString();
            } else if(view.getTag().equals("y")) {
                textViews[index].setGravity(Gravity.CENTER);
                textViews[index].setPadding(0, 0, 0, 14);
                ((ImageButton) view).setImageResource(R.drawable.buttonblue);
                view.setTag("x");


            }
            ((TextView) findViewById(R.id.entext)).setText(textContainer);

            if(currentw.get(0).kelimeen.toLowerCase().equals(textContainer.toLowerCase()))
            {   counter=0;
                  Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.wordlesuccest);
                ((TextView)dialog.findViewById(R.id.textView7)).setText("Aferim Doğru Cevap.");
                ((TextView)dialog.findViewById(R.id.EN)).setText(currentw.get(0).kelimeen);
                ((TextView)dialog.findViewById(R.id.TR)).setText(currentw.get(0).kelimetr);
                dialog.setCancelable(true);
                dialog.findViewById(R.id.wordlecomplateonay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        retryclick(view);
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
            else if(currentw.get(0).kelimeen.length()<=counter)
            {
                counter=0;


                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.wordlesuccest);
                ((TextView)dialog.findViewById(R.id.textView7)).setText("Yanlış Cevap.");
                ((TextView)dialog.findViewById(R.id.EN)).setText(currentw.get(0).kelimeen);
                ((TextView)dialog.findViewById(R.id.TR)).setText(currentw.get(0).kelimetr);
                ((TextView)dialog.findViewById(R.id.textView9)).setText("Sonraki");
                dialog.setCancelable(true);
                dialog.findViewById(R.id.wordlecomplateonay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        retryclick(view);
                        dialog.cancel();
                    }
                });
                dialog.show();


            }
        }else
        {


        }

    }

}