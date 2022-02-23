package CodigoFonte;

import java.io.IOException;
import java.net.Socket;

/*
 * Atividade Prática: Unidade IV
 * Aluno: Erick Cristian de Oliveira Pereira
 * Matrícula: 11621BSI265
 */

public class Task implements  Runnable
{
    Socket socket;
    Servidor servidor;

    public Task(Servidor serv, Socket skt)
    {
        servidor = serv;
        socket = skt;
    }

    //Aqui é onde cada conexão se torna uma thread e inicia
    // o download dos arquivos de maneira assíncrona para múltiplos usuários
    public void run() {

        try
        {
            servidor.RespostaComArquivo(socket);
        }
        catch
        (IOException e)
        {
            e.printStackTrace();
            System.out.println("====Erro na Thread====");
        }

        //System.out.println("Fim da conexão cliente: " + socket.getInetAddress());
    }
}
