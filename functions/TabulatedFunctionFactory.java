package functions;

public interface TabulatedFunctionFactory {

    // Три перегруженных метода, соответствующих конструкторам
    TabulatedFunction create(double leftX, double rightX, int pointsCount);
    TabulatedFunction create(double leftX, double rightX, double[] values);
    TabulatedFunction create(FunctionPoint[] points);
}

