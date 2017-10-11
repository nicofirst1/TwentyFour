/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twentyfour3.GUI;


/**
 *
 * @author nicolo
 */
public class player {
    
    public int pf;
    public String name;
    public player next, previous;
    public boolean isDead=false;
    
    public player(int pf1, String name1, player next1, player previous1)
    {
        pf=pf1;
        name=name1;
        next=next1;
        previous=previous1;
    }
    
    public void subtractPf(int newPf)
    {
        pf-=newPf;
        if(pf<=0) isDead=true;
    }
    
    public void addPf(int newPf)
    {
        pf+=newPf;
    }
    public void setNext(player newNext)
    {
        next=newNext;
    }
    
    public void setPrevious (player newPrevious)
    {
        previous=newPrevious;
    }
    
    
    
    
}
