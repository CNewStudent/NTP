package accounts;


public class UserProfile {
    private long id;
    private String login;
    private String pass;

    public UserProfile(long id, String login, String pass){
        this.id = id;
        this.login = login;
        this.pass = pass;
    }

    public UserProfile(String login){
        this.id = -1L;
        this.login = login;
        this.pass = login;
    }

    public String getLogin(){return login;}
    public String getPass(){return pass;}
}
