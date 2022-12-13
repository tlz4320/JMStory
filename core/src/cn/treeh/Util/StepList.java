package cn.treeh.Util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;

public class StepList<E> implements Iterable<E> {
    static public class LinkNode<E> {
        public E getObject() {
            return object;
        }

        E object;
        public LinkNode<E> former, next;

        public LinkNode(E o) {
            object = o;
        }
    }

    public LinkNode<E> root;
    public LinkNode<E> tail;

    public LinkNode<E> now;
    public LinkNode<E> bef;
    float threshold;
    int size = 0;
    public E get(int index) {
        LinkNode<E> p = root;
        while(index-- > 0){
            p = p.next;
        }
        return p.object;
    }
    public E get() {
        if (now == null) {
            now = root;
            bef = root;
        }
        return now.object;
    }

    public int size() {
        return size;
    }

    public boolean isLast() {
        return now == tail;
    }

    public boolean isFirst() {
        return now == root;
    }

    public E get(float alpha) {
        if (now == null) {
            now = bef = root;
        }
        return alpha > threshold ? now.object : bef.object;
    }

    public void next() {
        bef = now;
        if (now == tail)
            now = root;
        else
            now = now.next;

    }

    public void before() {
        bef = now;
        if (now == root)
            now = tail;
        else
            now = now.former;
    }

    public void next(int goStep, float threshold) {
        while (goStep > 0) {
            next();
            goStep--;
        }
        while (goStep < 0) {
            before();
            goStep++;
        }
        this.threshold = threshold;
    }
    public void norm(){
        bef = now;
    }
    public boolean isEmpty() {
        return root == null;
    }

    public LinkNode<E> add(Object o) {
        size++;
        if (root == null)
            tail = root = new LinkNode(o);
        else {
            tail.next = new LinkNode(o);
            tail.next.former = tail;
            tail = tail.next;
        }
        return tail;
    }

    public void remove(Object o) {
        if (root.object.equals(o)) {
            root = root.next;
            root.former = null;
            return;
        }
        if (tail.object.equals(o)) {
            tail = tail.former;
            tail.next = null;
            return;
        }
        LinkNode<E> n = root;
        while (n != tail) {
            n = n.next;
            if (n.object.equals(o)) {
                n.former.next = n.next;
                n.next.former = n.former;
                return;
            }
        }
    }

    class PairWiseIterator<E> implements Iterator<E> {
        PairWiseIterator(LinkNode<E> root) {
            node = root;
        }

        LinkNode<E> node;

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public E next() {
            LinkNode<E> res = node;
            node = node.next;
            return res.getObject();
        }

        @Override
        public void remove() {

        }

    }

    @Override
    public Iterator<E> iterator() {
        return new PairWiseIterator<>(root);
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        LinkNode<E> node = root;
        while (node != null) {
            action.accept(node.getObject());
            node = node.next;
        }
    }


}
