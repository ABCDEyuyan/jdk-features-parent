package ch01;

public class BanZhang {
    private String name;
    private Address address;

    public BanZhang() {
    }

    public BanZhang(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "BanZhang{" +
                "name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
