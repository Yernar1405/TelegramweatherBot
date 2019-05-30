/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baztelegram;

/**
 *
 * @author Yera
 */
public class Model {
    private String name;
    private long temp;
    private long humidity;
    private String icon;
    private String main;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTemp() {
        return temp;
    }

    public void setTemp(long temp) {
        this.temp = temp;
    }

    public long getHumidity() {
        return humidity;
    }

    public void setHumidity(long humidity) {
        this.humidity = humidity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMain(String main) {
        return this.main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getMain() {
    return this.main;}
    
}
