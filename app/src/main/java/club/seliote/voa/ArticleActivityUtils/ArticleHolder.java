package club.seliote.voa.ArticleActivityUtils;

import java.util.Date;
import java.util.List;

public class ArticleHolder {

    private List<String> englishList;
    private List<String> chineseList;

    public ArticleHolder(List<String> english, List<String> chinese) {
        englishList = english;
        chineseList = chinese;
    }

    public List<String> getEnglishList() {
        return englishList;
    }

    public List<String> getChineseList() {
        return chineseList;
    }
}
