package ch06;

public class MyChild extends MyParent<Integer>{
    @Override
    public void setData(Integer data) {
        System.out.println("---child---");

    }

//这个方法叫桥接方法
  /*  @Override
    public void setData(Object data) {
       this.setData((Integer)data);

    }*/
}
