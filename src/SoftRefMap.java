import knapsack.Instance;
import knapsack.Item;
import knapsack.Result;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SoftRefMap {
    private int maxSize = 0;
    private int found = 0;
    private final int minimum = 1;
    private final int maximum = 100;

    HashMap<Long, SoftReference<TempClass>> solutionWeakHashMap;
    ArrayList<Class> solverClasses;

    public SoftRefMap(HashMap<Long, SoftReference<TempClass>> resultsHashMap, ArrayList<Class> foundClasses){
        this.solutionWeakHashMap= resultsHashMap;
        this.solverClasses=foundClasses;
    }
    private static Long generateRandomIntIntRange(int min, int max) {
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
                TempClass tempClass=new TempClass();
                tempClass.setResult(result);
                SoftReference<TempClass> tc=new SoftReference<TempClass>(tempClass);
                tc.get().setResult(result);
                solutionWeakHashMap.put(Long.valueOf(seed), tc);

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

            synchronized (System.out) {

                System.out.print(" TAKIE ROZWIAZANIE JUZ ISTNIEJE NR:" + seed);
                System.out.flush();


            }
            SoftReference value=solutionWeakHashMap.get(seed);
            if(value.get()==null)
            {
                found++;
                System.out.print(" RATIO : " + (float) (found * 1.0 / maxSize));
                System.out.flush();
                System.out.println(" ");
                System.out.flush();
                System.out.println("-----ELEMENT JUZ NIE ISTNIEJE-------");
            }

        }
    }

    private static Instance getRandomInstance(long seed) {

        long numberOfItems= seed %30+2;
        int capacity = (int)seed +50;

        ArrayList<Item> items = new ArrayList<>();
        for (int i = 1; i <= numberOfItems; i++) {
            items.add(new Item(i , (float)(2*i+2 ) % seed));
        }
        return new Instance(capacity, items);
    }


}
