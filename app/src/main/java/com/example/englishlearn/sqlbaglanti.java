package com.example.englishlearn;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TimeZone;

public class sqlbaglanti   {
    static Context context;
    static Connection conn;
    static int kisiSayisi = 0;
    static Statement stmt;

    static int currentUserId=-1;
    static String currentUserToken;

    public static void connectToSQL(Context a) {
        context = a;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String ip = "192.168.1.104";
        String port = "1433";
        String db = "Yproje";
        String username = "androiduser1";
        String password = "123456789S";
        String connUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + "/" + db + ";user=" + username + ";password=" + password + ";";
        try {
            conn = DriverManager.getConnection(connUrl);
            stmt = conn.createStatement();

        } catch (Exception e) {
            Log.e("SQL_ERROR", e.getMessage());

        }
    }


    public static void accountcreate(String username, String paswword) {
        String query = "INSERT INTO dbo.kulanici (user_name, user_pasword) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, paswword);
            // Görselin binary verisi
            // SQL sorgusunu çalıştır
            int rows = ps.executeUpdate();
            System.out.println("Rows inserted: " + rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    public static int getcurrentuserid() {


        return currentUserId;
    }

    public static boolean getuserLogin(String username, String pasword) {
        try {
            wordset();
            ResultSet rs = stmt.executeQuery("SELECT userid FROM dbo.kulanici WHERE user_name = '" + username + "' AND user_pasword = '" + pasword + "'");
            if (rs.next()) {
                currentUserId=rs.getInt(1);
                rs.close();
                return true;
            } else {
                rs.close();
                return false;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean getusersConfirm(String username) {
        try {
            ResultSet kisiSayisi1 = stmt.executeQuery("SELECT * FROM dbo.kulanici WHERE user_name = '" + username + "'");
            if (kisiSayisi1.next()) {
                kisiSayisi1.close();
                return false;
            } else {
                kisiSayisi1.close();
                return true;
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public   ArrayList<String> getcompleetlearnword(int userid,int count){
        ArrayList<String> Sing = new ArrayList();
        try {
            int localvrbl=0;
            String query = "SELECT d.kelimeTr,d.kelimeEn FROM dbo.kelimeler AS d  JOIN dbo.ezber AS e ON e.kelimeid = d.kelimeid where e.kelimetekrar >5 and userid ? ORDER BY e.kelimeid  ASC";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userid);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Sing.add(rs.getString(1));
                localvrbl++;
                if(localvrbl>=count)
                {
                    break;
                }
            }
            if(localvrbl==0)
            {
                Sing.add("light");
            }
            rs.close();
        } catch (Exception e) {
            Log.e("SQL_ERROR", e.getMessage());

        }
        // Şimdi tüm 10 veri combinedData içinde
        return Sing;
    }
    public ArrayList<kelimedata> getLearnedWords(  int count) {
        wordset();
        ArrayList<kelimedata> words = new ArrayList<>();
        try {
            String query = "SELECT TOP " + count + " d.kelimeTr, d.kelimeEn, e.kelimetekrar, e.kelimefirsdate, e.kelimeid " +
                    "FROM dbo.kelimeler AS d " +
                    "JOIN dbo.ezber AS e ON e.kelimeid = d.kelimeid " +
                    "WHERE e.userid = ? " +
                    "AND e.kelimetekrar > 5 " +
                    "AND e.kelimefirsdate <= DATEADD(DAY, 15, CAST(GETDATE() AS DATE)) " +
                    "AND LEN(d.kelimeEn) = 5 " +
                    "ORDER BY NEWID()";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, getcurrentuserid());
            ResultSet rs = pstmt.executeQuery();

           while (rs.next()) {
                words.add(new kelimedata(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getTimestamp(4),rs.getInt(5)));
            }



            rs.close();
            pstmt.close();
        } catch (Exception e) {
            Log.e("SQL_ERROR", e.getMessage());

        }
        if(!words.isEmpty())
        {
            return words;
        }
        else
        {
            return  randomGetLen5(1);
        }

    }

    public  static ArrayList<kelimedata>  randomGet(int count) {
        ArrayList<kelimedata> Sing = new ArrayList();
        try {

            String query = "SELECT TOP "+count+" k.kelimeTr,  k.kelimeEn,e.kelimetekrar,e.kelimefirsdate,e.kelimeid FROM dbo.ezber as e JOIN dbo.kelimeler as k ON e.kelimeid = k.kelimeid where e.userid = ? ORDER BY NEWID()";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, currentUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Sing.add(new kelimedata(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getTimestamp(4),rs.getInt(5)));

            }
            rs.close();
        } catch (Exception e) {
            Log.e("SQL_ERROR", e.getMessage());

        }

        return Sing;
    }
    public  static ArrayList<kelimedata>  randomGetLen5(int count) {
        ArrayList<kelimedata> Sing = new ArrayList();
        try {

            wordset();
            String query = "SELECT TOP "+count+" k.kelimeTr,  k.kelimeEn,e.kelimetekrar,e.kelimefirsdate,e.kelimeid FROM dbo.ezber as e JOIN dbo.kelimeler as k ON e.kelimeid = k.kelimeid where e.userid = ? AND LEN(k.kelimeEn) = 5  ORDER BY NEWID()";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, currentUserId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Sing.add(new kelimedata(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getTimestamp(4),rs.getInt(5)));

            }
            rs.close();
        } catch (Exception e) {
            Log.e("SQL_ERROR", e.getMessage());

        }

        return Sing;
    }
    public static   ArrayList<kelimedata>  ogrenilecekKelimeler(int count) {
        wordset();
        ArrayList<kelimedata> words = new ArrayList<>();
        try {
            String query = "SELECT TOP " + count + " d.kelimeTr, d.kelimeEn, e.kelimetekrar, e.kelimefirsdate, e.kelimeid " +
                    "FROM dbo.kelimeler AS d " +
                    "JOIN dbo.ezber AS e ON e.kelimeid = d.kelimeid " +
                    "WHERE e.userid = ? " +
                    "AND e.kelimetekrar < 6 " +
                    " AND e.kelimefirsdate <= DATEADD(DAY, 5, CAST(GETDATE() AS DATE)) " +
                    " ORDER BY NEWID()";


            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, getcurrentuserid());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                words.add(new kelimedata(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getTimestamp(4),rs.getInt(5)));
            }

            rs.close();
            pstmt.close();
        } catch (Exception e) {
            Log.e("SQL_ERROR", e.getMessage());

        }  if(words.isEmpty())
        {
            wordset();
            words=randomGet(1);
        }
        return words;
    }

    public static void learnWorldUpdate(kelimedata k) {
        try {
            String query = "UPDATE dbo.ezber SET kelimefirsdate = ?, kelimetekrar = kelimetekrar + 1 WHERE kelimeid = ? AND userid = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            int tekrarSayisi = k.kelime_tekrar;
            int[] dayValues = {1, 7, 30, 90, 180, 365};
            int daysAfter = dayValues[Math.min(tekrarSayisi - 1, dayValues.length - 1)];

            long futureTimeMillis = System.currentTimeMillis() + (daysAfter * 24L * 60 * 60 * 1000);
            pstmt.setTimestamp(1, new Timestamp(futureTimeMillis));
            pstmt.setInt(2, k.kelimeid);
            pstmt.setInt(3, getcurrentuserid());

            int updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                Log.d("SQL_UPDATE", "Kayıt başarıyla güncellendi.");
            } else {
                Log.d("SQL_UPDATE", "Hiçbir kayıt güncellenmedi.");
            }

            pstmt.close();

        } catch (Exception e) {
            Log.e("SQL_ERROR", e.getMessage());

        }
    }


