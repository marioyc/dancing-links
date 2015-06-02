import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class EMCSolver{
	private MatrixNode root;
	private List<MatrixNode> columnHeads;

	private int columns;
	private int primaryColumns;

	public List< List<Integer> > solutions;
	private ArrayList<Integer> solution;

	EMCSolver(MatrixNode root, int primaryColumns) throws ValidationException{
		this.root = root;
		columnHeads = new ArrayList<MatrixNode>();
		this.primaryColumns = primaryColumns;
		solutions = new ArrayList< List<Integer> >();
		solution = new ArrayList<Integer>();

		MatrixNode cur = root.right;
		columns = 0;

		while(cur != root){
			columnHeads.add(cur);
			cur = cur.right;
			++columns;
		}

		if(primaryColumns < 0 || primaryColumns > columns)
			throw new ValidationException("Number of primaryColumns is invalid");
	}

	private void cover(MatrixNode c){
        assert(c.r == -1 && c.c >= 0);
        c.right.left = c.left;
        c.left.right = c.right;

        MatrixNode i = c.down;

        while(i != c){
            MatrixNode j = i.right;

            while(j != i){
                j.down.up = j.up;
                j.up.down = j.down;

                j = j.right;
            }

            i = i.down;
        }
    }

    private void uncover(MatrixNode c){
        assert(c.r == -1 && c.c >= 0);
        MatrixNode i = c.up;

        while(i != c){
            MatrixNode j = i.left;

            while(j != i){
                j.down.up = j;
                j.up.down = j;

                j = j.left;
            }

            i = i.up;
        }

        c.right.left = c;
        c.left.right = c;
    }

    private void search(){
        if(root.right == root){
        	List<Integer> temp = new ArrayList<Integer>();

            for(int row : solution)
            	temp.add(row);
            
            Collections.sort(temp);
            solutions.add(temp);
        }else{
	        MatrixNode c = root.right;

	        cover(c);

	        if(c.c >= primaryColumns){
	        	search();
	        }

	        MatrixNode i = c.down;

	        while(i != c){
	            solution.add(i.r);
	            
	            MatrixNode j = i.right;

	            while(j != i){
	                cover(columnHeads.get(j.c));
	                j = j.right;
	            }

	            search();

	            j = i.left;

	            while(j != i){
	                uncover(columnHeads.get(j.c));
	                j = j.left;
	            }

	            solution.remove(solution.size() - 1);
	            i = i.down;
	        }

	        uncover(c);
    	}
    }

    public List< List<Integer> > solve(){
    	solutions.clear();

    	search();

    	return solutions;
    }
}