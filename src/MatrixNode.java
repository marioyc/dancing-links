public class MatrixNode{
	int r,c,s;
	MatrixNode left,right,up,down,head;

	MatrixNode(int r, int c){
		this.r = r;
		this.c = c;
		s = 0;

		left = null;
		right = null;
		up = null;
		down = null;
		head = this;
	}

	public String toString(){
		return "(" + r + "," + c + ")";
	}
}