package com.pivotal.calendar;

import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.db.Cursor;
import com.codename1.db.Database;
import com.codename1.db.Row;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.io.Storage;
import com.codename1.io.Util;
import com.codename1.ui.Button;
import com.codename1.ui.Calendar;
import com.codename1.ui.Command;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import java.io.IOException;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.table.TableLayout;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Calender{

    private Form current;
    private Resources theme;
    Form calendar, saveEvent, DataEvent,ShowEvent;
     Label dateLabel;
     TextField eventArea, descriptionArea;
     Customised cal;
     Database db = null;
     Cursor cur= null;
     Button add;
     
    public void init(Object context) {
        theme = UIManager.initFirstTheme("/theme");
        Toolbar.setGlobalToolbar(true);

 //=========================================================================================================
 //Database and Table initialization and declaration.
        String path = Display.getInstance().getDatabasePath("Events.db");
        FileSystemStorage fs = FileSystemStorage.getInstance();
        if(!fs.exists(path)) {
                    try (InputStream is = Display.getInstance().getResourceAsStream(getClass(), "/Events.db");
                            OutputStream os = fs.openOutputStream(path)) {
                            Util.copy(is, os);
                    } 
                            catch(IOException err) {
                                    Log.e(err);
                            }
        }
                    
                try{
                            db = Display.getInstance().openOrCreate("Events.db");
                           
                            db.execute("CREATE TABLE IF NOT EXISTS CalendarData (Date date NOT NULL,EventName varchar(255) NOT NULL, EventDescription varchar(255) NOT NULL)");
                            db.execute("INSERT INTO CalendarData(Date,EventName,EventDescription) VALUES(?,?,?)", new String[]{"2017-04-14","hi","hello"});
                            db.execute("INSERT INTO CalendarData(Date,EventName,EventDescription) VALUES(?,?,?)", new String[]{"2017-04-12","bye","hello"});
                }
                            catch(IOException e){
                                            e.printStackTrace();
                            }
                
                
//============================================================================================================         
       }
    
public void start() {
        if(current != null){
                    current.show();
                    return;
        }
//Splashscreen=============================================================================================================================
        try{
                SplashScreen spl = new SplashScreen();
                spl.show();
        
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                            calendar.show();
                                }
                            } , 4000);
         }
            catch(IOException e){
                            e.printStackTrace();
            }
//DataEvent & calendar Form and Componenet========================================================================================================
         
           ShowEvent = new Form("Event Explorer", new BoxLayout(BoxLayout.Y_AXIS));
            Command back2 = new Command("Back"){
                public void actionPerformed(ActionEvent ev){
                        calendar.show();
                }  
            };
            
         ShowEvent.setBackCommand(back2); 
         Toolbar Tbar = new Toolbar();
         ShowEvent.setToolbar(Tbar);
         Tbar.addCommandToLeftBar("",FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, UIManager.getInstance().getComponentStyle("Title")),back2);

          DataEvent = new Form("Data Explorer", new BorderLayout());
                Command back1 = new Command("Back"){
                        public void actionPerformed(ActionEvent ev){
                                    calendar.show();
                        }  
                };
                
            DataEvent.setBackCommand(back1);
            calendar = new Form("Events", new BoxLayout(BoxLayout.Y_AXIS));
            checkEvent();
            cal = new Customised();
            cal.check();
            calendar.add(cal);
       
            Label date = new Label("Event Date:- ");
            dateLabel = new Label();
            Label event = new Label("Event:- ");
            eventArea = new TextField();
            Label description = new Label("Event Description:- ");
            descriptionArea = new TextField();
       
            Container container = TableLayout.encloseIn(2, date, dateLabel, event, eventArea, description, descriptionArea);
            container.setUIID("container");
            calendar.add(container);
            
            cal.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {
                                                try{
                                                        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");
                                                        String selectedDate = new Date(cal.getSelectedDay()).toString();
                                                        Date date1 = sdf.parse(selectedDate);
                                                        SimpleDateFormat df  = new SimpleDateFormat("yyyy-MM-dd");
                                                        dateLabel.setText(df.format(date1));
                                                } 
                                                    catch (ParseException ex) {
                                                                ex.printStackTrace();
                                                    }                     
                                                            cal.createDay();
                            }  
            });
            
            
            Toolbar tb = new Toolbar();
            calendar.setToolBar(tb);
            
            tb.addCommandToRightBar("", FontImage.createMaterial(FontImage.MATERIAL_SAVE, UIManager.getInstance().getComponentStyle("TitleCommand")), (evt) -> {
                                                        saveEvent.show();
            });
            
            tb.addCommandToLeftBar("", FontImage.createMaterial(FontImage.MATERIAL_EVENT, UIManager.getInstance().getComponentStyle("TitleCommand")), (evt) -> {
                                try{
                                            cur = db.executeQuery("SELECT * FROM CalendarData");
                                            int columns = cur.getColumnCount();
                                            if(columns > 0) {
                                                        boolean next = cur.next();
                                                        if(next) {
                                                                    ArrayList<String[]> data = new ArrayList<>();
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
                                                                            DataEvent.add(BorderLayout.CENTER, new Table(new DefaultTableModel(columnNames, arr)));
                                                        } 
                                                            else {
                                                                            DataEvent.add(BorderLayout.CENTER, "Query returned no results");
                                                            }
                                            } 
                                                else {
                                                        DataEvent.add(BorderLayout.CENTER, "Query returned no results");
                                                }
                                 }
                                        catch(IOException e){
                                                        e.printStackTrace();
                                        }
                                
                            DataEvent.show();
                            dateLabel.setText("");
                            eventArea.setText("");
                            descriptionArea.setText("");
            });
        
                add= new Button("Add Event");
                Button clear = new Button("Clear Event");
                calendar.add(add);
                calendar.add(clear);
