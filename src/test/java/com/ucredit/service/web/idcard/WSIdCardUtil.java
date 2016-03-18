package com.ucredit.service.web.idcard;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

import com.ucredit.service.web.idcard.request.QueryValidatorServices_QueryValidatorServices_Client;

/**
 * 国政通身份证查询接口专用Util类
 * 加密是先des再base64，解密是先base64再des,中文采用GB18030编码
 *
 * @author luzongwei
 * @see QueryValidatorServices_QueryValidatorServices_Client
 */
public class WSIdCardUtil {

    /**
     * DES密钥
     * 国政通针对IP地址修改密钥,测试环境和线上环境可用不同密钥
     */
    public static final String DES_KEY = "12345678";

    /**
     * 向量密钥
     */
    private static final String IV_PARAM = "12345678";

    /**
     * 加密算法
     */
    private static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";

    public static String encode(String key, byte[] data) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // key的长度不能够小于8位字节
        Key secretKey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(WSIdCardUtil.ALGORITHM_DES);
        IvParameterSpec iv = new IvParameterSpec(
            WSIdCardUtil.IV_PARAM.getBytes()); // 向量
        AlgorithmParameterSpec paramSpec = iv;
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
        byte[] bytes = cipher.doFinal(data);
        return new String(Base64.encodeBase64(bytes), "GB18030");
    }

    public static String decode(String key, byte[] data) throws Exception {
        try {
            data = Base64.decodeBase64(data);
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key 的长度不能够小于 8 位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(WSIdCardUtil.ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(
                WSIdCardUtil.IV_PARAM.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(data), "GB18030");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public static String genImage(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;

        // 读取图片字节数组
        try (InputStream in = new FileInputStream(imgFilePath);) {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
        return null;
    }

    public static boolean GenerateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) {
            return false;
        }
//        BASE64Decoder decoder = new BASE64Decoder();

        try (OutputStream out = new FileOutputStream(imgFilePath);) {
//            // Base64解码
////            byte[] bytes = decoder.decodeBuffer(imgStr);
//            for (int i = 0; i < bytes.length; ++i) {
//                if (bytes[i] < 0) {// 调整异常数据
//                    bytes[i] += 256;
//                }
//            }
//            // 生成jpeg图片
//            out.write(bytes);
//            out.flush();
//            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            // 测试从Base64编码转换为图片文件

            String strImg = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCADcALIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD33IHUijcvqKNi/wB0UbF/uigAyD3FLTdi+gpPLIPDn6UAPopvz/7NGX/u0AOoqjf6lHp8PmSsoHoa5K817UzHJcy30dlbj/VHy8+ZQB3eQO9Vru+trKLzbiUInrXmsfxFXTrny7mT7dH5f/LOuZ1/xZdarefaIme3j/1cflf886jmL5T0q88f6NZMPMlfbjByvzJ/2z+/+lPtPiJ4cuhzetB/18JtrxCSNPM83yB+8/56U0wvj91HciP2pXHY+k7O9tL2HzLS5iuI/WOTfVqvmKK/vLC58yKaSCSP/lrH+7rvNE+J2qWo/wCJpCt5bf8APSL/AFlHtES0ew0Vi6P4jsdeh8yymHT/AFZ4cVsfP7VoSOpMgdxTfLPd2NLsX0FAC7l9RRkHoRRsX+6KTYvoKAHUUzy8dGIpfn9qAHUU35/9migB1FMy/wDdH50fvP8AZoAfRTMv/dH50eYBjII/CgB9RXE6W1u8z/dQZNSBlI6iuF+I/iAabYx20coEknvQByvifxXJcXEgiG8CToJK4u9vbm9/eXlzJJVGOYXMn7wj86p6lfq/EURSP6Vlc0JPt3+kx+X/AKurP2nzO1ZMTq0nyqPxqbMh9B+NAE8kssVEepSeZ+8P/f395VH96OwNKXZfvL+VOwjft7nzIv3n7yP/AKafvKkjkjil8v8A1dYMRVD5kchH1FaFtdi6j8u4I/66Uhmvb3tzbXscsckltJ/yzkjr0zwr8RIrnZZ6u8ccp+5cD/VmvJcvbyeVIRSTXCrJ5ufL+lCYWPp+ivK/h541Dx22k3bk+Z/x7SEf+Q69Sy/90fnWpmOopn7z/Zoy/wDdH50APophkA6gj8KcHU96AFopNy+o/OigBaKQkDqaTevrQA6ikBB6GloAqXdzBZ2ctzcKBHHHvfivnPxJqU+ratJcuB5kkn7uKvU/ihrxtbGPSoJAJLj7/wBK8dji82TzP+/VZtmiQ63so/L+7FUDWD3kojiixHWlbxxyfuo66ez02sqlQ6KdI5S38Lyyn95Vm48OSRx16Bb2QEXJpJbRB/FWHtDX2R5VJZSRVW8ryq9Eu9IU965W706SPzPMjq6dQyqUjIRQ3WIVoR20EkXmRqKbHb+V/wAs/MjqzGY4v/ttaGY7+zU9Kjit0H7uRR5dW47ny7n95J5dSZik/eeZ/wBM60AyfNk02T/plJXvfhHxD/b2ixyu+biMeXL/APF14lqEcUlt+7qfwh4hfw/rUciXX+jSfu5KadjM+jaKrWlwktsj7o+R/B0qwCD0NaGYtJtU9hS0UAJsX+6KKWigBvlr6Uu1fQUtFADfLX0qKc+VE8itjYM1PXM+O9QFj4WuQPvzjyxQB4p4jvbjWNZuLgAHzJKgkMkcX3B+dN/5Z0y8k/eRxVzs6EXdLjkMkfyj867jTrNz1bFcxpcf7yOu+s/9XXNUO2lsSxWynrzU32FfT9aWLpVuoAzpdNWXqKzLzw+skfyxkV08fWpPs/mCtBnkeqeHXh/eRNmsbMsv7qVR5n1r1rV9JBi4jrg9U0nypfMrSmY1Dj7hrmP/AFaDy/rVWPUZWj8rgVt3Ft5ccn/PP/V1gS/62tznL0VyT5e5s1GBBmTywfzqiJPLqSP/AF1Bke/fDLWF1Dw+LOTHm23Qf7Hau48tfSvBvhxq39neKreHzB5Vx+7r3utSBuwj7rEUnzj0NPooAbl/7v60U6igBmXP8OPrR+8/2afRQAzc46rmvM/itqIC2VkQRn95Xp9eO/EqcP4qji/55xx/zqKmxdPc4Z9pkjiDD93UGRJrXUVclRY4+RWPbwpLrXT9axNTrtMvBa/cj8ySuht9Xu4/vx5qlodjBEYpMfrXXRPaxRcCsDpRlp4nnj+/p0v1rVt9S+1R9h+FRx32k3Mnl/abbzP+ulSSW0cX+roNDRSUjsDVj7QY+qGs+KrFx/qqB2uQanr8EUePL8yuEv8AVLi+kk+z6dJXYTLC3EgrHuNV0q2k8uS4jik/660e0F7M4C9juIpP3kfl/wDXSuevZEB5b95XpWsCzuBHcxyH/nnLzXGanpSwyycVv7Q56tI5sy/3VJpd0h6Lj61NJF5R7Uw9Ks5DY0e5uba5trnIk+zyxydK+noJneBHK5yO1fLulH94Y6+orL/jxt/+uSfyrZDZMJB/ECKUOp6GnUhRT1FBAtFN8tPSigBSwHUijev94UgRR2/Ol2r6D8qAAEHoRXi3xC/5HK6/65x/+izXtBjU+30rx/4k2hj8Uxyqx/eRxjn61nU2NKe5x2o/8e1Zmj/vNa/66VZ1rzRYx9D9BWb4enk/t2L92azNP+Xh65Z2P+hUW9lJLe/abz95H/zzrR09JDFHhcVf+zyHqw/Cuc7TiZfDAk16S4Nz/oUn+sj8v95XVWcKi5kj3CO2/wCWX/TOrQ08d4yfxqwEWKPoKAK9vImfvCrsxB8uqxRY5OgpZYRyQSPxoGZ15bdq5y48JRXupfaY7mS3j/5aRxV2nkSPFww/Ko9sg6w5+lKmBgahpsVzJH5dt5fl/u6zv7A/deXcf8s/9XJXYSRP/wA8zVaeKU9wPqKYjx/xRpv9m3Ef/TSsNGEnVgK7L4gxEyWW5j+FcPFEoz3+tdFM4qn8Q63wnYpfajHFuH7z93X0iCD0IrxH4X6W1xqsdzJCPLj9q9tMan2+ldBnU3HUUwJt+6x/Hmg7x6GgzH0Uzc39z9aKAH0U3LnoAPrR+89FoAdXnXxR04yRWeoJ1jPl/nXoRLjsD9KxPE2mDWvD9xZ7MSYDx+0goGeE6pF5lt/2zrnNLl+zarby/wDTSupuAnl+VuFcZOgt7jGRWZvUPoTSpP3UVa1cd4b1BbjSbaTcP9XXVQXK445+lcbO5MnqOTpT/MZui4+tULyWTzPLAWi1gbuN8zzZKs1jRa3ZRat/Z0ci/aY/ermoa3aWFv8AaLpjHF7U0xGrD+8cVaeLIrKsrmGV/MjOI5K1FdefmFJjmR1n3HWr8kw9ayb24Xy/vCgHseU+PLnzdajj/wCecdc3bxebLHUutX/9patc3IUn95XR+AtCk1fxFbJ5IEUf7yTNddM4ah7J4D0caT4aiDpia4/eSfWuoqNFdVA+Xily4HIB+lanOPopgkHcEU7cvqKAFooooAKKMikyD3FAC0daKKAPG/G+jrZ64+yEeXL+8jx615rrmmLHL5sefzr6N8XaKuraZ5i/8fFt+8j+teGah5Ukkkcn7s/+i6zZ0fxEbnw+uFk0b7N1+zyd69Dt3APAAryDwXc/2brUltL/AMvFep28lc9Q6aX8M1vM/dVHJFFJ/rKh84DqaPtcMf8ArJoqzNSOSyjk/wCWf7z/AJ6VJJptt5f7z95/10qOPVLUy/60VJJfRS/uhLQH7wktLSG3j/dqKn2qe1RRyx+VR51ABIFHc1yviy/Wx0W4kwP+eUddBcS5NeY+PNS+03MdlH/q4/3klFPcyqnL6da/aJIo8V9AeAfDo0TSfMf/AFs/JrzPwBpUeo6pZyyf6sSZxXvK4VcZrtijhbHUUm4eopcj1qyApNq+gpaKAG+WvpRTqKAE2L/dFGxf7opaKAG+WvoB9KTYwOQ5+hp9FADCXx0Wvnb4gaY+k+K739wI7aT95HivoyvP/iboEeo6bHeJHmWLvQaU3qeERTXCXEdzG48yOTvXrekX8moWVtcF8eZH2ryd4/Klkjk/1tel6PFLHpUf/TOOuLEHThjqYgsn3vm+tZ15ptnLJ+8gFT2cvmx1Z8rzKzO0wo9JJP7ucR0+XSLjtflfpW1/ZMcv/LWSiTSPL/5eZZKdjT6wZtta3EJ4u5D9a0jM4i/egfhUnleVzVK8ufK/d1kZla8vH/5ZrXj95LcXUhkkA8ySvYI4v3Xm15z4g02TTtZubd4/3kcldGHOLEnffCezZ7Tzmc/6PM4kH1QCvWdi+lec/Ca3uYtLlkl/1cvzivSK7TkluJsX+6KTYuMAAfSnUUEjPLIPDn6Uvz/7NOooATLeg/OilooAZl/7o/OjMnoKfRQA3c/939aTzADggj8KfRQAgYHoar3cEV7Zy28nMciYNQz6lp9uH8+6t49nXMgrzH4meKIrjTzZae77VP7yRRRoa06bmYvjTRopNa823ltv3cX7zy62NMmgfTo5I8nzK4LS9Im1K2+0yQjy/wD0ZXf6FBCunR4ArixB2Uhwea3fzYVzWtb3/m/w1E6ZqtJbelc50ml9qkH8INSm9Lj5lxWJ9huf+WdzJVy3spP+Wl1JR7QzJrvUoLePHIk96oW9u9zL5twRmtFdPhPUCp/sqRR8KBQaFW4Iij6iuWvXsPF+oajcRp9nufL/ANXJ/wAtK6bUIlkjkyK4u4tptE1ayvbdhHc1ph6ljKpT9oeyaFarp2k21soJMUUaVrEv/d/WvNbf4jXflp5ltA+fQ11ekeL9P1U+VnyLj/nnJ1ru9ojiqYapDdG/mT0FLl/7v606irOcZ5gBwQQfpTgwPQ0tJsX+6KAFopNi/wB0UUAGQO9G9f7wrHv9c0zS1xcTp5n9xTk1y2peOjJFILCLZj/lpJUe0RvTw1SpsjtL/V7LTYvMupgi157r3xAvJxJFp3+jxf8APT/lpXLXl217JJJPJK8n/XWsglvM+VqydU9all0Kf8QvSXMkksnmS+ZJUUdt/aUnlyVWk8wdRmmRzyW1z5kafrWR2VKR3Gj20X2Ly60bO28oVk6ffiWPzYkH51q2bSTR5LY/CudnmF3y6TyR6inxRA4zk1P5SelFkJFbyQf9WRUsXap/KX0pvkgdCaAE8qiXpTdsi/xZFQgv/cH50xhJHXnuuXv23UfNj/1cf7uOtzxRroijksrc/vJP9ZJmuJzIeny1dM7MPT+2WvMxVuO/EXes+KNvLk3NUYVh6/nWh0bnc6J4yubM+WlyJIx0jlrv9J8WafqQ8tj5Ev8Azzk614Rbyp5o4rRt7xox+6lP51qqtjhq4WnU2PoaivItN8X32k3Jt2bfb+ZgJJ1ruNL8X2Oofu5HEMv/ADzk4rRVEeXUw06e50tFM3N/dH50Voc58/yXMkvaiSWX7N/20qtsB/5bRj61LcRny7b96v8Aqv8AlmK4j65UitJ5nlSetRxxSeZ/rakkcBOTKfpHS+bD/FFj60B7P94N8mlk/wBbLFT1uFTpHEaXUD5t7L/10oZpYLO9kspPNjkrqND1q2kk/eSeXL/00rjCQOpqHzU7HmsjOph6dQ9fjkq1Xj0GrX9v/wAess0fvWtb+MdXt+JBE/8A11iqPZnH9TqHpePrRj6157/wnepD/l1tj+FVp/HOsSfdRIv+ucdO5l9WqHo9xcR20fm3MkUUVcdrHjHzPNttP/d/9Na5CfUpr6TzLiaSSX/ppTdwPQ1fIdNPDiyS+YKljj83tUeaakiCPlBVnTaxpxxx/Z5f9Ii/8iVWjiP/AEzx/wBdKZHKPLl2jP7uovMkJ4XFMRJHFJ/zy/d1J+6qlHLL5kflyirn2y4HVpW+lAUy1cf8fMtRmT/prUtxfSRXFzuhjX953jqm1ws38EVBnVplv+0b3/n+loqn5w/55iitTm9kP80+1S3Eh8u2/wCuf/tSodo9RUkgg+zR/MKyPR/eEFxJ5cclRRyZl8vEVSOYTH9wUf8ALSkZ+zqe0GnYex/A0+9hP22T/W/631qWrFxFJLcyeXFLJQa+zMqSI+h/OpgrjoBU1xbXEfMkckf/AF0/d0fZpT/zz/7+Uxf8vCGoq07eOQXsefK/1n/PSquxf+e8NZ2NGiD+GjZntT5Fh83/AI+4v+/dTRR2pj/eXMn/AGzjq7EFVIGmk8vFOjiQk8H860beK2+0xyRyySeX+8/1dRebbf8APKX/AL+1AGe9pB5nf86WK2gEfEVT+ZH/AM8ql82L/nl/5EqzMbFFF5Un7qq1Xo5bb7NJ+7k/1kf/AC0qLdadzJFTLKcUX72pcy06MQeb/rh+dWY4H82PE0X/AH9osZ06Yy9Dm5l4k/1lVJY2Msf+srQuLaX7TIfL8z/rl+8qt5X7z/WxxUBUp/uzO8mf3/OitQW0mP8AWx0UjP2Y4GAf8syfrJVqUwfYov8ARIv9ZJWbVtv+PCH/AK6SVCdzouV5RBNH+6t44voajiLLIdrR/wDbaldjskqtHPJ5vX/lnWhnUZofarkf6uQJ/wBcqLy4n3/vPOk/dx/6yqvnv61auflkGP8AnlHWZrzsrSvITwmPrSxCQ9SPwpHiRvKyP+WlVoflHFaGXtH7QvW4H222JLH95600GAfx/pSW5P2iL/rpUW9vU0rmvOxZTAeMY+lNjAEWQx/Oo50U+Xmp4lHlUGHtp+0JrOSQSSYIP7uT/wBF1D58g62+fpVqOFMS8VVKDBoNOeRXkuLiPzMW5qUGU9gPrSyQp+94qbAoM6c5DiS1v8zf8tO1V2Cj1P1q3tH2GL/rpVWcDy6DZkcZh8z7oqe0hWW9ij5H73tVdQF6VZgY/aIv+ulBhTqfuxsiMuNrfnUZl2y9AfpS7jmoZf8Aj629qDSpUsWNz/3KKgzRTuLnP//Z";
            WSIdCardUtil.GenerateImage(strImg, "D:/temp.jpg");

            // 测试从图片文件转换为Base64编码
            System.out.println(WSIdCardUtil.genImage("D:/temp.jpg"));

            String code = WSIdCardUtil
                .encode(
                    WSIdCardUtil.DES_KEY,
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<data>\n  <message>\n    <status>-9994</status>\n    <value>路宗威,11010319830310121X</value>\n  </message>\n</data>"
                        .getBytes("GB18030"));
            System.out.println(code);
            System.out.println(WSIdCardUtil.decode(WSIdCardUtil.DES_KEY,
                code.getBytes("GB18030")));
            System.out
                .println(WSIdCardUtil
                    .decode(
                        WSIdCardUtil.DES_KEY,
                        "S078JKKtSvAMWap/sIGpd4eiphXoB0MB8EfFuFogsz8RLhrqnYxuAI8jchhX2zrYlCcVJcbeIfkwP7h6A+13QRIPDjUZPyuGoRHHl4EmIc43V81xC3h4JQtjgBbWbu9/khJBgkdSvCDdTGkXROVKa+QS8gpI01kyDnp9O56l6kJu8P/ZApcgYZOBfcGe1gU4XO+IJFNAzOE="
                            .getBytes("GB18030")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
