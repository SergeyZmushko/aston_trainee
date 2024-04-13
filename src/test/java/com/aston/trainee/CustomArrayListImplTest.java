package com.aston.trainee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomArrayListImplTest {

    private final CustomArrayListImpl<Integer> customArrayList = new CustomArrayListImpl<>();

    @BeforeEach
    public void clear() {
        customArrayList.clear();
    }

    @Test
    public void add100ElementsTest() {
        initList(100);
        assertEquals(100, customArrayList.size());
    }

    @Test
    public void add100000ElementsTest() {
        initList(100000);
        assertEquals(100000, customArrayList.size());
    }

    @Test
    public void add100ElementsAndGetWithIndex100ThenThrowsIndexOutOfBoundsException() {
        initList(100);
        assertThrows(IndexOutOfBoundsException.class, () -> customArrayList.get(100));
    }

    @Test
    public void add100000ElementsAndGetWithIndex100000ThenThrowsIndexOutOfBoundsException() {
        initList(100000);
        assertThrows(IndexOutOfBoundsException.class, () -> customArrayList.get(100000));
    }

    @Test
    public void addElementWithIncorrectIndexThenThrowsIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> customArrayList.add(1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> customArrayList.add(-1, 5));
    }

    @Test
    public void addElementWithIndexTest() {
        initList(100);
        customArrayList.add(54, 2);
        assertEquals(2, customArrayList.get(54));
        assertEquals(101, customArrayList.size());
    }

    @Test
    public void getMethodTest() {
        initList(100);
        assertEquals(0, customArrayList.get(0));
        assertEquals(99, customArrayList.get(99));
    }

    @Test
    public void getMethodWithIncorrectIndexThrowsIndexOutOfBoundException() {
        initList(100);
        assertThrows(IndexOutOfBoundsException.class, () -> customArrayList.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> customArrayList.get(100));
    }

    @Test
    public void removeElementWithIndexTest() {
        initList(100);
        customArrayList.remove(54);
        assertEquals(55, customArrayList.get(54));
        assertEquals(99, customArrayList.size());
    }

    @Test
    public void removeElementWithIncorrectIndexThenThrowsIndexOutOfBoundException() {
        initList(100);
        assertThrows(IndexOutOfBoundsException.class, () -> customArrayList.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> customArrayList.remove(100));
    }

    @Test
    public void testClearMethod() {
        initList(100);
        customArrayList.clear();
        assertEquals(0, customArrayList.size());
    }

    @Test
    public void testSortMethod() {
        for (int i = 20; i >= 0; i--) {
            customArrayList.add(i);
        }
        CustomArrayList<Integer> expected = new CustomArrayListImpl<>();
        for (int i = 0; i <= 20; i++) {
            expected.add(i);
        }
        customArrayList.sort();
        assertArrayEquals(expected.toArray(), customArrayList.toArray());
    }

    @Test
    public void testSortWithComparator() {
        for (int i = 0; i <= 20; i++) {
            customArrayList.add(i);
        }
        CustomArrayList<Integer> expected = new CustomArrayListImpl<>();
        for (int i = 20; i >= 0; i--) {
            expected.add(i);
        }
        customArrayList.sort((o1, o2) -> o2 - o1);
        assertArrayEquals(expected.toArray(), customArrayList.toArray());
    }

    @Test
    public void testSetMethod() {
        initList(15);
        customArrayList.set(2, 200);
        assertEquals(200, customArrayList.get(2));
    }

    private void initList(int size) {
        for (int i = 0; i < size; i++) {
            customArrayList.add(i);
        }
    }
}