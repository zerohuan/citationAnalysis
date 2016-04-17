package com.yjh.citation.anlysis.filter;

import com.yjh.citation.base.Filter;
import com.yjh.citation.base.FilterChain;
import com.yjh.citation.base.model.Document;
import com.yjh.citation.base.model.Reference;
import com.yjh.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yjh on 16-4-8.
 */
public class ReferenceExtractFilter implements Filter {
    private static final Pattern patternCN = Pattern.compile(
            "\\[?(\\d{1,3})(\\]|\\.)?" +
                    "(([一-龥]{2,4},)*?[一-龥]{2,4}\\.)?" +
                    "([一-龥a-zA-Z_0-9 ]{3,})" +
                    "(" +
                    "(((\\[[A-Z]\\])|\\.)([^\\[\\]]+?),?\\d{4},?)" +
                    "|" +
                    "(\\[(EB_OL|EB|OL)\\]\\.)" +
                    ")"
    );
    private static final Pattern patternENG = Pattern.compile(
            "(?i)^\\[?(\\d{1,3})(\\]|\\.)?" +
                    "(([a-z _]+[,\\.])*?[a-z _]+\\.)?" +
                    "([a-z_0-9 ]{20,})" +
                    "(" +
                    "(((\\[[A-Z]\\])|\\.)(.+?),?\\d{4})" +
                    "|" +
                    "(\\[(EB_OL|EB|OL)\\]\\.)" +
                    ")"
    );
    private static final Pattern patternNet = Pattern.compile(
            "(?i)^\\[?(\\d{1,3})(\\]|\\.)?" +
                    "([一-龥a-z _]{3,}\\.)?" +
                    "([一-龥a-z _,]{5,})\\.?\\[((EB)|(\\d{4})).+"
    );

    public static void main(String[] args) {
        Matcher matcher = patternENG.matcher("[11]Brier S.Cybersemiotics_A new Paradigm in analyzingthe Problems of Knowledge Organization and DocumentRetrieval[A].Ingwersen P,Pors N O.CoLIS2_Sec_ond International Conference on Conceptions of Libraryand Information Science;Integration in Perspective[M].CopenhagenDenmark_Royal School of Libraryand Information Science.1996_23_43.\n");
        if (matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    @Override
    public void doFilter(Document document, FilterChain chain) {
        Matcher matcherCN = patternCN.matcher("");
        Matcher matcherENG = patternENG.matcher("");
        Matcher matcherNet = patternNet.matcher("");
//        System.out.println("##" + document.getTitle() + "##");
        if (document.getReferenceContent() != null)
            try (BufferedReader reader =
                         new BufferedReader(new StringReader(document.getReferenceContent()))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    matcherCN.reset(line);
                    matcherENG.reset(line);
                    matcherNet.reset(line);
                    //匹配中文参考文献并且标题必须包含汉字
                    if (matcherCN.find() &&
                            StringUtil.containsRegex("[一-龥]",matcherCN.group(5))) {
                        handleMatcher(document, matcherCN, chain);
                    } else if (matcherENG.find()) {
                        handleMatcher(document, matcherENG, chain);
                    } else if (matcherNet.find()) {
                        Reference reference = new Reference();
                        //引用序号
                        reference.setNumber(Integer.parseInt(matcherNet.group(1)));
                        reference.setTitle(matcherNet.group(4));
                        reference.setCount(1);
                        chain.getContext().addTitle(StringUtil.standardTitle(reference.getTitle()));
                        document.addReference(reference);
                    }
                }
                if (document.getReferenceList().size() < 4 || document.getReferenceContent() == null) {
                    chain.getContext().addBadDocCount(document);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        chain.getContext().addHandledDocCount(document);
        chain.doFilter(document);
    }

    private void handleMatcher(Document document, Matcher matcher, FilterChain chain) {
        Reference reference = new Reference();
        //引用序号
        reference.setNumber(Integer.parseInt(matcher.group(1)));
        //作者
        String authors = matcher.group(3);
        if (authors != null) {
            if (authors.endsWith("."))
                authors = authors.substring(0, authors.length() - 1);
            reference.setAuthors(authors.split(","));
            for (int i = 0; i < reference.getAuthors().length; ++i)
                reference.getAuthors()[i] = reference.getAuthors()[i]
                        .replaceAll("(.+)等", "$1");
        }
        //标题
        String title = matcher.group(5);
        if (title.matches("[_ \\d,]*"))
            return;
        reference.setTitle(title);
        reference.setCount(1);
        chain.getContext().addTitle(StringUtil.standardTitle(title));
        document.addReference(reference);
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }
}
