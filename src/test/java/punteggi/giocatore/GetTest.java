package punteggi.giocatore;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class GetTest {
    
    private WebTarget punteggi;
    private String giocatore;

    public GetTest() {
        // Creazione del client e sua connessione a "punteggi"
        Client cli = ClientBuilder.newClient();
        punteggi = cli.target("http://localhost:56476/punteggi");
        giocatore = "giocatoreGetTest";
    }
    
    @Before
    public void aggiungiGiocatoreTest() {
        // Aggiunge un giocatore di test
        punteggi.queryParam("giocatore", giocatore)
                .request()
                .post(Entity.entity("", MediaType.TEXT_PLAIN));
    }
    
    @Test
    public void testGetOk() {
        Response r = punteggi.path(giocatore).request().get();
        assertEquals(Response.Status.OK.getStatusCode(),r.getStatus());
    }
    
    @Test
    public void testGetNotFound() {
        Response r = punteggi.path(giocatore+giocatore).request().get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(),r.getStatus());
    }
    
    @After 
    public void rimuoviGiocatoreTest() {
        punteggi.path(giocatore).request().delete();
    }
}
