/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.be;

/**
 *
 * @author XMdag
 */
public class Project {

    private int id;

    private String name;

    private int rate;

    private String clientName;

    private Client client;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Project(int id, String name, int rate, String clientName, int clientId, int clientRate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.clientName = clientName;
        this.client = new Client(clientId, clientName, clientRate);
    }

    public Project(int id, String name, int rate) {
        this.id = id;
        this.name = name;
        this.rate = rate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Client getCLient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return name;
    }
}
