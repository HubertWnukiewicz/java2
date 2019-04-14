

public class softThread extends Thread{
    private SoftRefMap problemMap;
    private int id;

    softThread(SoftRefMap problemMap, int id){
        this.id=id;
        this.problemMap = problemMap;
    }

    public void run(){

            while(true){

                try {
                   sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (System.out) {
                    System.out.print("ID= " + this.id);
                    System.out.flush();
                }
                problemMap.startAlg(id);
            }
        }
        public int getID(){
        return this.id;
        }
    }

