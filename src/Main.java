import knapsack.Instance;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public class Main {

    private static HashMap<Long, SoftReference<TempClass>> solutionSoftHashMap=new HashMap<>();
    private static WeakHashMap<Long, TempClass> solutionWeakHashMap = new WeakHashMap<>();
    private static HashMap<Long,TempClass> solutionHardHashMap=new HashMap<>();
    private static ArrayList<Class> solverClasses = new ArrayList<>();

    public static void main(String[] args) throws ClassNotFoundException, InterruptedException {

        ClassLoader classLoader = Main.class.getClassLoader();
        File file = new File(classLoader.getResource("cknapsack").getFile());
        System.out.println("Path: "+file.getPath());
        String packageName = "knapsack";

        ArrayList<Class> loadedClasses = null;

            loadedClasses = reloadClasses(file, packageName);
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

        //hardStart();
        //weakStart();
        softStart();

        loadedClasses=null;
        classLoader=null;


    }
    private static void softStart() throws InterruptedException {
        SoftRefMap softRefMap = new SoftRefMap(solutionSoftHashMap, solverClasses);
        softThread thread1=new softThread(softRefMap,1);
        softThread thread2=new softThread(softRefMap,2);
        softThread thread3=new softThread(softRefMap,3);
        softThread thread4=new softThread(softRefMap,4);
        softThread thread5=new softThread(softRefMap,5);
        softThread thread6=new softThread(softRefMap,6);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
        thread6.join();
    }
    private static void weakStart() throws InterruptedException {
        WeakRefMap softRefMap = new WeakRefMap(solutionWeakHashMap, solverClasses);
        weakThread thread1=new weakThread(softRefMap,1);
        weakThread thread2=new weakThread(softRefMap,2);
        weakThread thread3=new weakThread(softRefMap,3);
        weakThread thread4=new weakThread(softRefMap,4);
        weakThread thread5=new weakThread(softRefMap,5);


        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();


        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();

    }
    private static void hardStart() throws InterruptedException {
        HardRefMap softRefMap = new HardRefMap(solutionHardHashMap, solverClasses);
        hardThread thread1=new hardThread(softRefMap,1);
        hardThread thread2=new hardThread(softRefMap,2);
        hardThread thread3=new hardThread(softRefMap,3);
        hardThread thread4=new hardThread(softRefMap,4);
        hardThread thread5=new hardThread(softRefMap,5);
        hardThread thread6=new hardThread(softRefMap,6);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
        thread6.join();
    }

    private static ArrayList<Class> reloadClasses(File directory, String packageName) throws ClassNotFoundException {
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
                classes.addAll(reloadClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
