package bean;

public class User {
    private String name;
    private String passWord;

    public User(String name, String passWord) {
        this.name = name;
        this.passWord = passWord;
    }

    public String getName() {
        return name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
