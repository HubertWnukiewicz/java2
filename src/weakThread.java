public class weakThread extends Thread{
    private WeakRefMap problemMap;
    private int id;

    weakThread(WeakRefMap problemMap, int id){
        this.id=id;
        this.problemMap = problemMap;
    }

    public void run(){

        while(true){

            try {
                sleep(350);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (System.out) {
                System.out.print("ID= " + this.id);
                System.out.flush();
            }
            problemMap.startAlg();
        }
    }
    public int getID(){
        return this.id;
    }
}


