package com.sccl.lego.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 工具类
 * @author
 */
@Slf4j
public class StringUtils
{


    private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
    private static final char[] AMP_ENCODE = "&amp;".toCharArray();
    private static final char[] LT_ENCODE = "&lt;".toCharArray();
    private static final char[] GT_ENCODE = "&gt;".toCharArray();

    public static final String[] messageIds = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
        "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
        "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

    public static final String SEPARATOR = "/";

    //private static MessageDigest digest = null;

    public static final String NEWLINE = "\r\n";


    /**
     * 替换字符串

     *
     * @param line      源字符串
     * @param oldString 被替换的子字符串
     * @param newString 新子字符串

     * @return String 新字符串
     */
    public static final String replace(String line, String oldString, String newString)
    {
        if (line == null)
        {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0)
        {

            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0)
            {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

    /**
     * 替换字符串

     *
     * @param line      源字符串
     * @param oldString 被替换的子字符串
     * @param newString 新子字符串

     * @return String 新字符串
     */
    public static final String replaceFirst(String line, String oldString, String newString)
    {
        if (line == null)
        {
            return null;
        }

        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0)
        {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;

            buf.append(line2, i, line2.length - i);
            return buf.toString();
        }

        return line;
    }

    /**
     * 替换字符串，不区分大小写
     *
     * @param line      源字符串
     * @param oldString 被替换的子字符串
     * @param newString 新子字符串

     * @return String 新字符串
     */
    public static final String replaceIgnoreCase(String line, String oldString, String newString)
    {
        if (line == null)
        {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0)
        {
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0)
            {
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            return buf.toString();
        }
        return line;
    }

    /**
     * 替换字符串，不区分大小写，并返回替换的个数

     *
     * @param line      源字符串
     * @param oldString 被替换的子字符串
     * @param newString 新子字符串

     * @param count     被替换的个数，在返回时更新这个值

     * @return 新字符串
     */
    public static final String replaceIgnoreCase(String line, String oldString, String newString,
                                                 int[] count)
    {
        if (line == null)
        {
            return null;
        }
        String lcLine = line.toLowerCase();
        String lcOldString = oldString.toLowerCase();
        int i = 0;
        if ((i = lcLine.indexOf(lcOldString, i)) >= 0)
        {
            int counter = 0;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = lcLine.indexOf(lcOldString, i)) > 0)
            {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }

    /**
     * 替换字符串，并返回替换的个数
     *
     * @param line      源字符串
     * @param oldString 被替换的子字符串
     * @param newString 新子字符串

     * @param count     被替换的个数，在返回时更新这个值

     * @return 新字符串
     */
    public static final String replace(String line, String oldString, String newString, int[] count)
    {
        if (line == null)
        {
            return null;
        }
        int i = 0;
        if ((i = line.indexOf(oldString, i)) >= 0)
        {
            int counter = 0;
            counter++;
            char[] line2 = line.toCharArray();
            char[] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(line2.length);
            buf.append(line2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while ((i = line.indexOf(oldString, i)) > 0)
            {
                counter++;
                buf.append(line2, j, i - j).append(newString2);
                i += oLength;
                j = i;
            }
            buf.append(line2, j, line2.length - j);
            count[0] = counter;
            return buf.toString();
        }
        return line;
    }

    /**
     * 将带HTML标记的字符串中的“<”“>”标记替换成“&lt;”，“&gt;”

     *
     * @param in 要替换的字符串

     * @return String 替换后的字符串

     */
    public static final String escapeHTMLTags(String in)
    {
        if (in == null)
        {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = in.toCharArray();
        int len = input.length;
        StringBuffer out = new StringBuffer((int) (len * 1.3));
        for (; i < len; i++)
        {
            ch = input[i];
            if (ch > '>')
            {
                continue;
            }
            else if (ch == '<')
            {
                if (i > last)
                {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            }
            else if (ch == '>')
            {
                if (i > last)
                {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(GT_ENCODE);
            }
        }
        if (last == 0)
        {
            return in;
        }
        if (i > last)
        {
            out.append(input, last, i - last);
        }
        return out.toString();
    }
    public static final byte[] decodeHex(String hex)
    {
        if (hex == null){
            return new byte[0];
        }


        char[] chars = hex.toCharArray();
        byte[] bytes = new byte[chars.length / 2];
        int byteCount = 0;
        for (int i = 0; i < chars.length; i += 2)
        {
            byte newByte = 0x00;
            newByte |= hexCharToByte(chars[i]);
            newByte <<= 4;
            newByte |= hexCharToByte(chars[i + 1]);
            bytes[byteCount] = newByte;
            byteCount++;
        }
        return bytes;
    }
    private static final byte hexCharToByte(char ch)
    {
        switch (ch)
        {
            case '0':
                return 0x00;
            case '1':
                return 0x01;
            case '2':
                return 0x02;
            case '3':
                return 0x03;
            case '4':
                return 0x04;
            case '5':
                return 0x05;
            case '6':
                return 0x06;
            case '7':
                return 0x07;
            case '8':
                return 0x08;
            case '9':
                return 0x09;
            case 'a':
                return 0x0A;
            case 'b':
                return 0x0B;
            case 'c':
                return 0x0C;
            case 'd':
                return 0x0D;
            case 'e':
                return 0x0E;
            case 'f':
                return 0x0F;
        }
        return 0x00;
    }

    public static String encodeBase64(String data)
    {
        return encodeBase64(data.getBytes());
    }

    public static String encodeBase64(byte[] data)
    {
        if (data == null){
            return "";
        }

        int c;
        int len = data.length;
        StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
        for (int i = 0; i < len; ++i)
        {
            c = (data[i] >> 2) & 0x3f;
            ret.append(cvt.charAt(c));
            c = (data[i] << 4) & 0x3f;
            if (++i < len)
                c |= (data[i] >> 4) & 0x0f;

            ret.append(cvt.charAt(c));
            if (i < len)
            {
                c = (data[i] << 2) & 0x3f;
                if (++i < len)
                    c |= (data[i] >> 6) & 0x03;

                ret.append(cvt.charAt(c));
            }
            else
            {
                ++i;
                ret.append((char) fillchar);
            }

            if (i < len)
            {
                c = data[i] & 0x3f;
                ret.append(cvt.charAt(c));
            }
            else
            {
                ret.append((char) fillchar);
            }
        }
        return ret.toString();
    }

    /**
     * Decodes a base64 String.
     *
     * @param data a base64 encoded String to decode.
     * @return the decoded String.
     */
    public static String decodeBase64(String data)
    {
        return decodeBase64(data.getBytes());
    }

    /**
     * Decodes a base64 aray of bytes.
     *
     * @param data a base64 encode byte array to decode.
     * @return the decoded String.
     */
    public static String decodeBase64(byte[] data)
    {
        if (data == null){
            return "";
        }


        int c, c1;
        int len = data.length;
        StringBuffer ret = new StringBuffer((len * 3) / 4);
        for (int i = 0; i < len; ++i)
        {
            c = cvt.indexOf(data[i]);
            ++i;
            c1 = cvt.indexOf(data[i]);
            c = ((c << 2) | ((c1 >> 4) & 0x3));
            ret.append((char) c);
            if (++i < len)
            {
                c = data[i];
                if (fillchar == c)
                    break;

                c = cvt.indexOf((char) c);
                c1 = ((c1 << 4) & 0xf0) | ((c >> 2) & 0xf);
                ret.append((char) c1);
            }

            if (++i < len)
            {
                c1 = data[i];
                if (fillchar == c1)
                    break;

                c1 = cvt.indexOf((char) c1);
                c = ((c << 6) & 0xc0) | c1;
                ret.append((char) c);
            }
        }
        return ret.toString();
    }

    private static final int fillchar = '=';
    private static final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvwxyz"
        + "0123456789+/";

    /**
     * Converts a line of text into an array of lower case words using a
     * BreakIterator.wordInstance(). <p>
     * <p/>
     * This method is under the Jive Open Source Software License and was
     * written by Mark Imbriaco.
     *
     * @param text a String of text to convert into an array of words
     * @return text broken up into an array of words.
     */
    public static final String[] toLowerCaseWordArray(String text)
    {
        if (text == null || text.length() == 0)
        {
            return new String[0];
        }

        List<String> wordList = new ArrayList<String>();
        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(text);
        int start = 0;

        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
            .next())
        {
            String tmp = text.substring(start, end).trim();
            // Remove characters that are not needed.
            tmp = replace(tmp, "+", "");
            tmp = replace(tmp, "/", "");
            tmp = replace(tmp, "\\", "");
            tmp = replace(tmp, "#", "");
            tmp = replace(tmp, "*", "");
            tmp = replace(tmp, ")", "");
            tmp = replace(tmp, "(", "");
            tmp = replace(tmp, "&", "");
            if (tmp.length() > 0)
            {
                wordList.add(tmp);
            }
        }
        return (String[]) wordList.toArray(new String[wordList.size()]);
    }

    /**
     * Intelligently chops a String at a word boundary (whitespace) that occurs
     * at the specified index in the argument or before. However, if there is a
     * newline character before <code>length</code>, the String will be chopped
     * there. If no newline or whitespace is found in <code>string</code> up to
     * the index <code>length</code>, the String will chopped at <code>length</code>.
     * <p/>
     * For example, chopAtWord("This is a nice String", 10) will return
     * "This is a" which is the first word boundary less than or equal to 10
     * characters into the original String.
     *
     * @param string the String to chop.
     * @param length the index in <code>string</code> to start looking for a
     *               whitespace boundary at.
     * @return a substring of <code>string</code> whose length is less than or
     *         equal to <code>length</code>, and that is chopped at whitespace.
     */
    public static final String chopAtWord(String string, int length)
    {
        if (string == null)
        {
            return string;
        }

        char[] charArray = string.toCharArray();
        int sLength = string.length();
        if (length < sLength)
        {
            sLength = length;
        }

        // First check if there is a newline character before length; if so,
        // chop word there.
        for (int i = 0; i < sLength - 1; i++)
        {
            // Windows
            if (charArray[i] == '\r' && charArray[i + 1] == '\n')
            {
                return string.substring(0, i + 1);
            }
            // Unix
            else if (charArray[i] == '\n')
            {
                return string.substring(0, i);
            }
        }
        // Also check boundary case of Unix newline
        if (charArray[sLength - 1] == '\n')
        {
            return string.substring(0, sLength - 1);
        }

        // Done checking for newline, now see if the total string is less than
        // the specified chop point.
        if (string.length() < length)
        {
            return string;
        }

        // No newline, so chop at the first whitespace.
        for (int i = length - 1; i > 0; i--)
        {
            if (charArray[i] == ' ')
            {
                return string.substring(0, i).trim();
            }
        }

        return string.substring(0, length);
    }

    /**
     * 将字符串中的“<”等字符串替换成“&lt;”等字符串

     *
     * @param string String 要替换的字符串

     * @return String 替换后的字符串

     */
    public static final String escapeForXML(String string)
    {
        if (string == null)
        {
            return null;
        }
        char ch;
        int i = 0;
        int last = 0;
        char[] input = string.toCharArray();
        int len = input.length;
        StringBuffer out = new StringBuffer((int) (len * 1.3));
        for (; i < len; i++)
        {
            ch = input[i];
            if (ch > '>')
            {
                continue;
            }
            else if (ch == '<')
            {
                if (i > last)
                {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(LT_ENCODE);
            }
            else if (ch == '&')
            {
                if (i > last)
                {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(AMP_ENCODE);
            }
            else if (ch == '"')
            {
                if (i > last)
                {
                    out.append(input, last, i - last);
                }
                last = i + 1;
                out.append(QUOTE_ENCODE);
            }
        }
        if (last == 0)
        {
            return string;
        }
        if (i > last)
        {
            out.append(input, last, i - last);
        }
        return out.toString();
    }

    /**
     * 将字符串中的“&lt;” 等特殊字符串替换成“<”等正常的字符串
     *
     * @param string 要替换的字符串

     * @return String 替换后的字符串

     */
    public static final String unescapeFromXML(String string)
    {
        string = replace(string, "&lt;", "<");
        string = replace(string, "&gt;", ">");
        string = replace(string, "&quot;", "\"");
        return replace(string, "&amp;", "&");
    }

    /**
     * 获得字符串大小：单位k，精确到小数点后2位

     *
     * @param string
     * @return
     */
    public static final float getStringSize(String string)
    {
        float size = 0f;
        if (string.length() > 0)
        {
            size = string.length() / 1024f;
        }

        size = size * 100;
        if (size > (int) size)
        {
            size = size + 1;
        }
        size = size / 100f;

        DecimalFormat format = new DecimalFormat("##.##");

        return Float.parseFloat(format.format(size));
    }

    private static final char[] zeroArray = "0000000000000000".toCharArray();

    /**
     * 将字符串的左边补充0
     *
     * @param string String 要补充的字符串

     * @param length int 补0后的位数，最长16位

     * @return String 补0后的字符串

     */
    public static final String zeroPadString(String string, int length)
    {
        if (string == null || string.length() > length)
        {
            return string;
        }
        StringBuffer buf = new StringBuffer(length);
        buf.append(zeroArray, 0, length - string.length()).append(string);
        return buf.toString();
    }

    /**
     * 将日期转换成毫秒
     *
     * @param date Date 要转换的日期
     * @return String 转换后的毫秒数字符串
     */
    public static final String dateToMillis(Date date)
    {
        return zeroPadString(Long.toString(date.getTime()), 15);
    }

    /**
     * 清除html标签
     *
     * @return String
     */
    public static String cleanAndPaste(String html)
    {
        String result = "";
        int strLen = html.split("\\>").length;
        String[] tmpStrOut = new String[strLen];
        String[] tmpStrIn = new String[2];
        tmpStrOut = html.split("\\>");
        for (int i = 0; i < strLen; i++)
        {
            tmpStrIn = tmpStrOut[i].split("\\<");
            result += tmpStrIn[0];
        }
        result = replace(result, "&nbsp;", "");
        return result;
    }

    /**
     * 递归trim
     * @param baseStr 目标字符串
     * @param trimStr 要替换的字符串
     * @return
     */
    public static String trim(String baseStr, String trimStr)
    {
        if (baseStr == null || baseStr.trim().equals(""))
            return "";
        if (trimStr == null || trimStr.trim().equals(""))
            return baseStr;
        baseStr = baseStr.trim();
        trimStr = trimStr.trim();
        if (baseStr.length() < trimStr.length())
            return baseStr;
        String subStr = baseStr.substring(baseStr.length() - trimStr.length());
        if (subStr.equals(trimStr))
            return trim(baseStr.substring(0, baseStr.length() - trimStr.length()), trimStr);
        return baseStr;
    }

    /**
     * 确认一个字符串是否为空串
     * @param pStr
     * @return
     * @author
     */
    public static final boolean isEmpty(String pStr)
    {
        if (pStr == null)
            return true;
        return pStr.trim().equals("");

    }
    public static final boolean isNotEmpty(String pStr){
        return !isEmpty(pStr);
    }
    /**
     * @函数功能: BCD码转为10进制串(阿拉伯数据)
     * @输入参数: BCD码
     * @输出结果: 10进制串
     */
    public static String bcd2Str(byte[] bytes)
    {
        StringBuffer temp = new StringBuffer(bytes.length * 2);

        for (int i = 0; i < bytes.length; i++)
        {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1)
            : temp.toString();
    }
    public static String binary2Hex2(byte[] bys)
    {
        if (bys == null || bys.length < 1)
            return null;

        StringBuffer sb = new StringBuffer(100);

        for (int i = 0; i < bys.length; i++)
        {
            if (bys[i] >= 16)
                sb.append(Integer.toHexString(bys[i]));
            else if (bys[i] >= 0)
                sb.append("0" + Integer.toHexString(bys[i]));
            else
                sb.append(Integer.toHexString(bys[i]).substring(6, 8));
        }

        return sb.toString();
    }
    /**
     * 获取版本字符串
     * @param version
     * @return
     */
    public static String getVersionStr(byte version){
        int highVersion = version>>4;
        int lowVersion = 0x0f & version;
        return Integer.toHexString(highVersion) + "." + Integer.toHexString(lowVersion);
    }
    /**
     * 将二进制数据进行md5加密
     *
     * @param bys 待加密的二进制数据流
     * @return md5加密后的二进制数据流
     * @throws NoSuchAlgorithmException 如果java未提供md5加密方法则抛出此异常
     */
    public static byte[] md5Encode(byte[] bys) throws NoSuchAlgorithmException
    {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(bys);
        byte[] md5out = md5.digest();
        return md5out;
    }
    /**
     * 将字符串格式化为固定宽度，不够宽度时，左边填充空格
     * @param source
     * @param len
     * @return
     */
    public static String lpad(String source,int len){
        return lpad(source,len,' ');
    }
    /**
     * 将字符串格式化为固定宽度，不够宽度时，左边填充
     * @param source
     * @param len
     * @param fillStr
     * @return
     */
    public static String lpad(String source,int len,char fillStr){
        String str = source == null?"":source;
        while(str.length() < len){
            str = fillStr + str;
        }
        return str;
    }
    /**
     * 将字符串格式化为固定宽度，不够宽度时，右边填充空格
     * @param source
     * @param len
     * @return
     */
    public static String rpad(String source,int len){
        return rpad(source,len,' ');
    }
    /**
     * 将字符串格式化为固定宽度，不够宽度时，右边填充
     * @param source
     * @param len
     * @param fillStr
     * @return
     */
    public static String rpad(String source,int len,char fillStr){
        String str = source == null?"":source;
        while(str.length() < len){
            str += fillStr;
        }
        return str;
    }
    /**
     * 将整数转为指定长度的字符串,采用左填充0
     * @param ival
     * @param len
     * @return
     */
    public static final String lpad(int ival,int len){
        return lpad(ival,len,'0');
    }
    /**
     * 将整数转为指定长度的字符串,采用左填充
     * @param ival 需要转换的整数
     * @param len 长度
     * @return
     */
    public static final String lpad(int ival,int len,char fillStr){
        return lpad(Integer.toString(ival),len,fillStr);
    }
    /**
     * 将整数转化为指定字符串长度，采用右填充0
     * @param ival
     * @param len
     * @return
     */
    public static final String rpad(int ival,int len){
        return rpad(ival,len,'0');
    }
    /**
     * 将整信转为指字长度的字符串，采用右填充
     * @param ival
     * @param len
     * @param fillStr
     * @return
     */
    public static final String rpad(int ival,int len,char fillStr){
        return rpad(Integer.toString(ival),len,fillStr);
    }

    /**
     * 将长整信转为指字长度的字符串，采用左填充0
     * @param lval
     * @param len
     * @return
     */
    public static final String lpad(long lval,int len){
        return lpad(lval,len,'0');
    }
    /**
     * 将长整信转为指字长度的字符串，采用左填充
     * @param lval
     * @param len
     * @param fillStr
     * @return
     */
    public static final String lpad(long lval,int len,char fillStr){
        return lpad(Long.toString(lval),len,fillStr);
    }
    /**
     * 将整数转为指定长度的字符串,采用右填充0
     * @param lval
     * @param len
     * @return
     */
    public static final String rpad(long lval,int len){
        return rpad(lval,len,'0');
    }
    /**
     *  将整数转为指定长度的字符串,采用右填充
     * @param lval
     * @param len
     * @param fillStr
     * @return
     */
    public static final String rpad(long lval,int len,char fillStr){
        return rpad(Long.toString(lval),len,fillStr);
    }
    public static int strToInt(String str){
        int ret = 0;
        if (str != null){
            try{
                ret = Integer.parseInt(str.trim());
            }catch(NumberFormatException e){
                log.debug("格式化数字异常-->" + ret + "-->" + e.getMessage());
                ret = 0;
            }
        }
        return ret;
    }
    /**
     * 检查是否是有效的ip字符串
     * @param ipAddress
     * @return
     */
    public static boolean isLegalIP(String ipAddress) {
        //String ip = "([1-9]|[1-9]//d|1//d{2}|2[0-4]//d|25[0-5])(//.(//d|[1-9]//d|1//d{2}|2[0-4]//d|25[0-5])){3}";
        String ip = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }
    /**
     * 检查是否是有效的电子邮箱
     * @param mail
     * @return
     */
    public static boolean isLegalMail(String mail) {
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(mail);
        return matcher.matches();

    }
    public static String trim(String str)
    {
        if (str == null)
        {
            return "";
        }
        return str.trim();
    }

    public static String toString(Object obj)
    {
        if(obj == null)
        {
            return "";
        }
        return trim(obj.toString());
    }

    public static String allToLowerCase(String str)
    {
        if (isEmpty(str))
            return str;
        return str.toLowerCase();
    }

    public static String firstToLowerCase(String str)
    {
        return Character.toLowerCase(str.charAt(0)) + str.substring(1, str.length());
    }

    public static String firstToUpperCase(String str)
    {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length());
    }
    public static String replace(String str)
    {
        if (isEmpty(str))
            return str;
        int index = 0;
        while ((index < str.length()) && (index != -1))
        {
            index = str.indexOf(".", index);
            if (index >= 0)
            {
                str = str.substring(0, index) + "/" + str.substring(index + 1, str.length());
            }
        }
        return str;
    }

    public static String replaceToJava(String name)
    {
        if (isEmpty(name))
            return name;
        if (name.indexOf("_") == -1)
        {
            return firstToUpperCase(name);
        }
        String[] ss = name.split("_");
        StringBuffer retValue = new StringBuffer();
        for (String s : ss)
        {
            retValue.append(firstToUpperCase(s));
        }
        return retValue.toString();
    }

    public static String replaceToJavaAttribute(String name)
    {
        String str = replaceToJava(name);
        return firstToLowerCase(str);
    }

    public static String replaceUrl(String str, Object ... obj)
    {
        if (isEmpty(str))
            return str;
        if ((obj != null) && (obj.length > 0))
        {
            for (int i = 0; i < obj.length; i++)
            {
                str = str.replaceAll("\\{" + (i + 1) + "\\}", String.valueOf(obj[i]));
            }
        }
        return str;
    }

    public static String replaceParam(String str, Object ... obj)
    {
        if (isEmpty(str))
            return str;
        try
        {
            if ((obj != null) && (obj.length > 0))
            {
                for (int i = 0; i < obj.length; i++)
                {
                    String value = trim(String.valueOf(obj[i]));
                    str = str.replace("{" + i + "}", value);
                }
            }
        }
        catch (Exception e)
        {
            log.debug("字符串替换异常-->" + e.getMessage());
        }
        return str;
    }
    public static String replaceSqlUrl(String str, List<String> list)
    {
        if (isEmpty(str))
            return str;
        if ((list == null) || (list.size() == 0))
            return str;
        String[] ret = str.split("\\?");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ret.length - 1; i++)
        {
            sb.append(ret[i]);
            sb.append("'");
            sb.append((String) list.get(i));
            sb.append("'");
        }
        sb.append(ret[(ret.length - 1)]);
        return sb.toString();
    }

    public static String addSeparator(String path)
    {
        if (isEmpty(path))
            return path;
        if ((!path.endsWith("/")) && (!path.endsWith("\\")))
        {
            path = path + "/";
        }
        return path;
    }

    public static String removeFirstSeparator(String path)
    {
        if (isEmpty(path))
            return path;
        path = trim(path);
        if (path.startsWith("/") || path.startsWith("\\"))
        {
            path = path.substring(1);
        }
        return path;
    }
    public static String removeSeparator(String path)
    {
        if (isEmpty(path))
            return path;
        path = replaceAllSeparator(trim(path));
        if (path.startsWith("/"))
        {
            path = path.substring(1);
        }
        if (path.endsWith("/"))
        {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    public static String subString(String path, String prefix)
    {
        if ((isEmpty(path)) || (isEmpty(prefix)))
            return "";
        int beginIndex = path.indexOf(prefix);
        if (beginIndex == -1)
            return "";
        int endIndex = path.indexOf(prefix, beginIndex + 1);
        if (endIndex == -1)
            return "";
        return path.substring(beginIndex + 1, endIndex);
    }
    /**
     * 将所有反斜杠（\）替换成正斜杠（/）
     * @param path
     * @return
     */
    public static String replaceAllSeparator(String path)
    {
        String separator = "/";
        path = path.replaceAll("\\\\", separator);
        return path;
    }

    /**
     * java去除字符串中的空格、回车、换行符、制表符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;

    }

    /**
     * 随即生成指定位数的含验证码字符串
     *
     * @author Peltason
     *
     * @date 2007-5-9
     *            指定生成验证码位数
     * @return String
     */
/*	public static String random(int bit) {
		if (bit == 0)
			bit = 6; // 默认6位
		// 因为o和0,l和1很难区分,所以,去掉大小写的o和l
		String str = "";
		str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";// 初始化种子
		return RandomStringUtils.random(bit, str);// 返回6位的字符串
	}*/


    public static String replaceAllChina(String path)
    {
        if (isEmpty(path))
        {
            return "";
        }

        try
        {
            path = URLEncoder.encode(path, "UTF-8");
            return path.replaceAll("%3A", ":").replaceAll("%2F", "/");
        }
        catch (Exception e)
        {
            log.debug("字符串替换异常-->" + e.getMessage());
        }
        return path;
    }
    public static String getFileName(String filePath)
    {
        try
        {
            if (filePath.indexOf("\\") != -1)
            {
                filePath = filePath.replaceAll("\\\\", "/");
            }
            filePath = filePath.substring(filePath.lastIndexOf("/") + 1);
            return filePath;
        }
        catch (Exception e)
        {
            log.debug("获取文件名异常-->" + filePath + "-->" + e.getMessage());
        }
        return filePath;
    }
    /**
     * 解析字符串（如：张三(1),李四(2)）
     * @param str
     * @return key:值(1、2)，value：张三
     */
    public static Map<String, String> parseStr(String str)
    {
        Map<String, String> retMap = new HashMap<String, String>();
        if (isNotEmpty(str))
        {
            str = str.replaceAll("（", "(").replaceAll("）", ")").replaceAll("，", ",").trim();
            String[] infos = str.split(",");
            for (String info : infos)
            {
                if (isEmpty(info))
                {
                    continue;
                }
                int start = info.indexOf("(");
                int end = info.lastIndexOf(")");
                if ((start == -1) || (end == -1))
                    continue;
                String signName = trim(info.substring(0, start));
                String sign = trim(info.substring(start + 1, end));
                retMap.put(sign, signName);
            }
        }
        return retMap;
    }
    /**
     * 解析字符串（如：张三(1),李四(2)）
     * @param str
     * @return key:值(1、2)，value：张三
     */
    public static Long[] parseStrId(String str)
    {
        Map<String, String> retMap = parseStr(str);
        List<Long> list = new ArrayList<Long>();
        Set<String> keys = retMap.keySet();
        for(String key : keys)
        {
            try
            {
                list.add(Long.parseLong(trim(key)));
            }
            catch(Exception e){
                log.debug("String数据转换成Long异常-->" + key + "-->" + e.getMessage());
            }
        }
        return list.toArray(new Long[0]);
    }


    public static boolean isNaN(String str)
    {
        try
        {
            Double.parseDouble(str);
            return false;
        }
        catch (Exception e)
        {
            log.debug("String数据转换成Double异常-->" + str + "-->" + e.getMessage());
        }
        return true;
    }

    public static String unicode2Str(String str)
    {
        StringBuffer sb = new StringBuffer();
        String[] tmp = str.split("\\\\u");
        for (String s : tmp)
        {
            if (!isEmpty(s))
            {
                char tt = (char) Integer.parseInt(s.substring(0, 4), 16);
                sb.append(tt).append(s.substring(4));
            }
            else
            {
                sb.append(s);
            }
        }
        return sb.toString();
    }

    public static String str2Unicode(String str)
    {
        StringBuffer sb = new StringBuffer();
        if (!isEmpty(str))
        {
            for (int i = 0; i < str.length(); i++)
            {
                String code = Integer.toHexString(str.charAt(i)).toUpperCase();
                while (code.length() < 4)
                {
                    code = "0" + code;
                }
                sb.append("\\u").append(code);
            }
        }
        return sb.toString();
    }
    /**
     * 将int转换成String，不足位数的时候在前面补0
     * @param value	值
     * @param len	长度
     * @return
     */
    public static String int2String(int value, int len)
    {
        String tmp = value + "";
        while(tmp.length() < len)
        {
            tmp = "0" + tmp;
        }
        return tmp;
    }
    /**
     * 判断字符串是否是数字
     * @param str
     * @return true：是数字
     */
    public static boolean isDigest(String str)
    {
        try
        {
            Long.parseLong(str);
            return true;
        }
        catch(Exception e)
        {
            log.debug("String数据转换成Long异常-->" + str + "-->" + e.getMessage());
            return false;
        }
    }
    public static boolean isNull(String str)
    {
        return (str == null) || (str.trim().length() == 0);
    }

    public static boolean isNotNull(String str)
    {
        return !isNull(str);
    }
    public static int[] converStr2Int(String str)
    {
        if(isEmpty(str))
        {
            return new int[0];
        }
        String[] strs = str.split(",");
        List<Integer> list = new ArrayList<Integer>();
        for(String ss : strs)
        {
            if(isNotEmpty(ss))
            {
                list.add(Integer.parseInt(ss.trim()));
            }
        }
        int[] ret = new int[list.size()];
        for(int i = 0; i < list.size(); i++)
        {
            ret[i] = list.get(i).intValue();
        }
        return ret;
    }
    public static List<Long> converStr2Long(String str)
    {
        List<Long> list = new ArrayList<Long>();
        if(isEmpty(str))
        {
            return list;
        }
        String[] strs = str.split(",");
        for(String ss : strs)
        {
            if(isNotEmpty(ss))
            {
                list.add(Long.parseLong(ss.trim()));
            }
        }
        return list;
    }
    public static List<String> converStr2List(String strs)
    {
        return converStr2List(strs, ",");
    }
    public static List<String> converStr2List(String strs, String split)
    {
        List<String> retList = new ArrayList<String>();
        if(isEmpty(strs))
        {
            return retList;
        }
        if(split == null)
        {
            split = ",";
        }
        String[] strTmp = strs.split(split);
        for(String ss : strTmp)
        {
            if(isNotEmpty(ss))
            {
                retList.add(ss.trim());
            }
        }
        return retList;
    }
    public static Set<String> converStr2Set(String strs)
    {
        return converStr2Set(strs, ",");
    }
    public static Set<String> converStr2Set(String strs, String split)
    {
        Set<String> retList = new HashSet<String>();
        if(isEmpty(strs))
        {
            return retList;
        }
        if(split == null)
        {
            split = ",";
        }
        String[] strTmp = strs.split(split);
        for(String ss : strTmp)
        {
            if(isNotEmpty(ss))
            {
                retList.add(ss.trim());
            }
        }
        return retList;
    }
    /**
     * 将数组转换成字符串，以逗号分隔
     * @param objs
     * @return
     */
    public static String converArray2Str(Object[] objs)
    {
        if(objs == null || objs.length == 0)
        {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for(Object obj : objs)
        {
            if(obj != null)
            {
                sb.append(obj.toString()).append(",");
            }
        }
        if(sb.length() > 0)
        {
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }
    public static String converArr2Str(int[] arrs)
    {
        if(arrs == null || arrs.length == 0)
        {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for(int num : arrs)
        {
            sb.append(num).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }
    public static String converArray2Str(List<String> list)
    {
        if(list == null || list.isEmpty())
        {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for(String obj : list)
        {
            if(obj != null)
            {
                sb.append(obj).append(",");
            }
        }
        if(sb.length() > 0)
        {
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }
    public static String formatStringArray2Json(List<String> list){
        StringBuffer sb = new StringBuffer();
        if (list == null || list.size()==0)
            return null;
        else{
            sb.append("[");
            int i=1;
            for(String item :list){
                sb.append("\"").append(item).append("\"");
                if(i<list.size()-1){
                    sb.append(",");
                }
            }
            sb.append("]");
        }
        return sb.toString();
    }

    public static String encodeParamString(Map<String,Object> pMap){
        if (pMap==null || pMap.size()==0)
            return null;
        StringBuffer sb = new StringBuffer();
        for (String key : pMap.keySet()){
            sb.append(key).append("=").append(pMap.get(key)).append("&");
        }
        String result = sb.toString();
        result =  result.endsWith("&")? result.substring(0,result.length()-2):result;
        return result;
    }
    public static Map<String,Object> decodeParamString(String str){
        if (str==null || str.length()==0)
            return null;
        String[] paramArray = str.split("&");
        Map<String,Object> paramMap = new HashMap<String ,Object>();
        for (String paraminst: paramArray){
            String[] p= paraminst.split("=");
            if (p!=null && p.length==2)
                paramMap.put(p[0], p[1]);
        }
        return paramMap;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * 			：字节数组
     * @return
     */
    public static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * 			：字节
     * @return
     */
    public static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    public static String getUtf8(String str) {
        String ret = "";
        if (str != null && !str.isEmpty()) {
            try {
                ret = new String(str.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.debug("String数据转换编码异常-->" + str + "-->" + e.getMessage());
            }
        }
        return ret;
    }

    /**
     * 判断字符是否正则匹配(忽略英文大小写)
     * @param regex
     * @param message
     * @return
     */
    public static boolean isMatch(String regex, String message)
    {
        return isMatch(regex, message, true);
    }

    /**
     * 判断数据是否匹配
     * @param regex
     * @param message
     * @param ignoreCase
     * @return
     */
    public static boolean isMatch(String regex, String message, boolean ignoreCase)
    {
        Pattern pattern = Pattern.compile(regex, ignoreCase ? Pattern.CASE_INSENSITIVE : 0);
        Matcher matcher = pattern.matcher(message);
        return matcher.find();
    }
    /**
     * 判断二个对象是否相等
     * @param source
     * @param target
     * @return
     */
    public static boolean isEqual(Object source, Object target)
    {
        if(source == null && target == null)
        {
            return true;
        }
        if(source == null || target == null)
        {
            return false;
        }
        return source.toString().equals(target.toString());
    }
    /**
     * 判断源字符串是否包含目标字符串
     * @param source	源字符串
     * @param target	目标字符串
     * @return
     */
    public static boolean isIncloud(Object source, Object target)
    {
        if(source == null && target == null)
        {
            return true;
        }
        if(source == null || target == null)
        {
            return false;
        }
        return source.toString().indexOf(target.toString()) != -1;
    }
    public static boolean isNotCloud(Object source, Object target)
    {
        return !isIncloud(source, target);
    }
    /**
     * 换算单位值
     * @param value			值
     * @param unit			单位
     * @param defaultValue	默认值
     * @return	value为空或则为0时返回默认值，否则返回value+unit
     */
    public static String convertUnit(Object value, String unit, String defaultValue)
    {
        if(value == null)
        {
            return defaultValue;
        }
        BigDecimal bigValue = new BigDecimal(value.toString());
        if(bigValue.doubleValue() == 0)
        {
            return defaultValue;
        }
        else
        {
            return value + unit;
        }
    }
    public static boolean isLength(String str,int len){
        if(str==null){
            return false;
        }
        return str.length()==len;
    }
    public static String formatDigest(Object value)
    {
        if(value == null || isEmpty(value.toString()))
        {
            return "0.00";
        }
        return new DecimalFormat("##,##0.00").format(value);
    }
    /**
     * 格式化数字(没有小数则不显示)
     * @param value			数字
     * @param maxDecimal	保留最大小数位数
     * @return
     */
    public static String formatDigest(Object value, int maxDecimal)
    {
        if(value == null || isEmpty(value.toString()))
        {
            value = 0;
        }
        StringBuffer format = new StringBuffer();
        format.append("####");
        if(maxDecimal > 0)
        {
            format.append("0.");
        }
        while(maxDecimal > 0)
        {
            format.append("#");
            maxDecimal--;
        }
        return new DecimalFormat(format.toString()).format(value);
    }
    /**
     * 格式化数字(没有小数则显示0占位)
     * @param value			数字
     * @param maxDecimal	保留最大小数位数
     * @return
     */
    public static String formatDigestWithFill(Object value, int maxDecimal)
    {
        if(value == null || isEmpty(value.toString()))
        {
            value = 0;
        }
        StringBuffer format = new StringBuffer();
        format.append("####");
        if(maxDecimal > 0)
        {
            format.append("0.");
        }
        while(maxDecimal > 0)
        {
            format.append("0");
            maxDecimal--;
        }
        return new DecimalFormat(format.toString()).format(value);
    }
    public static String formatDigestWithEn(Object value)
    {
        if(value == null || isEmpty(value.toString()))
        {
            return "0.00";
        }
        return new DecimalFormat("####0.00").format(value);
    }
    public static String formatComputerDigest(Object value)
    {
        String union = "byte";
        if(value == null || value.toString().trim().length() == 0)
        {
            return "0" + union;
        }
        try
        {
            long digest = Long.parseLong(value.toString().trim());
            if(digest >= 1024)
            {
                digest = digest / 1024;
                union = "KB";
            }
            if(digest >= 1024)
            {
                digest = digest / 1024;
                union = "MB";
            }
            if(digest >= 1024)
            {
                digest = digest / 1024;
                union = "G";
            }
            if(digest >= 1024)
            {
                digest = digest / 1024;
                union = "T";
            }
            if(digest >= 1024)
            {
                digest = digest / 1024;
                union = "P";
            }
            return (Math.round(digest * 100) / 100) + union;//digest.toFixed(2)保留2位小数
        }
        catch(Exception e)
        {
            log.debug("格式化数字异常-->" + value + "-->" + e.getMessage());
            return value.toString();
        }
    }
    public static String formatComputerDigestWithFix(Object value)
    {
        String union = "byte";
        if(value == null || value.toString().trim().length() == 0)
        {
            return "0" + union;
        }
        try
        {
            double digest = Double.parseDouble(value.toString().trim());
            if(digest >= 1024)
            {
                digest = digest / 1024;
                union = "KB";
            }
            if(digest >= 1024)
            {
                digest = digest / 1024;
                union = "MB";
            }
            if(digest >= 1024)
            {
                digest = digest / 1024;
                union = "G";
            }
            if(digest >= 1024)
            {
                digest = digest / 1024;
                union = "T";
            }
            if(digest >= 1024)
            {
                digest = digest / 1024;
                union = "P";
            }
//			return (Math.round(digest * 100) / 100.0) + union;//digest.toFixed(2)保留2位小数
            return formatMoneyWithEn(digest) + " " + union;
        }
        catch(Exception e)
        {
            log.debug("格式化数字异常-->" + value + "-->" + e.getMessage());
            return value.toString();
        }
    }
    /**
     * 格式化存储数据（单位：MB）
     * @param value	数字，单位：byte
     * @return
     */
    public static String formatStoreDigestWithFix(Object value)
    {
        if(value == null || value.toString().trim().length() == 0)
        {
            return "0.000";
        }
        try
        {
            double digest = Double.parseDouble(value.toString().trim());
            double digestNew = digest / 1048576.0;
            if(digestNew == 0 && digest > 0)
            {
                digestNew = 0.001;
            }
            return formatMoneyWithEn(digestNew);
        }
        catch(Exception e)
        {
            log.debug("格式化数字异常-->" + value + "-->" + e.getMessage());
            return value.toString();
        }
    }
    /**
     * 格式化金额，以元为单位显示到分，每3位用逗号分隔
     * @param value
     * @return
     */
    public static String formatMoney(Object value)
    {
        if(value == null || isEmpty(value.toString()))
        {
            return "0.00";
        }
        return new DecimalFormat("##,##0.00").format(value);
    }
    /**
     * 格式化金额，以元为单位显示到分
     * @param value
     * @return
     */
    public static String formatMoneyWithEn(Object value)
    {
        if(value == null || isEmpty(value.toString()))
        {
            return "0.00";
        }
        return new DecimalFormat("####0.00").format(value);
    }
    /**
     * 格式化金额，以元为单位显示到厘，每3位用逗号分隔
     * @param value
     * @return
     */
    public static String formatMinMoney(Object value)
    {
        if(value == null || isEmpty(value.toString()))
        {
            return "0.000";
        }
        return new DecimalFormat("##,##0.000").format(value);
    }
    /**
     * 格式化金额，以元为单位显示到厘
     * @param value
     * @return
     */
    public static String formatMinMoneyWithEn(Object value)
    {
        if(value == null || isEmpty(value.toString()))
        {
            return "0.000";
        }
        return new DecimalFormat("####0.000").format(value);
    }

    public static String doMD5(String key){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++)
            {
                i = b[offset];
                if(i<0) i+= 256;
                if(i<16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            System.out.println("result: " + buf.toString());//32位的加密
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MD5加密异常-->" + key + "-->" + e.getMessage());
        }
        return null;

    }

    public static String formatDate(Long time, String pattern)
    {
        return DateUtils.convertTime2Str(time, pattern);
    }

    /**
     * 在字符串的前后添加指定符号
     * @param source
     * @param mark
     * @return
     */
    public static String fillWithMark(String source, String mark)
    {
        if(isEmpty(source))
        {
            return "";
        }
        if(isEmpty(mark))
        {
            return source;
        }
        source = source.trim();
        if(!source.startsWith(mark))
        {
            source = mark + source;
        }
        if(!source.endsWith(mark))
        {
            source = source + mark;
        }
        return source;
    }
    /**
     * 是否包含中文
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str)
    {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find())
        {
            return true;
        }
        return false;
    }
}
