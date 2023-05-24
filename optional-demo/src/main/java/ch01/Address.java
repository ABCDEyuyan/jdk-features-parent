package ch01;

public class Address {

    private String provine;
    private String city;

    public Address() {
    }

    public Address(String provine, String city) {
        this.provine = provine;
        this.city = city;
    }

    public String getProvine() {
        return provine;
    }

    public void setProvine(String provine) {
        this.provine = provine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "provine='" + provine + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
