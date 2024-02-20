import java.util.Deque;
import java.util.ArrayDeque;
import java.util.*;

/* -----------------------------------------
   Note: The ArrayDeque is an implementation 
         of the Deque ADT. That is, it is a 
		   double-ended queue. 

		   You can simulate both a Stack and 
		   a regular Queue with this data structure
		   in the following way:

		 Stack: push  ~ addFirst
		        pop   ~ removeFirst
		
		 Queue: enqueue ~ addLast
		        dequeue ~ removeFirst
  ------------------------------------------ */

public class MyBlobs extends Blobs{


	///////////////////////////////////////////////////////////////////////	
	///////////////////////////////////////////////////////////////////////	
	// do NOT change or remove this constructor. We will use it to create 
	// objects when testing your code. If it is removed, we cannot test 
	// your code!
	///////////////////////////////////////////////////////////////////////	
	public MyBlobs(){}
	///////////////////////////////////////////////////////////////////////	
	///////////////////////////////////////////////////////////////////////	

	@Override
	public Deque<Pixel> blobIterative(int start_row, int start_col){
		Deque<Pixel> bloblist = new ArrayDeque<Pixel>(); //the empty list
		Deque<Pixel> workinglist=new ArrayDeque<Pixel>();//working empty list
		
		Pixel np=image.getPixel(start_row, start_col);
		workinglist.addLast(np);
		
		
		while (workinglist.isEmpty()==false){
				Pixel p;
				p=workinglist.removeFirst();
				if(p.hasInk() && !p.visited()){
					//System.out.println("p: "+p.row+","+p.col);
					p.visited=true;
					bloblist.addLast(p);

					//look at one above and add it
					if(p.row>0){
						Pixel a=image.getPixel(p.row-1, p.col);
						workinglist.addLast(a);
					}
					//look below
					if(p.row<image.rows-1){
						Pixel a=image.getPixel(p.row+1, p.col);
						workinglist.addLast(a);
					}
					//lookleft
					if(p.col>0){
						Pixel a=image.getPixel(p.row, p.col-1);
						workinglist.addLast(a);
					}
					if(p.col<image.cols-1){
						Pixel a=image.getPixel(p.row, p.col+1);
						workinglist.addLast(a);
					}

					//look right
				}	
				
		}
		return bloblist;
	}
	
	@Override
	public void blobRecursiveHelper(int row, int col, Deque<Pixel> blobSoFar) {
		
		if(row < 0 || col < 0){
            return;
        }
        else if((col - 1 < 0 && row - 1 < 0) || row + 1 > 13 || col + 1 > 20){
            return;
        }
        else{
            if(col - 1 >= 0 && row - 1 >= 0 && col + 1 < 21 && row + 1 < 14 && super.image.getPixel(row, col - 1).visited && super.image.getPixel(row, col + 1).visited && super.image.getPixel(row + 1, col).visited && super.image.getPixel(row-1, col).visited){
                return;
            }

            if(super.image.getPixel(row, col).visited == false && super.image.getPixel(row, col).hasInk){
                blobSoFar.addLast(super.image.getPixel(row, col));
                super.image.getPixel(row, col).visited = true;
            }

            //up
            if(super.image.getPixel(row - 1, col).visited  == false){
                if(super.image.getPixel(row - 1, col).hasInk){
                    blobSoFar.addLast(super.image.getPixel(row - 1, col));
                    super.image.getPixel(row - 1, col).visited = true;
                    blobRecursiveHelper(row - 1, col, blobSoFar);
                }
                super.image.getPixel(row - 1, col).visited = true;
        
            } 

			if(super.image.getPixel(row, col + 1).visited  == false){
                if(super.image.getPixel(row, col + 1).hasInk){
                    blobSoFar.addLast(super.image.getPixel(row, col + 1));
                    super.image.getPixel(row, col + 1).visited = true;
                    blobRecursiveHelper(row, col + 1, blobSoFar);
                }
                super.image.getPixel(row, col + 1).visited = true;
            }
            //down
            if(super.image.getPixel(row + 1, col).visited  == false){
                if(super.image.getPixel(row + 1, col).hasInk){
                    blobSoFar.addLast(super.image.getPixel(row + 1, col));
                    super.image.getPixel(row + 1, col).visited = true;
                    blobRecursiveHelper(row + 1, col, blobSoFar);
                }
                super.image.getPixel(row + 1, col).visited = true;
            }
            //left
            if(super.image.getPixel(row, col - 1).visited  == false){
                if(super.image.getPixel(row, col - 1).hasInk){
                    blobSoFar.addLast(super.image.getPixel(row, col - 1));
                    super.image.getPixel(row, col - 1).visited = true;
                    blobRecursiveHelper(row, col - 1, blobSoFar);
                }
                super.image.getPixel(row, col - 1).visited = true;
            }
		}

	}
	
}	