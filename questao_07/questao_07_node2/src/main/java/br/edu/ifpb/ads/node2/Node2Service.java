package br.edu.ifpb.ads.node2;

import br.edu.ifpb.ads.node.questao_01_shared.SocketProcotol;
import br.edu.ifpb.ads.node.questao_01_shared.SocketUtils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 10/03/2017, 21:32:36
 */
public class Node2Service extends Thread{

    private final Socket clientSocket;
    private final String remoteIP;
    private final int remotePort;

    public Node2Service(Socket serverSocket, String remoteIP, int remotePort) {
        this.clientSocket = serverSocket;
        this.remoteIP = remoteIP;
        this.remotePort = remotePort;
    }

    private boolean checkProcessedMessage(List<Integer> values) {
        if (values.size() != 2) {
            return false;
        }

        Integer n1 = values.get(0);
        Integer n2 = values.get(1);

        if ((n1 == null || n2 == null) || (n1 == n2)) {
            return false;
        }

        return true;
    }

    @Override
    public void run(){
        try {
            System.out.println("Reading the message ...");
            String reciveMessage = SocketUtils.reciveMessage(clientSocket);
            System.out.println("Recived message: " + reciveMessage);
            
            System.out.println("Processing the message ...");
            List<Integer> integers = SocketProcotol.decodeMessage(reciveMessage);
            
            System.out.println("Checking the message ...");
            boolean check = checkProcessedMessage(integers);
            
            String finalMessage;
            
            if (check) {
                System.out.println("Repassing the message to Node3 ...");
                Socket node3 = new Socket(remoteIP, remotePort);
                SocketUtils.sendMessage(node3, reciveMessage);
                
                System.out.println("Wait an answer ...");
                finalMessage = SocketUtils.reciveMessage(node3);
                System.out.println("Recived answer: " + finalMessage);
                
                node3.close();
            } else {
                finalMessage = "0";
            }
            
            System.out.println("Return the answer to Node1 ...");
            SocketUtils.sendMessage(clientSocket, finalMessage);
        } catch (IOException ex) {
            Logger.getLogger(Node2Service.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
