package com.cnki;

import com.cnki.exception.BadResponseException;
import com.cnki.exception.ConcurrentException;
import com.cnki.model.DocForDownload;
import com.fetcher.Requester;
import com.fetcher.RequesterBuilder;
import com.fetcher.ResponseData;
import com.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yjh on 2016/3/19.
 */
public class DownLoadPDF {
    private static final String base_url = "http://epub.cnki.net";
    private static final String searchPreparedUrl = "http://epub.cnki.net/KNS/request/SearchHandler.ashx?action=&NaviCode=*&ua=1.21&PageName=ASP.brief_result_aspx&DbPrefix=SCDB&DbCatalog=%e4%b8%ad%e5%9b%bd%e5%ad%a6%e6%9c%af%e6%96%87%e7%8c%ae%e7%bd%91%e7%bb%9c%e5%87%ba%e7%89%88%e6%80%bb%e5%ba%93&ConfigFile=SCDB.xml&db_opt=CJFQ%2CCJFN%2CCDFD%2CCMFD%2CCPFD%2CIPFD%2CCCND%2CCCJD%2CHBRD&base_special1=%25&magazine_value1=%magazine_value1%&magazine_special1=%3D&publishdate_from=%publishdate_from%&publishdate_to=%publishdate_to%&his=0&__=Tue%20Feb%2025%202014%2020%3A13%3A53%20GMT%2B0800";
    private static final String searchUrl = "http://epub.cnki.net/kns/brief/brief.aspx?pagename=";
    private static final String listPageUrl = "http://epub.cnki.net/kns/brief/brief.aspx?curpage=%curpage%&RecordsPerPage=20&QueryID=7&ID=&turnpage=%turnpage%&tpagemode=L&dbPrefix=SCDB&Fields=&DisplayMode=listmode&PageName=ASP.brief_result_aspx";
    private SearchCondition condition;

    private Requester requester = new RequesterBuilder()
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0")
            .setEncoding("utf8")
            .build();
    private int page = 0;
    private int item = 0;

    public static void downloadBySearch(SearchCondition condition, int startPage, int itemNumber) throws InterruptedException {
        int result;
        //直到正常返回下载结束
        DownLoadPDF downLoadPDF = new DownLoadPDF();
        while ((result = downLoadPDF.searchByCondition(condition, startPage, itemNumber)) != 0) {
            //先关闭上一个下载器
            try {
                downLoadPDF.requester.close();
            } catch (IOException e) {}
            switch (result) {
                case 1: //IO,中断异常等其他错误
                    System.out.println("捕捉到错误，准备释放连接，重新续接!" + condition.getJournal() + "，第" + downLoadPDF.page +
                            " 页，第" + downLoadPDF.item + "篇");
                    TimeUnit.SECONDS.sleep(5);
                    break;
                case 2: //用户数并发限制
                    System.out.println("当前用户并发数已满！");
                    TimeUnit.SECONDS.sleep(30);
                    break;
                case 3: //未取到正确的标题，响应内容错误
                    System.out.println("响应内容异常");
                    //重新开始
                    TimeUnit.SECONDS.sleep(5);
                    break;
            }
            startPage = downLoadPDF.page;
            itemNumber = downLoadPDF.item;
            downLoadPDF = new DownLoadPDF();
        }
    }

