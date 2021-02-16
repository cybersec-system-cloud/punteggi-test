package punteggi.main;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PostTest {
    
    private WebTarget punteggi;

    public PostTest() {
        // Creazione del client e sua connessione a "punteggi"
        Client cli = ClientBuilder.newClient();
        punteggi = cli.target("http://localhost:56476/punteggi");
    }
    
    @Test
    public void testPostCreated() throws ParseException {
        String giocatore = "giocatorePerUnitTestPostCreated";
        
        // Creazione del punteggio di "giocatore"
        Response r = punteggi.queryParam("giocatore", giocatore)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Reperimento del giocatore aggiunto
        Response rGet = punteggi.path(giocatore).request().get();
        
        // Rimozione del punteggio aggiunto
        // (per ripristinare lo stato precedente al test)
        punteggi.path(giocatore).request().delete();
        
        // Verifica che la risposta sia "201 Created"
        assertEquals(Response.Status.CREATED.getStatusCode(),r.getStatus());
        // Verifica che il giocatore sia stato creato
        assertEquals(Response.Status.OK.getStatusCode(),rGet.getStatus());
        // Verifica che il record aggiunto sia 
        // <"giocatorePerUnitTestPostCreated",1000>
        String bodyGet = rGet.readEntity(String.class);
        JSONParser parser = new JSONParser();
        JSONObject p = (JSONObject) parser.parse(bodyGet);
        String giocatoreCreato = (String) p.get("giocatore");
        Long punteggioCreato = (Long) p.get("punteggio");
        assertEquals(giocatore,giocatoreCreato);
        assertEquals(1000,punteggioCreato.intValue());
    }

    @Test
    public void testPostConPunteggioCreated() throws ParseException {
        String giocatore = "giocatorePerUnitTestPostCreated";
        int punteggio = 12345;
        
        // Creazione del punteggio di "giocatore"
        Response r = punteggi.queryParam("giocatore", giocatore)
                            .queryParam("punteggio", punteggio)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Reperimento del giocatore aggiunto
        Response rGet = punteggi.path(giocatore).request().get();
        
        // Rimozione del punteggio aggiunto
        // (per ripristinare lo stato precedente al test)
        punteggi.path(giocatore).request().delete();
        
        // Verifica che la risposta sia "201 Created"
        assertEquals(Response.Status.CREATED.getStatusCode(),r.getStatus());
        // Verifica che il giocatore sia stato creato
        assertEquals(Response.Status.OK.getStatusCode(),rGet.getStatus());
        // Verifica che il record aggiunto sia 
        // <"giocatorePerUnitTestPostCreated",1000>
        String bodyGet = rGet.readEntity(String.class);
        JSONParser parser = new JSONParser();
        JSONObject p = (JSONObject) parser.parse(bodyGet);
        String giocatoreCreato = (String) p.get("giocatore");
        Long punteggioCreato = (Long) p.get("punteggio");
        assertEquals(giocatore,giocatoreCreato);
        assertEquals(punteggio,punteggioCreato.intValue());
    }
    
    @Test
    public void testPostBadRequest() {
        // Creazione di un punteggio "senza giocatore"
        Response r = punteggi.request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Verifica che la risposta sia 400 Bad Request
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),r.getStatus());
    }
    
    @Test
    public void testPostConPunteggioBadRequest() {
        int punteggio = 12345;
        // Creazione di un punteggio "senza giocatore"
        Response r = punteggi.queryParam("punteggio",punteggio)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Verifica che la risposta sia 400 Bad Request
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(),r.getStatus());
    }
    
    @Test
    public void testPostConflict() {
        String giocatore = "giocatoreTestPostConflict";
        
        // Creazione del punteggio di "giocatore"
        punteggi.queryParam("giocatore",giocatore).request()
                .post(Entity.entity("", MediaType.TEXT_PLAIN));
        // Creazione di un altro punteggio per lo stesso "giocatore"
        Response r = punteggi.queryParam("giocatore",giocatore)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Verifica che la risposta sia 409 Conflict
        assertEquals(Response.Status.CONFLICT.getStatusCode(),r.getStatus());
    }
    
    @Test
    public void testPostConPunteggioConflict() {
        String giocatore = "giocatoreTestPostConflict";
        int punteggio = 12354;
        
        // Creazione del punteggio di "giocatore"
        punteggi.queryParam("giocatore",giocatore)
                .queryParam("punteggio",punteggio)
                .request()
                .post(Entity.entity("", MediaType.TEXT_PLAIN));
        // Creazione di un altro punteggio per lo stesso "giocatore"
        Response r = punteggi.queryParam("giocatore",giocatore)
                            .queryParam("punteggio",punteggio)
                            .request()
                            .post(Entity.entity("", MediaType.TEXT_PLAIN));
        
        // Verifica che la risposta sia 409 Conflict
        assertEquals(Response.Status.CONFLICT.getStatusCode(),r.getStatus());
    }
}
