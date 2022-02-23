package CodigoFonte;

import java.io.*;
import java.net.*;
import java.util.*;

final class HttpRequest implements Runnable {

    public HttpRequest(Socket socket) throws Exception {
        this.socket = socket;
    }

    final static String CRLF = "\r\n";
    Socket socket;


    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void processRequest() throws Exception {


            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(isr);

            /*int aux = 0;
            while (!br.ready())
            {
                System.out.println("br ainda não está pronto: " + aux);
                throw new Exception("");
                //aux++;
            }*/

            String requestLine = br.readLine();
            String headerLine = null;
            String log = requestLine + System.getProperty("line.separator");

        try {
            if(requestLine == null)
                throw new Exception("Erro: requestLine veio nula");
/*
            while ((headerLine = br.readLine()).length() != 0) {
                //log = log + (headerLine + System.getProperty("line.separator"));
                //System.out.println(headerLine);
            }
*/
            StringTokenizer requisicao = new StringTokenizer(requestLine);
            String metodo = requisicao.nextToken();
            String arquivo = requisicao.nextToken();
            arquivo = "." + arquivo;
            FileInputStream fis = null;
            boolean existeArq = true;
            String linhaStatus = null;
            String linhaContentType = null;
            String msgHtml = null;

            if (metodo.equals("GET")) {
                try {
                    fis = new FileInputStream(arquivo);
                } catch (FileNotFoundException e) {
                    existeArq = false;
                }

                if (existeArq) {
                    linhaStatus = "HTTP/1.0 200 OK" + CRLF;
                    linhaContentType = "Content-type: application/octet-stream" + CRLF;
                } else {
                    linhaStatus = "HTTP/1.0 404 Not found" +
                            CRLF;
                    linhaContentType = "Content-type: text/html" +
                            CRLF;
                    msgHtml = "<HTML><HEAD><TITLE> Arquivo Nao Encontrado" +
                            "</TITLE></HEAD>" + "<BODY> Arquivo Nao Encontrado </BODY></HTML>";
                }

                dos.writeBytes(linhaStatus);
                dos.writeBytes(linhaContentType);
                dos.writeBytes(CRLF);
            }

            if (existeArq) {
                sendBytes(fis, dos);
                fis.close();
                fis = null;
            } else {
                dos.writeBytes(msgHtml);
            }
            //Log(dos, log, socket);

        }catch (Exception e)
        {
            System.out.println("Erro: " + e.getMessage());
        }
        finally {
            isr.close();
            dos.flush();
            dos.close();
            br.close();
            socket.close();
            System.out.println("Deu bom");
        }
    }

    private void sendBytes(FileInputStream fis, DataOutputStream os)
            throws Exception {
//Construir um buffer de 1k para comportar os bytes no caminho para o socket
        byte[] buffer = new byte[1024];
        int bytes = 0;
//Copiar o arquivo requisitado dentro da cadeia de saída do socket
//enquanto o arquivo não estiver no fim, ou seja, -1..copie
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
    }

    private void Log(DataOutputStream dos, String log, Socket socket) {
        try {

            Date date = new Date(System.currentTimeMillis());
            String dataRequisicao = date.toString();
            String pulaLinha =
                    System.getProperty("line.separator");
            FileWriter fw = new FileWriter("arquivo_de_log.txt",
                    true);
            fw.write("------------------------------------------------------" + pulaLinha);
            fw.write("Data de Requisicao: " + dataRequisicao + " GMT " +
                    pulaLinha);
            fw.write("ENDEREÇO DE ORIGEM:PORTA: " +
                    socket.getLocalSocketAddress().toString() +
                    pulaLinha);
            fw.write("Conteúdo Requisitado: " + log + pulaLinha);
            fw.write("Quantidade de bytes transmitidos: " +
                    dos.size() +
                    pulaLinha);
            fw.write("------------------------------------------------------" + pulaLinha);
            fw.write(pulaLinha);
            fw.close();
        } catch (IOException io) {
            System.out.println(io.getMessage());
        }
    }
}
