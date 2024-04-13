package com.aston.trainee;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Реализация динамического массива. Имплементация CustomArrayList интерфейса.
 * Реализует основные операции для взаимодействия с динамическим массивом. Принимает
 * элементы любого типа, включая null.
 *
 * @param <E> - тип элементов в коллекции/
 * @author Siarhei Zmushko
 * @version 1.0
 */
public class CustomArrayListImpl<E> implements CustomArrayList<E> {
    /**
     * Строка для форматирования сообщения при выбросе IndexOutOfBoundException.
     */
    private static final String INDEX_OUT_OF_BOUND_EXCEPTION_FORMAT_MESSAGE = "index: %d, size: %d";

    /**
     * Сообщение при установке отрицательного capacity.
     */
    private static final String START_CAPACITY_NOT_LESS_THEN_ZERO = "start capacity must not be less then 0";
    /**
     * Массив для хранения элементов коллекции.
     */
    private Object[] elementData;
    /**
     * Количество элементов в коллекции.
     */
    private int size = 0;
    /**
     * Стартовый размер коллекции при инициализации.
     */
    private static final int START_CAPACITY = 10;

    /**
     * Размер массива для хранения элементов.
     */
    private int capacity = 0;

    /**
     * Конструктор - создание нового объекта и инициализация массива, вместимостью startCapacity.
     *
     * @param startCapacity - значение стартовой вместимости
     * @throws IllegalArgumentException - неверный ввод значения стартовой вместимости.
     */
    public CustomArrayListImpl(int startCapacity) {
        if (startCapacity > 0) {
            this.elementData = new Object[startCapacity];
        } else if (startCapacity == 0) {
            this.elementData = new Object[0];
        } else {
            throw new IllegalArgumentException(START_CAPACITY_NOT_LESS_THEN_ZERO);
        }
    }

    /**
     * Конструктор - создание нового объекта и инициализация массива, вместимостью
     * для 10 элементов.
     */
    public CustomArrayListImpl() {
        this.elementData = new Object[START_CAPACITY];
    }

    /**
     * Добавляет элемент в конец коллекции.
     *
     * @param element - доавляемый элемент в коллекцию.
     */
    @Override
    public void add(E element) {
        if (size >= capacity) {
            ensureCapacity();
        }
        elementData[size++] = element;
    }

    /**
     * Вставляет элемент по индексу, при этом, те элементы, индекс которых
     * больше - смещаются на 1 позицию вправо.
     *
     * @param index   - индекс для вставки элемента.
     * @param element - элемент для вставки в коллекцию.
     * @throws IndexOutOfBoundsException - при выходе за пределы массива.
     *                                   (index < 0 || index >= size())
     */
    @Override
    public void add(int index, Object element) {
        checkIndex(index);
        if (capacity <= index) {
            ensureCapacity();
        }
        checkIndex(index);
        for (int i = elementData.length - 1; i > index; i--) {
            elementData[i] = elementData[i - 1];
        }
        elementData[index] = element;
        size++;
    }

    /**
     * Возвращает элемент из коллекции по индексу.
     *
     * @param index - индекс элемента для получения.
     * @return элемент из коллекции по указанному индексу.
     * @throws IndexOutOfBoundsException - при выходе за пределы массива.
     *                                   (index < 0 || index >= size())
     */
    @Override
    public E get(int index) {
        checkIndex(index);
        return (E) elementData[index];
    }

    /**
     * Удаляет элемент из коллекции с указанным индексом.
     *
     * @param index - индекс элемента для удаления.
     * @return возвращает удаленный элемент.
     * @throws IndexOutOfBoundsException - при выходе за пределы массива.
     *                                   (index < 0 || index >= size())
     */
    @Override
    public E remove(int index) {
        Object element = elementData[index];
        int elToCopy = size - index - 1;
        System.arraycopy(elementData, index + 1, elementData, index, elToCopy);
        size--;
        return (E) element;
    }

    /**
     * Очищает коллекцию.
     */
    @Override
    public void clear() {
        elementData = new Object[START_CAPACITY];
        size = 0;
    }

    /**
     * Сортирует эллементы в коллекции в естественном порядке.
     */
    @Override
    public void sort() {
        Arrays.sort(elementData, 0, size);
    }

    /**
     * Заменяет элемент в коллекции с указанным индексом.
     *
     * @param index   - индекс позиции для вставки элемента.
     * @param element - заменяющий элемент.
     * @throws IndexOutOfBoundsException - при выходе за пределы массива.
     *                                   (index < 0 || index >= size())
     */
    @Override
    public void set(int index, E element) {
        elementData[index] = element;
    }


    /**
     * Сортирует элементы в коллекции по заданному компаратору.
     *
     * @param comparator - компаратор, задающий порядок сортировки.
     */
    @Override
    public void sort(Comparator<E> comparator) {
        Arrays.sort((E[]) elementData, 0, size, comparator);
    }

    /**
     * Возвращает размер коллекции (количество элементов).
     *
     * @return размер коллекции (количество элементов).
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Возвращает массив, содержащий все элементы колеллекции.
     *
     * @return массив, содержащий все элементы колеллекции.
     */
    public E[] toArray() {
        return (E[]) Arrays.copyOf(elementData, size);
    }

    /**
     * Проверяет индекс массива.
     *
     * @param index - индекс для проверки.
     * @throws IndexOutOfBoundsException - выбрасывается исключение при выходе за пределы массива
     *                                   (index < 0 || index >= size).
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.format(INDEX_OUT_OF_BOUND_EXCEPTION_FORMAT_MESSAGE, index, size));
        }
    }

    /**
     * Увеличивает вместимость массива путем создания нового массива большего
     * размера и копирования в него всех элементов из первоначального массива.
     */
    private void ensureCapacity() {
        int newCapacity = (capacity * 3) / 2 + 1;
        capacity = newCapacity;
        Object[] newArray = new Object[newCapacity];
        System.arraycopy(elementData, 0, newArray, 0, size);
        elementData = newArray;
    }
}
