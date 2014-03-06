package ru.algorithmist.sparsity.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Sergey Edunov
 */
public class Predictors<P> implements Iterable<P>{

    private List<P> data;

    public Predictors() {
        data = Collections.synchronizedList(new ArrayList<P>());
    }

    public Predictors(int size) {
        data = Collections.synchronizedList(new ArrayList<P>(size));
        for(int i=0; i<size; i++) {
            data.add(null);
        }
    }

    public int size() {
        return data.size();
    }

    public P get(int row) {
        return data.get(row);
    }

    public void add(P label) {
        data.add(label);
    }

    public void set(int i, P label) {
        data.set(i, label);
    }

    @Override
    public Iterator<P> iterator() {
        return data.iterator();
    }
}
