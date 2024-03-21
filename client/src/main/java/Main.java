import commands.Command;
import commands.CommandHandler;
import commands.general.HelpCmd;
import commands.general.QuitCmd;
import commands.session.LoginCmd;
import commands.session.LogoutCmd;
import commands.session.RegisterCmd;
import driver.AppDriver;
import server.ServerFacade;
import state.AppState;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CommandHandler handler = new CommandHandler();
        handler.add(new Command(new String[] {"help", "h", "?"}, new HelpCmd()));
        handler.add(new Command(new String[] {"quit", "exit"}, new QuitCmd()));
        handler.add(new Command(new String[] {"register"}, new RegisterCmd()));
        handler.add(new Command(new String[] {"login"}, new LoginCmd()));
        handler.add(new Command(new String[] {"logout"}, new LogoutCmd()));

        ServerFacade facade;
        try {
            facade = new ServerFacade("http://localhost:15723");
        } catch (IOException ex) {
            System.out.println("Unable to connect to the server.");
            return;
        }

        AppState state = new AppState(handler, facade);
        AppDriver driver = new AppDriver(state);
        driver.run();
    }
}