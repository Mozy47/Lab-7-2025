import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import functions.*;
import functions.basic.*;

import threads.*;

public class Main {
    public static void main(String[] args){
        //testIterator();
        // testFactory();
        // testReflection();
        testReflectionIO();
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

    public static void testReflectionIO() {
        System.out.println("=== Тестирование рефлексивного чтения объектов ===");
        
        try {
            // Тест 1: Запись в байтовый поток и чтение через рефлексию
            System.out.println("\n1. Тест байтового потока:");
            
            // Создаем тестовую функцию и записываем в поток
            TabulatedFunction original = new ArrayTabulatedFunction(0, 10, new double[]{0, 5, 10});
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            TabulatedFunctions.outputTabulatedFunction(original, byteOut);
            
            // Читаем через рефлексию как LinkedListTabulatedFunction
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            TabulatedFunction readFunction = TabulatedFunctions.inputTabulatedFunction(
                LinkedListTabulatedFunction.class, byteIn);
            
            System.out.println("   Оригинал: " + original.getClass().getSimpleName());
            System.out.println("   Прочитано: " + readFunction.getClass().getSimpleName());
            System.out.println("   Данные: " + readFunction);
            
            // Тест 2: Запись в символьный поток и чтение через рефлексию
            System.out.println("\n2. Тест символьного потока:");
            
            // Создаем другую функцию
            TabulatedFunction original2 = new LinkedListTabulatedFunction(
                new FunctionPoint[]{new FunctionPoint(1, 1), new FunctionPoint(2, 4), new FunctionPoint(3, 9)}
            );
            StringWriter writer = new StringWriter();
            TabulatedFunctions.writeTabulatedFunction(original2, writer);
            
            // Читаем через рефлексию как ArrayTabulatedFunction
            StringReader reader = new StringReader(writer.toString());
            TabulatedFunction readFunction2 = TabulatedFunctions.readTabulatedFunction(
                ArrayTabulatedFunction.class, reader);
            
            System.out.println("   Оригинал: " + original2.getClass().getSimpleName());
            System.out.println("   Прочитано: " + readFunction2.getClass().getSimpleName());
            System.out.println("   Данные: " + readFunction2);
            
            // Тест 3: Проверка корректности данных
            System.out.println("\n3. Проверка корректности данных:");
            boolean dataCorrect = true;
            for (int i = 0; i < original2.getPointsCount(); i++) {
                if (Math.abs(original2.getPointX(i) - readFunction2.getPointX(i)) > 1e-10 ||
                    Math.abs(original2.getPointY(i) - readFunction2.getPointY(i)) > 1e-10) {
                    dataCorrect = false;
                    break;
                }
            }
            System.out.println("   Данные сохранены корректно: " + dataCorrect);
            
        } catch (IOException e) {
            System.out.println("   Ошибка ввода-вывода: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("   Неожиданная ошибка: " + e.getMessage());
        }
        
        System.out.println("\n=== Тестирование рефлексивного чтения завершено ===");
    }
}

    
