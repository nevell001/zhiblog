package com.zhi.framework.config;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 图形验证码算式生成器单元测试。
 *
 * <p>注册/登录的人机校验靠它出题，答案由后端算好写进 Redis，所以"题面算式的值必须等于答案"
 * 是唯一的正确性契约；同时除法不能出现除零，减法不能出现负数答案。</p>
 */
class KaptchaTextCreatorTest
{
    private static final Pattern CHALLENGE = Pattern.compile("^(\\d+)([+*/-])(\\d+)=\\?@(-?\\d+)$");

    private final KaptchaTextCreator creator = new KaptchaTextCreator();

    @Test
    void everyGeneratedChallengeMatchesItsOwnAnswer()
    {
        Set<String> operators = new HashSet<>();
        for (int i = 0; i < 4000; i++)
        {
            String text = creator.getText();
            Matcher matcher = CHALLENGE.matcher(text);
            assertThat(matcher.matches()).as("非法题面 %s", text).isTrue();

            int left = Integer.parseInt(matcher.group(1));
            int right = Integer.parseInt(matcher.group(3));
            int answer = Integer.parseInt(matcher.group(4));
            String operator = matcher.group(2);
            operators.add(operator);

            switch (operator)
            {
                case "+":
                    assertThat(left + right).as(text).isEqualTo(answer);
                    break;
                case "-":
                    // 大数在前，答案不为负
                    assertThat(Math.max(left, right) - Math.min(left, right)).as(text).isEqualTo(answer);
                    break;
                case "*":
                    assertThat(left * right).as(text).isEqualTo(answer);
                    break;
                default:
                    assertThat(right).as("除数不能为 0：" + text).isNotZero();
                    assertThat(left % right).as("必须整除：" + text).isZero();
                    assertThat(left / right).as(text).isEqualTo(answer);
            }
        }
        assertThat(operators).containsExactlyInAnyOrder("+", "-", "*", "/");
    }

    @Test
    void operandsStayInsideTheTenStepTable()
    {
        for (int i = 0; i < 2000; i++)
        {
            Matcher matcher = CHALLENGE.matcher(creator.getText());
            assertThat(matcher.matches()).isTrue();
            assertThat(Integer.parseInt(matcher.group(1))).isBetween(0, 10);
            assertThat(Integer.parseInt(matcher.group(3))).isBetween(0, 10);
        }
    }
}
