package Astar;
 
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.PriorityQueue;

import Astar.AStar1.Cell;
 
public class store {
 
	static class Cell implements Serializable{  
        int heuristicCost = 0; //h
        int finalCost = 0; //f=g+h
        int i, j;
        Cell parent = null; 
        
        Cell(int i, int j){
            this.i = i;
            this.j = j; 
        }
    }
    public void storeObject(Cell[][] x,String filename){
         
        OutputStream ops = null;
        ObjectOutputStream objOps = null;
        try {
            ops = new FileOutputStream(filename);
            objOps = new ObjectOutputStream(ops);
            objOps.writeObject(x);
            objOps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(objOps != null) objOps.close();
            } catch (Exception ex){
                 
            }
        }
         
    }
    
    static Cell [][] grid = new Cell[101][101]; 
    static Boolean [][] path = new Boolean[101][101];
    static Boolean [][] pathback = new Boolean[101][101];
    static PriorityQueue<Cell> open;
     
    static boolean closed[][];
    static int startI, startJ;
    static int endI, endJ;
    static int p;
            
    public static void setBlocked(int i, int j){
        grid[i][j] = null;
    }
    
    public static void setStartCell(int i, int j){
        startI = i;
        startJ = j;
    }
    
    public static void setEndCell(int i, int j){
        endI = i;
        endJ = j; 
    } 
    
    public static Cell[][] initgrid(int si, int sj, int ei, int ej){
    	grid = new Cell[101][101];
        
        //set start position
        setStartCell(si, sj);  
        
        //set End Location
        setEndCell(ei, ej); 
        
        //calculate h
        for(int i=0;i<101;++i){
           for(int j=0;j<101;++j){
               grid[i][j] = new Cell(i, j);
               grid[i][j].heuristicCost = Math.abs(i-endI)+Math.abs(j-endJ);
           }
        }
        grid[si][sj].finalCost = 0;
        
        
        // Set blocked cells with 30% probability of being blocked
        for(int i=0;i<101;++i){
     	   for(int j=0;j<101;++j){
     		   if((i==si && j==sj) || (i==ei && j==ej)){
     			   continue;
     		   }
     		   double r= Math.random();
     		   if(r<0.3){
     			   setBlocked(i,j);
     		   }
     	   }
        }
        
        
        for(int i=0;i<101;++i){
      	   for(int j=0;j<101;++j){
      		   path[i][j]=false; 
      	   }
         }
        
        
        //initial grid
        /*
        System.out.println("Grid: ");
         for(int i=0;i<101;++i){
             for(int j=0;j<101;++j){
                if(i==si&&j==sj)System.out.print("S   "); //source cell
                else if(i==ei && j==ej)System.out.print("D   ");  //destination cell
                else if(grid[i][j]!=null)System.out.printf("%-3d ", 0);// unblocked cell
                else System.out.print("1   "); //blocked cell
             }
             System.out.println();
         } 
         System.out.println();
    	*/
        return grid;
    }
    
    
     
    public void displayObjects(String filename){
         
        InputStream fileIs = null;
        ObjectInputStream objIs = null;
        try {
            fileIs = new FileInputStream(filename);
            objIs = new ObjectInputStream(fileIs);
            Cell[][] grid = (Cell[][]) objIs.readObject();
            //System.out.println(grid);
            
            
            System.out.println("Grid: ");
            for(int i=0;i<101;++i){
                for(int j=0;j<101;++j){
                   if(grid[i][j]!=null)System.out.printf("%-3d ", 0);// unblocked cell
                   else System.out.print("1   "); //blocked cell
                }
                System.out.println();
            } 
            System.out.println();
            
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(objIs != null) objIs.close();
            } catch (Exception ex){
                 
            }
        }
         
    }
     
    public static void main(String a[]){
        
        store m = new store();
        String[] names = {"g1.txt","g2.txt","g3.txt","g4.txt","g5.txt","g6.txt","g7.txt","g8.txt","g9.txt","g10.txt","g11.txt","g12.txt","g13.txt","g14.txt","g15.txt","g16.txt","g17.txt","g18.txt","g19.txt","g20.txt","g21.txt","g22.txt","g23.txt","g24.txt","g25.txt","g26.txt","g27.txt","g28.txt","g29.txt","g30.txt","g31.txt","g32.txt","g33.txt","g34.txt","g35.txt","g36.txt","g37.txt","g38.txt","g39.txt","g40.txt","g41.txt","g42.txt","g43.txt","g44.txt","g45.txt","g46.txt","g47.txt","g48.txt","g49.txt","g50.txt"}; 
       
        for (int i=0;i<50;++i){
        Cell[][] x = initgrid(0,0,100,100);
        m.storeObject(x,names[i]);
        }
        
    }
}