    private int searchByCondition(SearchCondition condition, int startPage, int itemNumber) {
        this.condition = condition;
        if(page == 0)
            page = startPage;
        if(item == 0)
            item = itemNumber;
        try {
            ResponseData responseData = requester.createExample(searchPreparedUrl.replace("%magazine_value1%", URLEncoder.encode(condition.getJournal(), "utf-8"))
                    .replace("%publishdate_from%", condition.getStartDate()).replace("%publishdate_to%", condition.getEndDate())).doGet();

            String url = searchUrl + responseData.getBody().replace("&nbsp;","") + "&t=" + System.currentTimeMillis() + "&S=1&keyValue=";
            ResponseData countResponse = requester.createExample(url).doGet();
            //得到有多少结果
            int pageCount = 0;
            String pc = StringUtil.findFirst(countResponse.getBody(), "找到([\\d,]+?)条结果", new StringUtil.StrFilter() {
                @Override
                public String filter(Matcher matcher) {
                    return matcher.group(1).replace(",", "");
                }
            });
            if (pc != null)
                pageCount = Integer.parseInt(pc);

            //处理每一页的情况
            StringBuilder infoBuffer = new StringBuilder();
            for (int i = page; i <= Math.ceil(((double)pageCount) / 20); i++) {
                requester.createExample(searchPreparedUrl.replace("%magazine_value1%", URLEncoder.encode(condition.getJournal(), "utf-8"))
                        .replace("%publishdate_from%", condition.getStartDate()).replace("%publishdate_to%", condition.getEndDate())).doGet();
                String pageUrl = listPageUrl.replace("%curpage%", i + "").replace("%turnpage%", (i - 1) + "");
                ResponseData listResponse = requester.createExample(pageUrl).doGet();

                //解析其中的每一条搜索结果
                Document doc = Jsoup.parse(listResponse.getBody());
                Elements pages = doc.select(".fz14");
                for (int j = item - 1; j < pages.size(); ++j) {
                    String docUrl = base_url + pages.get(j).attr("href");
                    ResponseData docResponse1 = requester.createExample(docUrl)
                            .setRedirectAuto(false)
                            .addHeader("Referer", pageUrl)
                            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .addHeader("Host", "epub.cnki.net")
                            .addHeader("Connection", "keep-alive")
                            .doGet();
                    String realDetailUrl = docResponse1.getLocation();
                    ResponseData detailResponse = requester.createExample(realDetailUrl)
                            .setRedirectAuto(true)
                            .addHeader("Referer", pageUrl)
                            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .addHeader("Host", "epub.cnki.net")
                            .addHeader("Connection", "keep-alive")
                            .doGet();
                    infoBuffer.setLength(0);
                    System.out.print(infoBuffer.append(condition.isNeedPDF() ? "正在下载《" : "正在更新信息《").append(condition.getJournal())
                            .append("》，第" + (page = i)).append("页，第")
                            .append(item = j + 1).append("条，"));
                    pdfDownload(detailResponse.getBody(), realDetailUrl, condition.isNeedPDF());
                }
                //只有下载第startPage是需要重指定行号开始，这里条目号必须恢复到1
                item = 1;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof BadResponseException)
                return 3;
            else if (e instanceof ConcurrentException)
                return 2;
            else
                return 1;
        }
    }

