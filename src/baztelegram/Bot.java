/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baztelegram;
/**
 *Методы для буфиризации данных
 *Методы для чтения URL сылки
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
/**
 * Методы для работы с SQL БД
 * библатека SQL
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Методы для работы с JSON Фарматом
 * библатека JSON
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
/**
 * Методы для работы телеграм ботам
 * Библиотека телеграм
 */
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


/**
 *
 * @author Yera
 */
public class Bot extends TelegramLongPollingBot {
    /**
     * Переменные для авторизации
     */
    private String url = "jdbc:postgresql://localhost:5432/postgres";
    private String login = "postgres";
    private String password = "yernar1405";
    
    @Override
    public void onUpdateReceived(Update update) {
       
        try {
            /**
             * Подключение к SQl БД
             */
        long chat_id = update.getMessage().getChatId();
            SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id);
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(url,login,password);
            Statement statement = con.createStatement();
            /**
             * получения данный по пользователю телеграм бота
             */
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            String user_username = update.getMessage().getChat().getUserName();
            String message_text = update.getMessage().getText();
            long user_id = update.getMessage().getChat().getId();
            /**
             * Обрашение к класса которые выводят погоду
             */    
            Weather w = new Weather();
            Model model = new Model();
            Languages_message lang = new Languages_message();
            /**
         * получение данных по пользователю из таблицы Users
         * 
         */       
            /***
             *Проверка на выбор языка
             * 
             */
             
            ResultSet lg = statement.executeQuery(String.format("Select * from map_user_languages where id_user = %d;", user_id));
            /**
             * Если в таблицы есть какие либы данные по пользователю, то будет сним работат.
             * Eсли нет, то внесет пользователя в таблицу, с начальным статусом 
             */
            if(lg.next()){
               ResultSet rs = statement.executeQuery(String.format("Select * from users where id_user = %d;", user_id));
                if(rs.next()){
                /**
                 * Проверяеть статус пользователья из таблицы Users и работаеть сним
                 * Статус нуже для вызова Keyboard в необходимом формате и сохраняет шаги пользователя
                 */
                String status = rs.getString("status");
                String feedback="";
                
                /***
                 * получения клавиатуры Города + Добавить город
                 * статус по которому будет ожидать название города
                 */
                if (message_text.equals("Погода")) {        
                message.setText(user_first_name+ " " +lang.getLanguages_message(user_id, "info_setcity"));
                statement.executeUpdate(String.format("update users set status = 'CITY' where id_user = %d;",user_id));
                message.setReplyMarkup(Map_status_kayboard.getKeyboard("CITY", user_id));
                }
                /**
                 * Кнопка возврата на главное меню
                 */
                else if(message_text.equals("Главная")){
                statement.executeUpdate(String.format("update users set status = 'MAIN' where id_user = %d;",user_id));
                message.setReplyMarkup(Map_status_kayboard.getKeyboard("MAIN", user_id));
                message.setText(user_first_name + " " + lang.getLanguages_message(user_id, "info_main"));
                }
                /**
                 * Настройки доступно только выбор языка
                 */
                else if(message_text.equals("Настройки")){
                statement.executeUpdate(String.format("update users set status = 'STNG' where id_user = %d;",user_id));
                message.setReplyMarkup(Map_status_kayboard.getKeyboard("STNG", user_id));
                message.setText(lang.getLanguages_message(user_id, "info_setting"));
                }
                else if (message_text.equals("Язык")){
                message.setReplyMarkup(Map_status_kayboard.getKeyboard("LANG", user_id));
                message.setText("Тілді таңдаңыз"+"\n"+"Выбери язык"+"\n"+"Choose a language");}
                else if(message_text.equals("KZ")||message_text.equals("EN")||message_text.equals("RU")){
                statement.executeUpdate(String.format("update map_user_languages set language_id = '%s' where id_user = %d;",message_text,user_id));
                message.setReplyMarkup(Map_status_kayboard.getKeyboard("MAIN", user_id));
                message.setText("=)");
                }
                /**
                 * Шаг для сохранения отзыва в таблице 
                 */
                else if(message_text.equals("Отзыв")){
                statement.executeUpdate(String.format("update users set status = 'FDBK' where id_user = %d;",user_id));
                message.setReplyMarkup(Map_status_kayboard.getKeyboard("FDBK", user_id));
                message.setText(lang.getLanguages_message(user_id, "info_feedback"));
                }
                else if(message_text.equals("Предложение")|| message_text.equals("Жалоба")){
                    statement.executeUpdate(String.format("update users set status = 'FIDD' where id_user = %d;",user_id));
                message.setReplyMarkup(Map_status_kayboard.getKeyboard("GO", user_id));
                }
                else if(status.equals("FIDD")){
                statement.executeUpdate(String.format("insert into feedback(id_user, feed_titl, feedback_term) values(%d,'%s','%s');", user_id,feedback,message_text));    
                message.setReplyMarkup(Map_status_kayboard.getKeyboard("MAIN", user_id));
                statement.executeUpdate(String.format("update users set status = 'MAIN' where id_user = %d;",user_id));
                }
                /**
                 * Шаг для связки города с пользователям
                 */
                else if(message_text.equals("Добавить город")){
                statement.executeUpdate(String.format("update users set status = 'ADCT' where id_user = %d;",user_id));
                message.setReplyMarkup(Map_status_kayboard.getKeyboard("ADCT", user_id));
                message.setText(user_first_name+" "+ lang.getLanguages_message(user_id, "info_addcity"));
                }
                else if (status.equals("ADCT")){
                statement.executeUpdate(String.format("update users set status = 'CITY' where id_user = %d;",user_id));
                try{
                w.addcity(message_text,user_id);
                message.setText(w.addcity(message_text,user_id)+"\n"+Weather.getWeather(message_text, model));
                message.setReplyMarkup(Map_status_kayboard.getKeyboard("CITY", user_id));
                }catch(FileNotFoundException e){message.setText(lang.getLanguages_message(user_id,"error_setcity"));}
                catch (ClassCastException e){message.setText(w.addcity(message_text,user_id)+"\n"+lang.getLanguages_message(user_id,"error_format"));}
                }
               /**
                * Получения погоды
                */
                else if (status.equals("CITY") && !message_text.equals("Добавить город") && !message_text.equals("Главная")){
                try{message.setReplyMarkup(Map_status_kayboard.getKeyboard("CITY", user_id));
                message.setText(Weather.getWeather(message_text, model));
                }
                catch (ClassCastException e){message.setText(lang.getLanguages_message(user_id,"error_format"));
                }
                catch(FileNotFoundException e){message.setText(lang.getLanguages_message(user_id,"error_setcity"));}
                }
                else { 
                    /**
                     * для вызова клавиятуры если вдруг закрыл или потерял ее 
                     */
                    if (
                        status.equals("CITY")){message.setReplyMarkup(Map_status_kayboard.getKeyboard("CITY", user_id));
                    message.setText("Необходимо пользоваться консолью");
                    }
                else if (
                        status.equals("MAIN")){message.setReplyMarkup(Map_status_kayboard.getKeyboard("MAIN", user_id));
                message.setText("Необходимо пользоваться консолью");
                }
                else if (
                        status.equals("STNG")){message.setReplyMarkup(Map_status_kayboard.getKeyboard("STNG", user_id));
                message.setText("Необходимо пользоваться консолью");
                }
                else if (
                        status.equals("FDBK")){message.setReplyMarkup(Map_status_kayboard.getKeyboard("FDBK", user_id));
                message.setText("Необходимо пользоваться консолью");
                }
                else {
                message.setText("Error");}}
                }
                else{
                    /**
                     * Создание статуса пользователя
                     */
                message.setText(user_first_name+lang.getLanguages_message(user_id,"info_welcome"));
                statement.executeUpdate(String.format("insert into users(id_user, first_name, status) values(%d, '%s', 'MAIN');", user_id, user_first_name));
                message.setReplyMarkup(Map_status_kayboard.getKeyboard("MAIN", user_id));
                }}
            /**
             * Создания пользователя в связке с языком
             */
            else{if(message_text.equals("KZ")||message_text.equals("EN")||message_text.equals("RU")){
            statement.executeUpdate(String.format("insert into map_user_languages(language_id, id_user) values('%s',%d);",message_text,user_id));
            message.setText("Done");
             message.setReplyMarkup(Map_status_kayboard.getKeyboard("GO", user_id));
            }
            else{message.setReplyMarkup(Map_status_kayboard.getKeyboard("LANG", user_id));
                message.setText("Тілді таңдаңыз"+"\n"+"Выбери язык"+"\n"+"Choose a language");}}
         
                execute(message); // Sending our message object to user
       } catch (TelegramApiException e) {
                e.printStackTrace();
                 
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "YeraTestChatIntBot";
    }
    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "743415362:AAH5AkLTU8MGG5g5cyNeUDb6jVY3iiIyxS4";
    }
    }
