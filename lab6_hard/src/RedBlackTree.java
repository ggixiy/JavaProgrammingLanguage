import java.util.ArrayList;
import java.util.List;

public class RedBlackTree {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        int key;
        boolean color;
        Node left, right, parent;

        Node(int key, boolean color) {
            this.key = key;
            this.color = color;
        }
    }

    // Фіктивний чорний листок NIL
    private final Node NIL;
    private Node root;

    public RedBlackTree() {
        NIL = new Node(0, BLACK);
        NIL.left = NIL.right = NIL.parent = NIL;
        root = NIL;
    }

    // Rotations ------------------------------------

    private void rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != NIL) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == NIL) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != NIL) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == NIL) root = x;
        else if (y == y.parent.right) y.parent.right = x;
        else y.parent.left = x;
        x.right = y;
        y.parent = x;
    }

    // Add ----------------------------------------------------

    public boolean add(int key) {
        Node parent = NIL, cur = root;
        while (cur != NIL) {
            if (key == cur.key) return false;
            parent = cur;
            cur = key < cur.key ? cur.left : cur.right;
        }
        Node z = new Node(key, RED);
        z.left = z.right = NIL;
        z.parent = parent;
        if (parent == NIL) root = z;
        else if (key < parent.key) parent.left = z;
        else parent.right = z;

        fixAfterAdd(z);
        return true;
    }

    private void fixAfterAdd(Node z) {
        while (z.parent.color == RED) {
            Node grand = z.parent.parent;
            if (z.parent == grand.left) {
                Node uncle = grand.right;
                if (uncle.color == RED) {                 // дядько червоний
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    grand.color = RED;
                    z = grand;
                } else {
                    if (z == z.parent.right) {            // "зигзаг"
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;               // "пряма лінія"
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {                                      // дзеркально
                Node uncle = grand.left;
                if (uncle.color == RED) {
                    z.parent.color = BLACK;
                    uncle.color = BLACK;
                    grand.color = RED;
                    z = grand;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        root.color = BLACK;
    }

    // Detour ---------------------------------

    // Прямий обхід: корінь, ліве, праве
    public List<Integer> preOrder() {
        List<Integer> result = new ArrayList<>();
        preOrder(root, result);
        return result;
    }

    // Симетричний обхід: ліве, корінь, праве (дає елементи за зростанням)
    public List<Integer> inOrder() {
        List<Integer> result = new ArrayList<>();
        inOrder(root, result);
        return result;
    }

    private void preOrder(Node n, List<Integer> r) {
        if (n == NIL) return;
        r.add(n.key);
        preOrder(n.left, r);
        preOrder(n.right, r);
    }

    private void inOrder(Node n, List<Integer> r) {
        if (n == NIL) return;
        inOrder(n.left, r);
        r.add(n.key);
        inOrder(n.right, r);
    }

    // Output --------------------------------------------------

    // коди кольорів для консолі
    private static final String RED_COLOR = "\u001B[31;1m";
    private static final String GRAY_COLOR = "\u001B[90m";
    private static final String RESET = "\u001B[0m";

    @Override
    public String toString() {
        if (root == NIL) return "(дерево порожнє)\n";
        StringBuilder sb = new StringBuilder();
        draw(sb, root, "", true, true, "");
        return sb.toString();
    }

    private void draw(StringBuilder sb, Node n, String indent,
                      boolean isLast, boolean isRoot, String side) {
        sb.append(indent);
        if (!isRoot) sb.append(isLast ? "└── " : "├── ").append(side).append(": ");
        if (n == NIL) {
            sb.append(GRAY_COLOR).append("NIL").append(RESET).append('\n');
            return;
        }
        if (n.color == RED) sb.append(RED_COLOR).append(n.key).append("(R)").append(RESET);
        else                sb.append(n.key).append("(B)");
        sb.append('\n');
        if (n.left == NIL && n.right == NIL) return;
        String childIndent = indent + (isRoot ? "" : (isLast ? "    " : "│   "));
        draw(sb, n.left,  childIndent, false, false, "L");
        draw(sb, n.right, childIndent, true,  false, "R");
    }
}