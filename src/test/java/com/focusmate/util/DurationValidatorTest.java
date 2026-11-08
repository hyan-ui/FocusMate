package com.focusmate.util;

import com.focusmate.exception.InvalidDurationException;
import com.focusmate.util.DurationValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

    class DurationValidatorTest {
        /*全自动测试机
        * 专为测试而生，从此以后测试不再手输*/

        @Test//用于标记junit测试方法
        void 正常输入_返回数字() throws Exception {
            assertEquals(25, DurationValidator.validate("25"));
        }

        /*语法“断言相等”
        * 和“断言”类似，下一个绝对的判断：在预期的功能中绝对成立的“恒等式（表达式？maybe）”
        * 如果不成立，那么就是bug或实现不对（后置条件实现错误）
        * 断言相等[assertEquals] 即输入值所得输出与预期输出相同，不同就报错*/


        @Test
        void 两边有空格_也能通过() throws Exception {
            assertEquals(25, DurationValidator.validate(" 25 "));
        }

        @Test
        void 不是数字_抛异常() {
            assertThrows(InvalidDurationException.class,
                    () -> DurationValidator.validate("abc"));
        }

        /*断言异常，即此输入断定其会引发某个异常（或该异常的超类）*/

        @Test
        void 零_抛异常() {
            assertThrows(InvalidDurationException.class,
                    () -> DurationValidator.validate("0"));
        }

        @Test
        void 超过上限_抛异常() {
            assertThrows(InvalidDurationException.class,
                    () -> DurationValidator.validate("121"));
        }
    }

