package com.yjh.citation.base.resource;

import com.yjh.citation.base.model.Document;

/**
 * Created by yjh on 16-4-7.
 */
public abstract class MetaRule {
    private String name;
    private String sign;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public abstract void doSet(Document document, String line);

    protected String clean(String str) {
        return str.replace(getSign(), "").replace("；","").trim();
    }

    public static class AuthorCNV extends MetaRule {
        {
            setSign("【作者】");
        }
        @Override
        public void doSet(Document document, String line) {
            document.setAuthorCN(clean(line).split(" "));
        }
    }

    public static class AuthorEng extends MetaRule {
        {
            setSign("【Author】");
        }
        @Override
        public void doSet(Document document, String line) {
            document.setAuthorEng(clean(line));
        }
    }

    public static class Institution extends MetaRule {
        {
            setSign("【机构】");
        }
        @Override
        public void doSet(Document document, String line) {
            document.setInstitution(clean(line));
        }
    }

    public static class Abstract extends MetaRule {
        {
            setSign("【摘要】");
        }
        @Override
        public void doSet(Document document, String line) {
            document.setAbstract_(clean(line));
        }
    }

    public static class Keywords extends MetaRule {
        {
            setSign("【关键词】");
        }
        @Override
        public void doSet(Document document, String line) {
            document.setKeywords(clean(line).split(" "));
        }
    }

    public static class Category extends MetaRule {
        {
            setSign("【分类号】");
        }
        @Override
        public void doSet(Document document, String line) {
            document.setCategory(line.replaceAll(".*" + getSign() + "(.*)", "$1").trim().split(";"));
        }
    }

    public static void main(String[] args) {
        System.out.println("123".split(";").length);
    }
}
