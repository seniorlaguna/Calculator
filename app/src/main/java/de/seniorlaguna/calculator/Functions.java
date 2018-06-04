package de.seniorlaguna.calculator;

import com.udojava.evalex.AbstractFunction;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.util.List;

public class Functions {

    public static AbstractFunction factorial = new AbstractFunction("fak", 1) {
        @Override
        public BigDecimal eval(List<BigDecimal> parameters) {

            //enough params
            if (parameters.size() != 1) {
                throw new Expression.ExpressionException("Wrong Parameter Count");
            }

            //get int value
            int value = parameters.get(0).intValue();
            BigDecimal res = new BigDecimal(1);

            //get factorial
            for (int i=1; i<=value; i++) {
                res = res.multiply(new BigDecimal(i));
            }

            return res;
        }
    };

}
