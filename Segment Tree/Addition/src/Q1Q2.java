import java.util.Scanner;


class Node {
    long sum;
    long min;
    long max;
    long lazy;

    public Node() {
        lazy = 0;
    }
}

class SegmentTree {
    private int[] array;
    private Node[] tree;
    private int size;

    public SegmentTree(int [] array) {
        this.array = array;
        size = 2 * (int) Math.pow(2, (int) (Math.ceil(Math.log(array.length) / Math.log(2))));
        tree = new Node[size];
        build(1, 0, array.length - 1);
    }

    public void build(int root, int start, int end) {
        if (start > end)
            return;
        else {
            tree[root] = new Node();
            if (start == end) {
                tree[root].min = array[start];
                tree[root].max = array[start];
                tree[root].sum = array[start];
            } else {
                int mid = (end + start) / 2;
                build(2 * root, start, mid);
                build(2 * root + 1, mid + 1, end);
                tree[root].sum = tree[root * 2].sum + tree[root * 2 + 1].sum;
                tree[root].min = Math.min(tree[root * 2].min, tree[root * 2 + 1].min);
                tree[root].max = Math.max(tree[root * 2].max, tree[root * 2 + 1].max);
            }
        }
    }

    public void update(int l, int r, long x) {
        update(1, 0, array.length - 1, l - 1, r - 1, x);
    }

    public void update(int root, int start, int end, int l, int r, long x) {
        if (tree[root].lazy != 0) {
            tree[root].max += tree[root].lazy;
            tree[root].min += tree[root].lazy;
            tree[root].sum += tree[root].lazy * (end - start + 1);
            if (start != end) {
                tree[root * 2].lazy += tree[root].lazy;
                tree[root * 2 + 1].lazy += tree[root].lazy;
            }
            tree[root].lazy = 0;
        }
        if (start > end || start > r || end < l)
            return;
        if (start >= l && end <= r) {
            tree[root].sum += x * (end - start + 1);
            tree[root].min += x;
            tree[root].max += x;
            if (start != end) {
                tree[root * 2].lazy += x;
                tree[root * 2 + 1].lazy += x;
            }
            return;
        }
        int mid = (end + start) / 2;
        update(root * 2,start,mid, l, r, x);
        update(root * 2 + 1,mid+1,end, l, r, x);
        tree[root].sum = tree[root * 2].sum + tree[root * 2 + 1].sum;
        tree[root].min = Math.min(tree[root * 2].min, tree[root * 2 + 1].min);
        tree[root].max = Math.max(tree[root * 2].max, tree[root * 2 + 1].max);
    }

    public long getMax(int l, int r) {
        return getMax(1,0,array.length-1, l - 1, r - 1);
    }

    public long getMax(int root,int start, int end, int l, int r) {
        if (tree[root].lazy != 0) {
            tree[root].sum += tree[root].lazy * (end - start + 1);
            tree[root].min += tree[root].lazy;
            tree[root].max += tree[root].lazy;
            if (start != end) {
                tree[root * 2].lazy += tree[root].lazy;
                tree[root * 2 + 1].lazy += tree[root].lazy;
            }
            tree[root].lazy = 0;
        }
        if (start > end || start > r || end < l)
            return Long.MIN_VALUE;
        if (start >= l && end <= r)
            return tree[root].max;
        int mid = (start + end) / 2;
        return Math.max(getMax(root * 2,start,mid, l, r), getMax(root * 2 + 1,mid+1,end, l, r));
    }


    public long getMin(int l, int r) {
        return getMin(1,0,array.length-1,l - 1, r - 1);
    }

    public long getMin(int root,int start, int end, int l, int r) {
        if (tree[root].lazy != 0) {
            tree[root].sum += tree[root].lazy * (end - start + 1);
            tree[root].min += tree[root].lazy;
            tree[root].max += tree[root].lazy;
            if (start != end) {
                tree[root * 2].lazy += tree[root].lazy;
                tree[root * 2 + 1].lazy += tree[root].lazy;
            }
            tree[root].lazy = 0;
        }
        if (start > end || start > r || end < l)
            return Long.MAX_VALUE;
        if (start >= l && end <= r)
            return tree[root].min;
        int mid = (start + end) / 2;
        return Math.min(getMin(root * 2,start,mid, l, r), getMin(root * 2 + 1,mid+1,end, l, r));
    }

    public long getSum(int l, int r) {
        return getSum(1,0 ,array.length-1, l - 1, r - 1);
    }

    public long getSum(int root,int start, int end, int l, int r) {
        if (tree[root].lazy != 0) {
            tree[root].sum += tree[root].lazy * (end - start + 1);
            tree[root].min += tree[root].lazy;
            tree[root].max += tree[root].lazy;
            if (start != end) {
                tree[root * 2].lazy += tree[root].lazy;
                tree[root * 2 + 1].lazy += tree[root].lazy;
            }
            tree[root].lazy = 0;
        }
        if (start > end || start > r || end < l)
            return 0;
        if (start >= l && end <= r)
            return tree[root].sum;
        int mid = (start + end) / 2;
        return getSum(root * 2,start,mid, l, r)+ getSum(root * 2 + 1,mid+1,end, l, r);
    }

}


public class Q1Q2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String query;
        int x, y, z;
        int n = input.nextInt();
        int q = input.nextInt();
        int [] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = input.nextInt();
        }
        SegmentTree segmentTree = new SegmentTree(array);
        for (long i = 0; i < q; i++) {
            query = input.next();
            switch (query) {
                case "add":
                    x = input.nextInt();
                    y = input.nextInt();
                    z = input.nextInt();
                    segmentTree.update(x, y, z);
                    break;
                case "get_max":
                    x = input.nextInt();
                    y = input.nextInt();
                    System.out.println(segmentTree.getMax(x, y));
                    break;
                case "get_min":
                    x = input.nextInt();
                    y = input.nextInt();
                    System.out.println(segmentTree.getMin(x, y));
                    break;
                case "get_sum":
                    x = input.nextInt();
                    y = input.nextInt();
                    System.out.println(segmentTree.getSum(x, y));
                    break;
            }
        }
    }
}
