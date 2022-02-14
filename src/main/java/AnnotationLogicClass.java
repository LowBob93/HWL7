import Annotation.AfterSuite;
import Annotation.BeforeSuite;
import Annotation.Test;



import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class AnnotationLogicClass {
    private static Class aClass;
    private static Object TClass;

    public static void start(String className) throws ClassNotFoundException {
        aClass = Class.forName(className);
        initTestClass();
    }

    public static void start(Class clazz) {
        aClass = clazz;
        initTestClass();
    }

    private static void initTestClass() {
        if (aClass.getAnnotation(Test.class) == null) {
            throw new RuntimeException("no @Test annotation found");
        }
        try {
            TClass = aClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    private static void runMethod(Method method) throws IllegalAccessException, InvocationTargetException {
        int modifiers = method.getModifiers();
        if (Modifier.isPrivate(modifiers)) {
            method.setAccessible(true);
            method.invoke(TClass);
            method.setAccessible(false);
        } else {
            method.invoke(TClass);
        }
    }
    public static void tests() throws InvocationTargetException, IllegalAccessException {
        Method[] methods = aClass.getDeclaredMethods();
        Method before = null;
        Method after = null;
        List<Method> testMethods = new ArrayList<>();
        for (Method i : methods) {
            if (i.getAnnotation(BeforeSuite.class) != null) {
                if (before == null) {
                    before = i;
                } else {
                    throw new RuntimeException("More then one @BeforeSuite annotation in the class");
                }
            } else if (i.getAnnotation(AfterSuite.class) != null) {
                if (after == null) {
                    after = i;
                } else {
                    throw new RuntimeException("More then one @AfterSuite annotation in the class");
                }
            } else if (i.getAnnotation(Test.class) != null) {
                testMethods.add(i);
            }
        }
        testMethods.sort((a, b) -> {
            int priorityA = Math.min(a.getAnnotation(Test.class).checkTestPriority(), 10);
            int priorityB = Math.min(b.getAnnotation(Test.class).checkTestPriority(), 10);
            return priorityA - priorityB;
        });

        if (before != null) {
            runMethod(before);
        }
        if (!testMethods.isEmpty()) {
            for (Method test : testMethods) {
                runMethod(test);
            }
        }
        if (after != null) {
            runMethod(after);
        }
    }

}
