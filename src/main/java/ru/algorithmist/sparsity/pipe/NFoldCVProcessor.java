package ru.algorithmist.sparsity.pipe;

import ru.algorithmist.sparsity.utils.RandomUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author Sergey Edunov
 */
public class NFoldCVProcessor<I> extends AbstractProcessor<Iterable<I>, Iterable<I>> {

    private int mod;
    private int size;
    private List<I> cv;

    public NFoldCVProcessor(int mod, int size) {
        this.mod = mod;
        this.size = size;
    }

    @Override
    public Iterable<I> train(final Iterable<I> input) throws Exception {
        cv = new ArrayList<I>();
        final Iterator<I> inputIterator = input.iterator();
        Iterable<I> res = new Iterable<I>() {
            @Override
            public Iterator<I> iterator() {
                return new Iterator<I>() {

                    private I next;

                    @Override
                    public boolean hasNext() {
                        while (next==null && inputIterator.hasNext()) {
                            I v = inputIterator.next();
                            if ( RandomUtils.nextInt(size) == mod) {
                                cv.add(v);
                            } else {
                                next = v;
                            }
                        }
                        return next!=null;
                    }

                    @Override
                    public I next() {
                        while (next==null && inputIterator.hasNext()) {
                            I v = inputIterator.next();
                            if ( RandomUtils.nextInt(size) == mod) {
                                cv.add(v);
                            } else {
                                next = v;
                            }
                        }
                        I temp = next;
                        next = null;
                        return temp;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Removing from CV Processor iterator is not supported");
                    }
                };
            }
        };
        return res;
    }

    @Override
    public Iterable<I> process(Iterable<I> input) throws Exception {
        return cv;
    }

}
