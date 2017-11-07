/*************************************************************************
 *  author: Li Dai 05/14/2017
 *  purpose : Hush, Hush Kleine Hexe memory board game 
 *************************************************************************/
// this is used to allow us to use classes in all the libraries of java.io
import java.io.*;
import java.util.Random;

public class hushgame {

    public static void main(String[] args) {
      InputStreamReader input = new InputStreamReader(System.in);
      BufferedReader reader = new BufferedReader(input);   
      Random rand = new Random();
  
        final int MAX_ROWS = 5;
        final int MAX_COLS = 5;

        int[][] myArray = new int[MAX_ROWS][MAX_COLS];
        Boolean[][] isCovered= new Boolean[MAX_ROWS][MAX_COLS];
        
        //input number of players
        boolean flag=true;
        int numPlayer=0;
        numPlayer=intInputer(2, 4, "please enter the number of players(2,3 or 4)");
        
        //initialize board
        FillMyArray(myArray,isCovered);
        PrintArray(myArray,isCovered);
        int maxRound=1000;
        int currentRound=0;
        int dice;
        boolean isWinner=false;
        while(!isWinner){
          //no winner, continue playing
            currentRound=currentRound+1;
            int playerID;
            for(playerID=1;playerID<numPlayer+1;playerID++){
              if(!isWinner){
                System.out.println("player "+playerID+"'s turn");
              }
              boolean roll=true;
              while(roll & (!isWinner)){
                 System.out.println("type anything or nothing and press enter to roll");
                 String letsroll;
                 try{
                   letsroll=reader.readLine();
                     }catch (IOException e){
                        System.out.println("Error reading");
                      }
                 dice=rand.nextInt(6);
                 //dice=1;
                 System.out.println("the outcome of the dice is: " + dice);
                 //shuffle
                 if(dice==0){
                     int numShuffle=0;
                     System.out.println("you diced shuffle");
                     numShuffle=intInputer(1, 5, "please enter the number of shuffles(1-5) you want");
                      for(int j=1;j<numShuffle+1;j++){
                        System.out.println("round of shuffler: "+ j + " out of " +numShuffle+" shuffle(s)");
                        int firstCol=0, secondCol=0;
                         firstCol=intInputer(1, 5, "input first column of the shuffler(1-5)")-1;
                         secondCol=firstCol;
                         while(secondCol==firstCol)
                         {
                              secondCol=intInputer(1, 5, "input second column of the shuffler(1-5)")-1;
                              if(secondCol==firstCol){
                                System.out.println("invalid value, please enter a colomn different from the first column");
                              }
                         }
                         Shuffler(myArray,firstCol,secondCol);
                      }
                      System.out.println("done shuffle, next player");
                      roll=false;
                 }else{
                    int inputCol=0;
                    inputCol=intInputer(1, 5, "please indicate which column has color "+ GetCharEquivalent(dice)+", please enter 1-5")-1;
                    
                    //guessed right
                    if(CheckHat(myArray,GetCharEquivalent(dice),inputCol)){
                      System.out.println("WOW, your answer is correct. "+ GetCharEquivalent(dice)+" move one block forward and you can roll again.");
                       //move one block
                      int myRow;
                      myRow=FindRow(myArray,inputCol);
                      // System.out.println("myrow="+ myRow);
                      myArray[myRow][inputCol]=0;
                      isCovered[myRow][inputCol]=false;
                      myArray[myRow-1][inputCol]=dice;
                      isCovered[myRow-1][inputCol]=true;
                      PrintArray(myArray,isCovered);
                      //check if there is a winner
                      isWinner=IsThereWinner(myArray);
                      if(isWinner){
                        System.out.println("Congrats, player "+playerID+" won!!!");
                      }
                    }else{
                       System.out.println("Sorry, "+GetCharEquivalent(dice)+" is not in column "+ (inputCol+1)+".");
                       String wrongColor;
                       wrongColor=GetCharEquivalent(myArray[FindRow(myArray,inputCol)][inputCol]);
                       System.out.println("the color in column "+(inputCol+1)+" is "+ wrongColor +".");
                       System.out.println("next player");
                       roll=false;
                    }
                 }
              }
            }
        }
    }
    
