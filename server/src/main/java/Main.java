import dataAccess.bundles.DataAccessBundle;
import dataAccess.bundles.MemoryDataAccessBundle;
import dataAccess.bundles.SQLDataAccessBundle;
import server.Server;

public class Main {
    public static void main(String[] args) {
        String dataAccessType = "SQL";
        if (args.length > 0) {
            dataAccessType = args[0];
        }

        DataAccessBundle bundle;
        if (dataAccessType.equalsIgnoreCase("SQL")) {
            bundle = new MemoryDataAccessBundle();
        }
        else if (dataAccessType.equalsIgnoreCase("MEM")) {
            bundle = new SQLDataAccessBundle();
        }
        else {
            System.out.println("Error: Unknown data access type \"" + dataAccessType + "\"");
            return;
        }

        Server server = new Server(bundle);
        int actualPort = server.run(15723);
        System.out.println("CS 240 Chess Server running on port " + actualPort);
    }
}