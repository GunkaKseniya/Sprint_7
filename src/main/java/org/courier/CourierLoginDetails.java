package org.courier;

public class CourierLoginDetails {
    private String login;
    private String password;


    public CourierLoginDetails(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierLoginDetails fromCourier (Courier courier) {
        return new CourierLoginDetails(courier.getLogin(), courier.getPassword());
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}