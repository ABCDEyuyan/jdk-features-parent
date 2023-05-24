package ch01;

import java.util.Optional;

public class ClassInfo {
    private String no;
    private BanZhang banZhang;

    public ClassInfo() {
    }

    public ClassInfo(String no) {
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public BanZhang getBanZhang() {
        return banZhang;
    }

    public Optional<BanZhang> getBanZhang2() {
        return Optional.ofNullable(banZhang);
    }
    public void setBanZhang(BanZhang banZhang) {
        this.banZhang = banZhang;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "no='" + no + '\'' +
                ", banZhang=" + banZhang +
                '}';
    }
}
