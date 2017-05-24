package router;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Router {

    public static void main(String[] args) throws IOException {
        // Lista de endereços IP dos vizinhos
        ArrayList<String> ip_list = new ArrayList<>();

        // Lê arquivo de entrada com lista de IP dos roteadores vizinhos
        try (BufferedReader inputFile = new BufferedReader(new FileReader("neighbors_ips.txt"))) {
            String ip;

            while ((ip = inputFile.readLine()) != null) {
                ip_list.add(ip);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Router.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        // Cria instâncias da tabela de roteamento e das threads de envio e recebimento de mensagens
        RouterTable tabela = new RouterTable();
        Thread sender = new Thread(new MessageSender(tabela, ip_list));
        Thread receiver = new Thread(new MessageReceiver(tabela));

        sender.start();
        receiver.start();
    }
}