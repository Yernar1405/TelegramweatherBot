/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baztelegram;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Yera
 */
public class Languages_message {
    private String url = "jdbc:postgresql://localhost:5432/postgres";
    private String login = "postgres";
    private String password = "yernar1405";
   
   /**
    * Класс для локализации
    */ 
    private String languages;
    private String term_id;
    private String term_message;
    private long user_id;
    private String main_button_weather;
    private String main_button_settings;
    private String main_button_feedback;
    private String city_button_addcity;
    private String stng_button_language;
    private String fdbk_button_sentence;
    private String fdbk_button_complaint;
    
    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public String getTerm_message() {
        return term_message;
    }

    public void setTerm_message(String term_message) {
        this.term_message = term_message;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getMain_button_weather() {
        return main_button_weather;
    }

    public void setMain_button_weather(String main_button_weather) {
        this.main_button_weather = main_button_weather;
    }

    public String getMain_button_settings() {
        return main_button_settings;
    }

    public void setMain_button_settings(String main_button_settings) {
        this.main_button_settings = main_button_settings;
    }

    public String getMain_button_feedback() {
        return main_button_feedback;
    }

    public void setMain_button_feedback(String main_button_feedback) {
        this.main_button_feedback = main_button_feedback;
    }

    public String getCity_button_addcity() {
        return city_button_addcity;
    }

    public void setCity_button_addcity(String city_button_addcity) {
        this.city_button_addcity = city_button_addcity;
    }

    public String getStng_button_language() {
        return stng_button_language;
    }

    public void setStng_button_language(String stng_button_language) {
        this.stng_button_language = stng_button_language;
    }

    public String getFdbk_button_sentence() {
        return fdbk_button_sentence;
    }

    public void setFdbk_button_sentence(String fdbk_button_sentence) {
        this.fdbk_button_sentence = fdbk_button_sentence;
    }

    public String getFdbk_button_complaint() {
        return fdbk_button_complaint;
    }

    public void setFdbk_button_complaint(String fdbk_button_complaint) {
        this.fdbk_button_complaint = fdbk_button_complaint;
    }

    public String getLanguages_message (long user_id, String term_id) throws ClassNotFoundException, SQLException{
        Class.forName("org.postgresql.Driver");
    Connection con = DriverManager.getConnection(url,login,password);
    Statement statement = con.createStatement();
    
    ResultSet test = statement.executeQuery(String.format("Select l.term from languages l inner join map_user_languages ul on ul.language_id = l.language_id where l.term_id = '%s' and  ul.id_user = %d;", term_id, user_id));
    while (test.next()) {
    this.term_message = String.valueOf(test.getString(1));
    System.out.println(String.valueOf(test.getString(1)));
    }    
        return this.term_message;
    }  
}
