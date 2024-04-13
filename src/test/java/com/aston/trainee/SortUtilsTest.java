package com.aston.trainee;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class SortUtilsTest {

    private final CustomArrayList<String> customArrayList = new CustomArrayListImpl<>();

    @BeforeEach
    public void init() {
        customArrayList.add("Ivan");
        customArrayList.add("Devid");
        customArrayList.add("Anton");
        customArrayList.add("Kirill");
        customArrayList.add("Arkadii");
        customArrayList.add("Evgenii");
    }

    @AfterEach
    public void clear() {
        customArrayList.clear();
    }

    @Test
    public void quickSortWithComparatorWithNaturalOrderTest() {
        CustomArrayList<String> expectedResult = new CustomArrayListImpl<>();
        expectedResult.add("Anton");
        expectedResult.add("Arkadii");
        expectedResult.add("Devid");
        expectedResult.add("Evgenii");
        expectedResult.add("Ivan");
        expectedResult.add("Kirill");

        SortUtils.quickSort(customArrayList, Comparator.naturalOrder());
        assertArrayEquals(expectedResult.toArray(), customArrayList.toArray());
    }

    @Test
    public void quickSortWithComparatorWithReverseOrderTest() {
        CustomArrayList<String> expectedResult = new CustomArrayListImpl<>();
        expectedResult.add("Kirill");
        expectedResult.add("Ivan");
        expectedResult.add("Evgenii");
        expectedResult.add("Devid");
        expectedResult.add("Arkadii");
        expectedResult.add("Anton");

        SortUtils.quickSort(customArrayList, Comparator.reverseOrder());
        assertArrayEquals(expectedResult.toArray(), customArrayList.toArray());
    }

    @Test
    public void quickSortWithComparatorTest() {
        assertThrows(IllegalArgumentException.class, () -> SortUtils.quickSort(null, Comparator.naturalOrder()));
    }

    @Test
    public void quickSortWithComparableWithNaturalOrderTest() {
        CustomArrayList<String> expectedResult = new CustomArrayListImpl<>();
        expectedResult.add("Anton");
        expectedResult.add("Arkadii");
        expectedResult.add("Devid");
        expectedResult.add("Evgenii");
        expectedResult.add("Ivan");
        expectedResult.add("Kirill");

        SortUtils.quickSort(customArrayList);
        assertArrayEquals(expectedResult.toArray(), customArrayList.toArray());
    }

    @Test
    public void quickSortWithComparableWithReverseOrderTest() {
        Car astonMartin = new Car("Aston Martin");
        Car citroen = new Car("Citroen");
        Car bmw = new Car("BMW");
        Car audi = new Car("Audi");
        Car volkswagen = new Car("Volkswagen");
        Car lada = new Car("Lada");

        CustomArrayList<Car> actualList = new CustomArrayListImpl<>();
        actualList.add(astonMartin);
        actualList.add(citroen);
        actualList.add(bmw);
        actualList.add(audi);
        actualList.add(volkswagen);
        actualList.add(lada);

        CustomArrayList<Car> expectedResult = new CustomArrayListImpl<>();
        expectedResult.add(volkswagen);
        expectedResult.add(lada);
        expectedResult.add(citroen);
        expectedResult.add(bmw);
        expectedResult.add(audi);
        expectedResult.add(astonMartin);

        SortUtils.quickSort(actualList);

        assertArrayEquals(expectedResult.toArray(), actualList.toArray());
    }
}
