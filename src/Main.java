import knapsack.Instance;
import knapsack.Result;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    private static Map<Long, Result> solutionWeakHashMap = new HashMap<>();
    private static ArrayList<Class> solverClasses = new ArrayList<>();

    public static void main(String[] args) throws ClassNotFoundException {

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("cknapsack").getFile());
        System.out.println("Path: "+file.getPath());
        String packageName = "knapsack";

        ArrayList<Class> loadedClasses = null;

            loadedClasses = findClasses(file, packageName);
            System.out.println("Classloader task finished.");

            System.out.println("Loaded classes: \n{}"+ //
                    loadedClasses.stream().map(c -> c.toString()) //
                            .collect(Collectors.joining("\n")));



        for (Class c : loadedClasses) {
            try {
                c.getMethod("StartAlgorithm", Instance.class);
                solverClasses.add(c);
            } catch (NoSuchMethodException e) {
                System.out.println("Method 'StartAlgorithm' not found in class {}."+ c.getName());
            }
        }

        System.out.println("Finished loading solver classes:");
        System.out.println("\n{}"+ solverClasses.stream().map(c -> c.toString()).collect(Collectors.joining("\n")));

        RefMap problemMap = new RefMap(solutionWeakHashMap, solverClasses);

        myThread  t1 = new myThread (problemMap,1);
        myThread  t2 = new myThread (problemMap,2);
        myThread  t3 = new myThread (problemMap,3);
        myThread  t4 = new myThread (problemMap,4);
        myThread  t5 = new myThread (problemMap,5);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
            t5.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        ArrayList<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            System.out.println("Current directory: "+directory.getName());
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            System.out.println(file.getName());
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
