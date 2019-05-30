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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Scanner;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
/**
 *
 * @author Yera
 */
public class Weather {
    
    private String url = "jdbc:postgresql://localhost:5432/postgres";
    private String login = "postgres";
    private String password = "yernar1405";
    private String cityN;
    private long city_id;
    
    /**
     * добавление города в таблицу в связке с пользователем
     */
    public String addcity (String city,long user_id) throws MalformedURLException, IOException, ParseException, ClassNotFoundException, SQLException{
       
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection(url,login,password);
        Statement statement = con.createStatement();
        
        URL url = new URL(String.format ("http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=6bab4d6713adbf3a428b1f2a7454395d", city));
        URLConnection con1 = null;

        con1=url.openConnection();
        InputStream is = con1.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;

        StringBuilder sb = new StringBuilder();
        while ((line=br.readLine())!=null){
            sb.append(line); 
        } 
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
        String cityName = (String) jsonObject.get("name");
        cityN = cityName;
        city_id = (long) jsonObject.get("id");
        ResultSet s = statement.executeQuery(String.format("Select city.city_name from city where id_user = %d and id_city = %d", user_id, city_id));
        System.out.println(s);
        ResultSet rs = statement.executeQuery(String.format("Select * from city where id_user = %d and id_city = %d", user_id, city_id));
        if (rs.next()){
        return "Город "+cityN+" уже есть в списке!";}
        else{
        statement.executeUpdate(String.format("insert into city(id_user, id_city, city_name, city_tital) values(%d, %d, '%s', 'MAIN');", user_id, city_id, cityN));
        return "Город "+cityN+" добавлен в список!";}}
    /**
     * Формирования отображения погоды и получения инормации с API и компеляция json формата
     */
    public static String getWeather(String message, Model model) throws IOException, ParseException {
        //d6ed86d2e08740a312cc462f62b2b6dd
        URL url = new URL(String.format ("http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&APPID=6bab4d6713adbf3a428b1f2a7454395d", message));
        URLConnection con1 = null;

        con1=url.openConnection();
        InputStream is = con1.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;

        StringBuilder sb = new StringBuilder();
        while ((line=br.readLine())!=null){
            sb.append(line); 
        } 
        JSONParser jsonParser = new JSONParser();
        JSONObject object = (JSONObject) jsonParser.parse(sb.toString());
        model.setName((String) object.get("name"));

        JSONObject main = (JSONObject) object.get("main");
        model.setTemp((long) main.get("temp"));
        model.setHumidity((long) main.get("humidity"));

        JSONArray getArray = (JSONArray) object.get("weather");
        Iterator i = getArray.iterator();
        JSONObject obj = (JSONObject) i.next();
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));

        return "City: "+model.getName()+"\n"+
                "Weather: "+model.getMain()+"\n"+
                "Temperature: "+model.getTemp()+"C"+"\n"+
                "Humidity: "+model.getHumidity()+"%"+"\n"+
                "http://openweathermap.org/img/w/"+model.getIcon()+".png";
    }
}
