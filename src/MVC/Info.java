/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MVC;

/**
 *
 * @author angus_000
 */
public class Info {
    String name;
    String imgurl;
    double price;
    double rating;
    
    public Info(String n, String url, double p, double r){
        name = n;
        imgurl = url;
        price = p;
        rating = r;
    }
    
    public String getName(){
        return name;
    }
    
    public String getURL(){
        return imgurl;
    }
    
    public double getPrice(){
        return price;
    }
    
    public double getRating(){
        return rating;
    }
}
