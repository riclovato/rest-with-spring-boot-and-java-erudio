package com.ricklovato.erudio.controllers;

import com.ricklovato.erudio.converters.NumberConverter;
import com.ricklovato.erudio.exceptions.UnsupportedMathOperationException;
import com.ricklovato.erudio.math.SimpleMath;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MathController {

    private final AtomicLong counter = new AtomicLong();
    private SimpleMath math = new SimpleMath();
@RequestMapping(value = "/sum/{numberOne}/{numberTwo}", method = RequestMethod.GET)
    public Double sum(@PathVariable(value = "numberOne") String numberOne,
                      @PathVariable(value = "numberTwo") String numberTwo) throws Exception {
    if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
        throw new UnsupportedMathOperationException("Please set a numeric value!");
    }
        return math.sum(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }
    @RequestMapping(value = "/sub/{numberOne}/{numberTwo}",method = RequestMethod.GET)
    public double sub(@PathVariable(value = "numberOne") String numberOne,
                      @PathVariable(value = "numberTwo") String numberTwo){
     if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)){
         throw new UnsupportedMathOperationException("Please set a numeric value!");
     }
     return math.sub(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    @RequestMapping(value = "/div/{numberOne}/{numberTwo}",method = RequestMethod.GET)
    public double div(@PathVariable(value = "numberOne") String numberOne,
                      @PathVariable(value = "numberTwo") String numberTwo){
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)){
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return math.div(NumberConverter.convertToDouble(numberOne),NumberConverter.convertToDouble(numberTwo));
    }

    @RequestMapping(value = "/multi/{numberOne}/{numberTwo}",method = RequestMethod.GET)
    public double multi(@PathVariable(value = "numberOne") String numberOne,
                      @PathVariable(value = "numberTwo") String numberTwo){
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)){
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return math.multi(NumberConverter.convertToDouble(numberOne),NumberConverter.convertToDouble(numberTwo));
    }

    @RequestMapping(value = "/avg/{numberOne}/{numberTwo}",method = RequestMethod.GET)
    public double avg(@PathVariable(value = "numberOne") String numberOne,
                        @PathVariable(value = "numberTwo") String numberTwo){
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)){
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return math.avg((NumberConverter.convertToDouble(numberOne)), NumberConverter.convertToDouble(numberTwo));
    }

    @RequestMapping(value = "/sqr/{numberOne}",method = RequestMethod.GET)
    public double sqr(@PathVariable(value = "numberOne") String numberOne){
        if(!NumberConverter.isNumeric(numberOne)){
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        }
        return math.sqr(NumberConverter.convertToDouble(numberOne));
    }

}
