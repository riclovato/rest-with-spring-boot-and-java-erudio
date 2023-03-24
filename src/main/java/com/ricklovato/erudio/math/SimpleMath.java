package com.ricklovato.erudio.math;

import com.ricklovato.erudio.converters.NumberConverter;
import com.ricklovato.erudio.exceptions.UnsupportedMathOperationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class SimpleMath {
    public Double sum(Double numberOne, Double numberTwo) {

        return numberOne + numberTwo;
    }

    public double sub(Double numberOne, Double numberTwo) {

        return numberOne - numberTwo;
    }


    public double div(Double numberOne, Double numberTwo) {

        return numberOne / numberTwo;
    }


    public double multi(Double numberOne, Double numberTwo) {
        return numberOne * numberTwo;
    }


    public double avg(Double numberOne, Double numberTwo) {

        return (numberOne + numberTwo) / 2;
    }


    public double sqr(Double numberOne) {

        return Math.sqrt(numberOne);
    }
}
