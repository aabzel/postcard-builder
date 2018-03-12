package postCard;

import java.io.*;
/**
 * Created by aabzel on 27.01.2018.
 */
public class CharFigure {
    Integer fontNum;
    String svgFigure;
    char symbol;
    Double indent;

    CharFigure(){
        System.setProperty("console.encoding","utf-8");
        fontNum=0;
        symbol = '_';
        indent = 0.0;
        svgFigure = "";
    }

    public  void setChar (Integer inFontNumb, char inSymbol, Double inIndent){
        System.setProperty("console.encoding","utf-8");
        fontNum = inFontNumb;
        symbol = inSymbol;
        indent = inIndent;
        //System.out.println(fontNum +" "+ symbol+" " +indent);
    }
}
