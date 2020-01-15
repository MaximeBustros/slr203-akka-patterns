package demo;
import java.util.ArrayList;

public class MergedRequest {
    ArrayList<Request> requests;

    public MergedRequest() {
        requests = new ArrayList<Request>();
    }

    public void add(Request req) {
        requests.add(req);
    }

    public void clear() {
        requests.clear();
    }
}