    public static int intInputer(int min, int max, String msg){
      InputStreamReader input = new InputStreamReader(System.in);
      BufferedReader reader = new BufferedReader(input);   
      int output=-1;
      while(output<min | output>max){
        System.out.println(msg);
        
        try{
            output = Integer.parseInt(reader.readLine());
        }catch (Throwable t){
          System.out.println("Error reading, please enter an interger");
        }
      }
      return output;
    }
    
    public static void Shuffler(int[][] myArray, int fcol,int scol){
       int frow,srow;
       frow=FindRow(myArray, fcol);
       srow=FindRow(myArray, scol);
       int temp;
       temp=myArray[frow][fcol];
       myArray[frow][fcol]=myArray[srow][scol];
       myArray[srow][scol]=temp;
    }
    public static int FindRow(int[][] myArray, int col){
      int i;
      for (i = 0; i<myArray.length ; i++ ) { // array.length = max rows
       if(myArray[i][col]>0){ 
         return i;
       }
      }
      return i;
    }
    
    
    public static boolean CheckHat(int[][] myArray, String color, int column){
      boolean flag=false;
      int i=0;
      while(i<myArray[0].length && !flag) {
        if(GetCharEquivalent(myArray[i][column]).equals(color)){ 
         flag=true;
        }
        i++;
      }
      return flag;
    }
    
    public static boolean IsThereWinner(int[][] myArray){
      boolean iswinner=false;
      int j = 0;
      while (j <myArray[0].length && !iswinner) {
         if(myArray[0][j] > 0){
           iswinner=true;
         }
         else j++;
      }
      return iswinner;
    }
     ///initialization of the grid
    public static void FillMyArray(int[][] myArray, Boolean[][] isCovered){
      int i, j;
     for (i = 0; i<myArray.length ; i++ ) { // array.length = max rows
       for(j = 0; j <myArray[0].length; j++) { // array[0].length = max cols
         myArray[i][j] = 0;
         isCovered[i][j]=false;
       }
     }
     
     myArray[myArray.length-1][0]=1;
     isCovered[myArray.length-1][0]=true;
     myArray[myArray.length-1][1]=2;
     isCovered[myArray.length-1][1]=true;
     myArray[myArray.length-1][2]=3;
     isCovered[myArray.length-1][2]=true;
     myArray[myArray.length-1][3]=4;
     isCovered[myArray.length-1][3]=true;
     myArray[myArray.length-1][4]=5;
     isCovered[myArray.length-1][4]=true;
     
    }
    
 
    public static void PrintArray(int[][] myArray,Boolean[][] isCovered) {
      int i, j;
      int[][] myPrintArry;
      for (i = 0; i<myArray.length ; i++ ) { // array.length = max rows
        System.out.print((i+1)+" ");
        for(j = 0; j <myArray[0].length; j++) { // array[0].length = max cols
          if(isCovered[i][j]){
            System.out.print( GetCharEquivalentCovered(myArray[i][j]) + " ");
          } else {
            System.out.print( GetCharEquivalent(myArray[i][j]) + " ");
          }
        }
        System.out.println();
      }
      System.out.print("  ");
      for(i=0;i<myArray.length;i++)
      {
        System.out.print((i+1)+" ");
      }
      System.out.println();
    }
    
    public static String GetCharEquivalent(int a){
      
      if (a==0)
        return "*";
      else if (a==1)
        return "G";        
      else if (a==2)
        return "R";   
      else if (a==3)
        return "B";         
      else if (a==4)
        return "O";   
      else if (a==5)
        return "Y"; 
      else
        return " ";

    }
    
    public static String GetCharEquivalentCovered(int a){
      if(a==0)
        return "*";   
      else  
        return "X";
      
    }
    
}


