public class Pixel{
	protected int     row;     // row of pixel
	protected int     col;     // column of pixel
	protected boolean hasInk;  // true if this pixel has ink 
	protected boolean visited; // has this pixel been visited
  	protected Object  extra;   // place for you to store something (if needed)


	public Pixel(int row, int col){
		this(row, col, false);
	}
	public Pixel(int row, int col, boolean hasInk){
		this.row = row;
		this.col = col;
		this.hasInk = hasInk;
		this.visited = false;
		this.extra = null;
	}

	public int getRow(){ return this.row; }
	public int getCol(){ return this.col; }
	public boolean hasInk(){ return this.hasInk; }
	public boolean visited(){ return this.visited; }
	public Object getExtra(){ return this.extra; }

	public void setRow(int r){this.row = r;}
	public void setCol(int c){this.col = c;}
	public void setInk(boolean ink){this.hasInk = ink;}
  	public void setVisited(boolean v){ this.visited = v;}
	public void setExtra(Object obj){ this.extra = obj; }

  /** Clear all settable state of this pixel; set everything to 'zero' */
  public void clear(){
	   this.hasInk = false;
		this.visited = false;
		this.extra = null;
	}

	/** Check if two pixels are at the same location
	 * 
	 * @param other is another Pixel object
	 * @return
	 */
	public boolean samePlace(Pixel other){
		return row == other.row && col == other.col;
	}

	@Override
	public String toString(){
		return "{" + row + "," + col + "}";
	}
}
