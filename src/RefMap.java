import knapsack.Instance;
import knapsack.Item;
import knapsack.Result;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RefMap {

    private final int minRange = 1;
    private final int maxRange = 500;
    int totalHits = 0;
    int missedHits = 0;
    HashMap<Long, Result> solutionWeakHashMap;
    ArrayList<Class> solverClasses;

    public RefMap(Map<Long, Result> solutionWeakHashMap, List<Class> solverClasses){

        this.solutionWeakHashMap=(HashMap<Long,Result>) solutionWeakHashMap;
        this.solverClasses=(ArrayList<Class>) solverClasses;
    }

    public synchronized void solve(){
        long seed = generateRandomIntIntRange(minRange, maxRange);
        totalHits++;

        if(!solutionWeakHashMap.containsKey(seed)){

            Instance instance = getRandomInstance(seed);
            long algorithm = generateRandomIntIntRange(0, solverClasses.size() - 1);

            try {

                Class algorithmClass = solverClasses.get((int)algorithm);

                Object solver = algorithmClass.newInstance();

                Method solveMethod = algorithmClass.getMethod("StartAlgorithm", Instance.class);
                Result solution = (Result) solveMethod.invoke(solver, instance);

                System.out.println(" ==> Problem solved: Seed " + seed +", class: "+ algorithmClass.toString()+ ", solution: " + solution.calculateFinalValue());
                System.out.flush();
                solutionWeakHashMap.put(seed, solution);

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
            missedHits++;
            System.out.println("There already was solution with seed " + seed);
            System.out.flush();
            System.out.println("Miss ratio : " + missedHits * 1.0 / totalHits );
            System.out.flush();
            System.out.println("Total hits : " + totalHits);
            System.out.flush();
        }
    }


    private static Instance getRandomInstance(long seed) {

        long problemSize = seed / 2;
        int capacity = (int) seed * 2 + 5;

        ArrayList<Item> items = new ArrayList<>();
        for (int i = 1; i <= problemSize; i++) {
            items.add(new Item(i + 5, (float)(i * 1.5 + seed) % seed));
        }
        return new Instance(capacity, items);
    }

    private static long generateRandomIntIntRange(int min, int max) {

        Random r = new Random();
        int selectedValue = r.nextInt((max - min) ) + min;
        System.out.println(selectedValue);
        return Long.valueOf(selectedValue);
    }
}
