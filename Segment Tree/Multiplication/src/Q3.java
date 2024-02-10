import java.util.Scanner;


class Node {
    long sum;
    long sumlazy;
    long multiLazy;

    public Node() {
        sumlazy = 0L;
        multiLazy = 1L;
    }
}

class SegmentTree {
    private int[] array;
    private Node[] tree;
    private int size;
    private long mod = 1000000007L;

    public SegmentTree(int [] array) {
        this.array = array;
        size = 2 * (int) Math.pow(2, (int) (Math.ceil(Math.log(array.length) / Math.log(2))));
        tree = new Node[size];
        build(1, 0, array.length - 1);
    }

//    public long mod (long number){
//        return number % mod;
//    }

    public void build(int root, int start, int end) {
        if (start > end)
            return;
        else {
            tree[root] = new Node();
            if (start == end) {
                tree[root].sum = array[start]%mod;
            } else {
                int mid = (end + start) / 2;
                build(2 * root, start, mid);
                build(2 * root + 1, mid + 1, end);
                tree[root].sum = ((tree[root * 2].sum%mod) + (tree[root * 2 + 1].sum%mod))%mod;
            }
        }
    }
    public void propagate(int root , int start , int end){
        if (tree[root].sumlazy != 0 || tree[root].multiLazy!=1) {
            tree[root].sum = (((tree[root].sumlazy * (end - start + 1))%mod) +
                    (tree[root].sum*tree[root].multiLazy)%mod)%mod;
            if (start != end) {
                tree[root * 2].multiLazy =((tree[root*2].multiLazy%mod) *(tree[root].multiLazy%mod))%mod;
                tree[root * 2].sumlazy = ((tree[root*2].sumlazy % mod ) * (tree[root].multiLazy%mod))%mod;
                tree[root * 2+1].multiLazy =((tree[root*2+1].multiLazy % mod) * (tree[root].multiLazy%mod))%mod;
                tree[root * 2+1].sumlazy = ((tree[root*2+1].sumlazy% mod) * (tree[root].multiLazy%mod));
                tree[root * 2].sumlazy = ((tree[root].sumlazy%mod) +(tree[root*2].sumlazy%mod));
                tree[root * 2+1].sumlazy = ((tree[root].sumlazy%mod) + (tree[root*2+1].sumlazy%mod));
            }
            tree[root].sumlazy = 0;
            tree[root].multiLazy=1;
        }
    }

    public void update(int l, int r, long x) {
        update(1, 0, array.length - 1, l - 1, r - 1, x);
    }

    public void update(int root, int start, int end, int l, int r, long x) {
        propagate(root,start,end);
        if (start > end || start > r || end < l)
            return;
        if (start >= l && end <= r) {
            tree[root].sum = (x * (end - start + 1))+tree[root].sumlazy+tree[root].sum*tree[root].multiLazy;
            tree[root].sum = (tree[root].sum)%mod ;
            if (start != end) {
                tree[root * 2].sumlazy = (x%mod)+(tree[root*2].sumlazy%mod);
                tree[root * 2].sumlazy %= mod;
                tree[root * 2+1].sumlazy = (x%mod)+(tree[root*2+1].sumlazy%mod);
                tree[root * 2+1].sumlazy %= mod ;
            }
            return;
        }
        int mid = (end + start) / 2;
        update(root * 2,start,mid, l, r, x);
        update(root * 2 + 1,mid+1,end, l, r, x);
        tree[root].sum = (tree[root * 2].sum%mod) + (tree[root * 2 + 1].sum%mod);
        tree[root].sum = tree[root].sum % mod ;
    }

    public void multiply(int l, int r, long x) {
        multiply(1, 0, array.length - 1, l - 1, r - 1, x);
    }

    public void multiply(int root, int start, int end, int l, int r, long x) {

        propagate(root,start,end);
        if (start > end || start > r || end < l)
            return;
        if (start >= l && end <= r) {
            tree[root].sum = ((x%mod)* ((tree[root].sum*tree[root].multiLazy + tree[root].sumlazy)%mod))%mod ;
            if (start != end) {
                tree[root * 2].multiLazy = (x%mod)*(tree[root*2].multiLazy%mod);
                tree[root * 2].multiLazy %= mod;
                tree[root * 2+1].multiLazy = (x%mod)*(tree[root*2+1].multiLazy%mod);
                tree[root * 2+1].multiLazy %= mod;
                tree[root * 2].sumlazy = (x%mod)*(tree[root*2].sumlazy%mod);
                tree[root * 2].sumlazy %= mod;
                tree[root * 2+1].sumlazy = (x%mod)*(tree[root*2+1].sumlazy%mod);
                tree[root * 2+1].sumlazy %= mod ;
            }
            return ;
        }
        int mid = (end + start) / 2;
        multiply(root * 2,start,mid, l, r, x);
        multiply(root * 2 + 1,mid+1,end, l, r, x);
        tree[root].sum = ((tree[root * 2].sum%mod) + (tree[root * 2 + 1].sum%mod))%mod;
        tree[root].sum = (tree[root].sum)%mod ;
    }


    public long getSum(int l, int r) {
        return getSum(1,0 ,array.length-1, l - 1, r - 1);
    }

    public long getSum(int root,int start, int end, int l, int r) {
        propagate(root,start,end);
        if (start > end || start > r || end < l)
            return 0;
        if (start >= l && end <= r)
            return (tree[root].sum)%mod;
        int mid = (start + end) / 2;
        return ((getSum(root * 2,start,mid, l, r)%mod)+ (getSum(root * 2 + 1,mid+1,end, l, r)%mod))%mod;
    }

}

public class Q3{
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
                case "multiply":
                    x = input.nextInt();
                    y = input.nextInt();
                    z = input.nextInt();
                    segmentTree.multiply(x, y,z);
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
