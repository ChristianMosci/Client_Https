/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package christianmosci_progettoclienthttp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author mosci.christian
 */


public class httpClient {
   
    private Socket socket;              
    private BufferedReader reader;      
    private BufferedWriter writer;      
    private String host;          
    private int porta; 

    public httpClient(Socket socket, BufferedReader reader, BufferedWriter writer, String host, int porta) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.host = host;
        this.porta = porta;
    }

    // getter e setter 
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }
    
    // Metodo per creare la connessione con il server
    public void socketManager() throws IOException{
     
        // crea i socket con la porta e l'host specificato
        socket = new Socket(host, porta);
 
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    
    }
    
    // Metodo per inviare una richiesta e leggere la risposta
    public String inviaComando(String command) throws IOException{
        // Scrive la richiesta sullo stream
        writer.write(command);
        //invia i dati al server
        writer.flush();

        // Legge tutte le righe della risposta
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
        }

        // restituisce la risposta
        return response.toString();
    
        
    }

    // Metodo per fare una richiesta di tipo GET
    public String Get(String path)throws IOException{
        
       // Costruisco l’header della richiesta GET
        String request =
                "GET " + path + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "User-Agent: JavaHttpClient/1.0\r\n" +
                "Connection: close\r\n\r\n"; 

        // Invia la richiesta e ottiene la risposta
        return inviaComando(request);
    
    }
    
    // Metodo per eseguire una richiesta di tipo HEAD
    public String Head(String path)throws IOException{
    // Costruisco l’header della richiesta head
        String request =
                "HEAD " + path + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "User-Agent: JavaHttpClient/1.0\r\n" +
                "Connection: close\r\n\r\n";

        // Invia la richiesta e ottiene la risposta
        return inviaComando(request);
    
    
    }
     // Metodo per chiudere le connessioni aperte
    public void close() throws IOException {
        if (reader != null) reader.close();
        if (writer != null) writer.close();
        if (socket != null && !socket.isClosed()) socket.close();
    }
    
     // Metodo principale per gestire la comunicazione
    public String eseguiRichiesta(String tipo, String path) throws IOException {
        // connessione al server
        socketManager(); 
        String risposta;
        
       // e sceglie quale tipo di richesta fare
        if (tipo.equalsIgnoreCase("GET")) {
            risposta = Get(path);
        } else if (tipo.equalsIgnoreCase("HEAD")) {
            risposta = Head(path);
        } else {
            throw new IOException("Tipo di richiesta non supportato: " + tipo);
        }

       // chiude la connessione
        close();

        return risposta;
    }
}
