import commands.Command;
import commands.CommandHandler;
import commands.gameplay.DrawCmd;
import commands.gameplay.LeaveCmd;
import commands.gameplay.MoveCmd;
import commands.games.CreateGameCmd;
import commands.games.JoinGameCmd;
import commands.games.ListGamesCmd;
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
    public static CommandHandler createHandler() {
        CommandHandler handler = new CommandHandler();
        handler.add(new Command(new String[] {"help", "h", "?"}, new HelpCmd()));
        handler.add(new Command(new String[] {"quit", "exit"}, new QuitCmd()));
        handler.add(new Command(new String[] {"register", "reg"}, new RegisterCmd()));
        handler.add(new Command(new String[] {"login", "li"}, new LoginCmd()));
        handler.add(new Command(new String[] {"logout", "lo"}, new LogoutCmd()));
        handler.add(new Command(new String[] {"newgame", "ng"}, new CreateGameCmd()));
        handler.add(new Command(new String[] {"listgames", "lg"}, new ListGamesCmd()));
        handler.add(new Command(new String[] {"join", "j"}, new JoinGameCmd(true, "ws://localhost:15723")));
        handler.add(new Command(new String[] {"watch", "w"}, new JoinGameCmd(false, "ws://localhost:15723")));
        handler.add(new Command(new String[] {"draw", "dr"}, new DrawCmd()));
        handler.add(new Command(new String[] {"leave", "lv"}, new LeaveCmd()));
        handler.add(new Command(new String[] {"move", "mv"}, new MoveCmd()));
        return handler;
    }

    public static void main(String[] args) {
        CommandHandler handler = createHandler();

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