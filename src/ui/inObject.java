
package ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
/**
 * clase que extiende de objectInputStream 
 * modificamos la clase para que nos sobreescriba la cabecera cada vez
 * que leemos los datos, asi podemos utilizar siempre el mismo objeto para recibir los datos
 * @author Tautvydas
 */
public class inObject extends ObjectInputStream {

    public inObject() throws IOException {
        super(null);
    }

    public inObject(InputStream inputStream) throws IOException {
        super(inputStream);
    }

    @Override
    protected void readStreamHeader() throws IOException, StreamCorruptedException {
       
    }
    
    
    
}
