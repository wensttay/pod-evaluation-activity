package br.edu.ifpb.ads.node4;

import br.edu.ifpb.ads.questao_03_shared.Configs;
import br.edu.ifpb.ads.questao_03_shared.SocketProcotol;
import br.edu.ifpb.ads.questao_03_shared.SocketUtils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Wensttay de Sousa Alencar <yattsnew@gmail.com>
 * @date 11/03/2017, 04:01:01
 */
public class Node4 {

    public static void main(String[] args) throws IOException {

        System.out.println("Starting Node4 ...");
        ServerSocket serverSocket = new ServerSocket(Configs.NODE_4_PORT);
        while (true) {
            System.out.println("Waiting a client ...");
            Socket clientNode = serverSocket.accept();

            System.out.println("Decoding the request messege ...");
            String reciveMessage = SocketUtils.reciveMessage(clientNode);
            String[] decodeMessage = SocketProcotol.decodeMessage(reciveMessage);

            System.out.println("Data received:");
            for (String string : decodeMessage) {
                System.out.println(string);
            }

            System.out.println("Processing the request message ...");
            int result = 0;
            if (decodeMessage.length == 3) {
                int x = Integer.parseInt(decodeMessage[0]);
                int y = Integer.parseInt(decodeMessage[2]);

                if (decodeMessage[1].equals("sum")) {
                    System.out.println("Obtendo a soma de " + x + " e " + y);
                    result = x + y;
                } else if (decodeMessage[1].equals("diff")) {
                    System.out.println("Obtendo a diferença entre " + x + " e " + y);
                    result = x - y;
                }
            }

            System.out.println("Returning the result ...");
            SocketUtils.sendMessage(clientNode, "" + result);
            clientNode.close();
        }
    }
}
