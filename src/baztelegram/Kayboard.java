/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baztelegram;

import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 *
 * @author Yera
 */
/***
 * Класс создан для урошения создания формата клавиатуры
 * Ограничение в 4 столбца
 * Нет ограничения количестов кнопок
 * Размер кнопок зависет от каличесто букв и кнопок для экономии место на экране
 */
public class Kayboard {
ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    
List<KeyboardRow> keyboard = new ArrayList<>();;
KeyboardRow row;

public boolean button_list (KeyboardRow row){
    return this.keyboard.add(row);}

public boolean button (String k1,String k2,String k3,String k4){
  row = new KeyboardRow();
  row.add(k1);
  row.add(k2);
  row.add(k3);
  row.add(k4);
return button_list (row);}
public boolean button (String k1,String k2,String k3){
  row = new KeyboardRow();
  row.add(k1);
  row.add(k2);
  row.add(k3);
return button_list (row);}
public boolean button (String k1,String k2){
  row = new KeyboardRow();
  row.add(k1);
  row.add(k2);
return button_list (row);}
public boolean button (String k1){
  row = new KeyboardRow();
  row.add(k1);
return button_list (row);}
public ReplyKeyboardMarkup display (){
    this.keyboardMarkup.setKeyboard(keyboard);
    this.keyboardMarkup.setResizeKeyboard(Boolean.TRUE);
return this.keyboardMarkup;}

    void clear() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

