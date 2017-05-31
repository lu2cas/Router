package router;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
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
        RouterTable router_table = new RouterTable();
router_table.updateTable("*192.168.1.2;1*192.168.1.3;1", InetAddress.getByName("192.168.1.1"));
System.out.println(ip_list); System.exit(0);
        Thread sender = new Thread(new MessageSender(router_table, ip_list));
        Thread receiver = new Thread(new MessageReceiver(router_table));

        sender.start();
        receiver.start();
    }
}