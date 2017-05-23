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

public class MessageSender implements Runnable{
    public RouterTable routerTable; // Tabela de roteamento
    public ArrayList<String> neighbors; // Lista de IP's dos roteadores vizinhos

    public MessageSender(RouterTable router_table, ArrayList<String> neighbors) {
        this.routerTable = router_table;
        this.neighbors = neighbors;
    }

    @Override
    public void run() {
        DatagramSocket clientSocket = null;
        byte[] sendData;
        InetAddress IPAddress = null;

        // Cria socket para envio de mensagem
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException e) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, e);
            return;
        }

        while(true){
            // Pega a tabela de roteamento no formato string, conforme especificado pelo protocolo
            String tabela_string = this.routerTable.get_table_string();

            // Converte string para array de bytes para envio pelo socket
            sendData = tabela_string.getBytes();

            // Anuncia a tabela de roteamento para cada um dos vizinhos
            for (String ip : this.neighbors){
                // Converte string com o IP do vizinho para formato InetAddress
                try {
                    IPAddress = InetAddress.getByName(ip);
                } catch (UnknownHostException e) {
                    Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, e);
                    continue;
                }

                // Configura pacote para envio da menssagem para o roteador vizinho na porta 5000
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5000);

                // Realiza envio da mensagem
                try {
                    clientSocket.send(sendPacket);
                } catch (IOException e) {
                    Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, e);
                }
            }

            /*
             * Espera 10 segundos antes de realizar o próximo envio. Contudo, caso
             * a tabela de roteamento sofra uma alteração, ela deve ser reenvida aos
             * vizinhos imediatamente
             */
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}