package com.aston.trainee;

import java.util.Comparator;

/**
 * Класс содержит набор статических методов для сортировки коллекций типа CustomArrayList.
 * Для сортировки коллекции используется алгоритм быстрой сортировки.
 *
 * @author Siarhei Zmushko
 * @version 1.0
 */
public class SortUtils {
    private static final String SOURCE_LIST_NOT_NULL = "Source list must not be null";
    private static final String ARGUMENT_NOT_NULL = "Arguments must not be null";

    /**
     * Метод сортирует коллекцию в порядке, указанном при помощи компаратора
     *
     * @param source     - коллекция для сортировки.
     * @param comparator - компаратор, указывающий порядок сортировки.
     * @param <E>        - тип коллекции.
     */
    public static <E> void quickSort(CustomArrayList<E> source, Comparator<? super E> comparator) {
        if (source == null || comparator == null) {
            throw new IllegalArgumentException(ARGUMENT_NOT_NULL);
        }
        quickSort(source, 0, source.size() - 1, comparator);
    }

    /**
     * Рекурсивный метод quickSort на вход принимает массив, а также нижний (from)
     * и верхний (to) индексы. Если from меньше to, происходит разделение массива
     * с помощью метода partition, а затем рекурсивно вызывается quickSort для левой и правой части.
     *
     * @param source     - коллекция для сортировки.
     * @param from       - индекс элемента с какого начинать сортировку.
     * @param to         - индекс элемента по который проводить сортировку.
     * @param comparator - компаратор, определяющий порадок сортировки.
     * @param <E>        - тип элементов коллекции.
     */
    private static <E> void quickSort(CustomArrayList<E> source, int from, int to, Comparator<? super E> comparator) {
        if (from < to) {
            int partitionIndex = partition(source, from, to, comparator);
            quickSort(source, from, partitionIndex - 1, comparator);
            quickSort(source, partitionIndex + 1, to, comparator);
        }
    }

    /**
     * Метод определяет положение опорного элемента и переставляет элементы так, чтобы
     * элементы меньше опорного находились слева от него, а большие — справа.
     * Этот метод возвращает индекс опорного элемента после перестановки.
     *
     * @param source     - коллекция для сортировки.
     * @param from       - индекс элемента с какого начинать сортировку.
     * @param to         - индекс элемента по который проводить сортировку.
     * @param comparator - компаратор, определяющий порадок сортировки.
     * @param <E>        - тип элементов коллекции.
     * @return индекс опорного элемента после перестановки.
     */
    private static <E> int partition(CustomArrayList<E> source, int from, int to, Comparator<? super E> comparator) {
        E pivot = source.get(to);
        int i = from - 1;
        for (int j = from; j < to; j++) {
            if (comparator.compare(source.get(j), pivot) < 0) {
                i++;
                swap(source, i, j);
            }
        }
        swap(source, i + 1, to);
        return i + 1;
    }

    /**
     * Метод сортирующий коллекцию, содержащую тип элементов, реализующих интерфейс Comparable.
     *
     * @param source - коллекция для сортировки.
     * @param <E>    - тип элементов коллекции.
     */
    public static <E extends Comparable<E>> void quickSort(CustomArrayList<E> source) {
        if (source == null) {
            throw new IllegalArgumentException(SOURCE_LIST_NOT_NULL);
        }
        quickSort(source, 0, source.size() - 1);
    }

    /**
     * Рекурсивный метод quickSort на вход принимает массив, а также нижний (from)
     * и верхний (to) индексы. Если from меньше to, происходит разделение массива
     * с помощью метода partition, а затем рекурсивно вызывается quickSort для левой и правой части.
     *
     * @param source - коллекция для сортировки.
     * @param from   - индекс элемента с какого начинать сортировку.
     * @param to     - индекс элемента по который проводить сортировку.
     * @param <E>    - тип элементов коллекции.
     */
    private static <E extends Comparable<E>> void quickSort(CustomArrayList<E> source, int from, int to) {
        if (from < to) {
            int partitionIndex = partition(source, from, to);
            quickSort(source, from, partitionIndex - 1);
            quickSort(source, partitionIndex + 1, to);
        }
    }

    /**
     * Метод определяет положение опорного элемента и переставляет элементы так, чтобы
     * элементы меньше опорного находились слева от него, а большие — справа.
     * Этот метод возвращает индекс опорного элемента после перестановки.
     *
     * @param source - коллекция для сортировки.
     * @param from   - индекс элемента с какого начинать сортировку.
     * @param to     - индекс элемента по который проводить сортировку.
     * @param <E>    - тип элементов коллекции.
     * @return индекс опорного элемента после перестановки.
     */
    private static <E extends Comparable<E>> int partition(CustomArrayList<E> source, int from, int to) {
        E pivot = source.get(to);
        int i = from - 1;
        for (int j = from; j < to; j++) {
            if (source.get(j).compareTo(pivot) < 0) {
                i++;
                swap(source, i, j);
            }
        }
        swap(source, i + 1, to);
        return i + 1;
    }

    /**
     * Метод заменяющий два элемента в коллекции местами по их индексам.
     *
     * @param source - коллекция в которой необходимо поменять местами элементы.
     * @param index1 - индекс первого элемента.
     * @param index2 - индекс второго элемента.
     * @param <E>    - тип элементов коллекции.
     */
    private static <E> void swap(CustomArrayList<E> source, int index1, int index2) {
        E temp = source.get(index1);
        source.set(index1, source.get(index2));
        source.set(index2, temp);
    }

}
