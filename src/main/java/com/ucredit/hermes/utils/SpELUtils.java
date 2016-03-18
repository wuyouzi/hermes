/**
 * Copyright(c) 2011-2013 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author JinShaofei
 */
public class SpELUtils {
    private static ExpressionParser parser = new SpelExpressionParser();
    private static Logger logger = LoggerFactory.getLogger(SpELUtils.class);

    public static String dealTemplate(String template, Object obj) {
        if (template != null && obj != null) {
            logger.debug(template);

            // context
            Expression expression = parser.parseExpression(template,
                ParserContext.TEMPLATE_EXPRESSION);
            return expression.getValue(obj, String.class);
        } else {
            return null;
        }
    }

    public static Object extractProperty(Object o, String exp) {
        Expression e = parser.parseExpression(exp);
        return e.getValue(o);
    }
}
