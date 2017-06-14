package router;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Router {

    public static void main(String[] args) throws IOException {
        // Seleciona o IP local
        String localhost_ip = "";
        try {
            InetAddress inet = InetAddress.getLocalHost();
            InetAddress[] ips = InetAddress.getAllByName(inet.getCanonicalHostName());
            if (ips  != null ) {
                for (int i = 0; i < ips.length; i++) {
                    localhost_ip = ips[i].getHostAddress();
                    String[] octet = localhost_ip.split("\\.");
                    if (Integer.parseInt(octet[0]) == 10) {
                        break;
                    }
                }
            }
        } catch (UnknownHostException e) {

        }

        // Lista de endereços IP dos vizinhos
        ArrayList<String> ip_list = new ArrayList<String>();

        // Lê arquivo de entrada com lista de IP dos roteadores vizinhos
        try (BufferedReader inputFile = new BufferedReader(new FileReader("neighbors-ips.txt"))) {
            String ip;

            while ((ip = inputFile.readLine()) != null) {
                ip_list.add(ip);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // Cria instâncias da tabela de roteamento e das threads de envio e recebimento de mensagens
        Semaphore sender_mutex = new Semaphore(0);
        Semaphore receiver_mutex = new Semaphore(0);

        RouterTable router_table = new RouterTable(ip_list, localhost_ip);

        Thread message_sender = new Thread(new MessageSender(router_table, ip_list, sender_mutex, receiver_mutex));
        Thread message_receiver = new Thread(new MessageReceiver(router_table, receiver_mutex, sender_mutex));

        message_sender.start();
        message_receiver.start();
    }
}