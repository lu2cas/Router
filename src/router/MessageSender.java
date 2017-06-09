package router;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class MessageSender implements Runnable {
    private RouterTable routerTable;
    private ArrayList<String> neighbors;
    private Semaphore senderMutex;
    private Semaphore receiverMutex;

    public MessageSender(RouterTable router_table, ArrayList<String> neighbors, Semaphore sender_mutex, Semaphore receiver_mutex) {
        this.routerTable = router_table;
        this.neighbors = neighbors;
        this.senderMutex = sender_mutex;
        this.receiverMutex = receiver_mutex;
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
            e.printStackTrace();
        }

        while (true) {
            //this.routerTable.removeInactiveRouters();

            // Pega a tabela de roteamento no formato string, conforme especificado pelo protocolo
            String table_string = this.routerTable.getTableString();

            // Converte string para array de bytes para envio pelo socket
            data = table_string.getBytes();

            // Anuncia a tabela de roteamento para cada um dos vizinhos
            for (String ip : this.neighbors) {
                // Converte string com o IP do vizinho para formato InetAddress
                try {
                    neighbor_ip = InetAddress.getByName(ip);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }

                // Configura pacote para envio da menssagem para o roteador vizinho na porta 5000
                DatagramPacket packet = new DatagramPacket(data, data.length, neighbor_ip, 5000);

                // Realiza envio da mensagem
                try {
                    client_socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Libera a thread do MessageReceiver
            this.receiverMutex.release();

//System.out.println(table_string); System.exit(0);
            /*
             * Espera 10 segundos antes de realizar o próximo envio. Contudo,
             * caso a tabela de roteamento sofra uma alteração, ela deve ser
             * reenvida aos vizinhos imediatamente
             */
            //Thread.sleep(10000);
            try {
                this.senderMutex.tryAcquire(10, TimeUnit.SECONDS);
System.out.println(this.routerTable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}