package Astar;

import java.util.*;

public class AStar {
    
    static class Cell{  
        int heuristicCost = 0; //h
        int finalCost = 0; //f=g+h
        int i, j;
        Cell parent; 
        
        Cell(int i, int j){
            this.i = i;
            this.j = j; 
        }
    }
    
    static Cell [][] grid = new Cell[101][101]; 
    
    static PriorityQueue<Cell> open;
     
    static boolean closed[][];
    static int startI, startJ;
    static int endI, endJ;
    static boolean path[][];
            
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
    
    public static void initgrid(int si, int sj, int ei, int ej){
    	grid = new Cell[20][20];
        
        //set start position
        setStartCell(si, sj);  
        
        //set End Location
        setEndCell(ei, ej); 
        
        //calculate h
        for(int i=0;i<20;++i){
           for(int j=0;j<20;++j){
               grid[i][j] = new Cell(i, j);
               grid[i][j].heuristicCost = Math.abs(i-endI)+Math.abs(j-endJ);
           }
        }
        grid[si][sj].finalCost = 0;
        
        
        // Set blocked cells with 30% probability of being blocked
        for(int i=0;i<20;++i){
     	   for(int j=0;j<20;++j){
     		   if((i==si && j==sj) || (i==ei && j==ej)){
     			   continue;
     		   }
     		   double r= Math.random();
     		   if(r<0.3){
     			   setBlocked(i,j);
     		   }
     	   }
        }
        
        //initial grid
        System.out.println("Grid: ");
         for(int i=0;i<20;++i){
             for(int j=0;j<20;++j){
                if(i==si&&j==sj)System.out.print("S   "); //source cell
                else if(i==ei && j==ej)System.out.print("D   ");  //destination cell
                else if(grid[i][j]!=null)System.out.printf("%-3d ", 0);// unblocked cell
                else System.out.print("1   "); //blocked cell
             }
             System.out.println();
         } 
         System.out.println();
    	
    }
    
    static void checkAndUpdateCost(Cell current, Cell t, int cost){
        if(t == null || closed[t.i][t.j])return;//t blocked or already visited
        int t_final_cost = t.heuristicCost+cost;
        
        boolean inOpen = open.contains(t);
        if(!inOpen || t_final_cost<t.finalCost){
            t.finalCost = t_final_cost;
            t.parent = current;
            if(!inOpen)open.add(t);
        }
    }
    
    public static void AStar(){ 
    	
    	closed = new boolean[20][20];
        open = new PriorityQueue<>((Object o1, Object o2) -> {
             Cell c1 = (Cell)o1;
             Cell c2 = (Cell)o2;

             return c1.finalCost<c2.finalCost?-1:
                     c1.finalCost>c2.finalCost?1:0;
         });
        
        open.add(grid[startI][startJ]);// add start to open
        
        Cell current;
        
        while(true){ 
            current = open.poll();//current element in the queue
            if(current==null)break;// queue empty
            closed[current.i][current.j]=true; 

            if(current.equals(grid[endI][endJ])){
                return; 
            } 
            Cell t;  //t is the next state
            if(current.i-1>=0){
                t = grid[current.i-1][current.j];
                checkAndUpdateCost(current, t, current.finalCost+1); 
            } 

            if(current.j-1>=0){
                t = grid[current.i][current.j-1];
                checkAndUpdateCost(current, t, current.finalCost+1); 
            }

            if(current.j+1<grid.length){
                t = grid[current.i][current.j+1];
                checkAndUpdateCost(current, t, current.finalCost+1); 
            }

            if(current.i+1<grid.length){
                t = grid[current.i+1][current.j];
                checkAndUpdateCost(current, t, current.finalCost+1);  
            }
        }
    }
    
    
    
    public static void backtrack( int si, int sj, int ei, int ej){
      
           //backtracking to get path
           path=new boolean[20][20];
           if(closed[endI][endJ]){
        	    System.out.println("Path: ");
                Cell current = grid[endI][endJ];
                while(current.parent!=null){
                	path[current.i][current.j]=true;
                	current=current.parent;
                } 
                for(int i=0;i<20;++i){
                    for(int j=0;j<20;++j){
                       if(i==si&&j==sj)System.out.print("S   "); //source cell
                       else if(i==ei && j==ej)System.out.print("D   ");  //destination cell
                       else if(path[i][j])System.out.printf("P   ");//cell in path
                       else if(grid[i][j]!=null)System.out.printf("%-3d ", 0);//unblocked cell
                       else System.out.print("1   "); //blocked cell
                    }
                    System.out.println();
                } 
           }else System.out.println("No possible path");
           
           System.out.println();
    }
     
    public static void main(String[] args) throws Exception{   
    	initgrid(0,0,19,19);
    	AStar();
        backtrack(0,0,19,19); 
    }
}