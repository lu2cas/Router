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
        this.mutex = mutex;
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

        byte[] data = new byte[1024];

        while (true) {
            // Cria um DatagramPacket
            DatagramPacket packet = new DatagramPacket(data, data.length);

            try {
                // Aguarda o recebimento de uma mensagem
                serverSocket.receive(packet);
            } catch (IOException e) {
                Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, e);
            }

            // Transforma a mensagem em string
            String table_string = new String(packet.getData());

            // Obtem o IP de origem da mensagem
            InetAddress sender_ip = packet.getAddress();

            try {
                // Garante acesso exclusivo à zona crítica
                this.mutex.acquire();

                this.routerTable.updateTable(table_string, sender_ip);

                // Libera acesso à zona crítica
                this.mutex.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}