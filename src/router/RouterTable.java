package router;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class RouterTable {
    /*
     * Implemente uma estrutura de dados para manter a tabela de roteamento.
     * A tabela deve possuir: IP destino, métrica e IP de saída.
     */

	private ArrayList<RouterTableRow> routerTable;

    public RouterTable() {}

    public void updateTable(String table, InetAddress IPAddress) {
        // Atualize a tabela de rotamento a partir da string recebida
        // System.out.println(IPAddress.getHostAddress() + ": " + table);

        String destinationIP;
        int metric;
        String outgoingIP = IPAddress.getHostAddress();

        if (!table.equals("!")) {
            String[] table_rows = table.split("*");
            String[] table_row;
            RouterTableRow routerTableRow;
            for (int i = 0; i < table_rows.length; i++) {
                table_row = table_rows[i].split(";");
                destinationIP = table_row[0];
                metric = Integer.parseInt(table_row[1]);
                routerTableRow = new RouterTableRow(destinationIP, metric, outgoingIP);
                //@todo Verificar condições de atualização da tabela
                //this.routerTable.add(routerTableRow);
            }
        }
    }

    public String getTableString() {
        String table_string = "!"; // Tabela de roteamento vazia conforme especificado no protocolo

        // Converta a tabela de rotamento para string, conforme formato definido no protocolo

        return table_string;
    }
}