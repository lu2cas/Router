package router;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Route {

    private String destinationIP;
    private int metric;
    private String outgoingIP;
    private Date modificationDate;

    public Route(String destinationIP, int metric, String outgoingIP) {
        super();
        this.outgoingIP = outgoingIP;
        this.destinationIP = destinationIP;
        this.metric = metric;
        this.modificationDate = new Date();
    }

    public String getDestinationIP() {
        return this.destinationIP;
    }

    public int getMetric() {
        return this.metric;
    }

    public String getOutgoingIP() {
        return this.outgoingIP;
    }

    public Date getModificationDate() {
        return this.modificationDate;
    }

    public void setDestinationIP(String destination_ip) {
        this.destinationIP = destination_ip;
    }

    public void setMetric(int metric) {
        this.metric = metric;
    }

    public void setOutgoingIP(String outgoing_ip) {
        this.outgoingIP = outgoing_ip;
    }

    public void setModificationDate(Date modification_date) {
        this.modificationDate = modification_date;
    }
}