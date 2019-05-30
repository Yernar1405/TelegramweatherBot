/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baztelegram;

import static com.oracle.util.Checksums.update;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;


/**
 *
 * @author Yera
 */

/***
 *Класс для создания формата клавиатуры
 * 
 */
public class Map_status_kayboard {
    /**
     * Определяет клаватуру нужного формата, по статусу
     */
    public static ReplyKeyboardMarkup getKeyboard (String status, long user_id) throws ClassNotFoundException, SQLException{
    if(status.equals("MAIN")){
    Kayboard main = new Kayboard();
                main.button("Погода", "Настройки");
                main.button("Отзыв");
    return main.display();}
    else if (status.equals("CITY")){
    String url = "jdbc:postgresql://localhost:5432/postgres";
    String login = "postgres";
    String password = "yernar1405";
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection(url,login,password);
        Statement statement = con.createStatement();
    Kayboard wthe = new Kayboard();
     ResultSet test = statement.executeQuery(String.format("Select city_name from city where id_user = %d;", user_id));
               while (test.next()){
                   wthe.button (test.getString(1)); 
               }
    wthe.button("Добавить город");
    wthe.button("Главная");
    return wthe.display();
    }
    else if (status.equals("STNG")){
        Kayboard stng = new Kayboard();
    stng.button("Язык");
    stng.button("Главная");
    return stng.display();
            }
    else if (status.equals("FDBK")){
    Kayboard fdbk = new Kayboard();
    fdbk.button("Предложение","Жалоба");
    fdbk.button("Главная");
    return fdbk.display();
    }
    else if (status.equals("ADCT")){
    Kayboard ADCT = new Kayboard();
    ADCT.button("Главная");
    return ADCT.display();
    }else if (status.equals("LANG")){
    Kayboard LANG = new Kayboard();
    LANG.button("KZ");
    LANG.button("RU");
    LANG.button("EN");       
    return LANG.display();
    }
    else if (status.equals("GO")){
    Kayboard LANG = new Kayboard();
    LANG.button("START!");      
    return LANG.display();
    
    }
    else{return null;}
    
}
}