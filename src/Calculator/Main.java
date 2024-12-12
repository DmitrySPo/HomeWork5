package Calculator;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws Exception {
        Class<CalculatorImpl> calculatorClass = CalculatorImpl.class;

        // Задание 2: Получаем все методы класса и его суперклассов
        Method[] methods = calculatorClass.getMethods();

        for (Method method : methods) {
            System.out.println(method.getName());
        }
    }


}