package demo;

class EndTask {
    public String text;

    public EndTask(String text) {
        this.text = text;
    }

    public EndTask(Request req) {
        this.text = req.text;
    }
}