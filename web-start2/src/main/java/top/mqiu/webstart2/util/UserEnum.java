package top.mqiu.webstart2.util;


public enum UserEnum {
//    zs(1,"zs", "123456"), li("ls", "123");
    zs("zs", "123456"),
    li("ls", "123");


    public final String username;
    public final String password;


    private UserEnum(String username, String password) {
        this.username = username;
        this.password = password;
    }

}