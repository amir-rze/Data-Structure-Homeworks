import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.Scanner;

class Node {
    int minPrime;
    int maxPrime ;
    public Node(){
         minPrime = Integer.MAX_VALUE;
         maxPrime = Integer.MIN_VALUE;
    }
}



class SegmentTree {
    private int[][] array;
    private Node[][] tree;
    private int size, size1, size2;

    public SegmentTree(int[][] array, int n, int m) {
        this.array = array;
        size1 = 2 * (int) Math.pow(2, (int) (Math.ceil(Math.log(n) / Math.log(2))));
        size2 = 2 * (int) Math.pow(2, (int) (Math.ceil(Math.log(m) / Math.log(2))));
        tree = new Node[size1][];
        for (int i = 0; i < size1; i++) {
            tree[i] = new Node[size2];
        }
        build_x(1, 0, n - 1);
    }


    public boolean isPrime(int number) {
        if (number < 2)
            return false;
        for (int i = 2; i <= (int) Math.sqrt((double) number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public void build_y(int rootX, int x1, int x2, int rootY, int y1, int y2) {
        tree[rootX][rootY] = new Node();
        if (y1 == y2) {
            if (x1 == x2) {
                if (isPrime(array[x1][y1])) {
                    tree[rootX][rootY].maxPrime = array[x1][y1];
                    tree[rootX][rootY].minPrime = array[x1][y1];
                } else {
                    tree[rootX][rootY].maxPrime = Integer.MIN_VALUE;
                    tree[rootX][rootY].minPrime = Integer.MAX_VALUE;
                }
            } else {
                tree[rootX][rootY].minPrime = Math.min(tree[rootX * 2][rootY].minPrime
                        , tree[rootX * 2 + 1][rootY].minPrime);
                tree[rootX][rootY].maxPrime = Math.max(tree[rootX * 2][rootY].maxPrime
                        , tree[rootX * 2 + 1][rootY].maxPrime);
            }
        } else {
            int midC = (y1 + y2) / 2;
            build_y(rootX, x1, x2, rootY * 2, y1, midC);
            build_y(rootX, x1, x2, rootY * 2 + 1, midC + 1, y2);
            tree[rootX][rootY].minPrime = Math.min(tree[rootX][rootY * 2].minPrime
                    , tree[rootX][rootY * 2 + 1].minPrime);
            tree[rootX][rootY].maxPrime = Math.max(tree[rootX][rootY * 2].maxPrime
                    , tree[rootX][rootY * 2 + 1].maxPrime);
        }
    }


    public void build_x(int rootX, int x1, int x2) {
        if (x1 != x2) {
            int midR = (x1 + x2) / 2;
            build_x(rootX * 2, x1, midR);
            build_x(rootX * 2 + 1, midR + 1, x2);
        }
        build_y(rootX, x1, x2, 1, 0, array[0].length - 1);
    }

    void update_y(int rootX, int x1, int x2, int rootY, int y1, int y2, int x, int y, int value) {
        if (y1 == y2) {
            if (x1 == x2) {
                if (isPrime(value)) {
                    tree[rootX][rootY].maxPrime = value;
                    tree[rootX][rootY].minPrime = value;
                } else {
                    tree[rootX][rootY].maxPrime = Integer.MIN_VALUE;
                    tree[rootX][rootY].minPrime = Integer.MAX_VALUE;
                }
            } else {
                tree[rootX][rootY].minPrime = Math.min(tree[rootX * 2][rootY].minPrime
                        , tree[rootX * 2 + 1][rootY].minPrime);
                tree[rootX][rootY].maxPrime = Math.max(tree[rootX * 2][rootY].maxPrime
                        , tree[rootX * 2 + 1][rootY].maxPrime);
            }
        } else {
            int midC = (y1 + y2) / 2;
            if (y <= midC)
                update_y(rootX, x1, x2, rootY * 2, y1, midC, x, y, value);
            else
                update_y(rootX, x1, x2, rootY * 2 + 1, midC + 1, y2, x, y, value);
            tree[rootX][rootY].minPrime = Math.min(tree[rootX][rootY * 2].minPrime
                    , tree[rootX][rootY * 2 + 1].minPrime);
            tree[rootX][rootY].maxPrime = Math.max(tree[rootX][rootY * 2].maxPrime
                    , tree[rootX][rootY * 2 + 1].maxPrime);
        }
    }

    void update_x(int rootX, int x1, int x2, int x, int y, int value) {
        if (x1 != x2) {
            int midR = (x1 + x2) / 2;
            if (x <= midR)
                update_x(rootX * 2, x1, midR, x, y, value);
            else
                update_x(rootX * 2 + 1, midR + 1, x2, x, y, value);
        }
        update_y(rootX, x1, x2, 1, 0, array[0].length - 1, x, y, value);
    }


    int getMin_y(int rootX, int rootY, int y1, int y2, int c1, int c2) {
        if (c1 > c2)
            return Integer.MAX_VALUE;
        if (c1 == y1 && y2 == c2) {
//            if(isPrime(tree[rootX][rootY].minPrime))
            return tree[rootX][rootY].minPrime;
//            return Integer.MAX_VALUE;
        }
        int midC = (y1 + y2) / 2;
        return Math.min(getMin_y(rootX, rootY * 2, y1, midC, c1, Math.min(c2, midC)),
                getMin_y(rootX, rootY * 2 + 1, midC + 1, y2, Math.max(c1, midC + 1), c2));
    }

    int getMin_x(int rootX, int x1, int x2, int r1, int r2, int c1, int c2) {
        if (r1 > r2)
            return Integer.MAX_VALUE;
        if (r1 == x1 && x2 == r2)
            return getMin_y(rootX, 1, 0, array[0].length - 1, c1, c2);
        int midR = (x1 + x2) / 2;
        return Math.min(getMin_x(rootX * 2, x1, midR, r1, Math.min(r2, midR), c1, c2),
                getMin_x(rootX * 2 + 1, midR + 1, x2, Math.max(r1, midR + 1), r2, c1, c2));
    }


    int getMax_y(int rootX, int rootY, int y1, int y2, int c1, int c2) {
        if (c1 > c2)
            return Integer.MIN_VALUE;
        if (c1 == y1 && y2 == c2) {
//            if(isPrime(tree[rootX][rootY].maxPrime))
                return tree[rootX][rootY].maxPrime;
//            return Integer.MIN_VALUE;
        }
        int midC = (y1 + y2) / 2;
        return Math.max(getMax_y(rootX, rootY * 2, y1, midC, c1, Math.min(c2, midC)),
                getMax_y(rootX, rootY * 2 + 1, midC + 1, y2, Math.max(c1, midC + 1), c2));
    }

    int getMax_x(int rootX, int x1, int x2, int r1, int r2, int c1, int c2) {
//        print();
        if (r1 > r2)
            return Integer.MIN_VALUE;
        if (r1 == x1 && x2 == r2)
            return getMax_y(rootX, 1, 0, array[0].length - 1, c1, c2);
        int midR = (x1 + x2) / 2;
        return Math.max(getMax_x(rootX * 2, x1, midR, r1, Math.min(r2, midR), c1, c2),
                getMax_x(rootX * 2 + 1, midR + 1, x2, Math.max(r1, midR + 1), r2, c1, c2));
    }
}


public class Q5 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String query;
        int w, x, y, z, result;
        int n = input.nextInt();
        int m = input.nextInt();
        int q = input.nextInt();
        int[][] array = new int[n][];

        for (int i = 0; i < n; i++) {
            array[i] = new int[m];
            for (int j = 0; j < m; j++) {
                array[i][j] = input.nextInt();
            }
        }
        SegmentTree segmentTree = new SegmentTree(array, n, m);
        for (int i = 0; i < q; i++) {
            query = input.next();
            switch (query) {
                case "get_min_prime":
                    w = input.nextInt();
                    x = input.nextInt();
                    y = input.nextInt();
                    z = input.nextInt();
                    result = segmentTree.getMin_x(1, 0, array.length - 1, w - 1, y - 1, x - 1, z - 1);
                    if (result == Integer.MAX_VALUE )
                        System.out.println("no_primes_found");
                    else
                        System.out.println(result);
                    break;
                case "get_max_prime":
                    w = input.nextInt();
                    x = input.nextInt();
                    y = input.nextInt();
                    z = input.nextInt();
                    result = segmentTree.getMax_x(1, 0, array.length - 1, w - 1, y - 1, x - 1, z - 1);
                    if (result == Integer.MIN_VALUE || result<2 )
                        System.out.println("no_primes_found");
                    else
                        System.out.println(result);
                    break;
                case "set":
                    x = input.nextInt();
                    y = input.nextInt();
                    z = input.nextInt();
                    segmentTree.update_x(1, 0, array.length-1, x - 1, y - 1, z);
                    break;
            }
        }
    }
}
