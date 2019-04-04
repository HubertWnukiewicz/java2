import knapsack.Result;

public class TempClass {
    Result result;
    String data;

    public TempClass(){
        result=new Result();
        data=createDataSize(300000);
    }
    public Result getResult() {
        return result;
    }


    public void setResult(Result result) {
        this.result = result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String createDataSize(int msgSize){
        msgSize=msgSize/2;
        msgSize=msgSize*1024;
        StringBuilder sb=new StringBuilder(msgSize);
        for(int i=0;i<msgSize;i++){
            sb.append('x');
        }
        return sb.toString();
    }
}
