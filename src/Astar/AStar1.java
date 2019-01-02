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
import java.util.*;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class AStar1 extends Application {
    
	static class Cell implements Serializable{  
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
    static Boolean [][] path = new Boolean[101][101];
    static Boolean [][] pathback = new Boolean[101][101];
    static Boolean [][] pathadap = new Boolean[101][101];
    static PriorityQueue<Cell> open;
     
    static boolean closed[][];
    static int startI =0, startJ = 0;
    static int endI=100, endJ=100;
    static int q=0;
            
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
   
        return grid;
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
    
    static void checkAndUpdateCost2(Cell current, Cell t, int cost){
    	int h1 = grid[startI][startJ].heuristicCost;
        if(t == null || closed[t.i][t.j])return;//t blocked or already visited
        int t_final_cost = h1-cost;
        
        boolean inOpen = open.contains(t);
        if(!inOpen || t_final_cost<t.finalCost){
            t.finalCost = t_final_cost;
            t.parent = current;
            if(!inOpen)open.add(t);
        }
    }
    
    public static void AStar(){ 
    	for(int i=0;i<101;++i){
       	   for(int j=0;j<101;++j){
       		   path[i][j]=false; 
       	   }
          }
    	
    	closed = new boolean[101][101];
        open = new PriorityQueue<>((Object o1, Object o2) -> {
             Cell c1 = (Cell)o1;
             Cell c2 = (Cell)o2;

             return c1.finalCost<c2.finalCost?-1:
                     c1.finalCost>c2.finalCost?1:0;
         });
        
        
        
        open.add(grid[startI][startJ]);// add start to open
        
        Cell current;
        
        while(true){ 
        	int r = open.size();
            if(r>q) q=r;	
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
    
    public static void AStarBack(){ 
    	
    	for(int i=0;i<101;++i){
       	   for(int j=0;j<101;++j){
       		   pathback[i][j]=false; 
       		   closed[i][j]=false;
       	   }
          }
    	open.clear();
    	for(int i=0;i<101;++i){
        	   for(int j=0;j<101;++j){
        		   if (grid[i][j]!=null){
        		   grid[i][j].finalCost=0;
        		   grid[i][j].parent=null;
        		   }
        	   }
           }
	   
        open.add(grid[endI][endJ]);// add start to open
        
        Cell current;
        
        while(true){ 
            current = open.poll();//current element in the queue
            if(current==null)break;// queue empty
            closed[current.i][current.j]=true; 

            if(current.equals(grid[startI][startJ])){
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

    
    public static void adapAStar(){ 
    	
    	for(int i=0;i<101;++i){
       	   for(int j=0;j<101;++j){
       		   pathadap[i][j]=false; 
       		   closed[i][j]=false;
       	   }
          }
    	open.clear();
    	for(int i=0;i<101;++i){
        	   for(int j=0;j<101;++j){
        		   if (grid[i][j]!=null){
        		   grid[i][j].finalCost=0;
        		   grid[i][j].parent=null;
        		   }
        	   }
           }
	   
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
        	    //System.out.println("Path: ");
                Cell current = grid[endI][endJ];
                while(current.parent!=null){
                	path[current.i][current.j]=true;
                	current=current.parent;
                }
                /*
                for(int i=0;i<101;++i){
                    for(int j=0;j<101;++j){
                       if(i==si&&j==sj)System.out.print("S   "); //source cell
                       else if(i==ei && j==ej)System.out.print("D   ");  //destination cell
                       else if(path[i][j])System.out.printf("P   ");//cell in path
                       else if(grid[i][j]!=null)System.out.printf("%-3d ", 0);//unblocked cell
                       else System.out.print("1   "); //blocked cell
                    }
                    System.out.println();
                } 
                */
           }else{
        	   System.out.println("No possible path");
        	   
           }
           
           System.out.println();
    }
    
    public static void backtrack2( int si, int sj, int ei, int ej){
        
        //backtracking to get path
        if(closed[startI][startJ]){
     	    //System.out.println("Path: ");
             Cell current = grid[startI][startJ];
             while(current.parent!=null){
             	pathback[current.i][current.j]=true;
             	current=current.parent;
             }
             /*
             for(int i=0;i<101;++i){
                 for(int j=0;j<101;++j){
                    if(i==si&&j==sj)System.out.print("S   "); //source cell
                    else if(i==ei && j==ej)System.out.print("D   ");  //destination cell
                    else if(path[i][j])System.out.printf("P   ");//cell in path
                    else if(grid[i][j]!=null)System.out.printf("%-3d ", 0);//unblocked cell
                    else System.out.print("1   "); //blocked cell
                 }
                 System.out.println();
             } 
             */
        }else{
     	   System.out.println("No possible path");
     	   
        }
        
        System.out.println();
 }
    
public static void backtrack3( int si, int sj, int ei, int ej){
        
        //backtracking to get path
        if(closed[endI][endJ]){
     	    //System.out.println("Path: ");
             Cell current = grid[endI][endJ];
             while(current.parent!=null){
             	pathadap[current.i][current.j]=true;
             	current=current.parent;
             }
             /*
             for(int i=0;i<101;++i){
                 for(int j=0;j<101;++j){
                    if(i==si&&j==sj)System.out.print("S   "); //source cell
                    else if(i==ei && j==ej)System.out.print("D   ");  //destination cell
                    else if(path[i][j])System.out.printf("P   ");//cell in path
                    else if(grid[i][j]!=null)System.out.printf("%-3d ", 0);//unblocked cell
                    else System.out.print("1   "); //blocked cell
                 }
                 System.out.println();
             } 
             */
        }else{
     	   System.out.println("No possible path");
     	   
        }
        
        System.out.println();
 }
    
    public void storeObject(Cell[][] c,String filename){
        
        OutputStream ops = null;
        ObjectOutputStream objOps = null;
        try {
            ops = new FileOutputStream(filename);
            objOps = new ObjectOutputStream(ops);
            objOps.writeObject(c);
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
    
    public void loadObjects(String filename){
        
        InputStream fileIs = null;
        ObjectInputStream objIs = null;
        try {
            fileIs = new FileInputStream(filename);
            objIs = new ObjectInputStream(fileIs);
            Cell[][] grid1 = (Cell[][]) objIs.readObject();
            for (int i=0;i<101;++i){
            	for (int j=0;j<101;++j){
            	grid[i][j] = grid1[i][j];
            	}
            }
                        
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
    
    
    // javafx method for displaying grid
          
    @Override
    public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		GridPane root = new GridPane();
	    final int size = 101;
	   
	   
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
                square.setWidth(5);
                square.setHeight(5);
                square.setStrokeWidth(0.2);
                square.setStroke(Color.BLACK);
                //square.widthProperty().bind(root.widthProperty().divide(size));
                //square.heightProperty().bind(root.heightProperty().divide(size));
            }
        }
	    
	    Line l = new Line();
	    l.setStartX(450);
	    l.setStartY(0);
	    l.setEndX(450);
	    l.setEndY(1);
	    l.setStroke(Color.WHITE);
	    l.setStrokeWidth(5);
	    root.add(l,103,1);
	    //GridPane root2 = new GridPane();
	    
	    for (int i = 0; i < size; ++i) {
	    	//System.out.println("working after");
            for (int j = 0; j < size; ++j) {
                Rectangle square2 = new Rectangle();
                Color color;
                if (i==startI && j==startJ) color = Color.GREEN;
                else if (i==endI && j==endJ) color = Color.RED;
                else if (path[i][j]) color= Color.YELLOW;
                //else if (pathback[i][j]) color= Color.AQUA;
                else if (grid[i][j]==null) color = Color.BLACK;
                else color = Color.WHITE;
                square2.setFill(color);
                root.add(square2, j+105, i);
                square2.setWidth(5);
                square2.setHeight(5);
                square2.setStrokeWidth(0.2);
                square2.setStroke(Color.BLACK);
                //square2.widthProperty().bind(root.widthProperty().divide(size));
                //square2.heightProperty().bind(root.heightProperty().divide(size));
            }
        }
	    
	    
	    primaryStage.setScene(new Scene(root,1500,1500));
	    primaryStage.show();
	}
	
       
     
    public static void main(String[] args) throws Exception{   
    	
    	int v=0,p1=0;
    	
    	AStar1 m = new AStar1();
        
    	//Generating grid worlds and storing it.
    	/*
    	String[] names = {"g1.txt","g2.txt","g3.txt","g4.txt","g5.txt","g6.txt","g7.txt","g8.txt","g9.txt","g10.txt","g11.txt","g12.txt","g13.txt","g14.txt","g15.txt","g16.txt","g17.txt","g18.txt","g19.txt","g20.txt","g21.txt","g22.txt","g23.txt","g24.txt","g25.txt","g26.txt","g27.txt","g28.txt","g29.txt","g30.txt","g31.txt","g32.txt","g33.txt","g34.txt","g35.txt","g36.txt","g37.txt","g38.txt","g39.txt","g40.txt","g41.txt","g42.txt","g43.txt","g44.txt","g45.txt","g46.txt","g47.txt","g48.txt","g49.txt","g50.txt"}; 
          
        for (int i=0;i<50;++i){
        Cell[][] x = initgrid(0,0,100,100);
        m.storeObject(x,names[i]);
        }
        */
    	
    	//loading gridworld
    	m.loadObjects("g1.txt");
    	    	
    	
    	// Forward A*
    	AStar();
    	backtrack(0,0,100,100);
        for(int i=0;i<100;++i){
        	for (int j=0;j<100;++j){
        		if(closed[i][j]) v=v+1;
        		if(path[i][j]) p1=p1+1;
        	}
        }
        
        System.out.printf("No of cells Visited in forward A* : ");
        System.out.println(v);
        
        System.out.printf("No of cells in path in forward A* : ");
        System.out.println(p1);
        launch(args);
        v=0;p1=0;
        
        // Backward A*
        /*
        AStarBack();
       
        backtrack2(0,0,100,100);
        for(int i=0;i<100;++i){
        	for (int j=0;j<100;++j){
        		if(closed[i][j]) v=v+1;
        		if(pathback[i][j]) p1=p1+1;
        		
        	}
        }
        System.out.printf("No of cells Visited in Reverse A* : ");
        System.out.println(v);
        System.out.printf("No of cells in path in forward A* : ");
        System.out.println(p1);
        */
    	
    	//adaptive A*
        /*
    	adapAStar();
    	backtrack3(0,0,100,100);
    	
    	v=0;p1=0;
    	
    	for(int i=0;i<100;++i){
        	for (int j=0;j<100;++j){
        		if(closed[i][j]) v=v+1;
        		if(pathadap[i][j]) p1=p1+1;
        		
        	}
        }
        System.out.printf("No of cells Visited in Reverse A* : ");
        System.out.println(v);
        System.out.printf("No of cells in path in forward A* : ");
        System.out.println(p1);
        
        */
    }
    }	
