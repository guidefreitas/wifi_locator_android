package guidefreitas.wififinder;

/**
 * Created by guilherme on 9/19/14.
 */
public class WifiInfo {
    private String bssid;
    private String ssid;
    private int signal;
    private int frequency;

    public WifiInfo(String bssid, String ssid, int signal){
        this.bssid = bssid;
        this.ssid = ssid;
        this.signal = signal;
    }

    public WifiInfo(){

    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String toPipe(){
        String data = "";
        data += this.bssid;
        data += "," + this.ssid;
        data += "," + this.signal;
        data += "," + this.frequency;
        return data;
    }
}
