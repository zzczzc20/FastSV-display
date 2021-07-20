import java.util.*;
public class SV {
    public static void main(String args[]){
        System.out.println("This is the display program for FastSV");
    }
}

class SVKernel {
    private int TotOperations;
    private int OperationsInParallel;
    private ArrayList<Integer> FatherVector;
    private ArrayList<Integer> FatherVectorBuffer;
    private ArrayList<Integer> NextFatherVector;
    private ArrayList<Integer> GrandFatherVector;
    private ArrayList<Integer> EdgeStartPoint;
    private ArrayList<Integer> EdgeEndPoint;
    private boolean compareTwoArrayList(ArrayList<Integer> a, ArrayList<Integer> b) {
        boolean ret = true;
        if(a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++){
            if(a.get(i) != b.get(i)) return false;
        }
        return ret;
    }
    private void memcpy(){
        for(int i = 0; i < FatherVector.size(); i++){
            FatherVectorBuffer.set(i, FatherVector.get(i));
        }
    }
    private void updateFatherVector(){
        for (int i = 0; i < FatherVector.size(); i++){
            FatherVector.set(i, NextFatherVector.get(i));
        }
        setGrandFatherVector();
    }
    private void setGrandFatherVector(){
        for(int i = 0; i < GrandFatherVector.size(); i++){
            GrandFatherVector.set(i, FatherVector.get(FatherVector.get(i)));
        }
    }
    private void shortCutting(){
        TotOperations += GrandFatherVector.size();
        OperationsInParallel += 1;
        for(int i = 0; i < GrandFatherVector.size(); i++){
            NextFatherVector.set(i, GrandFatherVector.get(i));
        }     
    }
    private void fnextValidAssign(Integer fnextStatus, Integer toBeAssign){
        if(fnextStatus > toBeAssign){
            NewFatherVector.set(fnextStatus, toBeAssign);
        }
    }
    private void OriginslTreeHooking(){
        for(int i = 0; i < EdgeStartPoint.size(); i++){
            if(FatherVector.get(EdgeStartPoint.get(i)) == GrandFatherVector.get(EdgeStartPoint.get(i))){
                fnextValidAssign(FatherVector.get(EdgeStartPoint.get(i)), FatherVector.get(EdgeEndPoint.get(i)));
            }
        }
        updateFatherVector();
    }
    private void StochasticHookingAndAggressiveHooking(){
        for(int i = 0; i < EdgeStartPoint.size(); i++){
            fnextValidAssign(FatherVector.get(EdgeStartPoint.get(i)), GrandFatherVector.get(EdgeEndPoint.get(i)));
        }
        for(int i = 0; i < EdgeStartPoint.size(); i++){
            fnextValidAssign(EdgeStartPoint.get(i), GrandFatherVector.get(EdgeEndPoint.get(i)));
        }        
        updateFatherVector();
    }
    SVKernel(int NumOfElements, ArrayList<Integer> Start, ArrayList<Integer> End){
        FatherVector = new ArrayList<Integer>(NumOfElements);
        FatherVectorBuffer = new ArrayList<Integer>(NumOfElements);
        NextFatherVector = new ArrayList<Integer>(NumOfElements);
        GrandFatherVector = new ArrayList<Integer>(NumOfElements);
        TotOperations = 0;
        OperationsInParallel = 0;
        for (int i = 0; i < NumOfElements; i++){
            FatherVector.set(i, i);
            NextFatherVector.set(i, i);
            GrandFatherVector.set(i, i);
        }
        EdgeStartPoint = Start;
        EdgeEndPoint = End;
    }
    public void print(){
        System.out.println("Total operations: " + TotOperations);
        System.out.println("Operations in parallel computing: " + OperationsInParallel);
    }
    public void printVector(){
        System.out.println("FatherVector: ");
        String s = "";
        for(int i = 0; i < FatherVector.size(); i++){
            s += FatherVector.get(i);
            s += " ";
        }
        System.out.println(s);
        System.out.println("GrandFatherVector: "); 
        s = "";
        for(int i = 0; i < GrandFatherVector.size(); i++){
            s += GrandFatherVector.get(i);
            s += " ";
        }
    }
    public void OriginalSV(){
        int i = 0;
        while (true) {
            OriginslTreeHooking();
            shortCutting();
            if(i != 0){
                if(compareTwoArrayList(FatherVector, FatherVectorBuffer)) break;
            }
            memcpy();
            i++;
        }
    }
    public void FastSV(){
        int i = 0;
        while (true) {
            StochasticHookingAndAggressiveHooking();
            shortCutting();
            if(i != 0){
                if(compareTwoArrayList(FatherVector, FatherVectorBuffer)) break;
            }
            memcpy();
            i++;
        }
    }
}
