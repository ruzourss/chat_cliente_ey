package ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tautvydas
 */
public class hiloIO extends Thread{
    
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket canal;
    private boolean sesion;
    private ArrayList<String> historial;
    private final Principal principal;
    private Chat Chat;

    public hiloIO(Socket canal,Principal principal) {
        this.canal = canal;
        sesion=true;
        historial= new ArrayList<>();
        this.principal=principal;
    }
    
    
    @Override
    public void run() {
       obtenerCanales();
       controlDeFlujos();
    }
    
    private void obtenerCanales(){
        try {
            in = new ObjectInputStream(canal.getInputStream());
            out = new ObjectOutputStream(canal.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(hiloIO.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    
    private void controlDeFlujos(){
        Chat = new Chat(this);
        try {         
            out.writeUTF(estados.SEND_NICK);
            out.flush();
            while(sesion){
                switch(in.readUTF()){
                case estados.GET_NICK:
                    out.writeUTF(principal.getjTextFieldNick().getText());
                    out.flush();
                    System.out.println("Envio nick");
                    break;
                case estados.NICK_OK:
                    out.writeUTF(estados.GET_RECORD);
                    out.flush();
                    System.out.println("Dame el historial");
                    //a la espera de recibir el historial
                    historial = (ArrayList<String>) in.readObject();
                    System.out.println("Recibo historial");
                    cargaHistorial(historial);
                    //aviso al servidor de que le voy a enviar mensajes
                    out.writeUTF(estados.SEND_MESSAGES);
                    out.flush();
                    System.out.println("aviso de que voy enviar mensajes");
                    break;
                case estados.GET_MESSAGES:
                    //en caso de que ha llegado en este estado vamos a lanzar el otro hilo que realize la 
                    //conexión por el puerto de control
                    new ConexionControl(Chat.getjListConectados(),this.getName()).start();
                    System.out.println("preparado para enviar mensajes");
                    principal.setVisible(false);
                    Chat.setVisible(true);
                    //dejamos a este hilo que este siempre recibiendo los mensajes
                    leerMensajes();
                    break;
                case estados.NICK_ERROR:
                    principal.getjLabelError().setText("Nick repetido");
                    System.out.println("Error con el nick");
                    sesion=false;
                    break;
                default:
                    System.out.println("No se puedo leer el dato");
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(hiloIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(hiloIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    
    private void leerMensajes(){
        try {
            while(sesion){
                String mensajeRecibido = (String) in.readObject();
                Chat.getjTextAreaPanel().append(mensajeRecibido+"\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(hiloIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(hiloIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enviarMensaje(String mensaje){
        try {
            out.writeObject(mensaje);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(hiloIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Método que carga el historial de mensajes y lo muestra en la ventana principal
     * @param historico el array con los mensajes
     */
    private void cargaHistorial(ArrayList<String> historico){
        for(int i=0;i<historico.size();i++){
            Chat.getjTextAreaPanel().append(historico.get(i)+"\n");
        }
    }
    
}
