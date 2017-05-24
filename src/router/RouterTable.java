package router;

import java.net.InetAddress;

public class RouterTable {
    /*
     * Implemente uma estrutura de dados para manter a tabela de roteamento.
     * A tabela deve possuir: IP destino, métrica e IP de saída.
     */

    public RouterTable() {}

    public void updateTable(String table, InetAddress IPAddress) {
        // Atualize a tabela de rotamento a partir da string recebida
        System.out.println(IPAddress.getHostAddress() + ": " + table);
    }

    public String getTableString() {
        String tabela_string = "!"; // Tabela de roteamento vazia conforme especificado no protocolo

        // Converta a tabela de rotamento para string, conforme formato definido no protocolo

        return tabela_string;
    }
}