public class hardThread extends Thread{
    private HardRefMap problemMap;
    private int id;

    hardThread(HardRefMap problemMap, int id){
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



