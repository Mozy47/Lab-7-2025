package functions;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.StringTokenizer;

public class TabulatedFunctions {
    
    // ------------ Лабораторная работа №7 --------------
    
    // ------ Задание №2 ---------
    
    // Приватное статическое поле фабрики
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    // Метод для замены фабрики 
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory){
        factory = newFactory;
    }

    // Три перегруженных метода создания табулированных функций
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.create(leftX, rightX, pointsCount);
    }
    
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.create(leftX, rightX, values);
    }
    
    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.create(points);
    }

    // --------- Задание №3: Методы с рефлексией ----------

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, int pointsCount){
        // Проверяем, что класс реализует TabulatedFunciton
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)){
            throw new IllegalArgumentException("Класс " + functionClass.getName() + " не реализует интерфейс TabulatedFunction");
        }

        try {
            // Ищем конструктор с параметрами (double, double, int)
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, int.class);
            
            // Создаем объект с помощью рефлексии
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);
        
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор (double, double, int) в классе " + 
                                             functionClass.getName(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта " + functionClass.getName(), e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, double[] values) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() + 
                                             " не реализует интерфейс TabulatedFunction");
        }
        
        try {
            // Ищем конструктор с параметрами (double, double, double[])
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, double[].class);
            
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор (double, double, double[]) в классе " + 
                                             functionClass.getName(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта " + functionClass.getName(), e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, FunctionPoint[] points) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() + 
                                             " не реализует интерфейс TabulatedFunction");
        }
        
        try {
            // Ищем конструктор с параметрами (FunctionPoint[])
            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);
            
            return (TabulatedFunction) constructor.newInstance((Object) points);
            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор (FunctionPoint[]) в классе " + 
                                             functionClass.getName(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта " + functionClass.getName(), e);
        }
    }

    // Метод создания объекта с помощью рефлексии
    public static TabulatedFunction tabulate(Class<?> functionClass, Function function, 
                                            double leftX, double rightX, int pointsCount) {
        // Проверка, что отрезок табулирования находится в области определения функции
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException(
                "Отрезок табулирования [" + leftX + ", " + rightX + "] " +
                "выходит за область определения функции [" + 
                function.getLeftDomainBorder() + ", " + function.getRightDomainBorder() + "]");
        }
        
        // Проверка на область определения
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница (" + leftX + ") должна быть меньше правой (" + rightX + ")");   
        }
        // Проверка на кол-во точек
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Кол-во точек должно быть не меньше двух");
        }

        // Создаем массив значений функции
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        // Заполняем массив значениями
        for (int i = 0; i < pointsCount; ++i) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        // Используем рефлексивное создание
        return createTabulatedFunction(functionClass, leftX, rightX, values);
    }



    // Приватный конструктор
    private TabulatedFunctions(){}

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        // Проверка, что отрезок табулирования находится в области определения функции
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()){
            throw new IllegalArgumentException(
                "Отрезок табулирования [" + leftX + ", " + rightX + "] " +
                "выходит за область определения функции [" + 
                function.getLeftDomainBorder() + ", " + function.getRightDomainBorder() + "]");
        }
        
        // Проверка на область определения
        if (leftX >= rightX){
            throw new IllegalArgumentException("Левая граница (" + leftX + ") должна быть меньше правой (" + rightX + ")");   
        }
        // Проверка на кол-во точек
        if (pointsCount < 2){
            throw new IllegalArgumentException("Кол-во точек должно быть не меньше двух");
        }

        // Создаем массив значений функции
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        // Заполняем массив значениями
        for (int i = 0; i < pointsCount; ++i){
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }

        // Используем фабрику вместо прямого создания
        return createTabulatedFunction(leftX, rightX, values);
    }

    // ------------------- ЗАДАНИЕ 7 --------------------

    // Вывод табулированной функции в байтовый поток
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);
        
        // Записываем кол-во точек
        dataOut.writeInt(function.getPointsCount());

        // Записываем координаты точек (x,y)
        for (int i = 0; i < function.getPointsCount(); ++i){
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }

        // Гарантируем, что все данные записаны в поток, оставляя поток открытым
        dataOut.flush();
    }

    // Ввод табулированной функции из байтового потока
    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);
        // Читаем кол-во точек
        int pointsCount = dataIn.readInt();

        // Читаем координты точек
        double[] xValues = new double[pointsCount];
        double[] yValues = new double[pointsCount];

        for (int i = 0; i < pointsCount; ++i){
            xValues[i] = dataIn.readDouble();
            yValues[i] = dataIn.readDouble();
        }

        // Используем фабрику вместо прямого создания
        return createTabulatedFunction(xValues, yValues);
    }

    // Запись табулированной функции в символьный поток
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) throws IOException{
        PrintWriter writer = new PrintWriter(out);

        // Записываем кол-во точек
        writer.print(function.getPointsCount());
        writer.print(" ");

        // Записываем координаты точек через пробел
        for (int i = 0; i < function.getPointsCount(); ++i){
            writer.print(function.getPointX(i));
            writer.print(" ");
            writer.print(function.getPointY(i));
            if (i < function.getPointsCount() - 1) { // Чтобы исключить лишний пробел
                writer.print(" ");
            }
        }
        
        // Flush, но не закрываем поток
        writer.flush();
    }

    // Чтение табулированной функции из символьного потока
    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(in);

        // Читаем кол-во точек
        tokenizer.nextToken();
        int pointsCount = (int) tokenizer.nval;

        // Читаем координаты точек
        double[] xValues = new double[pointsCount];
        double[] yValues = new double[pointsCount];
        
        for (int i = 0; i < pointsCount; i++) {
            tokenizer.nextToken(); // x координата
            xValues[i] = tokenizer.nval;
            
            tokenizer.nextToken(); // y координата  
            yValues[i] = tokenizer.nval;
        }
        
        // Используем фабрику вместо прямого создания
        return createTabulatedFunction(xValues, yValues);
    }

    // Вспомогательный метод для создания из массивов x и y (вынужденная мера в связи предыдущей реализацией кода)
    private static TabulatedFunction createTabulatedFunction(double[] xValues, double[] yValues) {
        // Проверяем, что массивы одинаковой длины
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException("Массивы x и y должны быть одинаковой длины");
        }
        if (xValues.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не меньше 2");
        }
        
        // Проверяем упорядоченность x
        for (int i = 1; i < xValues.length; i++) {
            if (xValues[i] <= xValues[i - 1]) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию x");
            }
        }
        
        // Создаем массив точек и используем существующий метод фабрики
        FunctionPoint[] points = new FunctionPoint[xValues.length];
        for (int i = 0; i < xValues.length; i++) {
            points[i] = new FunctionPoint(xValues[i], yValues[i]);
        }
        return createTabulatedFunction(points);
    }
}
