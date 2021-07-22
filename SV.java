import java.util.*;
public class SV {
    public static void main(String args[]){
        System.out.println("This is the display program for FastSV");
        int[] ES = {0, 1, 2, 3, 4, 5, 3, 2, 4, 1};
        int[] EE = {1, 2, 3, 4, 5, 4, 2, 1, 3, 0};
        ArrayList<Integer> ESAL = new ArrayList<Integer>();
        ArrayList<Integer> EEAL = new ArrayList<Integer>();
        for(int i = 0; i < ES.length; i++){
            ESAL.add(ES[i]);
            EEAL.add(EE[i]);
        }
        SVKernel FastSV = new SVKernel(6, ESAL, EEAL);
        SVKernel SlowSV = new SVKernel(6, ESAL, EEAL);
        SVKernel BFSV = new SVKernel(6, ESAL, EEAL);
        FastSV.FastSV(true);
        SlowSV.OriginalSV(true);
        BFSV.BruteForce(true);
        System.out.println("This is FastSV: ");
        FastSV.printVector();
        FastSV.print();
        System.out.println("This is SlowSV: ");
        SlowSV.printVector();
        SlowSV.print();
        System.out.println("This is BruteForce: ");
        BFSV.printVector(true);
        BFSV.print();
    }
}

class SVKernel {
    private int TotOperations;
    private int OperationsInParallel;
    private ArrayList<Integer> FatherVector;
    private ArrayList<Integer> FatherVectorBuffer;
    private ArrayList<Integer> NextFatherVector;
    private ArrayList<Integer> GrandFatherVector;
    private ArrayList<Integer> BruteForceVector;
    private ArrayList<Integer> BruteForceVectorBuffer;
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
    private void memcpy(boolean choice){
        if(choice){
            for(int i = 0; i < FatherVector.size(); i++){
                BruteForceVectorBuffer.set(i, BruteForceVector.get(i));
            }
        }
    }
    private void updateFatherVector(){
        TotOperations += 2 * FatherVector.size();
        OperationsInParallel += 2;
        for (int i = 0; i < FatherVector.size(); i++){
            FatherVector.set(i, NextFatherVector.get(i));
        }
        setGrandFatherVector();
    }
    private void setGrandFatherVector(){
        TotOperations += 3 * GrandFatherVector.size();
        OperationsInParallel += 3;
        for(int i = 0; i < GrandFatherVector.size(); i++){
            GrandFatherVector.set(i, FatherVector.get(FatherVector.get(i)));
        }
    }
    private void shortCutting(){
        TotOperations += 2 * GrandFatherVector.size();
        OperationsInParallel += 2;
        for(int i = 0; i < GrandFatherVector.size(); i++){
            NextFatherVector.set(i, GrandFatherVector.get(i));
        }     
    }
    private void fnextValidAssign(Integer fnextStatus, Integer toBeAssign){
        if(fnextStatus > toBeAssign){
            NextFatherVector.set(fnextStatus, toBeAssign);
        }
    }
    private void OriginslTreeHooking(){
        OperationsInParallel += (8 + 1 + 2); // 8: get operation 1: compare operation 2: assign operation
        TotOperations += EdgeStartPoint.size() * (8 + 1 + 2);
        for(int i = 0; i < EdgeStartPoint.size(); i++){
            if(FatherVector.get(EdgeStartPoint.get(i)) == GrandFatherVector.get(EdgeStartPoint.get(i))){
                fnextValidAssign(FatherVector.get(EdgeStartPoint.get(i)), FatherVector.get(EdgeEndPoint.get(i)));
            }
        }
        updateFatherVector();
    }
    private void StochasticHookingAndAggressiveHooking(){
        OperationsInParallel += (7 + 2 + 2); // 7: get operations, 2 * 2: 2 fnextValidAssign
        TotOperations += EdgeStartPoint.size() * (7 + 2 + 2);
        for(int i = 0; i < EdgeStartPoint.size(); i++){
            fnextValidAssign(FatherVector.get(EdgeStartPoint.get(i)), GrandFatherVector.get(EdgeEndPoint.get(i)));
        }
        for(int i = 0; i < EdgeStartPoint.size(); i++){
            fnextValidAssign(EdgeStartPoint.get(i), GrandFatherVector.get(EdgeEndPoint.get(i)));
        }        
        updateFatherVector();
    }
    SVKernel(int NumOfElements, ArrayList<Integer> Start, ArrayList<Integer> End){
        FatherVector = new ArrayList<Integer>();
        FatherVectorBuffer = new ArrayList<Integer>();
        NextFatherVector = new ArrayList<Integer>();
        GrandFatherVector = new ArrayList<Integer>();
        BruteForceVector = new ArrayList<Integer>();
        BruteForceVectorBuffer = new ArrayList<Integer>();
        TotOperations = 0;
        OperationsInParallel = 0;
        for (int i = 0; i < NumOfElements; i++){
            FatherVector.add(i);
            NextFatherVector.add(i);
            GrandFatherVector.add(i);
            FatherVectorBuffer.add(i);
            BruteForceVector.add(i);
            BruteForceVectorBuffer.add(i);
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
        System.out.println(s);
    }
    public void printVector(boolean choice){
        if(choice){
            System.out.println("BruteForceVector: ");
            String s = "";
            for(int i = 0; i < FatherVector.size(); i++){
                s += BruteForceVector.get(i);
                s += " ";
            }
            System.out.println(s);
        }
    }
    public void OriginalSV(boolean print){
        int i = 0;
        while (true) {
            OriginslTreeHooking();
            shortCutting();
            if(i != 0){
                if(compareTwoArrayList(FatherVector, FatherVectorBuffer)) break;
            }
            memcpy();
            i++;
            if(print) {
                System.out.println("Round number: " + i);
                printVector();
            }
        }
    }
    public void FastSV(boolean print){
        int i = 0;
        while (true) {
            StochasticHookingAndAggressiveHooking();
            shortCutting();
            if(i != 0){
                if(compareTwoArrayList(FatherVector, FatherVectorBuffer)) break;
            }
            memcpy();
            i++;
            if(print) {
                System.out.println("Round number: " + i);
                printVector();
            }
        }
    }
    public void BruteForce(boolean print){
        int i = 0;
        while (true) {
            for (int iter = 0; iter < EdgeStartPoint.size(); iter++){
                TotOperations += 3; // 2 get + 1 compare
                OperationsInParallel += 3;
                if(EdgeStartPoint.get(iter) == EdgeEndPoint.get(iter)){
                    continue;
                }
                else{
                    Integer small = min(EdgeStartPoint.get(iter), EdgeEndPoint.get(iter));
                    Integer big = max(EdgeStartPoint.get(iter), EdgeEndPoint.get(iter));
                    replace(EdgeStartPoint, big, small);
                    replace(EdgeEndPoint, big, small);
                    replace(BruteForceVector, big, small);
                }
                if(print) {
                    System.out.println("Round number: " + (i * EdgeStartPoint.size() + iter + 1));
                    printVector(true);
                }
            }
            if(i != 0){
                if(compareTwoArrayList(BruteForceVector, BruteForceVectorBuffer)) break;
            }
            memcpy(true);
            i++;
        }
    }
    private Integer min(Integer a, Integer b){
        return (a < b) ? a : b;
    }
    private Integer max(Integer a, Integer b){
        return (a > b) ? a : b;
    }
    private void replace(ArrayList<Integer> collection, Integer tobereplaced, Integer value){
        TotOperations += collection.size() * 3;
        OperationsInParallel += 3;
        for(int i = 0; i < collection.size(); i++){
            if(collection.get(i) == tobereplaced){
                collection.set(i, value);
            }
        }
    }
}
