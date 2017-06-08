package router;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Router {

    public static void main(String[] args) throws IOException {
        // Lista de endereços IP dos vizinhos
        ArrayList<String> ip_list = new ArrayList<String>();

        // Lê arquivo de entrada com lista de IP dos roteadores vizinhos
        try (BufferedReader inputFile = new BufferedReader(new FileReader("neighbors-ips.txt"))) {
            String ip;

            while ((ip = inputFile.readLine()) != null) {
                ip_list.add(ip);
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, e);
            return;
        }

        // Cria instâncias da tabela de roteamento e das threads de envio e recebimento de mensagens
        Semaphore mutex_a = new Semaphore(1);
        Semaphore mutex_b = new Semaphore(0);

        RouterTable router_table = new RouterTable();

        System.out.println(router_table);

        Thread message_sender = new Thread(new MessageSender(router_table, ip_list, mutex_a, mutex_b));
        Thread message_receiver = new Thread(new MessageReceiver(router_table, mutex_b, mutex_a));
        //Thread router_table_cleaner = new Thread(new RouterTableCleaner(router_table, mutex_a, mutex_b));

        message_sender.start();
        message_receiver.start();
        //router_table_cleaner.start();
    }
}