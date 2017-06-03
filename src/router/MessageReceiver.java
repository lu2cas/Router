package router;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.Semaphore;

public class MessageReceiver implements Runnable {
    private RouterTable routerTable;
    private Semaphore tableMutex;
    private Semaphore socketMutex;

    public MessageReceiver(RouterTable router_table, Semaphore table_mutex, Semaphore socket_mutex) {
        this.routerTable = router_table;
        this.tableMutex = table_mutex;
        this.socketMutex = socket_mutex;
    }

    @Override
    public void run() {
        DatagramSocket server_socket = null;

        try {
            // Inicializa o servidor para aguardar datagramas na porta 5000
            server_socket = new DatagramSocket(5000);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        byte[] data = new byte[1024];
        String table_string;

        while (true) {
            // Cria um DatagramPacket
            DatagramPacket packet = new DatagramPacket(data, data.length);

            try {
                // Aguarda o recebimento de uma mensagem
                server_socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Transforma a mensagem em string
            table_string = new String(packet.getData());

            // Obtem o IP de origem da mensagem
            InetAddress sender_ip = packet.getAddress();

            try {
                // Garante acesso exclusivo à zona crítica
                this.tableMutex.acquire();

                if (this.routerTable.updateTable(table_string, sender_ip)) {
                    System.out.println(this.routerTable);
                    this.socketMutex.release();
                }

                // Libera acesso à zona crítica
                this.tableMutex.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}