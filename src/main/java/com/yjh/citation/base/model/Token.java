package com.yjh.citation.base.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * token in document after participle
 * Created by yjh on 16-4-10.
 */
public class Token implements Serializable {
    private static final int SITE_COUNT = Site.values().length;
    private String word;
    private int[] tfs = new int[SITE_COUNT];
    private int idf;
    private double value;

    public enum Site {
        TITLE, ABSTRACT, CONTEXT, REFERENCE
    }

    public int totalF() {
        int res = 0;
        for (int t : tfs)
            res += t;
        return res;
    }

    public int getIdf() {
        return idf;
    }

    public void setIdf(int idf) {
        this.idf = idf;
    }

    public void setTf(Site site, int tf) {
        if (tfs != null) {
            tfs[site.ordinal()] = tf;
        }
    }

    public int[] getTfs() {
        return tfs;
    }

    public void setTfs(int[] tfs) {
        this.tfs = tfs;
    }

    public double getValue() {
        for (int tf : tfs)
            value += tf * idf;
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        return !(word != null ? !word.equals(token.word) : token.word != null);

    }

    @Override
    public int hashCode() {
        return word != null ? word.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Token{" +
                "idf=" + idf +
                ", word='" + word + '\'' +
                ", tfs=" + Arrays.toString(tfs) +
                ", value=" + value +
                '}';
    }
}
