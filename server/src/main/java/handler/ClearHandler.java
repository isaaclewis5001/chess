package handler;

import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    private final ClearService clearService;

    public Object clear(Request request, Response response) {
        clearService.clear();
        return "";
    }

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }
}
