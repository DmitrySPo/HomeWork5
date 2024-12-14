import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils {

    public static void assign(Object to, Object from) {
        // Получаем классы объектов
        Class<?> toClass = to.getClass();
        Class<?> fromClass = from.getClass();

        // Получаем все методы объекта 'from'
        Method[] fromMethods = fromClass.getMethods();

        try {
            // Проходим по всем методам объекта 'from'
            for (Method fromMethod : fromMethods) {
                // Проверяем, является ли текущий метод геттером
                if (isGetter(fromMethod)) {
                    String propertyName = getPropertyNameFromGetter(fromMethod);

                    // Находим соответствующий сеттер в объекте 'to'
                    Method toSetter = findCorrespondingSetter(toClass, propertyName, fromMethod.getReturnType());

                    // Если нашли сеттер, выполняем копирование свойства
                    if (toSetter != null) {
                        // Вызываем геттер у объекта 'from', чтобы получить значение свойства
                        Object value = fromMethod.invoke(from);

                        // Вызываем сеттер у объекта 'to', передавая полученное значение
                        toSetter.invoke(to, value);
                    }
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Ошибка при копировании свойств", e);
        }
    }

    private static boolean isGetter(Method method) {
        if ((method.getParameterCount() == 0)
                && ((method.getName().startsWith("get") && !void.class.equals(method.getReturnType()))
                || (method.getName().startsWith("is") && boolean.class.equals(method.getReturnType())))) {
            return true;
        }
        return false;
    }

    private static String getPropertyNameFromGetter(Method getter) {
        String name = getter.getName();
        if (name.startsWith("get")) {
            name = name.substring(3);
        } else if (name.startsWith("is")) {
            name = name.substring(2);
        }
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    private static Method findCorrespondingSetter(Class<?> clazz, String propertyName, Class<?> type) {
        String setterName = "set" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);

        try {
            // Пытаемся найти сеттер с точным типом
            Method setter = clazz.getMethod(setterName, type);

            // Проверка совместимости типов
            if (setter.getParameterTypes()[0].isAssignableFrom(type)) {
                return setter;
            }
        } catch (NoSuchMethodException ignored) {}

        // Если точного совпадения нет, проверяем, есть ли сеттер с супертипом
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                Class<?> paramType = method.getParameterTypes()[0];

                // Проверка совместимости типов
                if (paramType.isAssignableFrom(type)) {
                    return method;
                }
            }
        }

        return null; // Не удалось найти подходящий сеттер
    }
}