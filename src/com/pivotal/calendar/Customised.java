
package com.pivotal.calendar;

import com.codename1.ui.Button;
import com.codename1.ui.Calendar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;

/**
 *
 * @author gaurav
 */
public class Customised extends Calendar{
    public Customised(){
        
    }

    @Override
    protected void updateButtonDayDate(Button dayButton, int currentMonth, int day) {
       dayButton.setText(""+day);
       dayButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent evt) {
               dayButton.getAllStyles().setBgColor(0xef5555);
       dayButton.setText("* "+day);
       dayButton.getLabelForComponent();
           }
           
       });
       
       
       
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
