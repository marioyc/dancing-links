import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main{
    static List<Integer> solution;

    static void cover(MatrixNode c){
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

    static void solveEMC(MatrixNode root){
        if(root.right == null){
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
            solution.add(i.r);
            
            MatrixNode j = i.right;

            while(j != i){
                cover(j);
                j = j.right;
            }

            solveEMC(root);

            j = i.left;

            while(j != i){
                uncover(j);
                j = j.left;
            }

            solution.remove(solution.size() - 1);
        }

        uncover(c);
    }

    public static void main(String args[]) throws ValidationException{
        if(args[0].equals("emc")){
            Scanner in = new Scanner(System.in);

            // Read test case
            int primaryColumns = in.nextInt();
            int secondaryColumns = in.nextInt();
            int rows = in.nextInt();
            int columns = primaryColumns + secondaryColumns;

            System.out.println(primaryColumns + " " + secondaryColumns + " " + rows);
            
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

            solveEMC(root);
        }else if(args[0].equals("pavage")){
            System.out.println("PAVAGE");
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