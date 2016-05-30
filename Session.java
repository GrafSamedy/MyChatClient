package pckg;

public class Session {
    private static final Session SESSION = new Session();
    private String id = "";

    private Session(){

    }

    public static Session getInstance(){
        return SESSION;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