//============================================================================================================
//Saved Events Form Starts  ====================================================================================
                saveEvent = new Form("Saved Event", new BoxLayout(BoxLayout.Y_AXIS));
                Command back = new Command("Back"){
                        public void actionPerformed(ActionEvent ev){
                                        calendar.show();
                        }  
                };
                
                
                saveEvent.setBackCommand(back);
                Toolbar tb1 = new Toolbar();
                saveEvent.setToolBar(tb1);
                tb1.addCommandToLeftBar("", FontImage.createMaterial(FontImage.MATERIAL_ARROW_BACK, UIManager.getInstance().getComponentStyle("TitleCommand")), back);
                
//Save Events Form Ends =======================================================================================
//Add events action============================================================================================

                add.addActionListener(new ActionListener() {
                            @Override
                             public void actionPerformed(ActionEvent evt) {
                                                    Log.p(dateLabel.getText().toString());     
       
                                                    if((dateLabel.getText()== "") || (eventArea.getText() == "") || (descriptionArea.getText()== "")){
                                                                                            Dialog.show("Required field", "Please fill all the fields", "OK", "");
                                                    }
                                                            else    {

                                                                            Label date1 = new Label("Event Date:- ");
                                                                            Label dateLabel1 = new Label(dateLabel.getText());
                                                                            Label event1 = new Label("Event:- ");
                                                                            Label eventArea1 = new Label(eventArea.getText());
                                                                            Label description1 = new Label("Event Description:- ");
                                                                            Label descriptionArea1 = new Label(descriptionArea.getText());
        
                                                                            Container container1 = TableLayout.encloseIn(2, date1,dateLabel1,event1, eventArea1, description1, descriptionArea1);
                                                                            container1.setUIID("container");
                                                                            saveEvent.add(container1);           
                                                                            ToastBar.showMessage("Event successfull saved",FontImage.MATERIAL_SAVE, 4000);
                            
                                                                             try{                   
                                                                                        String[] values = new String[]{dateLabel.getText(),eventArea.getText(),descriptionArea.getText()};
                                                                                        db.execute("INSERT INTO CalendarData('Date','EventName','EventDescription') VALUES(?,?,?)", values);
                                                                             }
                                                                                    catch(IOException e){
                                                                                                            e.printStackTrace();
                                                                                    }
                                                                      }
                              }
               });
                
//=============================================================================================================
          }


public void stop() {
                    current = Display.getInstance().getCurrent();
                    if(current instanceof Dialog) {
                                    ((Dialog)current).dispose();
                                    current = Display.getInstance().getCurrent();
                    }
}
                                    
public void destroy() {
          }

//Customised Calendar dayButton=================================================================================
public class Customised extends Calendar{
          ArrayList<String[]> data = new ArrayList<>();
          int i,j,columns;
          Calender obj = new Calender();
         
          public Customised(){
             
          }
          
          
          public void  check(){
                        
                    ArrayList<String[]> ArrayData = new ArrayList<String[]>();
                    try{
                                ShowEvent.removeAll();
                                cur =  db.executeQuery("select strftime('%d', Date) from CalendarData");
                                int columns2 = cur.getColumnCount();
                                if(columns2 > 0) {
                                                boolean next = cur.next();
                                                if(next) {
                                                            String[] columnNames = new String[columns2];
                                                            for(int iter = 0 ; iter < columns ; iter++) {
                                                                            columnNames[iter] = cur.getColumnName(iter);
                                                            }
                                                            while(next) {
                                                                        Row currentRow = cur.getRow();
                                                                        String[] currentRowArray = new String[columns2];
                                                                        for(int iter = 0 ; iter < columns2 ; iter++) {
                                                                                        currentRowArray[iter] = currentRow.getString(iter);
                                                                        }
                                                                        ArrayData.add(currentRowArray);
                                                                        next = cur.next();
                                                            }
                                                            Object[][] arr = new Object[ArrayData.size()][];
                                                            ArrayData.toArray(arr);
                                                }
                                }
                    }  catch(IOException e){
                                        e.printStackTrace();
                       } 
          
                    for( int x = 0 ; x< ArrayData.size(); x++){
                                            Log.p(ArrayData.get(x)[0]);
                                            //s1.writeObject("Date"+x, ArrayData.get(x)[0]); 
                    }
         
         }
          
          
        
