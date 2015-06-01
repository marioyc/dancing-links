public class MatrixNode{
	int r,c;
	MatrixNode left,right,up,down;

	MatrixNode(int r, int c){
		this.r = r;
		this.c = c;

		left = null;
		right = null;
		up = null;
		down = null;
	}

	public String toString(){
		return "(" + r + "," + c + ")";
	}
}