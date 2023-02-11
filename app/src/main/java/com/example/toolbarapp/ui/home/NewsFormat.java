package com.example.toolbarapp.ui.home;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsFormat {

    // Функция преобразования из общей даты в правильную
    public String dateFormat(Long a){
        Date date = new Date(a*1000);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formater1 = new SimpleDateFormat("HH:mm");
        return formater1.format(date) + " " + formater.format(date);
    }

    // Функция праавильного показа строки
    public String textFormat(String stroke){
        if (stroke.length() > 200){
            return stroke.substring(0,200) + " ..." + "\n" + "Посмотреть больше";
        }
        return stroke;
    }

    // Функция праавильного показа названия
    public String titleFormat(String stroke){
        if (stroke.length() > 14){
            return stroke.substring(0,14) + " ...";
        }
        return stroke;
    }
}
