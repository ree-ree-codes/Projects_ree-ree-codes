import java.util.Deque;
import java.util.ArrayDeque;

public abstract class Blobs{

	public Image image;

	public Blobs(){}
	public Blobs(Image image){
		this.image = image;
	}
	public final void setImage(Image image){
		this.image = image;
	}

	/**
	 * Wrapper for the recursive helper method
	 * @param row a row in the image
	 * @param col a column in the image
	 * @return a list of all pixels in the blob that overlaps with the input row and column. 
	 *        (This may be an empty list.) 
	 */
	public final Deque<Pixel> blobRecursive(int starting_row, int starting_col){
		// initialize the list of pixels
		Deque<Pixel> blobSoFar = new ArrayDeque<Pixel>();
		
		// find all the pixels 
		// note: this method does not return anything. instead, it 
		//       changes the state of blobSoFar
		blobRecursiveHelper(starting_row, starting_col, blobSoFar);
      
		// return the list of pixels
		return blobSoFar;
	}


	// this is the recursive method that you need to override 
	public abstract void blobRecursiveHelper(int row, int col, Deque<Pixel> blobSoFar);

	// this is the iterative method that you need to override
	public abstract Deque<Pixel> blobIterative(int start_row, int start_col);


}
