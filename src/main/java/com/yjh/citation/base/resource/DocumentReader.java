package com.yjh.citation.base.resource;

import com.yjh.citation.base.model.Document;
import com.yjh.io.FileAccessor;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;

/**
 *
 * Created by yjh on 16-4-7.
 */
public class DocumentReader {
    private List<Document> documents;
    private boolean isRecursion;
    private String suffix;
    private List<MetaRule> rules;
    private int id;
    private final String[] themes = {"信息服务", "信息检索", "文献计量", "知识管理", "竞争情报", "资源共享"};

    public DocumentReader(boolean isRecursion, String suffix, MetaRule...rules) {
        this.isRecursion = isRecursion;
        this.suffix = suffix;
        documents = new ArrayList<>();
        this.rules = new ArrayList<>();
        this.rules.addAll(Arrays.asList(rules));
    }

    public List<Document> load(Resource resource) throws IOException {
        File dir = new File(resource.getContextPath());
        return loadInternal(dir, resource.getMetaPath(), resource.getEncoding());
    }

    private List<Document> loadInternal(File file, String metaPath, String encoding) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return file.isDirectory() || pathname.getName().endsWith(suffix);
                    }
                });
                String fileMeta = metaPath + "/" + file.getName() + "/";
                for (File f : files) {
                    if(f.isFile()) {
                        loadInternal(f, fileMeta, encoding);
                    } else if (isRecursion && f.isDirectory()) {
                        loadInternal(f, metaPath, encoding);
                    }
                }
            } else {
                Document document = new Document();
                document.setTitle(file.getName().replace(suffix, ""));
                document.setContext(FileAccessor.readAll(file, encoding));
                for (int i = 0; i < themes.length; ++i) {
                    if (metaPath.contains(themes[i])) {
                        System.out.println(i + " " + themes[i] + " "+ metaPath);
                        document.setTheme(i);
                    }
                }

                try (Scanner in = new Scanner(new File(metaPath + file.getName()), encoding)) {
                    while (in.hasNextLine()) {
                        String line = in.nextLine();
                        for (MetaRule rule : rules) {
                            document.accept(rule, line);
                        }
                    }
                }
                documents.add(document);
            }
        }
        return documents;
    }


    private static void c_b(String path) {
        File dir = new File(path);
        File[] dirs = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for (File d : dirs) {
            File[] files = d.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    pathname.renameTo(new File(pathname.getPath().replace("——","-").replaceAll("[-|、|\\-|（|）|(|)]",
                            "_")));
                    if (pathname.getName().endsWith("txt") && !new File(pathname.getPath().replace("txt","pdf"))
                            .exists())
                        System.out.println(pathname.getName());
                    return true;
                }
            });
        }
    }

    public static void main(String[] args) throws IOException {
//        c_b("/home/yjh/data/txt");
//        c_b("/home/yjh/data/base");

        DocumentReader reader = new DocumentReader(true, ".txt",
                new MetaRule.AuthorCNV(),
                new MetaRule.AuthorEng(),
                new MetaRule.Category(),
                new MetaRule.Institution(),
                new MetaRule.Keywords(),
                new MetaRule.Abstract());

        reader.load(new Resource("/home/yjh/data/txt", "gbk", "/home/yjh/data/base"));
    }
}
