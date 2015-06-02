import java.util.Scanner;
import java.util.List;

public class Main{
    static boolean checkTurn0(int r, int c, String p[], int id[][], MatrixNode last[], int coverRows){
        int R = p.length,C = p[0].length();

        if(!(r >= 0 && r + R <= id.length && c >= 0 && c + C <= id[0].length))
            return false;

        for(int i = 0;i < R;++i)
            for(int j = 0;j < C;++j)
                if(p[i].charAt(j) == '*' && id[r + i][c + j] == -1)
                    return false;
        
        MatrixNode first = null,lastInRow = null;

        for(int i = 0;i < R;++i)
            for(int j = 0;j < C;++j)
                if(p[i].charAt(j) == '*'){
                    MatrixNode cur = new MatrixNode(coverRows,id[i][j]);

                    if(lastInRow != null){
                        assert(lastInRow.c < cur.c);
                        lastInRow.right = cur;
                        cur.left = lastInRow;
                    }else{
                        first = cur;
                    }

                    lastInRow = cur;
                }

        lastInRow.right = first;
        first.left = lastInRow;

        return true;
    }

    static boolean checkTurn1(int r, int c, String p[], int id[][], MatrixNode last[], int coverRows){
        int R = p.length,C = p[0].length();
        if(!(r >= 0 && r + C <= id.length && c >= 0 && c + R <= id[0].length))
            return false;

        for(int i = 0;i < R;++i)
            for(int j = 0;j < C;++j)
                if(p[i].charAt(j) == '*' && id[r + j][c + (R - 1 - i)] == -1)
                    return false;

        return true;
    }
    static boolean checkTurn2(int r, int c, String p[], int id[][], MatrixNode last[], int coverRows){
        int R = p.length,C = p[0].length();
        if(!(r >= 0 && r + R <= id.length && c >= 0 && c + C <= id[0].length))
            return false;

        for(int i = 0;i < R;++i)
            for(int j = 0;j < C;++j)
                if(p[i].charAt(j) == '*' && id[r + (R - 1 - i)][c + (C - 1 - j)] == -1)
                    return false;

        return true;
    }
    static boolean checkTurn3(int r, int c, String p[], int id[][], MatrixNode last[], int coverRows){
        int R = p.length,C = p[0].length();
        if(!(r + C <= id.length && c >= 0 && c + R <= id[0].length))
            return false;

        for(int i = 0;i < R;++i)
            for(int j = 0;j < C;++j)
                if(p[i].charAt(j) == '*' && id[r + (C - 1 - j)][c + i] == -1)
                    return false;

        return true;
    }

    public static void main(String args[]) throws ValidationException{
        Scanner in = new Scanner(System.in);

        if(args.length > 0 && args[0].equals("emc")){
            // Read test case
            int primaryColumns = in.nextInt();
            int secondaryColumns = in.nextInt();
            int rows = in.nextInt();
            int columns = primaryColumns + secondaryColumns;
            
            String M[] = new String[rows];
            M[0] = in.nextLine();

            for(int i = 0;i < rows;++i){
                M[i] = in.nextLine();
                //System.out.println(M[i]);
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
                }else if(j < primaryColumns){
                    throw new ValidationException("Column " + j + " can not be covered");
                }
            }

            /*for(int j = 0;j < columns;++j){
                System.out.println("column " + j);
                MatrixNode cur = columnHeads[j].down;

                while(cur != columnHeads[j]){
                    System.out.println(cur.r + " " + cur.c);
                    cur = cur.down;
                }
            }*/

            EMCSolver solver = new EMCSolver(root, primaryColumns);
            List< List<Integer> > solutions = solver.solve();

            if(solutions.size() == 0) System.out.println("NO SOLUTION");
            else{
                for(List<Integer> l : solutions){
                    for(int x : l){
                        System.out.print(x + 1 + " ");
                    }

                    System.out.println();
                }
            }
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
                for(int r = 0;r < rows;++r){
                    for(int c = 0;c < columns;++c){
                        if(checkTurn0(r,c,pieceM[p],id,last,coverRows)){
                            ++coverRows;
                        }

                        if(checkTurn1(r,c,pieceM[p],id,last,coverRows)){
                            ++coverRows;
                        }

                        if(checkTurn2(r,c,pieceM[p],id,last,coverRows)){
                            ++coverRows;
                        }

                        if(checkTurn3(r,c,pieceM[p],id,last,coverRows)){
                            ++coverRows;
                        }
                    }
                }
            }

            System.out.println("coverColumns = " + coverColumns + ", coverRows = " + coverRows);
        }else{
            System.out.println("Invalid command");
        }
    }
}