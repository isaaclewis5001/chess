package ui;

import server.ServerException;

public class CommonMessages {
    public static void issueConnecting() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Issue connecting to server. Try again or check that the server is available.");
    }
    public static void serverException(ServerException ex) {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "Unexpected server error: ");
        System.out.println(ex.getMessage());
    }
    public static void badAuth() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + "Your login is invalid or expired. You will now be logged out.");
    }
}
