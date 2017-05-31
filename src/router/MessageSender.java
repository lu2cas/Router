package router;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageSender implements Runnable {
    public RouterTable routerTable;
    public ArrayList<String> neighbors;

    public MessageSender(RouterTable router_table, ArrayList<String> neighbors) {
        this.routerTable = router_table;
        this.neighbors = neighbors;
    }

    @Override
    public void run() {
        DatagramSocket client_socket = null;
        byte[] data;
        InetAddress neighbor_ip = null;

        // Cria socket para envio de mensagem
        try {
            client_socket = new DatagramSocket();
        } catch (SocketException e) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, e);
            return;
        }

        while (true) {
            // Pega a tabela de roteamento no formato string, conforme especificado pelo protocolo
            String table = this.routerTable.getTableString();

            // Converte string para array de bytes para envio pelo socket
            data = table.getBytes();

            // Anuncia a tabela de roteamento para cada um dos vizinhos
            for (String ip : this.neighbors) {
                // Converte string com o IP do vizinho para formato InetAddress
                try {
                    neighbor_ip = InetAddress.getByName(ip);
                } catch (UnknownHostException e) {
                    Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, e);
                    continue;
                }

                // Configura pacote para envio da menssagem para o roteador vizinho na porta 5000
                DatagramPacket packet = new DatagramPacket(data, data.length, neighbor_ip, 5000);

                // Realiza envio da mensagem
                try {
                    client_socket.send(packet);
                } catch (IOException e) {
                    Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, e);
                }
            }

            /*
             * Espera 10 segundos antes de realizar o próximo envio. Contudo,
             * caso a tabela de roteamento sofra uma alteração, ela deve ser
             * reenvida aos vizinhos imediatamente
             */
            try {
                System.out.println("SLEEP");
            } catch (Exception e) {
                Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}