          @Override
          protected void updateButtonDayDate(Button dayButton,int currentMonth, int day) {
            
                            Storage storeValue = Storage.getInstance();
                            Storage storeNumber = Storage.getInstance();
                            String parseNumber = storeNumber.readObject("number").toString();
                            int Number = Integer.parseInt(parseNumber);
           // Log.p("Hello from codename one"+storeValue.readObject("Date"+Number).toString());
          /*  
          if(day==Integer.parseInt(s4.readObject("Date"+Number).toString())){
               dayButton.setText("*"+day);
               Number--;
           }else{
              dayButton.setText(""+day);
          }
        Log.p("updateButton() call");
        */
         
                            if(storeNumber.readObject("number").toString()!=null) {
                                                    if(day!=Integer.parseInt(storeValue.readObject("Date"+Number).toString())){
                                                                                    dayButton.setText(""+day);
                                                    } else{
                                                                dayButton.setText("*"+day);
                                                                Number--;
                                                                if((Number<0)) {
                                                                                   Number++;
                                                                }
                                                        }
                                                    Number--;
                            } else{
                                        dayButton = new Button(""+day);
                              }
         
          
          
          dayButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {     
//Check which date having how many number of events===============================================================
                                                try{
                                                        ShowEvent.removeAll();
                                                        cur =  db.executeQuery("SELECT EventName, EventDescription from CalendarData  WHERE Date = ? ", dateLabel.getText());
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
                                                }       catch(IOException e) {
                                                                                e.printStackTrace();
                                                        }
                                        for( i = 0 ; i< data.size(); i++){
                                                                Log.p(data.get(i)[0]);
                                        }
                    
            Label a = new Label(dateLabel.getText());
            Label b = new Label("          "+i);
            Container container1 = TableLayout.encloseIn(2, a,b);
            container1.setUIID("container1");
          
            ShowEvent.add(container1);
          
          
            for( i = 0 ; i< data.size(); i++){
                            for(j = 0; j<columns; j++){
                                            Log.p(data.get(i)[j]);
                                            SpanLabel spanData = new SpanLabel(data.get(i)[j]);
                                            spanData.setUIID("SpanLabel");
                                            ShowEvent.add(spanData);
                            }
                            Label space = new Label("=======================");
                            ShowEvent.add(space);
                            Log.p("###################");
            }
            data.clear();
        
            if(i>0){
                        if(Dialog.show("Choose action", "What you want to do?", "Add Events","View Events")) {
                                    calendar.show();
                        }
                        else {
                                  ShowEvent.show();
                        }
            }
            else {
                    Dialog.show("Add event","There is no event to display, Please add events first","OK","");
            }
          
//============================================================================================================
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

public void checkEvent(){
      ArrayList<String[]> DateData = new ArrayList<String[]>();
        try{
            
        cur = db.executeQuery("select distinct strftime('%d', Date) from CalendarData order by Date desc");
        int column = cur.getColumnCount();
        if(column > 0){
                boolean next = cur.next();
                if(next){
                   
                    String[] columnNames = new String[column];
                    for(int iter = 0; iter<column ; iter++ ){
                        columnNames[iter] = cur.getColumnName(iter);
                     }
       while(next) {
                Row currentRow = cur.getRow();
                String[] currentRowArray = new String[column];
                for(int iter = 0 ; iter < column ; iter++) {
                             currentRowArray[iter] = currentRow.getString(iter);
                        }
                DateData.add(currentRowArray);
                next = cur.next();
          }
               Object[][] arr1 = new Object[DateData.size()][];
                DateData.toArray(arr1);
          }
      }
}               catch(IOException e){
                            e.printStackTrace();
              }
       Storage storeNumber = Storage.getInstance();
        Storage storeValue = Storage.getInstance();
                for(int i = 0 ; i< DateData.size(); i++){           
                 /*  Log.p(DateData.get(i)[0]);
                storeValue.writeObject("Date"+i, DateData.get(i)[0]);
                 Log.p(storeValue.readObject("Date"+i).toString());
                 storeNumber.writeObject("number", i);*/
          }
     
}

}