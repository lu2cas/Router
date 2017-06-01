package router;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageReceiver implements Runnable {
    private RouterTable routerTable;
    private Semaphore mutex;

    public MessageReceiver(RouterTable router_table, Semaphore mutex) {
        this.routerTable = router_table;
    }

    @Override
    public void run() {
        DatagramSocket serverSocket = null;

        try {
            // Inicializa o servidor para aguardar datagramas na porta 5000
            serverSocket = new DatagramSocket(5000);
        } catch (SocketException e) {
            Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, e);
            return;
        }

        byte[] receiveData = new byte[1024];

        while (true) {
            // Cria um DatagramPacket
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            try {
                // Aguarda o recebimento de uma mensagem
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, e);
            }

            // Transforma a mensagem em string
            String table = new String(receivePacket.getData());

            // Obtem o IP de origem da mensagem
            InetAddress IPAddress = receivePacket.getAddress();

            this.routerTable.updateTable(table, IPAddress);
        }
    }
}