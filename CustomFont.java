package postCard;

import java.io.*;
import java.util.regex.*;
import java.util.ArrayList;
/**
 * Created by aabzel on 27.01.2018.
 */
public class CustomFont {
    // Font number
    int fontNum;
    int amountOfchars;
    // list of chars
    ArrayList<CharFigure> listOffigures;


    CustomFont(int inFontNum){
        amountOfchars=0;
        fontNum = inFontNum;
        listOffigures = new ArrayList<CharFigure>();
        listOffigures.clear();
        // init empty list of chars
    }

    void load_font(String fontNumber) {

        System.setProperty("console.encoding","utf-8");
        // count the ammount line in file
        amountOfchars = 0;
        System.out.println("Load indents.");
        String inPutFileAdderss = "C:\\!!docs\\1_my_prj\\1_plotter\\3_Fonts\\" + fontNumber + "_vectorFonts\\indents.txt";
        String lineStr;

        try {
            System.setProperty("console.encoding","utf-8");
            BufferedReader readerIndents = new BufferedReader(new FileReader(inPutFileAdderss));
            do {
                lineStr = readerIndents.readLine();
                if (null != lineStr) {
                    amountOfchars++;
                    System.out.println(lineStr);
                    // add a char to list
                    Pattern p = Pattern.compile("\\'.?\\'");
                    Matcher m = p.matcher(lineStr);
                    String symbol="0" , indent="0.0";
                    if(m.find()) {
                        symbol =  m.group().subSequence(1, m.group().length() - 1).toString();
                        System.out.println("char: "+"<" +symbol.charAt(0) + ">");
                    } else {
                        System.out.println("lack of data char");
                    }

                    Pattern pDouble = Pattern.compile(" [+-]?(\\d+([.]\\d*)?|[.]\\d+)");
                    Matcher mDouble = pDouble.matcher(lineStr);
                    if(mDouble.find()) {
                        indent =  mDouble.group().subSequence(1, mDouble.group().length() ).toString();
                        System.out.println("intend: "+"<"+ indent+">" );
                    }else {
                        System.out.println("lack of data indent");
                    }
                    // add to list
                    CharFigure figuCh = new CharFigure();
                    figuCh.setChar(Integer.parseInt(fontNumber),
                                   symbol.charAt(0),
                                   Double.parseDouble(indent)
                                  );
                    listOffigures.add(figuCh);



                }

            } while (null != lineStr);
            readerIndents.close();
        } catch(IOException e) {
            System.out.println("\n\nError to load indents!");
        }

        System.out.println("Amount of chars in font: "+amountOfchars +" "+listOffigures.size());
        //System.out.println( "d indent: "+calcInterval('d') );
        //System.out.println( "й indent: "+calcInterval('й') );
    }

    public Double calcInterval(char inChar){
        System.setProperty("console.encoding","utf-8");
        for(int i=0; i<listOffigures.size() ; i++){
            if(inChar == listOffigures.get(i).symbol){
                return listOffigures.get(i).indent;
            }
        }
        System.out.println( "\n Error Lack of indent for " + inChar );
        return 0.0;
    }
}
