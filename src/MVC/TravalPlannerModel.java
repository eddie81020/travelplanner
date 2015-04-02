/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MVC;

import java.util.LinkedList;

/**
 *
 * @author Eddie
 */
public class TravalPlannerModel {
    private String Departure;
    private String Destination;
    private LinkedList<TPListener> listeners;
    
    public TravalPlannerModel(){
        Departure = "";
        Destination = "";
        listeners = new LinkedList<>();
    }
    
    public void setDeparture(String s){
        Departure = s;
        
    }
    
    public void setDestination(String s){
        Destination = s;
        for(TPListener next : listeners){
            next.rebuild(s, s);
        }
    }
    
    public String getDeparture(){
        return Departure;
    }
    
    public String getDestination(){
        return Destination;
    }
    
    public void addListener(TPListener newListener){
        listeners.add(newListener);
    }
    
}
