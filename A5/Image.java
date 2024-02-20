import java.util.Deque;

public class Image{

	protected Pixel[][] pixels;
	protected int rows;
	protected int cols;

	public Image(int rows, int cols){
		this.pixels = new Pixel[rows][cols];
		this.rows = rows;
		this.cols = cols;
	}

	public Pixel getPixel(int row, int col){
		return this.pixels[row][col];
	}

	public void setPixel(int row, int col, Pixel pixel){
		this.pixels[row][col] = pixel;
	}


  /** Clear all pixels in the image; setting all settable state to 'zero' */
  public void clearImage(){
		for(int row=0; row<this.rows; row+=1){
			for(int col=0; col<this.cols; col+=1){
				getPixel(row,col).clear();
			}
		}

	}

	/** Clear all extra information from the image */
	public void clearExtra(){
		for(int row=0; row<this.rows; row+=1){
			for(int col=0; col<this.cols; col+=1){
				getPixel(row,col).setExtra(null);
			}
		}

	}


	/** Creates a random image with a specified number of rows and columns. Each
	 *  pixel in the image will have ink with a given probability. 
	 * 
	 * @param rows is the number of rows in the output image
	 * @param cols it the number of columns in the output image
	 * @param prob is the probability, a number between 0 and 1, that any
	 *         pixel other then the one at (0,0) will have ink. 
	 * @return the random image 
	 */
	public static Image makeRandomImage(int rows, int cols, double prob){
		Image img = new Image(rows, cols);
		for(int row=0; row<rows; row+=1){
			for(int col=0; col<cols; col+=1){
				img.pixels[row][col] = new Pixel(row, col, Math.random()<prob);
			}
		}
		img.pixels[0][0].setInk(true);
		return img;
	}

	public static int[][] ti = {
		{0,0}, {0,1}, {0,2}, {0,3}, {0,4},
		{1,2},                                             {1,11}, {1,12}, {1,13}, {1,14},   {1,18},
		{2,2},                                             {2,11},                           {2,18},
		{3,2},  {3,4}, {3,5}, {3,6}, {3,7}, {3,8},         {3,11},           {3,16}, {3,17}, {3,18}, {3,19}, {3,20},
		{4,2},  {4,4}, {4,5}, {4,6}, {4,7}, {4,8}, {4,9},  {4,11}, {4,12}, {4,13}, {4,14},   {4,18},
		{5,2},  {5,4}, {5,5},               {5,8}, {5,9},                          {5,14},   {5,18},
		        {6,4}, {6,5}, {6,6}, {6,7}, {6,8}, {6,9},                          {6,14},   {6,18},
		        {7,4}, {7,5}, {7,6}, {7,7}, {7,8},         {7,11}, {7,12}, {7,13} ,{7,14},   {7,18},  {7,20},
		        {8,4}, {8,5}, 
		        {9,4}, {9,5 }, {9,6 }, {9,7 }, {9,8 }, {9,9 }, {9 ,10}, { 9,11},   {9,14}, {9,15}, {9,16}, {9,17},
		        {10,4},{10,5}, {10,6}, {10,7}, {10,8}, {10,9}, {10,10}, {10,11},   {10,14}, {10,15}, {10,16}, {10,17},
			 	  {11,4},{11,5}, {11,6}, {11,7}, {11,8}, {11,9}, {11,10}, {11,11},   {11,14}, {11,15}, {11,16}, {11,17},	
	  	                                                                           {12,14}, {12,15}, {12,16}, {12,17}
	};
	public static Image makeTestImage(){
		int rows=14, cols=21;
		Image img = new Image(rows, cols);
		for(int row=0; row<rows; row+=1){
			for(int col=0; col<cols; col+=1){
				img.pixels[row][col] = new Pixel(row, col, false);
			}
		}
		for(int[] rc : ti){
			img.pixels[rc[0]][rc[1]] = new Pixel(rc[0],rc[1], true);
		}
		return img;
	}


	public static String INK = "#"  ;     // "\u2588";
	public static String NO_INK = " ";    //


	@Override
	public String toString(){
		String ans = "+";
		for(int c=0; c<cols; c+=1){
			ans += "-";
		}
		ans += "+\n";
		for(int r=0; r<rows; r+=1){
			ans += "|";
			for(int c=0; c<cols; c+=1){
					ans += getPixel(r,c).hasInk()?INK:NO_INK;
			}
			ans += "|\n";
		}
		ans += "+";
		for(int c=0; c<cols; c+=1){
			ans += "-";
		}
		ans += "+";

		return ans;
	}

	/** An method to visualize the image and the
	 *  path that you explore for a given blob
	 * 
	 *  
	 * 
	 * @param path is the pixels visited while exploring a blob in the image
	 * @return a string that visualized the path of exploration in the image
	 */
	public String show_walk(Deque<Pixel> path){
		for(int i=0; i<path.size(); i+=1){
			Pixel p = path.removeFirst();
			getPixel(p.getRow(),p.getCol()).setExtra(i%10); // Integer
			path.addLast(p);
		}
		String ans = "+";
		for(int c=0; c<cols; c+=1){
			ans += "-";
		}
		ans += "+\n";
		for(int r=0; r<rows; r+=1){
			ans += "|";
			for(int c=0; c<cols; c+=1){
					if( getPixel(r,c).hasInk() ){
						if(getPixel(r,c).getExtra() == null){
							ans += INK;
						}else{
							ans += "" + getPixel(r,c).getExtra().toString();
						}
					}else{ 
						ans += NO_INK;
					}
			}
			ans += "|\n";
		}
		ans += "+";
		for(int c=0; c<cols; c+=1){
			ans += "-";
		}
		ans += "+";

		return ans;

	}


}
