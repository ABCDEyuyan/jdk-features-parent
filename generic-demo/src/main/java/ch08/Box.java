package ch08;

public class Box<T> {
    private T data;

    public Box() {
    }

    /*public Box(T data) {
        this.data = data;
    }*/

    public void set(T data){
        this.data = data;
    }

    public T get(){
        return this.data;
    }
}
