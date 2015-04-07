/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MVC;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author Eddie
 */
public class TravalPlannerModel {
    private String Departure;
    private String Destination;
    private LinkedList<TPListener> listeners;
    
    private LinkedList<Info> hotels;
    private LinkedList<Info> flights;
    private LinkedList<Info> sights;
    
    public TravalPlannerModel(){
        Departure = "";
        Destination = "";
        listeners = new LinkedList<>();
        hotels = new LinkedList<>();
        flights = new LinkedList<>();
        sights = new LinkedList<>();
        try {
            Scanner s = new Scanner(new File("hotel.txt")).useDelimiter("###################################");
            while (s.hasNext()) {
                Scanner t = new Scanner(s.next()).useDelimiter("\r\n#	");
                 String tempName = t.next();
                String tempURL = t.next();
                Scanner r = new Scanner(t.next());
                Double tempPrice = r.nextDouble();
                r = new Scanner(t.next());
                Double tempRating = r.nextDouble();
                     Info temp = new Info(tempName, tempURL, tempPrice, tempRating);
                hotels.add(temp);
             }
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
        try {
            Scanner s = new Scanner(new File("airlines.txt")).useDelimiter("###################################");
            while (s.hasNext()) {
                Scanner t = new Scanner(s.next()).useDelimiter("\r\n#	");
                String tempName = t.next();
                String tempURL = t.next();
                Scanner r = new Scanner(t.next());
                Double tempPrice = r.nextDouble();
                r = new Scanner(t.next());
                Double tempRating = r.nextDouble();
                Info temp = new Info(tempName, tempURL, tempPrice, tempRating);
                flights.add(temp);
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
        
        try {
            Scanner s = new Scanner(new File("sights.txt")).useDelimiter("###################################");
            while (s.hasNext()) {
                Scanner t = new Scanner(s.next()).useDelimiter("\r\n#	");
                String tempName = t.next();
                String tempURL = t.next();
                Scanner r = new Scanner(t.next());
                Double tempPrice = r.nextDouble();
                r = new Scanner(t.next());
                Double tempRating = r.nextDouble();
                Info temp = new Info(tempName, tempURL, tempPrice, tempRating);
                sights.add(temp);
            }
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
        
    }
    
    public void setDeparture(String s){
        Departure = s;
        
    }
    
    public void setDestination(String s){
        Destination = s;
        this.notifyListener();
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
    
    public void notifyListener(){
        for(TPListener next : listeners){
            next.rebuild(Destination, Destination);
        }
    }
}
