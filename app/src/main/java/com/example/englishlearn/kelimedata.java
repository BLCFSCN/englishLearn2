package com.example.englishlearn;

import java.sql.Timestamp;

public class kelimedata {

    public String  kelimetr,kelimeen;
    public int kelime_tekrar,kelimeid;
    public Timestamp firsdate;

    public kelimedata(String kelimetr, String kelimeen, int kelime_tekrar, Timestamp firsdate,int kelimeid) {
        this.kelimetr = kelimetr;
        this.kelimeen = kelimeen;
        this.kelime_tekrar = kelime_tekrar;
        this.firsdate = firsdate;
        this.kelimeid=kelimeid;
    }
}
