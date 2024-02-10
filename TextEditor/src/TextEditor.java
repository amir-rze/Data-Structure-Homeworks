import java.util.Scanner;

class Node{
    Node next;
    Node prev;
    private Character data;
    public Node(Character data,Node prev,Node next){
       this.prev=prev;
       this.next=next;
       this.data=data;
    }
    public Character getData() {
        return data;
    }
}
class MyLinkList<C> {
   private Node start = null ;
   private Node cursor = new Node(null,null,null);
   private int size = 0;
    public Node getCursor() {
        return cursor;
    }

    public void setCursor(Node cursor) {
        this.cursor = cursor;
    }
    public void add(Character c ){
        Node temp = start;
        if(temp==null){
            temp = new Node(c,null,null);
            start = temp ;
            cursor.prev=temp;
            return;
        }
        if(cursor.prev==null){
            cursor.next.prev=new Node(c,null,cursor.next);
            start=cursor.next.prev;
            cursor.prev=cursor.next.prev;
           return;
        }
        if(cursor.next==null){
            cursor.prev.next=new Node (c,cursor.prev,null);
            cursor.prev=cursor.prev.next;
            return;
        }
        cursor.prev.next=new Node(c,cursor.prev,cursor.next);
        cursor.prev=cursor.prev.next;
        cursor.next.prev=cursor.prev;
        return;
    }
    public void  backspasce(){
        if(cursor.prev==null)
            return;
       else if(cursor.prev.prev==null){
           if(cursor.next==null){
               cursor.prev=null;
               start=null;
               return;
           }
            cursor.next.prev=null;
            cursor.prev=null;
            return;
        }
        else if(cursor.next==null){
            cursor.prev.prev.next=null;
            cursor.prev=cursor.prev.prev;
        }
        else {
            cursor.next.prev = cursor.prev.prev;
            cursor.prev.prev.next = cursor.next;
            cursor.prev = cursor.next.prev;
            return;
        }
    }
    public void delete(){
        if(cursor.next==null)
            return;
        else if(cursor.next.next==null){
            if(cursor.prev==null){
                cursor.next=null;
                start=null;
                return;
            }
            cursor.prev.next=null;
            cursor.next=null;
        }
        else if(cursor.prev==null){
            cursor.next.next.prev=null;
            cursor.next=cursor.next.next;
        }
        else {
            cursor.prev.next = cursor.next.next;
            cursor.next.next.prev = cursor.prev;
            cursor.next = cursor.prev.next;
            return;
        }
    }
    public void move_left(){
        if(cursor.prev==null)
            return;
        cursor.next=cursor.prev;
        cursor.prev=cursor.prev.prev;
        return;
    }
    public void move_right(){
        if(cursor.next==null)
            return;
        cursor.prev=cursor.next;
        cursor.next=cursor.next.next;
        return;
    }
    public void show (){
        for (Node x = start;x!=null;x=x.next) {
            System.out.print(x.getData());
        }
        return;
    }
}

public class TextEditor {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String sentence = input.next();
        MyLinkList<Character> editedText = new MyLinkList <Character>();
        for (int i = 0; i <sentence.length() ; i++) {
            if (sentence.charAt(i) == 'R'){
                editedText.move_right();
            }
            else if(sentence.charAt(i) == 'L'){
                editedText.move_left();
            }
            else if(sentence.charAt(i) == 'B'){
                editedText.backspasce();
            }
            else if (sentence.charAt(i) == 'D'){
                editedText.delete();
            }
            else{
                editedText.add(sentence.charAt(i));
            }
        }
        editedText.show();
    }
}
