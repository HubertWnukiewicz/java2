public class myThread extends Thread{
    private RefMap problemMap;
    private int id;
    myThread(RefMap problemMap,int id){
        this.id=id;
        this.problemMap = problemMap;
    }

    public void run(){

        synchronized (problemMap){

            while(true){

                try {
                   sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("ID= "+this.id);
                System.out.flush();
                problemMap.solve();
            }
        }
    }
}
