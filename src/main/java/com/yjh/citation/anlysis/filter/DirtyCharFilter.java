package com.yjh.citation.anlysis.filter;

import com.yjh.citation.base.model.Document;
import com.yjh.citation.base.Filter;
import com.yjh.citation.base.FilterChain;

/**
 * 清除所有非法字符，不需要的特殊符号等
 * Created by yjh on 16-4-4.
 */
public class DirtyCharFilter implements Filter {
    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(Document document, FilterChain chain) {
        String context = document.getContext();
        context = context
                //LFCR回车换行不要去掉
                .replaceAll("[\u0000-\u0007\u0009\u000B-\u000C\u000E-\u0019]", "")
                .replace("　", " ")
                .replace("\\", "/")
                .replace("—", "-")
                .replace(")))", "——")
                .replace(",et al","")
                .replaceAll(", ?etc","")
                .replace("X()","00") //乱码
                .replace("", "");
        document.setContext(context);
        chain.doFilter(document);
    }

    public static void main(String[] args) {
        System.out.println("abcdefg".replaceAll("[a-ce-f]",""));
    }
}
