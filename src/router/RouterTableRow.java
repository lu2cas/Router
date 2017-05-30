package router;

import java.net.InetAddress;

public class RouterTableRow {

    private String destinationIP;
    private int metric;
    private String outgoingIP;

    public RouterTableRow(String destinationIP, int metric, String outgoingIP) {
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

}