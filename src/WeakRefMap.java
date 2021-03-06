import knapsack.Instance;
import knapsack.Item;
import knapsack.Result;

import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.WeakHashMap;

public class WeakRefMap {
    private int maxSize = 0;
    private int found = 0;
    private final int minimum = 1;
    private final int maximum = 200;

    WeakHashMap<Long, TempClass> solutionWeakHashMap;
    ArrayList<Class> solverClasses;


    public WeakRefMap(WeakHashMap<Long, TempClass> resultsHashMap, ArrayList<Class> foundClasses){
        this.solutionWeakHashMap= resultsHashMap;
        this.solverClasses=foundClasses;
    }
    private static long generateRandomIntIntRange(int min, int max) {
        Random r = new Random();
        int selectedValue = r.nextInt((max - min) ) + min;
        return Long.valueOf(selectedValue);
    }

    public void startAlg(){
        long seed = generateRandomIntIntRange(minimum, maximum);
        maxSize++;
        //128 750
        if(!solutionWeakHashMap.containsKey(seed)){

            Instance instance = getRandomInstance(seed);
            long algorithm = generateRandomIntIntRange(0, solverClasses.size() - 1);

            try {

                Class algorithmClass = solverClasses.get((int)algorithm);

                Object solver = algorithmClass.newInstance();

                Method solveMethod = algorithmClass.getMethod("StartAlgorithm", Instance.class);
                Result result = (Result) solveMethod.invoke(solver, instance);

                synchronized (System.out){
                    System.out.println(" Rozwiazanie: Seed " + seed +", klasa: "+ algorithmClass.toString()+ ", wynik: " + result.calculateFinalValue());
                    System.out.flush();
                }
                TempClass tc=new TempClass(10000);
                tc.setResult(result);
                solutionWeakHashMap.put(seed, tc);

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            found++;
            synchronized (System.out)
            {
                System.out.println(" TAKIE ROZWIAZANIE ISTNIEJE:  "+seed);
                System.out.print(" RATIO : " + (float) (found * 1.0 / maxSize));
            }
            }
    }

    private static Instance getRandomInstance(long seed) {

        long numberOfItems= (int)seed %20+2;
        int capacity = (int) seed %30+10;

        ArrayList<Item> items = new ArrayList<>();
        for (int i = 1; i <= numberOfItems; i++) {
            items.add(new Item(i , (float)(2*i+2 ) % seed));
        }
        return new Instance(capacity, items);
    }


}

