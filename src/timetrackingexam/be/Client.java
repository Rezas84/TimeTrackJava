/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.be;

/**
 *
 * @author saraf
 */
public class Client {
    private int id;
    private String name;
    private int rate;

    public Client(int id, String name, int rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
    }

    
    /**
     * @return the rate
     */
    public int getRate() {
        return rate;
    }

    /**
     * @param Rate the rate to set
     */
    public void setRate(int Rate) {
        this.rate = Rate;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
