package ru.algorithmist.sparsity.text;

import ru.algorithmist.sparsity.pipe.AbstractProcessor;
import ru.algorithmist.sparsity.pipe.Processor;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Sergey Edunov
 */
public class Tokenizer extends AbstractProcessor<Iterator<String>, Iterator<Iterator<String>>> {

    @Override
    public Iterator<Iterator<String>> train(Iterator<String> input) {
        return new TokenizerIterator(input);
    }

    @Override
    public Iterator<Iterator<String>> process(Iterator<String> input) {
        return new TokenizerIterator(input);
    }

    private static class TokenizerIterator implements Iterator<Iterator<String>> {

        private Iterator<String> input;

        private TokenizerIterator(Iterator<String> input) {
            this.input = input;
        }

        @Override
        public boolean hasNext() {
            return input.hasNext();
        }

        @Override
        public Iterator<String> next() {
            String v = input.next();
            String[] lemmas = v.split("[^\\w]");
            return Arrays.asList(lemmas).iterator();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
