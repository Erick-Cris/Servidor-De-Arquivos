package CodigoFonte;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* Atividade Prática: Unidade IV
* Aluno: Erick Cristian de Oliveira Pereira
* Matrícula: 11621BSI265
*/

public class Main {

    public static void main(String[] args) throws Exception {

        int porta = 8080;

        ExecutorService pool = Executors.newFixedThreadPool(10);

        ServerSocket socketServ = new ServerSocket(porta);

        Socket socketCli;

        while (true) {

            System.out.println( "Servidor Ativo" );

            socketCli = socketServ.accept();
            socketCli.setSoLinger(true, 0);
            //socketCli.setSoTimeout(1000);
            HttpRequest requisicao = new HttpRequest(socketCli);
            pool.execute(new Thread(requisicao));

        }

    }
}
