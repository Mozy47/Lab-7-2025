import functions.*;
import functions.basic.*;

import threads.*;

public class Main {
    public static void main(String[] args){
        //testIterator();
        // testFactory();
        testReflection();
    } 

    public static void testReflection() {
        System.out.println("=== Тестирование рефлексивного создания объектов ===");
        
        TabulatedFunction f;
        
        // Тест 1: Создание ArrayTabulatedFunction через границы и количество точек
        System.out.println("\n1. ArrayTabulatedFunction через границы и количество точек:");
        f = TabulatedFunctions.createTabulatedFunction(
            ArrayTabulatedFunction.class, 0, 10, 3);
        System.out.println("   Тип: " + f.getClass().getSimpleName());
        System.out.println("   Функция: " + f);
        
        // Тест 2: Создание ArrayTabulatedFunction через границы и значения
        System.out.println("\n2. ArrayTabulatedFunction через границы и значения:");
        f = TabulatedFunctions.createTabulatedFunction(
            ArrayTabulatedFunction.class, 0, 10, new double[] {0, 5, 10});
        System.out.println("   Тип: " + f.getClass().getSimpleName());
        System.out.println("   Функция: " + f);
        
        // Тест 3: Создание LinkedListTabulatedFunction через массив точек
        System.out.println("\n3. LinkedListTabulatedFunction через массив точек:");
        f = TabulatedFunctions.createTabulatedFunction(
            LinkedListTabulatedFunction.class, 
            new FunctionPoint[] {
                new FunctionPoint(0, 0),
                new FunctionPoint(5, 25),
                new FunctionPoint(10, 100)
            }
        );
        System.out.println("   Тип: " + f.getClass().getSimpleName());
        System.out.println("   Функция: " + f);
        
        // Тест 4: Табулирование с рефлексивным созданием
        System.out.println("\n4. Табулирование Sin с LinkedListTabulatedFunction:");
        f = TabulatedFunctions.tabulate(
            LinkedListTabulatedFunction.class, new Sin(), 0, Math.PI, 5);
        System.out.println("   Тип: " + f.getClass().getSimpleName());
        System.out.println("   Функция: " + f);
        
        // Тест 5: Ошибочный случай - класс не реализует TabulatedFunction
        System.out.println("\n5. Тест ошибки (класс не реализует TabulatedFunction):");
        try {
            f = TabulatedFunctions.createTabulatedFunction(
                String.class, 0, 10, 3);
        } catch (IllegalArgumentException e) {
            System.out.println("   Ожидаемая ошибка: " + e.getMessage());
        }
        
        System.out.println("\n=== Тестирование рефлексии завершено ===");
    }

    
    public static void testFactory() {
        System.out.println("=== Тестирование фабрик табулированных функций ===");
        
        Function f = new Cos();
        TabulatedFunction tf;
        
        // Тестируем фабрику по умолчанию (должна быть ArrayTabulatedFunction)
        System.out.println("\n1. Фабрика по умолчанию:");
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("   Тип: " + tf.getClass().getSimpleName());
        
        // Меняем на LinkedList фабрику
        System.out.println("\n2. LinkedList фабрика:");
        TabulatedFunctions.setTabulatedFunctionFactory(
            new LinkedListTabulatedFunction.LinkedListTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("   Тип: " + tf.getClass().getSimpleName());
        
        // Возвращаем Array фабрику
        System.out.println("\n3. Array фабрика:");
        TabulatedFunctions.setTabulatedFunctionFactory(
            new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory());
        tf = TabulatedFunctions.tabulate(f, 0, Math.PI, 11);
        System.out.println("   Тип: " + tf.getClass().getSimpleName());
        
        // Дополнительное тестирование других методов создания
        System.out.println("\n4. Тестирование разных методов создания:");
        
        // Тест создания через границы и количество точек
        TabulatedFunction tf1 = TabulatedFunctions.createTabulatedFunction(0, 10, 5);
        System.out.println("   create(0, 10, 5): " + tf1.getClass().getSimpleName());
        
        // Тест создания через границы и значения
        double[] values = {1, 2, 3, 4, 5};
        TabulatedFunction tf2 = TabulatedFunctions.createTabulatedFunction(0, 10, values);
        System.out.println("   create(0, 10, values): " + tf2.getClass().getSimpleName());
        
        // Тест создания через массив точек
        FunctionPoint[] points = {
            new FunctionPoint(0, 1),
            new FunctionPoint(2, 3), 
            new FunctionPoint(4, 5)
        };
        TabulatedFunction tf3 = TabulatedFunctions.createTabulatedFunction(points);
        System.out.println("   create(points): " + tf3.getClass().getSimpleName());
        
        System.out.println("\n=== Тестирование завершено ===");
    }
    
    public static void testIterator() {
        System.out.println("=== Тестирование итератора для ArrayTabulatedFunction ===");
        
        // Создаем тестовую функцию для ArrayTabulatedFunction
        double[] xValues = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] yValues = {2.0, 4.0, 6.0, 8.0, 10.0};
        TabulatedFunction arrayFunc = new ArrayTabulatedFunction(xValues, yValues);
        
        System.out.println("Итерация по точкам ArrayTabulatedFunction:");
        for (FunctionPoint point : arrayFunc) {
            System.out.println(point);
        }
        
        System.out.println("\n=== Тестирование итератора для LinkedListTabulatedFunction ===");
        
        // Создаем массив точек для LinkedListTabulatedFunction
        FunctionPoint[] points = {
            new FunctionPoint(1.0, 2.0),
            new FunctionPoint(2.0, 4.0),
            new FunctionPoint(3.0, 6.0),
            new FunctionPoint(4.0, 8.0),
            new FunctionPoint(5.0, 10.0)
        };
        
        TabulatedFunction linkedListFunc = new LinkedListTabulatedFunction(points);
        
        System.out.println("Итерация по точкам LinkedListTabulatedFunction:");
        for (FunctionPoint point : linkedListFunc) {
            System.out.println(point);
        }
    }


}

    
