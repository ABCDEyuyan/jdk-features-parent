package ch08;

public class Box<T> {

    public Box() {
    }

    public Box(T data) {
        this.data = data;
    }

    private T data;

    public void set(T data){
        this.data = data;
    }

    public T get(){
        return this.data;
    }
}
