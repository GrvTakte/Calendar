/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pivotal.calendar;

import com.codename1.components.SpanLabel;
import com.codename1.db.Cursor;
import com.codename1.db.Database;
import com.codename1.db.Row;
import com.codename1.io.Log;
import com.codename1.ui.Button;
import com.codename1.ui.Calendar;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Label;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.table.TableLayout;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author gaurav
 */
public class Customised extends Calendar {
    Database db = null;
    Cursor cur =null;         
    ArrayList<String[]> data = new ArrayList<>();
    int i,j,columns;
    Calender obj = new Calender();
          
           private int initCounter = 0;
           public int msg = 100;
      
          public Customised(){
              
          }
          
          @Override
          protected void updateButtonDayDate(Button dayButton,int currentMonth, int day) {
             
              Log.p( ""+this.initCounter);
              Log.p("msg="+this.msg);
              
              Log.p("updateButtonDayDate");
              
              
         dayButton.setText(""+day);
 
         

          dayButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {     
//Check which date having how many number of events===============================================================
          
          try{
               obj.ShowEvent.removeAll();
          cur =  db.executeQuery("SELECT Event, Description from CalendarData  WHERE Date = ? ", obj.dateLabel.getText());
          columns = cur.getColumnCount();
          if(columns > 0) {
          boolean next = cur.next();
          if(next) {               
          String[] columnNames = new String[columns];
          for(int iter = 0 ; iter < columns ; iter++) {
          columnNames[iter] = cur.getColumnName(iter);
          }
          while(next) {
          Row currentRow = cur.getRow();
          String[] currentRowArray = new String[columns];
          for(int iter = 0 ; iter < columns ; iter++) {
          currentRowArray[iter] = currentRow.getString(iter);
          }
          data.add(currentRowArray);
          next = cur.next();
          }
          Object[][] arr = new Object[data.size()][];
          data.toArray(arr);
          }
          }
          }catch(IOException e){
          e.printStackTrace();
          }
          for(i = 0 ; i< data.size(); i++){
          Log.p(data.get(i)[0]);
          }
                    
          Label a = new Label(obj.dateLabel.getText());
          Label b = new Label("          "+i);
          Container container1 = TableLayout.encloseIn(2, a,b);
          container1.setUIID("container1");
          
          obj.ShowEvent.add(container1);
          
          
              for( i = 0 ; i< data.size(); i++){
              for(j = 0; j<columns; j++){
          Log.p(data.get(i)[j]);
          SpanLabel spanData = new SpanLabel(data.get(i)[j]);
          spanData.setUIID("SpanLabel");
          obj.ShowEvent.add(spanData);    
          }
          Label space = new Label("=======================");
          obj.ShowEvent.add(space);
          Log.p("###################");
          }
          data.clear();
        
          if(i>0){
              if(Dialog.show("Choose action", "What you want to do?", "Add Events","View Events")){
                  obj.calendar.show();
              }
                  else{
                  obj.ShowEvent.show();
              }
          }else{
              Dialog.show("Add event","There is no event to display, Please add events first","OK","");
                      
          }
//============================================================================================================
          } 
          });
          
              
          }
          
    /*      @Override
          protected void initComponent(){
              ArrayList<String[]> data1 = new ArrayList<>();
              int k;
              Log.p("initComponent");
           
                try{
                      cur = db.executeQuery("select Date from CalendarData");
                      columns = cur.getColumnCount();
                      if(columns > 0) {
                          boolean next = cur.next();
                          if(next) {               
                                  String[] columnNames = new String[columns];
                                  for(int iter = 0 ; iter < columns ; iter++) {
                                      columnNames[iter] = cur.getColumnName(iter);
                                  }
                              while(next) {
                                  Row currentRow = cur.getRow();
                                  String[] currentRowArray = new String[columns];
                                  for(int iter = 0 ; iter < columns ; iter++) {
                                      currentRowArray[iter] = currentRow.getString(iter);
                                  }
                                  data1.add(currentRowArray);
                                  next = cur.next();
                               }
                            Object[][] arr = new Object[data1.size()][];
                            data1.toArray(arr);
                            //
                            Log.p("======= revalidate() ========");
                            this.initCounter ++;
                            //this.revalidate();
                            //this.forceRevalidate();
                            //this.getUIManager().
                                //this.invalidate();
                                
                                //initComponentImpl 
                            //
                          }
                      }
                }catch(IOException e){
                      e.printStackTrace();
                }
            /*    
            for(k = 0 ; k< data1.size(); k++){
                  Log.p(data1.get(k)[0]);
             }
            
          } */
          
          /*
          @Override
          protected boolean isInitialized(){
              boolean result = false;
              Log.p("isInitialised : "+super.isInitialized());
              return result;
          }
          */
          

       
          
          
          
          @Override
          protected Button createDay() {
          Button day = new Button();
          day.setAlignment(CENTER);
          day.setUIID("CalendarDay1");
          day.setEndsWith3Points(false);
          day.setTickerEnabled(false);
          return day;
          }
          
          
          
}
