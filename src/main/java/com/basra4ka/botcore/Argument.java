package com.basra4ka.botcore;

public class Argument<T> {

    private String name;
    private T value;

    private String description;

    public Argument(String name, T value, String description){
        this.name = name;
        this.value = value;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
