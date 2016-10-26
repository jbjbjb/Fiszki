/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fiszki;

/**
 *
 * @author Justyna
 */
public class Slowo {
    private String pl;
    private String en;
  
    
    public Slowo(){
        
    }
    public Slowo(String pl, String en) {
        this.pl = pl;
        this.en = en;
       
    }
    public void set(String pl, String en) {
        this.pl = pl;
        this.en = en;
       
    }
    public String getPl() {
        return pl;
    }

    public String getEn() {
        return en;
    }



   
    
}
