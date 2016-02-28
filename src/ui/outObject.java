package ui;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
/**
 * clase que extiende de ObjectOutputStream para enviar datos sin modificar la 
 * cabecera, asi podemos utilizar la misma instancia del objeto para el envio 
 * de cualquier objeto sin que se modifique la cabecera
 * @author Tautvydas
 */
public class outObject extends ObjectOutputStream {
    /**
     * Constructor por defecto
     * @throws IOException 
     */
    public outObject() throws IOException {
        super(null);
    }
    /**
     * constructor que recibe el flujo de salida
     * @param outputStream flujo de salida
     * @throws IOException 
     */
    public outObject(OutputStream outputStream) throws IOException {
        super(outputStream);
    }
    /**
     * método que no modifica la cabecera para que se pueda enviar los datos 
     * siempre con el mismo objeto
     * @throws IOException 
     */
    @Override
    protected void writeStreamHeader() throws IOException {
        //no hacemos nada para que no me modifice la cabezera y pueda escribr las veces que quiera con el mismo objeto
    }
}
