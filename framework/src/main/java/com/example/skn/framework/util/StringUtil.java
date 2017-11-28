package com.example.skn.framework.util;

import android.util.Base64;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class StringUtil {
    /**
     * 判断字符串类型不是空的(judge String type is not null )
     */
    public static boolean isEmpty(String data) {
        return data == null || data.length() == 0 || data.equals("");
    }

    /**
     * 手机号验证
     *
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8,7][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 判断email格式是否正确
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 验证密码格式
     * 6-16位数字与字母的组合
     */
    public static boolean checkPassword(String password) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$"); //验证密码
        m = p.matcher(password);
        b = m.matches();
        return b;
    }

    /**
     * 验证密码格式
     * 6-16位数字或字母的组合
     */
    public static boolean checkPasswordNumOrChar(String password) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[a-z0-9]{6,16}$"); //验证密码
        m = p.matcher(password);
        b = m.matches();
        return b;
    }

    /**
     * 身份证号码验证
     *
     * @param card
     * @return
     */
    public static boolean checkIdentityCard(String card) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        String check = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$";
        p = Pattern.compile(check);
        m = p.matcher(card);
        b = m.matches();
        return b;
    }

    /**
     * 身份证中间替换为*
     */
    public static String formatIDentityCard(String idCard) {
        if (!isEmpty(idCard) && idCard.trim().replace(" ", "").length() == 18) {
            String all = idCard.trim().replace(" ", "");
            return all.substring(0, 3) + "***********" + all.substring(14);
        }
        return idCard;
    }

    /***
     * 真实姓名验证
     *
     * @param trueName
     * @return
     */
    public static boolean checkTrueName(String trueName) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        String check = "^[\u4E00-\u9FA5]{2,8}(?:·[\u4E00-\u9FA5]{2,8})*$";
        p = Pattern.compile(check); // 验证真实姓名
        m = p.matcher(trueName);
        b = m.matches();
        return b;
    }

    /**
     * 判断是否是银行卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;

    }

    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 银行卡前面替换为*
     */
    public static String formatBankCardNumbers1(String cardNumber) {
        if (!isEmpty(cardNumber) && cardNumber.length() > 12) {
            cardNumber = "***** ***** ***** " + cardNumber.substring(cardNumber.length() - 4);
        }
        return cardNumber;
    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000));
    }

    /**
     * 判断字符串是否有特殊符号
     *
     * @param string
     * @return
     */
    public static boolean isEmoji(String string) {
        Pattern p = Pattern.compile("/[\u1F60-\u1F64]|[\u2702-\u27B0]|[\u1F68-\u1F6C]|[\u1F30-\u1F70]|[\u2600-\u26ff]/g");
        Matcher m = p.matcher(string);
        return m.matches();
    }

    /**
     * 获取渠道名
     *
     * @param ctx 此处习惯性的设置为activity，实际上context就可以
     * @return 如果没有获取成功，那么返回值为空
     */

    /**
     * 加密
     */
    public static String encrypting(String str) {
        try {
            SecretKeySpec key = new SecretKeySpec("MonsterWallet".getBytes(), "DES");
            IvParameterSpec iv = new IvParameterSpec("MonsterWallet".getBytes());
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] result = cipher.doFinal(str.getBytes());
            return Base64.encodeToString(result, Base64.DEFAULT).replaceAll("\\n", "").replaceAll("\\t", "").replaceAll("\\r", "");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 解密
     */
    public static String decryption(String str) {
        try {
            byte[] result = Base64.decode(str.getBytes(), Base64.DEFAULT);
            SecretKeySpec key = new SecretKeySpec("MonsterWallet".getBytes(), "DES");
            IvParameterSpec iv = new IvParameterSpec("MonsterWallet".getBytes());
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            Log.i("Base64Util", "b: " + new String(cipher.doFinal(result)));
            return new String(cipher.doFinal(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static final Pattern reUnicode = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");

    public static String encode(String s) {
        StringBuilder sb = new StringBuilder(s.length() * 3);
        for (char c : s.toCharArray()) {
            if (c < 256) {
                sb.append(c);
            } else {
                sb.append("\\u");
                sb.append(Character.forDigit((c >>> 12) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 8) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 4) & 0xf, 16));
                sb.append(Character.forDigit((c) & 0xf, 16));
            }
        }
        return sb.toString();
    }

    /*
   * Unicode解码
   */
    public static String decode(String s) {
        Matcher m = reUnicode.matcher(s);
        StringBuffer sb = new StringBuffer(s.length());
        while (m.find()) {
            m.appendReplacement(sb,
                    Character.toString((char) Integer.parseInt(m.group(1), 16)));
        }
        m.appendTail(sb);
        return sb.toString();
    }


}
