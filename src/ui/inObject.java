
package ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;

/**
 *
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
