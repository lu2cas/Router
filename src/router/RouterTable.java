package router;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RouterTable {
    /*
     * Implemente uma estrutura de dados para manter a tabela de roteamento. A
     * tabela deve possuir: IP destino, métrica e IP de saída.
     */

    private HashMap<String, Route> routerTable;

    public RouterTable() {
        this.routerTable = new HashMap<String, Route>();
    }

    public boolean updateTable(String table_string, InetAddress sender_ip) {
        boolean table_updated = false;
        String destination_ip;
        int metric;
        String outgoing_ip = sender_ip.getHostAddress();
        table_string = table_string.trim();
        String localhost_ip = "";

        try {
            localhost_ip = InetAddress.getLocalHost().getHostAddress().trim();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        localhost_ip = "127.0.0.1";

        // Verifica se a tabela recebida está vazia
        if (table_string.equals("!")) {
            destination_ip = outgoing_ip;

            if (!destination_ip.equals(localhost_ip)) {
                if (this.routerTable.containsKey(destination_ip)) {
                    // Atualiza rota na tabela de roteamento local como sendo a de um roteador vizinho
                    this.routerTable.get(destination_ip).setMetric(1);
                    this.routerTable.get(destination_ip).setOutgoingIP(outgoing_ip);
                    this.routerTable.get(destination_ip).setReceivedDate(new Date());
                } else {
                    // Insere a nova rota na tabela de roteamento local
                    this.routerTable.put(destination_ip, new Route(outgoing_ip, 1, outgoing_ip));
                }

                table_updated = true;
            }
        } else {
            String[] table_rows = table_string.substring(1).split("\\*");
            String[] table_row;

            // Percorre as linhas da tabela recebida
            for (int i = 0; i < table_rows.length; i++) {
                table_row = table_rows[i].split(";");

                destination_ip = table_row[0];
                metric = Integer.parseInt(table_row[1]);

                destination_ip = destination_ip.trim();

                // Ignora o próprio IP para atualização da tabela local
                if (destination_ip.equals(localhost_ip)) {
                    continue;
                } else if (this.routerTable.containsKey(destination_ip)) {
                    this.routerTable.get(destination_ip).setReceivedDate(new Date());

                    /*
                     * Se o IP de destino da tabela recebida já existe na tabela
                     * de roteamento local, verifica se a metrica é menor que a
                     * contida na tabela de roteamento local antes de atualizar a
                     * rota
                     */
                    if (metric + 1 < this.routerTable.get(destination_ip).getMetric()) {
                        this.routerTable.get(destination_ip).setMetric(metric);
                        this.routerTable.get(destination_ip).setOutgoingIP(outgoing_ip);
                        table_updated = true;
                    }
                } else {
                    // Insere a nova rota na tabela de roteamento local
                    this.routerTable.put(destination_ip, new Route(destination_ip, metric + 1, outgoing_ip));
                    table_updated = true;
                }
            }
        }

        return table_updated;
    }

    public String getTableString() {
        String table_string = "";

        // Verifica se a tabela de rotemento local está vazia
        if (!this.routerTable.isEmpty()) {
            // Transforma a tabela de roteamento local no formato em string da especificação
            Route route;
            for (HashMap.Entry<String, Route> entry : this.routerTable.entrySet()) {
                route = entry.getValue();
                table_string += "*";
                table_string += route.getDestinationIP();
                table_string += ";";
                table_string += route.getMetric();
            }
        } else {
            // Tabela de roteamento vazia
            table_string = "!";
        }

        return table_string;
    }

    public void removeInactiveRouters() {
        ArrayList<String> garbage = new ArrayList<String>();

        Date current_date = new Date();
        Route route;
        String outgoing_ip;

        for (HashMap.Entry<String, Route> entry : this.routerTable.entrySet()) {
            route = entry.getValue();
            outgoing_ip = route.getOutgoingIP();

            if (current_date.getTime() - this.routerTable.get(outgoing_ip).getReceivedDate().getTime() >= 30000) {
                garbage.add(outgoing_ip);
            }
        }

        for (String destination_ip : garbage) {
            this.routerTable.remove(destination_ip);
        }
    }

    public String toString() {
        Route route;
        SimpleDateFormat date_format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

        String header = "*** Tabela de roteamento local ***\n";

        String body = "";
        for (HashMap.Entry<String, Route> entry : this.routerTable.entrySet()) {
            route = entry.getValue();
            body += "IP de destino: " + route.getDestinationIP() + "\n";
            body += "Métrica: " + route.getMetric() + "\n";
            body += "IP de saída: " + route.getOutgoingIP() + "\n";
            body += "Data de recebimento: " + date_format.format(route.getReceivedDate()) + "\n";
        }

        if (body.isEmpty()) {
            body = "[vazia]\n";
        }

        return header + body;
    }
}