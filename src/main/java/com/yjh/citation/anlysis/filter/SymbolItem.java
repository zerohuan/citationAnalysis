package com.yjh.citation.anlysis.filter;

/**
 * Created by yjh on 16-4-6.
 */
public class SymbolItem {
    private String left;
    private String right;

    public SymbolItem(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }
}