    public static void deleteLocalWorld(int kelimeid) {
        try {
            String query = "DELETE FROM dbo.ezber WHERE kelimeid = ? AND userid = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, kelimeid);
            pstmt.setInt(2, getcurrentuserid());

            int deletedRows = pstmt.executeUpdate();

            if (deletedRows > 0) {
                Log.d("SQL_DELETE", "Kayıt başarıyla silindi.");
            } else {
                Log.d("SQL_DELETE", "Hiçbir kayıt silinmedi.");
            }

            pstmt.close();

        } catch (Exception e) {
            Log.e("SQL_ERROR", e.getMessage());

        }
    }

    public static void wordadd(String kelimetr, String kelimeEn) {
        try {
            String query = "INSERT INTO dbo.kelimeler (  kelimeTr, kelimeEn) VALUES ( ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, kelimetr);
            pstmt.setString(2, kelimeEn);
            pstmt.executeUpdate();
        } catch (Exception e) {
            Log.e("SQL_ERROR", e.getMessage());

        }
    }


    public static void wordset() {

        try {
            String query =
                    "INSERT INTO dbo.ezber (kelimeid, kelimetekrar, userid, kelimefirsdate) " +
                            "SELECT TOP 40 k.kelimeid, 0, " + getcurrentuserid() + ", GETDATE() " +
                            "FROM dbo.kelimeler k " +
                            "WHERE NOT EXISTS ( " +
                            "    SELECT 1 FROM dbo.ezber e " +
                            "    WHERE e.kelimeid = k.kelimeid AND e.userid = " + getcurrentuserid() +
                            ") " +
                            "AND NOT EXISTS ( " +
                            "    SELECT 1 FROM dbo.ezber e2 " +
                            "    WHERE e2.kelimeid = k.kelimeid AND CAST(e2.kelimefirsdate AS DATE) = CAST(GETDATE() AS DATE) " +
                            ")";
            stmt.executeUpdate(query);

// Mevcut kelimeleri kontrol eden sorgu
         query = "SELECT COUNT(*) FROM dbo.kelimeler AS k JOIN dbo.ezber AS e ON k.kelimeid = e.kelimeid " +
                    "WHERE e.kelimetekrar >= 0 AND e.kelimetekrar < 7 AND e.userid = " + getcurrentuserid() ;
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                if (rs.getInt(1) < 40) {

                 query =
                            "INSERT INTO dbo.ezber (kelimeid, kelimetekrar, userid, kelimefirsdate) " +
                                    "SELECT TOP 10 k.kelimeid, 0, " + getcurrentuserid() + ", GETDATE() " +
                                    "FROM dbo.kelimeler k " +
                                    "WHERE NOT EXISTS ( " +
                                    "    SELECT 1 FROM dbo.ezber e " +
                                    "    WHERE e.kelimeid = k.kelimeid AND e.userid = " + getcurrentuserid() +
                                    ")";
                  //  stmt.executeUpdate(query);
                 /*
       query =
                            "INSERT INTO dbo.ezber (kelimeid, kelimetekrar, userid, kelimefirsdate) " +
                                    "SELECT TOP 40 k.kelimeid, 0, " + getcurrentuserid() + ", GETDATE() " +
                                    "FROM dbo.kelimeler k " +
                                    "WHERE NOT EXISTS ( " +
                                    "    SELECT 1 FROM dbo.ezber e " +
                                    "    WHERE e.kelimeid = k.kelimeid AND e.userid = " + getcurrentuserid() +
                                    ") " +
                                    "AND NOT EXISTS ( " +
                                    "    SELECT 1 FROM dbo.ezber e2 " +
                                    "    WHERE e2.kelimeid = k.kelimeid AND CAST(e2.kelimefirsdate AS DATE) = CAST(GETDATE() AS DATE) " +
                                    ")";*/
                    stmt.executeUpdate(query);  rs.close();
                }
            }


        } catch (Exception e) {
            Log.e("SQL_ERROR", e.getMessage());

        }

    }
}
