package router;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class RouterTable {
    /*
     * Implemente uma estrutura de dados para manter a tabela de roteamento.
     * A tabela deve possuir: IP destino, métrica e IP de saída.
     */

	private HashMap<String, Route> routerTable;

    public RouterTable() {
        this.routerTable = new HashMap<String, Route>();
    }

    public void updateTable(String table, InetAddress IPAddress) {
        // Atualize a tabela de rotamento a partir da string recebida
        // System.out.println(IPAddress.getHostAddress() + ": " + table);

        String destination_ip;
        int metric;
        String outgoing_ip = IPAddress.getHostAddress();

        // Verifica se a tabela recebida não está vazia
        if (!table.equals("!")) {
            table = table.substring(1);
            String[] table_rows = table.split("\\*");
            String[] table_row;

            // Percorre a as linhas da tabela recebida
            for (int i = 0; i < table_rows.length; i++) {
                table_row = table_rows[i].split(";");
                destination_ip = table_row[0];
                metric = Integer.parseInt(table_row[1]);

                // Verfica se o IP de destino da tabela recebida já existe na tabela de roteamento local
                if (this.routerTable.containsKey(destination_ip)) {
                    // Se existe, verifica se a metrica é menor que a contida na tabela de roteamento local
                    if (metric < this.routerTable.get(destination_ip).getMetric()) {
                        this.routerTable.get(destination_ip).setMetric(metric);
                        this.routerTable.get(destination_ip).setOutgoingIP(outgoing_ip);
                    }
                } else {
                    // Insere a nova rota na tabela de roteamento local
                    this.routerTable.put(destination_ip, new Route(destination_ip, metric, outgoing_ip));
                }
            }
        }
    }

    public String getTableString() {
        String table_string = "";

        // Verifica se a tabela de rotemento local está vazia
        if (!this.routerTable.isEmpty()) {
            // Transforma a tabela de roteamento local no formato em string da especificação
            for (HashMap.Entry<String, Route> entry : this.routerTable.entrySet()) {
                Route route = entry.getValue();
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
}