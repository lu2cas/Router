package router;

import java.net.InetAddress;

public class Route {

    private String destinationIP;
    private int metric;
    private String outgoingIP;

    public Route(String destinationIP, int metric, String outgoingIP) {
        super();
        this.outgoingIP = outgoingIP;
        this.destinationIP = destinationIP;
        this.metric = metric;
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

    public void setDestinationIP(String destination_ip) {
        this.destinationIP = destination_ip;
    }

    public void setMetric(int metric) {
        this.metric = metric;
    }

    public void setOutgoingIP(String outgoing_ip) {
        this.outgoingIP = outgoing_ip;
    }

}