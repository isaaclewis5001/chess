package wsHandler;

import java.util.ArrayList;
import java.util.Iterator;

public class WsGame {
    private final ArrayList<WsClient> clients;
    private final int id;

    public WsGame(int id) {
        clients = new ArrayList<>();
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void addClient(WsClient client) {
        clients.add(client);
    }
    public Iterator<WsClient> clientsIterator() {
        return clients.listIterator();
    }
}
