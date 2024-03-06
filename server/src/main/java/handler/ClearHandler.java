package handler;

import dataAccess.DatabaseException;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    private final ClearService clearService;

    public Object clear(Request request, Response response) {
        try {
            clearService.clear();
        } catch (DatabaseException databaseException) {
            response.status(500);
        }
        return "";
    }

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }
}
