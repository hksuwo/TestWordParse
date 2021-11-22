package com.example.demo.utils;

import com.example.demo.entity.Question;
import com.example.demo.entity.QuestionTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.BeanUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordParseUtil {

    /**
     * 根据路径获取试题列表
     *
     * @param path
     * @return
     */
    public static List<Question> getQuestionList(String path) throws IOException {
        ArrayList<Question> list = new ArrayList<>();

        //读取试卷
        XWPFDocument doc = new XWPFDocument(POIXMLDocument.openPackage(path));
        List<IBodyElement> paragraphs = doc.getBodyElements();

        // 获取试卷标题，如:2021年福建省公务员录用考试《行测》卷
        String title = ((XWPFParagraph) paragraphs.get(0)).getParagraphText();
        for (int i = 1; i < paragraphs.size(); i++) {
            //首先匹配大题(列如:一、常识判断（每题0.8分)
            String paragraphText = ((XWPFParagraph) paragraphs.get(i)).getParagraphText();
            boolean match = matchQuestionType(paragraphText);
            // 表示匹配到了大题，例如： 一、常识判断（每题0.8分）
            if (match) {
                Double score = getScore(paragraphText);
                Integer typeId = getQuestionTypeId(paragraphText);
                i = getChoiceQuestionList(i, typeId, score, paragraphs, list);
            } else {
                //如果没有匹配到指定格式的大题,且内容不为空行,则认为大题格式错误
                System.out.println(paragraphText);
            }
        }
        return list;
    }

    /**
     * 判断字符串是否含有标识符
     *
     * @param str
     * @return
     */
    public static boolean matchIdentifier(String str) {
        Pattern p = Pattern.compile(QuestionTypeEnum.IDENTIFIER_REGEX.getType());
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


    /**
     * 获取大题类型对应的id
     *
     * @return
     */
    public static Integer getQuestionTypeId(String str) {
        //判断所给的大题信息是否符合格式
        Pattern p = Pattern.compile(QuestionTypeEnum.TYPE_REGEX.getType());
        Matcher m = p.matcher(str);
        if (m.find()) {
            String group = m.group();
            //返回常识判断对应的id
            if (QuestionTypeEnum.COMMON_SENSE_JUDGEMENT.getType().equals(group)) {
                return QuestionTypeEnum.COMMON_SENSE_JUDGEMENT.getCode();
            }
            //返回言语理解与表达对应的id
            else if (QuestionTypeEnum.UNDERSTANDING_AND_EXPRESSION.getType().equals(group)) {
                return QuestionTypeEnum.UNDERSTANDING_AND_EXPRESSION.getCode();
            }
            //返回数量关系对应的id
            else if (QuestionTypeEnum.QUANTITY_RELATIONSHIP.getType().equals(group)) {
                return QuestionTypeEnum.QUANTITY_RELATIONSHIP.getCode();
            }
            //返回判断推理对应的id
            else if (QuestionTypeEnum.JUDGEMENT_AND_REASONING.getType().equals(group)) {
                return QuestionTypeEnum.JUDGEMENT_AND_REASONING.getCode();
            }
            //返回资料分析对应的id
            else if (QuestionTypeEnum.DATA_ANALYSIS.getType().equals(group)) {
                return QuestionTypeEnum.DATA_ANALYSIS.getCode();
            }
        }
        return 0;
    }

    /**
     * 判断题目类型是否符合指定格式
     *
     * @param str
     * @return
     */
    public static boolean matchQuestionType(String str) {
        Pattern p = Pattern.compile(QuestionTypeEnum.QUESTION_TYPE_REGEX.getType());
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 获取题目类型对应的试题分数
     *
     * @param str
     * @return
     */
    public static Double getScore(String str) {
        Pattern p = Pattern.compile(QuestionTypeEnum.SCORE_REGEX.getType());
        Matcher m = p.matcher(str);
        if (m.find()) {
            try {
                return Double.parseDouble(m.group());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 获取某个大题内的试题列表
     *
     * @param i
     * @param score
     * @param paragraphs
     * @param list
     * @return
     */
    public static int getChoiceQuestionList(int i, Integer typeId, Double score, List<IBodyElement> paragraphs, List<Question> list) {
        i++;
        for (i = i; i < paragraphs.size(); i++) {
            //首先匹配大题(列如:一、常识判断，共20题。（每题0.8分)
            String contentType = getContent(paragraphs.get(i), new StringBuilder());
            //如果为空直接返回,不做处理
            if (StringUtils.isBlank(contentType)) {
                return i;
            }
            // 如果匹配到大题则返回
            boolean match = matchQuestionType(contentType);
            if (match) {
                i--;
                return i;
            }

            //获取题目中的【题文】信息
            String questionTopic = getContent(paragraphs.get(i), new StringBuilder());
            if (questionTopic.contains(QuestionTypeEnum.TOPIC.getType())) {
                i++;
                //截取掉【题文】标识符
                questionTopic = questionTopic.replaceAll(QuestionTypeEnum.TOPIC.getType(), "");
                //如果截取掉【题文】标识符后,还存在其他标识符,视为文档格式错误
                if (matchIdentifier(questionTopic)) {
                    throw new RuntimeException();
                }
                //如果标签不是以【选项】结尾，拼接多行【题文】信息
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.CHOICE.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    //如果截取出来的字符串仍含有标识符,视为文档格式错误
                    if (matchIdentifier(content)) {
                        throw new RuntimeException();
                    }
                    questionTopic += content;
                    i++;
                }
            }

            //获取题目中的【选项】信息
            String choice = getContent(paragraphs.get(i), new StringBuilder());
            if (choice.contains(QuestionTypeEnum.CHOICE.getType())) {
                i++;
                //截取掉【选项】标识符
                choice = choice.replaceAll(QuestionTypeEnum.CHOICE.getType(), "");
                //如果截取掉【选项】标识符后,还存在其他标识符,视为文档格式错误
                if (matchIdentifier(choice)) {
                    throw new RuntimeException();
                }
                //如果标签不是以【答案】结尾，拼接多行【选项】信息
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.ANSWER.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    //如果截取出来的字符串仍含有标识符,视为文档格式错误
                    if (matchIdentifier(content)) {
                        throw new RuntimeException();
                    }
                    choice += content;
                    i++;
                }
            }

            //获取题目的【答案】信息
            String answer = getContent(paragraphs.get(i), new StringBuilder());
            if (answer.contains(QuestionTypeEnum.ANSWER.getType())) {
                i++;
                //截取掉【答案】标识符
                answer = answer.replaceAll(QuestionTypeEnum.ANSWER.getType(), "");
                //如果截取掉【答案】标识符后,还存在其他标识符,视为文档格式错误
                if (matchIdentifier(answer)) {
                    throw new RuntimeException();
                }
                //如果标签不是以【解析】结尾，拼接多行【答案】信息
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.ANALYSIS.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    //如果截取出来的字符串仍含有标识符,视为文档格式错误
                    if (matchIdentifier(content)) {
                        throw new RuntimeException();
                    }
                    answer += content;
                    i++;
                }
            }

            //获取题目的【解析】信息
            String analysis = getContent(paragraphs.get(i), new StringBuilder());
            if (analysis.contains(QuestionTypeEnum.ANALYSIS.getType())) {
                i++;
                //截取掉【解析】标识符
                analysis = analysis.replaceAll(QuestionTypeEnum.ANALYSIS.getType(), "");
                //如果截取掉【解析】标识符后,还存在其他标识符,视为文档格式错误
                if (matchIdentifier(analysis)) {
                    throw new RuntimeException();
                }
                //如果标签不是以【结束】结尾，拼接多行【解析】信息
                while (!((XWPFParagraph) paragraphs.get(i)).getParagraphText().contains(QuestionTypeEnum.FINISH.getType())) {
                    String content = getContent(paragraphs.get(i), new StringBuilder());
                    //如果截取出来的字符串仍含有标识符,视为文档格式错误
                    if (matchIdentifier(content)) {
                        throw new RuntimeException();
                    }
                    analysis += content;
                    i++;
                }
            }

            //如果截取出来的字符串仍含有标识符,视为文档格式错误
//            if (matchIdentifier(questionTopic + choice + answer + analysis)) {
//                throw new RuntimeException();
//            }
            if (score == null) {
                score = 0.0;
            }
            Question q = new Question();
            q.setQuestionTopic(questionTopic);
            q.setChoice(divideChoice(choice));
            q.setAnswer(answer);
            q.setTypeId(typeId);
            q.setSort(list.size() + 1);
            q.setAnalysis(analysis);
            q.setScore(score);
            list.add(q);
        }
        return i;
    }

    /**
     * 用\n分隔答案信息
     *
     * @param choice
     * @return
     */
    public static String divideChoice(String choice) {
        String choiceA = choice.substring(choice.indexOf("A、"), choice.indexOf("B、")).trim();
        String choiceB = choice.substring(choice.indexOf("B、"), choice.indexOf("C、")).trim();
        String choiceC = choice.substring(choice.indexOf("C、"), choice.indexOf("D、")).trim();
        String choiceD = choice.substring(choice.indexOf("D、")).trim();
        return choiceA + "\n" + choiceB + "\n" + choiceC + "\n" + choiceD;
    }

    /**
     * 获取一行的内容
     *
     * @param body
     * @param content
     * @return
     */
    public static String getContent(IBodyElement body, StringBuilder content) {
        //将word中的图片存储到本地磁盘中
        ImageParseUtil imageParse = new ImageParseUtil("D:/others/test/", "D:/others/test/");
        //拿到所有的段落的表格，这两个属于同级无素
        if (body.getElementType().equals(BodyElementType.PARAGRAPH)) {
            //处理段落中的文本以及公式图片
            handleParagraph(content, body, imageParse);
        }
        return content.toString();
    }

    /**
     * 处理段落
     *
     * @param content
     * @param body
     * @param imageParser
     */
    public static void handleParagraph(StringBuilder content, IBodyElement body, ImageParseUtil imageParser) {
        XWPFParagraph p = (XWPFParagraph) body;
        if (p.isEmpty() || p.isWordWrap() || p.isPageBreak()) {
            return;
        }
        ParagraphChildUtil runOrMaths = new ParagraphChildUtil(p);
        List<Object> childList = runOrMaths.getChildList();

        for (Object child : childList) {
            if (child instanceof XWPFRun) {
                //处理段落中的文本以及图片

                handleParagraphRun(content, (XWPFRun) child, imageParser);
            }
        }
    }

    /**
     * 处理图片
     *
     * @param content
     * @param run
     * @param imageParser
     */
    private static void handleParagraphRun(StringBuilder content, XWPFRun run, ImageParseUtil imageParser) {
        // 有内嵌的图片
        List<XWPFPicture> pics = run.getEmbeddedPictures();
        if (pics != null && pics.size() > 0) {

            handleParagraphRunsImage(content, pics, imageParser);
        } else {
            //纯文本直接获取
            content.append(run.toString());
        }
    }

    /**
     * 获取图片
     *
     * @param content
     * @param pics
     * @param imageParser
     */
    private static void handleParagraphRunsImage(StringBuilder content, List<XWPFPicture> pics, ImageParseUtil imageParser) {

        for (XWPFPicture pic : pics) {
            //这里已经获取好了
            String path = imageParser.parse(pic.getPictureData().getData(),
                    pic.getPictureData().getFileName());

            content.append(String.format("<img src=\"%s\" />", path));
        }
    }

}
