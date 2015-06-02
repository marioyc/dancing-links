import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class EMCSolver{
	private MatrixNode root;
	private List<MatrixNode> columnHeads;

	private int columns;
	private int primaryColumns;
	private int maxK;

	public List< List<Integer> > solutions;
	private ArrayList<Integer> solution;

	EMCSolver(MatrixNode root, int primaryColumns, int maxK) throws ValidationException{
		this.root = root;
		columnHeads = new ArrayList<MatrixNode>();

		this.primaryColumns = primaryColumns;
		this.maxK = maxK;

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

		for(int i = 0;i < columns;++i){
			int cont = 0;
			cur = columnHeads.get(i).down;

			while(cur != columnHeads.get(i)){
				cur = cur.down;
				++cont;
			}

			columnHeads.get(i).s = cont;
		}
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

    private void search(int k){
        if(root.right == root){
        	List<Integer> temp = new ArrayList<Integer>();

            for(int row : solution)
            	temp.add(row);
            
            Collections.sort(temp);
            solutions.add(temp);
        }else{
	        MatrixNode c = root.right,cur = c.right,h;

	        if(k < maxK){
		        int best = c.s;

		        while(cur != root){
		        	if(cur.s < best){
		        		best = cur.s;
		        		c = cur;
		        	}

		        	cur = cur.right;
		        }
	    	}

	        cover(c);

	        if(c.c >= primaryColumns){
	        	search(k + 1);
	        }

	        MatrixNode i = c.down;

	        while(i != c){
	            solution.add(i.r);
	            
	            MatrixNode j = i.right;

	            while(j != i){
	            	h = j.head;
	                cover(h);
	                j = j.right;
	                --h.s;
	            }

	            search(k + 1);

	            j = i.left;

	            while(j != i){
	            	h = j.head;
	                uncover(h);
	                j = j.left;
	                ++h.s;
	            }

	            solution.remove(solution.size() - 1);
	            i = i.down;
	        }

	        uncover(c);
    	}
    }

    public List< List<Integer> > solve(){
    	solutions.clear();

    	search(0);

    	return solutions;
    }
}