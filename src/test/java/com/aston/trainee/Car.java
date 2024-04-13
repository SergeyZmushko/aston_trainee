package com.aston.trainee;

public record Car(String model) implements Comparable<Car> {

    @Override
    public int compareTo(Car o) {
        return o.model.compareTo(this.model);
    }

    @Override
    public String toString() {
        return "Car{" +
                "model='" + model + '\'' +
                '}';
    }
}