    private static final Pattern publishYearP = Pattern.compile("(\\d+)年\\d+期");
    private final Matcher publishYearM = publishYearP.matcher("");
    private DocForDownload pdfDownload( String detailBody, String refer, boolean needPDF)
            throws ConcurrentException, BadResponseException, IOException, InterruptedException {
        detailBody = detailBody.replace("&#xA;                            pdf&#xA;                        ", "pdfD");
        Document doc = Jsoup.parse(detailBody);
        //标题
        String title = doc.select("#chTitle").text().trim();
        System.out.println(title);
        //期刊名
        String journal = "";
        if(doc.select(".detailLink a").size() > 0)
            journal = doc.select(".detailLink a").get(0).text().trim();
        else {//如果没有取到标题，输出页面，直接结束
            throw new BadResponseException();
        }
        //作者
        Elements authorInfo = doc.select(".author p");
        String[] authorsA = null;
        String[] author_corporations = null;
        for(int i = 0; i < authorInfo.size(); i++) {
            Element e = authorInfo.get(i);
            if (e.text().contains("【作者】")) {
                Elements authors = e.select("a");
                authorsA = new String[authors.size()];
                for (int j = 0; j < authors.size(); j++) {
                    authorsA[j] = authors.get(j).text().trim();
                }
            } else if (e.text().contains("【机构】")) {
                Elements corporations = e.select("a");
                author_corporations = new String[corporations.size()];
                for (int j = 0; j < corporations.size(); j++) {
                    author_corporations[j] = corporations.get(j).text().trim();
                }
            }
        }
        String date = "";
        publishYearM.reset(detailBody);
        while(publishYearM.find()) {
            date = publishYearM.group(1);
        }
        String institution = "";
        Elements keywords = doc.select(".keywords");
        for(Element e : keywords) {
            if(e.text().contains("【基金】")) {
                institution = e.text().replaceAll("【基金】", "");
            }
        }

        //分类号
        String category = "";
        Elements categoryLis = doc.select("ul.break li");
        for (Element e : categoryLis) {
            if (e.text().contains("【分类号】")) {
                category = e.text().replaceAll("【分类号】","");
            }
        }

        //保存结果
        DocForDownload docPDF = new DocForDownload(title, journal, authorsA, author_corporations, date, institution);
        docPDF.setCategory(category);
        Elements downLinks = doc.select(".pdfD");
        if(downLinks.size() > 0) {
            Element downLink = downLinks.select("a").get(0);
            String downUrl = "http://www.cnki.net" +  downLink.attr("href").trim();
            //如果没有作者那就不要下载这篇文档
            if(authorsA != null && authorsA.length > 0) {// && journalName.equals(journal)
                StringBuilder filePath = new StringBuilder();
                String filename = StringUtil.standardFileName(docPDF.getName());
                File txtInfo = new File(filePath.append("/home/yjh/pdfDownload/").append(docPDF.getJournal()).append("/").append(filename).append(".txt").toString());
                filePath.setLength(0);
                if (needPDF) {
                    //下载pdf文件
                    File pdfDoc = new File(filePath.append("/home/yjh/pdfDownload/").append(docPDF.getJournal()).append("/").append(filename).append(".pdf").toString());
                    filePath.setLength(0);

                    if (!pdfDoc.exists()) {
                        pdfDoc.getParentFile().mkdirs();
                        pdfDoc.createNewFile();
                    }
                    if (!txtInfo.exists()) {
                        txtInfo.getParentFile().mkdirs();
                        txtInfo.createNewFile();
                    }

                    ResponseData pdfResponse = requester.createExample(downUrl)
                            .setRedirectAuto(false)
                            .addHeader("Referer", refer)
                            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .addHeader("Host", "www.cnki.net")
                            .addHeader("Connection", "keep-alive")
                            .addParam("password", "")
                            .addParam("username", "")
                            .doPost();
                    String downUrl2 = pdfResponse.getLocation();
                    ResponseData pdfResponse2 = requester.createExample(downUrl2)
                            .setRedirectAuto(false)
                            .addHeader("Referer", refer)
                            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .addHeader("Host", "www.cnki.net")
                            .addHeader("Connection", "keep-alive")
                            .addParam("password", "")
                            .addParam("username", "")
                            .doPost();
                    String downUrl3 = pdfResponse2.getLocation();
                    if (StringUtils.isEmpty(downUrl3)) {
                        if (pdfResponse2.getBody().contains("当前用户并发数已满！")) {
                            throw new ConcurrentException();
                        }
                    }
                    ResponseData pdfResponse3 = requester.createExample(downUrl3)
                            .setRedirectAuto(false)
                            .addHeader("Referer", refer)
                            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .addHeader("Connection", "keep-alive")
                            .doGet();
                    String downUrl4 = pdfResponse3.getLocation();
                    requester.createExample(downUrl4)
                            .setRedirectAuto(true)
                            .addHeader("Referer", refer)
                            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                            .addHeader("Connection", "keep-alive")
                            .doDownload(pdfDoc);
                }
                PrintWriter out = new PrintWriter(txtInfo);
                out.println(docPDF);
                out.close();
            } else {
                System.out.println("跳过非文献下载项!");
                TimeUnit.SECONDS.sleep(2);
                return null;
            }
        } else {
            System.out.println("downLinks error!");
        }
        return docPDF;
    }


    public static void main(String[] args) throws Exception {
        SearchCondition condition = new SearchCondition();
        condition.setStartDate("2006-01-01");
        condition.setEndDate("2015-12-31");
        condition.setJournal("中国图书馆学报"); //已经下完
        condition.setNeedPDF(false);
        DownLoadPDF.downloadBySearch(condition, 8, 18);
        condition.setNeedPDF(true);
        condition.setJournal("图书情报工作");
        DownLoadPDF.downloadBySearch(condition, 1, 1);
        condition.setJournal("情报理论与实践");
        DownLoadPDF.downloadBySearch(condition, 1, 1);
    }
}
