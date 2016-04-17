package com.yjh.citation.anlysis.filter;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.yjh.citation.base.Filter;
import com.yjh.citation.base.FilterChain;
import com.yjh.citation.base.model.Document;
import com.yjh.citation.base.model.Reference;
import com.yjh.citation.base.model.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yjh on 16-4-11.
 */
public class ParticipleFilter implements Filter {
    private final static Logger logger = LogManager.getLogger();

    private String basePath;
    private String stopWordPath;
    private int charsetType;

    private String libPath;
    private CLibrary tool;
    private StringBuilder buffer = new StringBuilder();

    private int maxKeyLimit = Integer.MAX_VALUE;
    private static final Pattern PATTERN = Pattern.compile("([^#]+?)/([^#]+?)/([^#]+?)/(\\d+?)#");

    @Override
    public void doFilter(Document document, FilterChain chain) {
        Matcher matcher = PATTERN.matcher("");
        //title
        matcher.reset(tool.NLPIR_GetKeyWords(document.getTitle(), maxKeyLimit, true));
        handleMatcher(document, matcher, Token.Site.TITLE, chain);

        //abstract
        matcher.reset(tool.NLPIR_GetKeyWords(document.getAbstract_(), maxKeyLimit, true));
        handleMatcher(document, matcher, Token.Site.ABSTRACT, chain);

        //context
        matcher.reset(tool.NLPIR_GetKeyWords(document.getContext(), maxKeyLimit, true));
        handleMatcher(document, matcher, Token.Site.CONTEXT, chain);

        //references
        buffer.setLength(0);
        if (!chain.getContext().isRefFWeight())
            for (Reference reference : document.getReferenceList())
                buffer.append(reference.getTitle());
        else
            for (Reference reference : document.getReferenceList())
                for (int i = 0; i < reference.getCount(); i++)
                    buffer.append(reference.getTitle());
        matcher.reset(tool.NLPIR_GetKeyWords(buffer.toString(), maxKeyLimit, true));
        handleMatcher(document, matcher, Token.Site.REFERENCE, chain);

        chain.doFilter(document);
    }

    private void handleMatcher(Document document, Matcher matcher, Token.Site site, FilterChain chain) {
        while (matcher.find()) {
            String tokenWord = matcher.group(1);
            Token token;
            if ((token = document.hasToken(tokenWord)) == null) {
                token = new Token();
                token.setWord(matcher.group(1));
                document.addToken(token);
            }
            int count = Integer.parseInt(matcher.group(4));
            int[] tfs = token.getTfs();
            if (Token.Site.CONTEXT == site) {
                int temp = tfs[0] + tfs[1];
                count = count > temp ? count - temp : 0;
            }
            token.setTf(site, count);
            document.addToken(token);
        }
    }

    @Override
    public void destroy() {
        tool.NLPIR_Exit();
    }

    @Override
    public void init() {
        basePath = "/home/yjh/dms/nlp/NLPIR";
        stopWordPath = "/home/yjh/dms/nlp/stop_word.txt";
        charsetType = 1;
        libPath = "/home/yjh/dms/nlp/libNLPIR.so";
        tool = (CLibrary) Native.loadLibrary(libPath, CLibrary.class);

        int init_flag = tool.NLPIR_Init(basePath, charsetType, "0");

        //TODO 添加自定义用户词典

        int stopWordCount = tool.NLPIR_ImportKeyBlackList(stopWordPath);

        tool.NLPIR_SaveTheUsrDic();

        if (0 == init_flag) {
            logger.error(tool.NLPIR_GetLastErrorMsg());
        }
    }

    public interface CLibrary extends Library {

        int NLPIR_Init(String sDataPath, int encoding,
                       String sLicenceCode);

        String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

        String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit,
                                 boolean bWeightOut);
        String  NLPIR_GetNewWords(String sLine, int maxLen, boolean bWeightOut);
        String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit,
                                     boolean bWeightOut);
        int NLPIR_AddUserWord(String sWord);//add by qp 2008.11.10
        int NLPIR_DelUsrWord(String sWord);//add by qp 2008.11.10
        int NLPIR_SaveTheUsrDic();
        int NLPIR_ImportKeyBlackList(String filename);
        String NLPIR_GetLastErrorMsg();
        void NLPIR_Exit();
    }

    public static void main(String[] args) {
        Filter filter = new ParticipleFilter();
        filter.init();
        Document document = new Document();
        filter.doFilter(document, new FilterChain(null));

    }
}
