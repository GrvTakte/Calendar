/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pivotal.calendar;

import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Toolbar;
import java.io.IOException;

/**
 *
 * @author gaurav
 */
public class SplashScreen extends Form {
    
    public SplashScreen() throws IOException{
        Toolbar tb = new Toolbar();
        this.getTitleArea().setUIID("Container");
        this.getUnselectedStyle().setBgImage(Image.createImage("/SplashScreen.jpg"));
    }
    
}
