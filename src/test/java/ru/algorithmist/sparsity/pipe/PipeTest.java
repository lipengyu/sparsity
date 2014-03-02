package ru.algorithmist.sparsity.pipe;

import org.junit.Test;
import ru.algorithmist.sparsity.data.SparseMatrix;
import ru.algorithmist.sparsity.text.Tokenizer;
import ru.algorithmist.sparsity.text.Vectorizer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * @author Sergey Edunov
 */
public class PipeTest {

    @Test
    public void testSimplePipe() throws Exception {
        Pipe<Iterator<String>, Iterator<Iterator<String>>> tokenizer
                = new Pipe<Iterator<String>, Iterator<Iterator<String>>>(
                new Tokenizer()
        );

        List<String> testSet = new ArrayList<String>();
        testSet.add("hello world");
        testSet.add("test set");

        Iterator<Iterator<String>> lemmas = tokenizer.train(testSet.iterator());

        int i = 0;
        StringBuilder res = new StringBuilder();
        for(; lemmas.hasNext(); ) {
            Iterator<String> sentence = lemmas.next();
            for(; sentence.hasNext(); ) {
                i++;
                res.append(sentence.next());
            }
        }
        assertEquals("Wrong number of elements", 4, i);
        assertEquals("helloworldtestset", res.toString());
    }

    @Test
    public void testComplexPipe() throws Exception {
        Pipe<Iterator<String>, SparseMatrix> pipe = new Pipe<Iterator<String>, Iterator<Iterator<String>>>(new Tokenizer()).add(new Vectorizer());
        System.out.println(pipe);
        List<String> testSet = new ArrayList<String>();
        testSet.add("hello world");
        testSet.add("hello test");

        SparseMatrix sm = pipe.train(testSet.iterator());
        System.out.println(sm);
    }
}
