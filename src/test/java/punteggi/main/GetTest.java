package punteggi.main;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.junit.Test;
import static org.junit.Assert.*;

public class GetTest {
    
    private WebTarget punteggi;

    public GetTest() {
        // Creazione del client e sua connessione a "punteggi"
        Client cli = ClientBuilder.newClient();
        punteggi = cli.target("http://localhost:56476/punteggi");
    }
    
    @Test
    public void getNotAllowed() {
        Response r = punteggi.request().get();
        
        assertEquals(Response.Status.METHOD_NOT_ALLOWED.getStatusCode(),r.getStatus());
    }
}
