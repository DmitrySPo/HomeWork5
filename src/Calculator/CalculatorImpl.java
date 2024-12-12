package Calculator;

public class CalculatorImpl implements Calculator {
    @Override
    public int calc(int arg) {
        System.out.println("Вызван calc:" + arg);
        try {
            System.out.println("Производятся супер сложные вычисления");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return arg * 5;
    }

}
