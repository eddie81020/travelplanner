/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MVC;

import javafx.scene.control.Label;

/**
 *
 * @author Eddie
 */
public class InfoTabLabel extends Label implements TPListener
{
    String labelName;
    public InfoTabLabel(String name, String s){
        super(s);
        labelName = name;
    }
    
    public void rebuild(String country, String info){
        this.setText("The " + labelName + " information of " + country + " should be shown here!!!!");
    }
}
