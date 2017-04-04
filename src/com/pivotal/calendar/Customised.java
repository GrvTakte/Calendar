/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pivotal.calendar;

import com.codename1.ui.Button;
import com.codename1.ui.Calendar;

/**
 *
 * @author gaurav
 */
public class Customised extends Calendar{
    public Customised(){
        
    }

    @Override
    protected void updateButtonDayDate(Button dayButton, int currentMonth, int day) {
       dayButton.getAllStyles().setBgColor(0xef5555);
       dayButton.setText("* "+day);
    }

    @Override
    protected Button createDay() {
       Button day = new Button();
        day.setAlignment(CENTER);
        day.setUIID("CalendarDay");
        day.setEndsWith3Points(false);
        day.setTickerEnabled(false);
        return day;
    }
}
