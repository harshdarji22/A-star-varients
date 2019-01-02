package Astar;

import java.util.*;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class Astar20 extends Application {
    
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
    
    static Cell [][] grid = new Cell[20][20]; 
    static Boolean [][] path = new Boolean[20][20];
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
        
        
        for(int i=0;i<20;++i){
      	   for(int j=0;j<20;++j){
      		   path[i][j]=false;
      		   
      	   }
         }
        
        
        //initial grid
        /*
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
    	*/
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
                
           }else{
        	   System.out.println("No possible path");
        	   p=1;
           }
           
           System.out.println();
    }
    
    // javafx method for displaying grid
    
    @Override
    public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		GridPane root = new GridPane();
	    final int size = 20;
	   
	   
	    for (int i = 0; i < size; ++i) {
	    	//System.out.println("working before");
            for (int j = 0; j < size; ++j) {
                Rectangle square = new Rectangle();
                Color color;
                if (i==startI && j==startJ) color = Color.GREEN;
                else if (i==endI && j==endJ) color = Color.RED;
                else if (grid[i][j]==null) color = Color.BLACK;
                else color = Color.WHITE;
                square.setFill(color);
                root.add(square, j, i);
                square.setWidth(20);
                square.setHeight(20);
                square.setStrokeWidth(1);
                square.setStroke(Color.BLACK);
                //square.widthProperty().bind(root.widthProperty().divide(size));
                //square.heightProperty().bind(root.heightProperty().divide(size));
            }
        }
	    
	    Line l = new Line();
	    l.setStartX(450);
	    l.setStartY(0);
	    l.setEndX(450);
	    l.setEndY(18);
	    l.setStroke(Color.WHITE);
	    l.setStrokeWidth(2);
	    root.add(l,22,1);
	    //GridPane root2 = new GridPane();
	    
	    for (int i = 0; i < size; ++i) {
	    	//System.out.println("working after");
            for (int j = 0; j < size; ++j) {
                Rectangle square2 = new Rectangle();
                Color color;
                if (i==startI && j==startJ) color = Color.GREEN;
                else if (i==endI && j==endJ) color = Color.RED;
                else if (path[i][j]) color= Color.YELLOW;
                else if (grid[i][j]==null) color = Color.BLACK;
                else color = Color.WHITE;
                square2.setFill(color);
                root.add(square2, j+24, i);
                square2.setWidth(20);
                square2.setHeight(20);
                square2.setStrokeWidth(1);
                square2.setStroke(Color.BLACK);
                //square2.widthProperty().bind(root.widthProperty().divide(size));
                //square2.heightProperty().bind(root.heightProperty().divide(size));
            }
        }
	    
	    
	    primaryStage.setScene(new Scene(root,880,440));
        //primaryStage.setScene(new Scene(root2,500,500));
	    primaryStage.show();
	}
	
    
    
     
    public static void main(String[] args) throws Exception{   
    	long beforeUsedMem0=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
    	initgrid(0,0,19,19);
    	long afterUsedMem0=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long actualMemUsed0=afterUsedMem0-beforeUsedMem0;
        System.out.println(actualMemUsed0);
    	
    	//AStar();
        //backtrack(0,0,19,19);
        //launch(args);
        
    	
    	
    }
   
	
}