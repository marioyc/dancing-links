import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main{
    static List<Integer> solution;

    static void cover(MatrixNode c){
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

    static void uncover(MatrixNode c){
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

    static void solveEMC(MatrixNode root, MatrixNode columnHeads[]){
        if(root.right == root){
            for(int row : solution){
                System.out.print(row + " ");
            }

            System.out.println();
            return;
        }

        MatrixNode c = root.right;

        cover(c);

        MatrixNode i = c.down;

        while(i != c){
            //System.out.println("k = " + solution.size() + " " + i.r);
            solution.add(i.r);
            
            MatrixNode j = i.right;

            while(j != i){
                cover(columnHeads[j.c]);
                j = j.right;
            }

            solveEMC(root,columnHeads);

            j = i.left;

            while(j != i){
                uncover(columnHeads[j.c]);
                j = j.left;
            }

            solution.remove(solution.size() - 1);
            i = i.down;
        }

        uncover(c);
    }

    public static void main(String args[]) throws ValidationException{
        Scanner in = new Scanner(System.in);

        if(args.length > 0 && args[0].equals("emc")){
            // Read test case
            int primaryColumns = in.nextInt();
            int secondaryColumns = in.nextInt();
            int rows = in.nextInt();
            int columns = primaryColumns + secondaryColumns;

            //System.out.println(primaryColumns + " " + secondaryColumns + " " + rows);
            
            String M[] = new String[rows];
            M[0] = in.nextLine();

            for(int i = 0;i < rows;++i){
                M[i] = in.nextLine();
                System.out.println(M[i]);
            }

            // build linked lists
            MatrixNode root = new MatrixNode(-1,-1);
            MatrixNode columnHeads[] = new MatrixNode[columns];
            MatrixNode Mnodes[][] = new MatrixNode[rows][columns];

            for(int i = 0;i < columns;++i){
                columnHeads[i] = new MatrixNode(-1,i);

                if(i > 0)
                    columnHeads[i].left = columnHeads[i - 1];
                else
                    columnHeads[i].left = root;
            }

            for(int i = columns - 2;i >= 0;--i){
                columnHeads[i].right = columnHeads[i + 1];
            }

            columnHeads[columns - 1].right = root;

            root.right = columnHeads[0];
            root.left = columnHeads[columns - 1];

            for(int i = 0;i < rows;++i){
                MatrixNode first = null,last = null;

                for(int j = 0;j < columns;++j){
                    if(M[i].charAt(j) == '1'){
                        Mnodes[i][j] = new MatrixNode(i,j);

                        if(last != null)
                            last.right = Mnodes[i][j];
                        else
                            first = Mnodes[i][j];

                        Mnodes[i][j].left = last;
                        last = Mnodes[i][j];
                    }
                }

                if(last != null){
                    first.left = last;
                    last.right = first;
                }
            }

            for(int j = 0;j < columns;++j){
                MatrixNode first = null,last = columnHeads[j];

                for(int i = 0;i < rows;++i){
                    if(M[i].charAt(j) == '1'){
                        if(last != null){
                            last.down = Mnodes[i][j];
                            Mnodes[i][j].up = last;
                        }else{
                            columnHeads[j].down = Mnodes[i][j];
                            Mnodes[i][j].up = columnHeads[j];
                        }

                        last = Mnodes[i][j];
                    }
                }

                if(last != null){
                    columnHeads[j].up = last;
                    last.down = columnHeads[j];
                }else{
                    throw new ValidationException("Column " + j + " can not be covered");
                }
            }

            solution = new ArrayList<Integer>();

            /*for(int j = 0;j < columns;++j){
                System.out.println("column " + j);
                MatrixNode cur = columnHeads[j].down;

                while(cur != columnHeads[j]){
                    System.out.println(cur.r + " " + cur.c);
                    cur = cur.down;
                }
            }*/

            solveEMC(root,columnHeads);
        }else if(args.length > 0 && args[0].equals("pavage")){
            int columns = in.nextInt();
            int rows = in.nextInt();

            String M[] = new String[rows];
            M[0] = in.nextLine();

            int id[][] = new int[rows][columns];
            int coverColumns = 0;

            for(int i = 0;i < rows;++i){
                M[i] = in.nextLine();

                for(int j = 0;j < columns;++j){
                    if(M[i].charAt(j) == '*'){
                        id[i][j] = coverColumns;
                        ++coverColumns;
                    }else id[i][j] = -1;
                }
            }

            int pieces = in.nextInt();
            int pieceRows[] = new int[pieces];
            int pieceColumns[] = new int[pieces];
            String pieceM[][] = new String[pieces][];

            for(int i = 0;i < pieces;++i){
                pieceColumns[i] = in.nextInt();
                pieceRows[i] = in.nextInt();
                pieceM[i] = new String[ pieceRows[i] ];

                pieceM[i][0] = in.nextLine();

                for(int j = 0;j < pieceRows[i];++j)
                    pieceM[i][j] = in.nextLine();
            }

            MatrixNode root = new MatrixNode(-1,-1);
            MatrixNode columnHeads[] = new MatrixNode[coverColumns];

            for(int i = 0;i < coverColumns;++i)
                columnHeads[i] = new MatrixNode(-1,i);

            MatrixNode last[] = new MatrixNode[coverColumns];
            int coverRows = 0;

            for(int i = 0;i < coverColumns;++i)
                last[i] = columnHeads[i];

            for(int p = 0;p < pieces;++p){
                if(checkTurn0(pieceM[p],M)){

                }

                if(checkTurn1(pieceM[p],M)){

                }

                if(checkTurn2(pieceM[p],M)){

                }

                if(checkTurn3(pieceM[p],M)){
                    
                }
            }
        }else{
            System.out.println("Invalid command");
        }
    }

    static class ValidationException extends Exception{
        ValidationException(String message){
            super(message);
        }
    }
}