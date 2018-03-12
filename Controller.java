package postCard;

import javafx.collections.FXCollections;


import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.ChoiceBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.ContextMenuEvent;

import java.io.*;

public class Controller {
    String curFontNumber;
    String outSvgFileName;
    ObservableList<String> FontList = FXCollections.observableArrayList("1","2","3","4","5","6","7","8","9","10","11","12","13");

    @FXML
    public TextArea textArea;

    @FXML
    public Canvas cnv;

    @FXML
    private Slider slider;

    @FXML
    private Slider line_dist_slider;

    @FXML
    private Button btn_svg;

    @FXML
    private ChoiceBox<String> choseBox;

    int sizeOfCanva=400;

    @FXML
    void OnDragDroppedChoiceBox(DragEvent event) {
        System.out.println("OnDragDroppedChoiceBox");
    }

    @FXML
    void OnMouseClickedChoiceBox(MouseEvent event) {
        System.out.println("OnMouseClickedChoiceBox");
    }

    @FXML
    void OnMouseDraggedChoiceBox(MouseEvent event) {
        System.out.println("OnMouseDraggedChoiceBox");
        //redraw();
    }

    @FXML
    void OnMouseExitedChoiceBox(MouseEvent event) {
        System.out.println("OnMouseExitedChoiceBox");
        //redraw();
    }


    @FXML
    void OnMousePressedChoiceBox(MouseEvent event) {
        System.out.println("OnMousePressedChoiceBox");
        //redraw();
    }


    @FXML
    void OnMouseReleasedChoiceBox(MouseEvent event) {
        System.out.println("OnMouseReleasedChoiceBox");
        //redraw();
    }

    @FXML
    void OnScrollFinishedChoiceBox(ScrollEvent event) {
        System.out.println("OnScrollFinishedChoiceBox");
        //redraw();
    }

    @FXML
    void OnTouchReleasedChoiceBox(TouchEvent event) {
        System.out.println("OnTouchReleasedChoiceBox");
        //redraw();
    }

    @FXML
    void OnDragDoneChoiceBox(DragEvent event) {
        System.out.println("OnDragDoneChoiceBox");
        //redraw();
    }

    @FXML
    void OnDragEnteredChoiceBox(DragEvent event) {
        System.out.println("OnDragEnteredChoiceBox");
        //redraw();
    }

    @FXML
    void OnMouseEnteredChoiceBox(MouseEvent event) {
        System.out.println("OnMouseEnteredChoiceBox");
        //redraw();
    }

    @FXML
    void OnContextMenuRequestedChoiceBox(ContextMenuEvent event) {
        System.out.println("OnContextMenuRequestedChoiceBox");
    }

    @FXML
    void OnMouseReleasedLine(MouseEvent event) {
        System.out.println("OnMouseReleasedLine");
        redraw();
    }

    @FXML
    void OnMouseReleasedZoom(MouseEvent event) {
        System.out.println("OnMouseReleasedZoom");
        redraw();
    }


    @FXML
    void OnKeyReleasedTextAria(KeyEvent event) {
        redraw();
    }

    @FXML
    private void initialize() {
        System.out.println("initialize");
        choseBox.setItems(FontList);
        choseBox.getSelectionModel().selectFirst();
        curFontNumber="88";
        cleanCanva();
    }

    public  void redraw() {
        System.setProperty("console.encoding","utf-8");

        String desFontNumber=choseBox.getValue();
        System.out.println("Desie font number: "+desFontNumber);

        if(Integer.parseInt(curFontNumber)!=Integer.parseInt(desFontNumber)){
            System.out.println("Generate font table");
            generate_font_table(choseBox.getValue());
            curFontNumber = desFontNumber;
        }


        cleanCanva();
        //zoom=0.4;
        zoom=10.0*slider.getValue();
        //System.out.println(slider.getValue());
        //slider.getValue();
        int chi;

        String text = textArea.getText();
        //System.out.print(text);


        try{
            FileWriter writer = new FileWriter(inputFile, false);
            writer.write(text);
            writer.close();
        }
        catch(IOException ex){
            System.out.print("Write Error");
        }


        System.out.print("composeSVGfile");
        composeSVGfile(desFontNumber);

        init();

        System.out.println("draw a svg file on canva");
        //draw a svg file on canva

        draw_svg_on_canva(outSvgFileName);

        // System.out.println("\n\nDone!");
        // очистить файл
        try{
            FileWriter writer2 = new FileWriter(inputFile, false);
            writer2.write("");
            writer2.close();
        }
        catch(IOException ex){
            System.out.print("Write Error");
        }


    }

    @FXML
    public void pressBtn(ActionEvent event){
        redraw();
    }


    public void draw_svg_on_canva(String SvgFileName) {
        int chi;
        try {
            BufferedReader in = new BufferedReader(new FileReader(SvgFileName));
            String s;
            // calc the amount of byte in file
            do{
                chi = in.read();
                //System.out.print((char)chi);
                input(chi);
                proc();
            }while (chi != (-1));
            in.close();
            System.out.println("draw  done");
        } catch(IOException e) {
            System.out.println("\n\nError draw svg file on canva!");
        }

    }

    @FXML
    void pressSvgBtn(ActionEvent event) {
        String cmdLine = "C:\\Program Files\\Inkscape\\inkscape.exe "+outSvgFileName ;
        try{
            System.out.println(cmdLine);
            Process p = Runtime.getRuntime().exec(cmdLine );
        }catch(IOException ехс){
            System.out.println("Error");
            return;
        }
    }

    public void generate_font_table(String fontNumber){

        // write your code here
        System.setProperty("console.encoding","utf-8");

        String lineStr;
        String fontVectorsStr="";
        int recordingEnable=0;

        System.out.println("Font number: "+fontNumber);
        String folderFontsAddr = "C:\\!!docs\\1_my_prj\\1_plotter\\3_Fonts\\"+fontNumber+"_vectorFonts\\";
        String folderCapitalAddress = folderFontsAddr+"chars\\2_capital_letters\\";
        String folderLittleAddress = folderFontsAddr+"chars\\1_lowercase_letters\\";

        System.out.println(folderCapitalAddress);
        System.out.println(folderLittleAddress);

        String folderDigitAddress = folderFontsAddr+"digits\\";

        String inPutFileAdderssАrus = folderCapitalAddress+"АРус.svg";
        String inPutFileAdderssБ = folderCapitalAddress+"Б.svg";
        String inPutFileAdderssВ = folderCapitalAddress+"В.svg";
        String inPutFileAdderssГ = folderCapitalAddress+"Г.svg";
        String inPutFileAdderssД = folderCapitalAddress+"Д.svg";
        String inPutFileAdderssЕ = folderCapitalAddress+"Е.svg";
        String inPutFileAdderssЁcap = folderCapitalAddress+"Ё.svg";
        String inPutFileAdderssЖ = folderCapitalAddress+"Ж.svg";
        String inPutFileAdderssЗ = folderCapitalAddress+"З.svg";
        String inPutFileAdderssИ = folderCapitalAddress+"И.svg";
        String inPutFileAdderssЙcap = folderCapitalAddress+"Й.svg";
        String inPutFileAdderssК = folderCapitalAddress+"К.svg";
        String inPutFileAdderssЛ = folderCapitalAddress+"Л.svg";
        String inPutFileAdderssМ = folderCapitalAddress+"М.svg";
        String inPutFileAdderssН = folderCapitalAddress+"Н.svg";
        String inPutFileAdderssО = folderCapitalAddress+"О.svg";
        String inPutFileAdderssП = folderCapitalAddress+"П.svg";
        String inPutFileAdderssР = folderCapitalAddress+"Р.svg";
        String inPutFileAdderssС = folderCapitalAddress+"С.svg";
        String inPutFileAdderssТ = folderCapitalAddress+"Т.svg";
        String inPutFileAdderssУ = folderCapitalAddress+"У.svg";
        String inPutFileAdderssФ = folderCapitalAddress+"Ф.svg";
        String inPutFileAdderssХ = folderCapitalAddress+"Х.svg";
        String inPutFileAdderssЦ = folderCapitalAddress+"Ц.svg";
        String inPutFileAdderssЧ = folderCapitalAddress+"Ч.svg";
        String inPutFileAdderssШ = folderCapitalAddress+"Ш.svg";
        String inPutFileAdderssЩ = folderCapitalAddress+"Щ.svg";
        String inPutFileAdderssЪ = folderCapitalAddress+"Ъ.svg";
        String inPutFileAdderssЫ = folderCapitalAddress+"Ы.svg";
        String inPutFileAdderssЬ = folderCapitalAddress+"Ь.svg";
        String inPutFileAdderssЭ = folderCapitalAddress+"Э.svg";
        String inPutFileAdderssЮ = folderCapitalAddress+"Ю.svg";
        String inPutFileAdderssЯ = folderCapitalAddress+"Я.svg";

        String inPutFileAdderssA = folderCapitalAddress+"A.svg";
        String inPutFileAdderssB = folderCapitalAddress+"B.svg";
        String inPutFileAdderssC = folderCapitalAddress+"C.svg";
        String inPutFileAdderssD = folderCapitalAddress+"D.svg";
        String inPutFileAdderssE = folderCapitalAddress+"E.svg";
        String inPutFileAdderssF = folderCapitalAddress+"F.svg";
        String inPutFileAdderssG = folderCapitalAddress+"G.svg";
        String inPutFileAdderssH = folderCapitalAddress+"H.svg";
        String inPutFileAdderssI = folderCapitalAddress+"I.svg";
        String inPutFileAdderssJ = folderCapitalAddress+"J.svg";
        String inPutFileAdderssK = folderCapitalAddress+"K.svg";
        String inPutFileAdderssL = folderCapitalAddress+"L.svg";
        String inPutFileAdderssM = folderCapitalAddress+"M.svg";
        String inPutFileAdderssN = folderCapitalAddress+"N.svg";
        String inPutFileAdderssO = folderCapitalAddress+"O.svg";
        String inPutFileAdderssP = folderCapitalAddress+"P.svg";
        String inPutFileAdderssQ = folderCapitalAddress+"Q.svg";
        String inPutFileAdderssR = folderCapitalAddress+"R.svg";
        String inPutFileAdderssS = folderCapitalAddress+"S.svg";
        String inPutFileAdderssT = folderCapitalAddress+"T.svg";
        String inPutFileAdderssU = folderCapitalAddress+"U.svg";
        String inPutFileAdderssV = folderCapitalAddress+"V.svg";
        String inPutFileAdderssW = folderCapitalAddress+"W.svg";
        String inPutFileAdderssX = folderCapitalAddress+"X.svg";
        String inPutFileAdderssY = folderCapitalAddress+"Y.svg";
        String inPutFileAdderssZ = folderCapitalAddress+"Z.svg";

        String inPutFileAdderssаRus = folderLittleAddress+"арус.svg";
        String inPutFileAdderssб = folderLittleAddress+"б.svg";
        String inPutFileAdderssв = folderLittleAddress+"в.svg";
        String inPutFileAdderssг = folderLittleAddress+"г.svg";
        String inPutFileAdderssд = folderLittleAddress+"д.svg";
        String inPutFileAdderssе = folderLittleAddress+"е.svg";
        String inPutFileAdderssё = folderLittleAddress+"ё.svg";
        String inPutFileAdderssж = folderLittleAddress+"ж.svg";
        String inPutFileAdderssз = folderLittleAddress+"з.svg";
        String inPutFileAdderssи = folderLittleAddress+"и.svg";
        String inPutFileAdderssй = folderLittleAddress+"й.svg";
        String inPutFileAdderssк = folderLittleAddress+"к.svg";
        String inPutFileAdderssл = folderLittleAddress+"л.svg";
        String inPutFileAdderssм = folderLittleAddress+"м.svg";
        String inPutFileAdderssн = folderLittleAddress+"н.svg";
        String inPutFileAdderssо = folderLittleAddress+"о.svg";
        String inPutFileAdderssп = folderLittleAddress+"п.svg";
        String inPutFileAdderssр = folderLittleAddress+"р.svg";
        String inPutFileAdderssс = folderLittleAddress+"с.svg";
        String inPutFileAdderssт = folderLittleAddress+"т.svg";
        String inPutFileAdderssу = folderLittleAddress+"у.svg";
        String inPutFileAdderssф = folderLittleAddress+"ф.svg";
        String inPutFileAdderssх = folderLittleAddress+"х.svg";
        String inPutFileAdderssц = folderLittleAddress+"ц.svg";
        String inPutFileAdderssч = folderLittleAddress+"ч.svg";
        String inPutFileAdderssш = folderLittleAddress+"ш.svg";
        String inPutFileAdderssщ = folderLittleAddress+"щ.svg";
        String inPutFileAdderssъ = folderLittleAddress+"ъ.svg";
        String inPutFileAdderssы = folderLittleAddress+"ы.svg";
        String inPutFileAdderssь = folderLittleAddress+"ь.svg";
        String inPutFileAdderssэ = folderLittleAddress+"э.svg";
        String inPutFileAdderssю = folderLittleAddress+"ю.svg";
        String inPutFileAdderssя = folderLittleAddress+"я.svg";

        String inPutFileAdderssaEng = folderLittleAddress+"aEng.svg";
        String inPutFileAdderssb = folderLittleAddress+"b.svg";
        String inPutFileAdderssc = folderLittleAddress+"c.svg";
        String inPutFileAdderssd = folderLittleAddress+"d.svg";
        String inPutFileAddersse = folderLittleAddress+"e.svg";
        String inPutFileAdderssf = folderLittleAddress+"f.svg";
        String inPutFileAdderssg = folderLittleAddress+"g.svg";
        String inPutFileAdderssh = folderLittleAddress+"h.svg";
        String inPutFileAdderssi = folderLittleAddress+"i.svg";
        String inPutFileAdderssj = folderLittleAddress+"j.svg";
        String inPutFileAdderssk = folderLittleAddress+"k.svg";
        String inPutFileAdderssl = folderLittleAddress+"l.svg";
        String inPutFileAdderssm = folderLittleAddress+"m.svg";
        String inPutFileAdderssn = folderLittleAddress+"n.svg";
        String inPutFileAddersso = folderLittleAddress+"o.svg";
        String inPutFileAdderssp = folderLittleAddress+"p.svg";
        String inPutFileAdderssq = folderLittleAddress+"q.svg";
        String inPutFileAdderssr = folderLittleAddress+"r.svg";
        String inPutFileAddersss = folderLittleAddress+"s.svg";
        String inPutFileAddersst = folderLittleAddress+"t.svg";
        String inPutFileAdderssu = folderLittleAddress+"u.svg";
        String inPutFileAdderssv = folderLittleAddress+"v.svg";
        String inPutFileAdderssw = folderLittleAddress+"w.svg";
        String inPutFileAdderssx = folderLittleAddress+"x.svg";
        String inPutFileAdderssy = folderLittleAddress+"y.svg";
        String inPutFileAdderssz = folderLittleAddress+"z.svg";

        String inPutFileAdderssQuotes = folderLittleAddress+"quotes.svg";
        String inPutFileAdderssColon = folderLittleAddress+"colon.svg";
        String inPutFileAdderssComma = folderLittleAddress+"comma.svg";
        String inPutFileAdderssDot = folderLittleAddress+"dot.svg";
        String inPutFileAdderssExclamationMark = folderLittleAddress+"ExclamationMark.svg";
        String inPutFileAdderssQuestionMark = folderLittleAddress+"QuestionMark.svg";
        String inPutFileAdderssDash= folderLittleAddress+"-.svg";
        String inPutFileAdderssreaderOpenBracket=folderLittleAddress+"(.svg";
        String inPutFileAdderssreaderClosedBracket=folderLittleAddress+").svg";

        String inPutFileAdderss0 = folderDigitAddress+"0.svg";
        String inPutFileAdderss1 = folderDigitAddress+"1.svg";
        String inPutFileAdderss2 = folderDigitAddress+"2.svg";
        String inPutFileAdderss3 = folderDigitAddress+"3.svg";
        String inPutFileAdderss4 = folderDigitAddress+"4.svg";
        String inPutFileAdderss5 = folderDigitAddress+"5.svg";
        String inPutFileAdderss6 = folderDigitAddress+"6.svg";
        String inPutFileAdderss7 = folderDigitAddress+"7.svg";
        String inPutFileAdderss8 = folderDigitAddress+"8.svg";
        String inPutFileAdderss9 = folderDigitAddress+"9.svg";

        String outPutFontTable = "C:\\!!docs\\1_my_prj\\1_plotter\\2_txtToSvg\\FontTableAuto.txt";
        BufferedWriter fontTableFileId = null;

        try{
            fontTableFileId = new BufferedWriter(new FileWriter(outPutFontTable));


            //_______________________________________________________________

            try{
                fontVectorsStr="";
                BufferedReader readerQuotes = new BufferedReader(new FileReader(inPutFileAdderssQuotes));
                do {
                    lineStr = readerQuotes.readLine();
                    if (null != lineStr) {
                        if(lineStr.contains("<path")){
                            recordingEnable=1;
                        }

                        if(lineStr.contains("</g>")){
                            recordingEnable=0;
                        }
                        if(1==recordingEnable)
                        {
                            fontVectorsStr=fontVectorsStr+lineStr;
                        }

                    }else{
                        //System.out.println("numOfSamplesInWavFile: "+numOfSamplesInWavFile);
                    }
                }while(null!=lineStr);
                readerQuotes.close();
                fontTableFileId.write("{\"}"+fontVectorsStr+"\n");
            }catch(IOException ехс){
                System.out.println("quotes error");
            }


            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerColon = new BufferedReader(new FileReader(inPutFileAdderssColon));
            do {
                lineStr = readerColon.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerColon.close();
            fontTableFileId.write("{:}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerZ = new BufferedReader(new FileReader(inPutFileAdderssZ));
            do {
                lineStr = readerZ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerZ.close();
            fontTableFileId.write("{Z}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerY = new BufferedReader(new FileReader(inPutFileAdderssY));
            do {
                lineStr = readerY.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerY.close();
            fontTableFileId.write("{Y}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerX = new BufferedReader(new FileReader(inPutFileAdderssX));
            do {
                lineStr = readerX.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerX.close();
            fontTableFileId.write("{X}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerW = new BufferedReader(new FileReader(inPutFileAdderssW));
            do {
                lineStr = readerW.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerW.close();
            fontTableFileId.write("{W}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerV = new BufferedReader(new FileReader(inPutFileAdderssV));
            do {
                lineStr = readerV.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerV.close();
            fontTableFileId.write("{V}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerU = new BufferedReader(new FileReader(inPutFileAdderssU));
            do {
                lineStr = readerU.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerU.close();
            fontTableFileId.write("{U}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerT = new BufferedReader(new FileReader(inPutFileAdderssT));
            do {
                lineStr = readerT.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerT.close();
            fontTableFileId.write("{T}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerS = new BufferedReader(new FileReader(inPutFileAdderssS));
            do {
                lineStr = readerS.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerS.close();
            fontTableFileId.write("{S}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerR = new BufferedReader(new FileReader(inPutFileAdderssR));
            do {
                lineStr = readerR.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerR.close();
            fontTableFileId.write("{R}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerQ = new BufferedReader(new FileReader(inPutFileAdderssQ));
            do {
                lineStr = readerQ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerQ.close();
            fontTableFileId.write("{Q}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerP = new BufferedReader(new FileReader(inPutFileAdderssP));
            do {
                lineStr = readerP.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerP.close();
            fontTableFileId.write("{P}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerO = new BufferedReader(new FileReader(inPutFileAdderssO));
            do {
                lineStr = readerO.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerO.close();
            fontTableFileId.write("{O}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerN = new BufferedReader(new FileReader(inPutFileAdderssN));
            do {
                lineStr = readerN.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerN.close();
            fontTableFileId.write("{N}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerM = new BufferedReader(new FileReader(inPutFileAdderssM));
            do {
                lineStr = readerM.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerM.close();
            fontTableFileId.write("{M}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerL = new BufferedReader(new FileReader(inPutFileAdderssL));
            do {
                lineStr = readerL.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerL.close();
            fontTableFileId.write("{L}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerK = new BufferedReader(new FileReader(inPutFileAdderssK));
            do {
                lineStr = readerK.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerK.close();
            fontTableFileId.write("{K}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerJ = new BufferedReader(new FileReader(inPutFileAdderssJ));
            do {
                lineStr = readerJ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerJ.close();
            fontTableFileId.write("{J}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerI = new BufferedReader(new FileReader(inPutFileAdderssI));
            do {
                lineStr = readerI.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerI.close();
            fontTableFileId.write("{I}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerH = new BufferedReader(new FileReader(inPutFileAdderssH));
            do {
                lineStr = readerH.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerH.close();
            fontTableFileId.write("{H}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerG = new BufferedReader(new FileReader(inPutFileAdderssG));
            do {
                lineStr = readerG.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerG.close();
            fontTableFileId.write("{G}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerF = new BufferedReader(new FileReader(inPutFileAdderssF));
            do {
                lineStr = readerF.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerF.close();
            fontTableFileId.write("{F}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerE = new BufferedReader(new FileReader(inPutFileAdderssE));
            do {
                lineStr = readerE.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerE.close();
            fontTableFileId.write("{E}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerD = new BufferedReader(new FileReader(inPutFileAdderssD));
            do {
                lineStr = readerD.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerD.close();
            fontTableFileId.write("{D}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerC = new BufferedReader(new FileReader(inPutFileAdderssC));
            do {
                lineStr = readerC.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerC.close();
            fontTableFileId.write("{C}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            try{
                fontVectorsStr="";
                BufferedReader readerB = new BufferedReader(new FileReader(inPutFileAdderssB));
                do {
                    lineStr = readerB.readLine();
                    if (null != lineStr) {
                        if(lineStr.contains("<path")){
                            recordingEnable=1;
                        }
                        if (lineStr.contains("</g>")) {
                            recordingEnable=0;
                        }
                        if (1==recordingEnable) {
                            fontVectorsStr=fontVectorsStr+lineStr;
                        }
                    }
                }while(null!=lineStr);
                readerB.close();
                fontTableFileId.write("{B}"+fontVectorsStr+"\n");
            }catch(IOException ехс){
                System.out.println("B eng error");
            }


            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerA = new BufferedReader(new FileReader(inPutFileAdderssA));
            do {
                lineStr = readerA.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerA.close();
            fontTableFileId.write("{A}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            try{
                fontVectorsStr="";
                BufferedReader readerz = new BufferedReader(new FileReader(inPutFileAdderssz));
                do {
                    lineStr = readerz.readLine();
                    if (null != lineStr) {
                        if(lineStr.contains("<path")){
                            recordingEnable=1;
                        }
                        if (lineStr.contains("</g>")) {
                            recordingEnable=0;
                        }
                        if (1==recordingEnable) {
                            fontVectorsStr=fontVectorsStr+lineStr;
                        }
                    }
                }while(null!=lineStr);
                readerz.close();
                fontTableFileId.write("{z}"+fontVectorsStr+"\n");
            }catch(IOException ехс){
                System.out.println("z eng error");
            }

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readery = new BufferedReader(new FileReader(inPutFileAdderssy));
            do {
                lineStr = readery.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readery.close();
            fontTableFileId.write("{y}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerx = new BufferedReader(new FileReader(inPutFileAdderssx));
            do {
                lineStr = readerx.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerx.close();
            fontTableFileId.write("{x}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerw = new BufferedReader(new FileReader(inPutFileAdderssw));
            do {
                lineStr = readerw.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerw.close();
            fontTableFileId.write("{w}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerv = new BufferedReader(new FileReader(inPutFileAdderssv));
            do {
                lineStr = readerv.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerv.close();
            fontTableFileId.write("{v}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readeru = new BufferedReader(new FileReader(inPutFileAdderssu));
            do {
                lineStr = readeru.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readeru.close();
            fontTableFileId.write("{u}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readert = new BufferedReader(new FileReader(inPutFileAddersst));
            do {
                lineStr = readert.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readert.close();
            fontTableFileId.write("{t}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readers = new BufferedReader(new FileReader(inPutFileAddersss));
            do {
                lineStr = readers.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readers.close();
            fontTableFileId.write("{s}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerr = new BufferedReader(new FileReader(inPutFileAdderssr));
            do {
                lineStr = readerr.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerr.close();
            fontTableFileId.write("{r}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerq = new BufferedReader(new FileReader(inPutFileAdderssq));
            do {
                lineStr = readerq.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerq.close();
            fontTableFileId.write("{q}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerp = new BufferedReader(new FileReader(inPutFileAdderssp));
            do {
                lineStr = readerp.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerp.close();
            fontTableFileId.write("{p}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readero = new BufferedReader(new FileReader(inPutFileAddersso));
            do {
                lineStr = readero.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readero.close();
            fontTableFileId.write("{o}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readern = new BufferedReader(new FileReader(inPutFileAdderssn));
            do {
                lineStr = readern.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readern.close();
            fontTableFileId.write("{n}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerm = new BufferedReader(new FileReader(inPutFileAdderssm));
            do {
                lineStr = readerm.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerm.close();
            fontTableFileId.write("{m}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerl = new BufferedReader(new FileReader(inPutFileAdderssl));
            do {
                lineStr = readerl.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerl.close();
            fontTableFileId.write("{l}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerk = new BufferedReader(new FileReader(inPutFileAdderssk));
            do {
                lineStr = readerk.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerk.close();
            fontTableFileId.write("{k}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerj = new BufferedReader(new FileReader(inPutFileAdderssj));
            do {
                lineStr = readerj.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerj.close();
            fontTableFileId.write("{j}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readeri = new BufferedReader(new FileReader(inPutFileAdderssi));
            do {
                lineStr = readeri.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readeri.close();
            fontTableFileId.write("{i}"+fontVectorsStr+"\n");


            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerh = new BufferedReader(new FileReader(inPutFileAdderssh));
            do {
                lineStr = readerh.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerh.close();
            fontTableFileId.write("{h}"+fontVectorsStr+"\n");


            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerg = new BufferedReader(new FileReader(inPutFileAdderssg));
            do {
                lineStr = readerg.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerg.close();
            fontTableFileId.write("{g}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerf = new BufferedReader(new FileReader(inPutFileAdderssf));
            do {
                lineStr = readerf.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerf.close();
            fontTableFileId.write("{f}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readere = new BufferedReader(new FileReader(inPutFileAddersse));
            do {
                lineStr = readere.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readere.close();
            fontTableFileId.write("{e}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerd = new BufferedReader(new FileReader(inPutFileAdderssd));
            do {
                lineStr = readerd.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerd.close();
            fontTableFileId.write("{d}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerc = new BufferedReader(new FileReader(inPutFileAdderssc));
            do {
                lineStr = readerc.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerc.close();
            fontTableFileId.write("{c}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerb = new BufferedReader(new FileReader(inPutFileAdderssb));
            do {
                lineStr = readerb.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if (lineStr.contains("</g>")) {
                        recordingEnable=0;
                    }
                    if (1==recordingEnable) {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerb.close();
            fontTableFileId.write("{b}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readera = new BufferedReader(new FileReader( inPutFileAdderssaEng));
            do {
                lineStr = readera.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readera.close();
            fontTableFileId.write("{a}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerDigit9 = new BufferedReader(new FileReader(inPutFileAdderss9));
            do {
                lineStr = readerDigit9.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerDigit9.close();
            fontTableFileId.write("{9}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerDigit8 = new BufferedReader(new FileReader(inPutFileAdderss8));
            do {
                lineStr = readerDigit8.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerDigit8.close();
            fontTableFileId.write("{8}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerDigit7 = new BufferedReader(new FileReader(inPutFileAdderss7));
            do {
                lineStr = readerDigit7.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerDigit7.close();
            fontTableFileId.write("{7}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerDigit6 = new BufferedReader(new FileReader(inPutFileAdderss6));
            do {
                lineStr = readerDigit6.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerDigit6.close();
            fontTableFileId.write("{6}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerDigit5 = new BufferedReader(new FileReader(inPutFileAdderss5));
            do {
                lineStr = readerDigit5.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerDigit5.close();
            fontTableFileId.write("{5}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerDigit4 = new BufferedReader(new FileReader(inPutFileAdderss4));
            do {
                lineStr = readerDigit4.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerDigit4.close();
            fontTableFileId.write("{4}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerDigit3 = new BufferedReader(new FileReader(inPutFileAdderss3));
            do {
                lineStr = readerDigit3.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerDigit3.close();
            fontTableFileId.write("{3}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerDigit2 = new BufferedReader(new FileReader(inPutFileAdderss2));
            do {
                lineStr = readerDigit2.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerDigit2.close();
            fontTableFileId.write("{2}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerDigit1 = new BufferedReader(new FileReader(inPutFileAdderss1));
            do {
                lineStr = readerDigit1.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerDigit1.close();
            fontTableFileId.write("{1}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerDigit0 = new BufferedReader(new FileReader(inPutFileAdderss0));
            do {
                lineStr = readerDigit0.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerDigit0.close();
            fontTableFileId.write("{0}"+fontVectorsStr+"\n");



            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerQuestionOpenBracket  = new BufferedReader(new FileReader(inPutFileAdderssreaderOpenBracket));
            do {
                lineStr = readerQuestionOpenBracket.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerQuestionOpenBracket.close();
            fontTableFileId.write("{(}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerQuestionClosedBracket  = new BufferedReader(new FileReader(inPutFileAdderssreaderClosedBracket));
            do {
                lineStr = readerQuestionClosedBracket.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerQuestionClosedBracket.close();
            fontTableFileId.write("{)}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerQuestionDash = new BufferedReader(new FileReader(inPutFileAdderssDash));
            do {
                lineStr = readerQuestionDash.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerQuestionDash.close();
            fontTableFileId.write("{-}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerQuestionComma = new BufferedReader(new FileReader(inPutFileAdderssComma));
            do {
                lineStr = readerQuestionComma.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerQuestionComma.close();
            fontTableFileId.write("{,}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerQuestionMark = new BufferedReader(new FileReader(inPutFileAdderssQuestionMark));
            do {
                lineStr = readerQuestionMark.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerQuestionMark.close();
            fontTableFileId.write("{?}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerExclamationMark = new BufferedReader(new FileReader(inPutFileAdderssExclamationMark));
            do {
                lineStr = readerExclamationMark.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerExclamationMark.close();
            fontTableFileId.write("{!}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerdot = new BufferedReader(new FileReader(inPutFileAdderssDot));
            do {
                lineStr = readerdot.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerdot.close();
            fontTableFileId.write("{.}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            try{
                fontVectorsStr="";
                BufferedReader readerАcap = new BufferedReader(new FileReader(inPutFileAdderssАrus));
                do {
                    lineStr = readerАcap.readLine();
                    if (null != lineStr) {
                        if(lineStr.contains("<path")){
                            recordingEnable=1;
                        }

                        if(lineStr.contains("</g>")){
                            recordingEnable=0;
                        }
                        if(1==recordingEnable)
                        {
                            fontVectorsStr=fontVectorsStr+lineStr;
                        }

                    }else{
                        //System.out.println("numOfSamplesInWavFile: "+numOfSamplesInWavFile);
                    }
                }while(null!=lineStr);
                readerАcap.close();
                fontTableFileId.write("{А}"+fontVectorsStr+"\n");
            }catch(IOException ехс){
                System.out.println("А rus error");
            }



            fontVectorsStr="";
            BufferedReader readerБ = new BufferedReader(new FileReader(inPutFileAdderssБ));
            do {
                lineStr = readerБ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }

                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable)
                    {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerБ.close();
            fontTableFileId.write("{Б}"+fontVectorsStr+"\n");

            //_______________________________________________________________

            try{

                fontVectorsStr="";
                BufferedReader readerВ = new BufferedReader(new FileReader(inPutFileAdderssВ));
                do {
                    lineStr = readerВ.readLine();
                    if (null != lineStr) {
                        if(lineStr.contains("<path")){
                            recordingEnable=1;
                        }
                        if(lineStr.contains("</g>")){
                            recordingEnable=0;
                        }
                        if(1==recordingEnable){
                            fontVectorsStr=fontVectorsStr+lineStr;
                        }
                    }
                }while(null!=lineStr);
                readerВ.close();
                fontTableFileId.write("{В}"+fontVectorsStr+"\n");

            }catch(IOException ехс){
                System.out.println("В error");
            }

            //_______________________________________________________________
            try{
                fontVectorsStr="";
                BufferedReader readerГ = new BufferedReader(new FileReader(inPutFileAdderssГ));
                do {
                    lineStr = readerГ.readLine();
                    if (null != lineStr) {
                        if(lineStr.contains("<path")){
                            recordingEnable=1;
                        }
                        if(lineStr.contains("</g>")){
                            recordingEnable=0;
                            lineStr = null;
                        }
                        if(1==recordingEnable){
                            fontVectorsStr=fontVectorsStr+lineStr;
                        }
                    }
                }while(null!=lineStr);
                readerГ.close();
                fontTableFileId.write("{Г}"+fontVectorsStr+"\n");
            }catch(IOException ехс){
                System.out.println("Г error");
            }

            //_______________________________________________________________

            try{
                fontVectorsStr="";
                BufferedReader readerД = new BufferedReader(new FileReader(inPutFileAdderssД));
                do {
                    lineStr = readerД.readLine();
                    if (null != lineStr) {
                        if(lineStr.contains("<path")){
                            recordingEnable=1;
                        }
                        if(lineStr.contains("</g>")){
                            recordingEnable=0;
                        }
                        if(1==recordingEnable){
                            fontVectorsStr=fontVectorsStr+lineStr;
                        }
                    }
                }while(null!=lineStr);
                readerД.close();
                fontTableFileId.write("{Д}"+fontVectorsStr+"\n");
            }catch(IOException ехс){
                System.out.println("Д error");
            }



            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЕ = new BufferedReader(new FileReader(inPutFileAdderssЕ));
            do {
                lineStr = readerЕ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЕ.close();
            fontTableFileId.write("{Е}"+fontVectorsStr+"\n");

            try{
                fontVectorsStr="";
                BufferedReader readerЁcap = new BufferedReader(new FileReader(inPutFileAdderssЁcap));
                do {
                    lineStr = readerЁcap.readLine();
                    if (null != lineStr) {
                        if(lineStr.contains("<path")){
                            recordingEnable=1;
                        }
                        if(lineStr.contains("</g>")){
                            recordingEnable=0;
                            lineStr =null;
                        }
                        if(1==recordingEnable){
                            fontVectorsStr=fontVectorsStr+lineStr;
                        }
                    }
                }while(null!=lineStr);
                readerЁcap.close();
                fontTableFileId.write("{Ё}"+fontVectorsStr+"\n");
            }catch(IOException ехс){
                System.out.println("Ё error");
            }




            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЖ = new BufferedReader(new FileReader(inPutFileAdderssЖ));
            do {
                lineStr = readerЖ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                        lineStr =null;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЖ.close();
            fontTableFileId.write("{Ж}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЗ = new BufferedReader(new FileReader(inPutFileAdderssЗ));
            do {
                lineStr = readerЗ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЗ.close();
            fontTableFileId.write("{З}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            try{
                fontVectorsStr="";
                BufferedReader readerИ = new BufferedReader(new FileReader(inPutFileAdderssИ));
                do {
                    lineStr = readerИ.readLine();
                    if (null != lineStr) {
                        if(lineStr.contains("<path")){
                            recordingEnable=1;
                        }
                        if(lineStr.contains("</g>")){
                            recordingEnable=0;
                        }
                        if(1==recordingEnable){
                            fontVectorsStr=fontVectorsStr+lineStr;
                        }
                    }
                }while(null!=lineStr);
                readerИ.close();
                fontTableFileId.write("{И}"+fontVectorsStr+"\n");
            }catch(IOException ехс){
                System.out.println("И error");
            }



            //_______________________________________________________________
            try{
                fontVectorsStr="";
                BufferedReader readerЙcap = new BufferedReader(new FileReader(inPutFileAdderssЙcap));
                do {
                    lineStr = readerЙcap.readLine();
                    if (null != lineStr) {
                        if(lineStr.contains("<path")){
                            recordingEnable=1;
                        }
                        if(lineStr.contains("</g>")){
                            recordingEnable=0;
                        }
                        if(1==recordingEnable){
                            fontVectorsStr=fontVectorsStr+lineStr;
                        }
                    }
                }while(null!=lineStr);
                readerЙcap.close();
                fontTableFileId.write("{Й}"+fontVectorsStr+"\n");
            }catch(IOException ехс){
                System.out.println("Й error");
            }

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerК = new BufferedReader(new FileReader(inPutFileAdderssК));
            do {
                lineStr = readerК.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerК.close();
            fontTableFileId.write("{К}"+fontVectorsStr+"\n");


            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЛ = new BufferedReader(new FileReader(inPutFileAdderssЛ));
            do {
                lineStr = readerЛ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЛ.close();
            fontTableFileId.write("{Л}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerМ = new BufferedReader(new FileReader(inPutFileAdderssМ));
            do {
                lineStr = readerМ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerМ.close();
            fontTableFileId.write("{М}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerН = new BufferedReader(new FileReader(inPutFileAdderssН));
            do {
                lineStr = readerН.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerН.close();
            fontTableFileId.write("{Н}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerО = new BufferedReader(new FileReader(inPutFileAdderssО));
            do {
                lineStr = readerО.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerО.close();
            fontTableFileId.write("{О}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerП = new BufferedReader(new FileReader(inPutFileAdderssП));
            do {
                lineStr = readerП.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerП.close();
            fontTableFileId.write("{П}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerР = new BufferedReader(new FileReader(inPutFileAdderssР));
            do {
                lineStr = readerР.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerР.close();
            fontTableFileId.write("{Р}"+fontVectorsStr+"\n");


            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerС = new BufferedReader(new FileReader(inPutFileAdderssС));
            do {
                lineStr = readerС.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerС.close();
            fontTableFileId.write("{С}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerТ = new BufferedReader(new FileReader(inPutFileAdderssТ));
            do {
                lineStr = readerТ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerТ.close();
            fontTableFileId.write("{Т}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerУ = new BufferedReader(new FileReader(inPutFileAdderssУ));
            do {
                lineStr = readerУ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerУ.close();
            fontTableFileId.write("{У}"+fontVectorsStr+"\n");


            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerФ = new BufferedReader(new FileReader(inPutFileAdderssФ));
            do {
                lineStr = readerФ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerФ.close();
            fontTableFileId.write("{Ф}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerХ = new BufferedReader(new FileReader(inPutFileAdderssХ));
            do {
                lineStr = readerХ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerХ.close();
            fontTableFileId.write("{Х}"+fontVectorsStr+"\n");


            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЦ = new BufferedReader(new FileReader(inPutFileAdderssЦ));
            do {
                lineStr = readerЦ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЦ.close();
            fontTableFileId.write("{Ц}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЧ = new BufferedReader(new FileReader(inPutFileAdderssЧ));
            do {
                lineStr = readerЧ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЧ.close();
            fontTableFileId.write("{Ч}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerШ = new BufferedReader(new FileReader(inPutFileAdderssШ));
            do {
                lineStr = readerШ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerШ.close();
            fontTableFileId.write("{Ш}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЩ = new BufferedReader(new FileReader(inPutFileAdderssЩ));
            do {
                lineStr = readerЩ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЩ.close();
            fontTableFileId.write("{Щ}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЪ = new BufferedReader(new FileReader(inPutFileAdderssЪ));
            do {
                lineStr = readerЪ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЪ.close();
            fontTableFileId.write("{Ъ}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЫ = new BufferedReader(new FileReader(inPutFileAdderssЫ));
            do {
                lineStr = readerЫ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЫ.close();
            fontTableFileId.write("{Ы}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЬ = new BufferedReader(new FileReader(inPutFileAdderssЬ));
            do {
                lineStr = readerЬ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЬ.close();
            fontTableFileId.write("{Ь}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЭ = new BufferedReader(new FileReader(inPutFileAdderssЭ));
            do {
                lineStr = readerЭ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЭ.close();
            fontTableFileId.write("{Э}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЮ = new BufferedReader(new FileReader(inPutFileAdderssЮ));
            do {
                lineStr = readerЮ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЮ.close();
            fontTableFileId.write("{Ю}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerЯ = new BufferedReader(new FileReader(inPutFileAdderssЯ));
            do {
                lineStr = readerЯ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerЯ.close();
            fontTableFileId.write("{Я}"+fontVectorsStr+"\n");


            //_____________________________________________________________
            fontVectorsStr="";
            BufferedReader readerа = new BufferedReader(new FileReader(inPutFileAdderssаRus));
            do {
                lineStr = readerа.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }

                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable)
                    {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }

                }else{
                    //System.out.println("numOfSamplesInWavFile: "+numOfSamplesInWavFile);
                }
            }while(null!=lineStr);
            readerа.close();
            fontTableFileId.write("{а}"+fontVectorsStr+"\n");

            fontVectorsStr="";
            BufferedReader readerб = new BufferedReader(new FileReader(inPutFileAdderssб));
            do {
                lineStr = readerб.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }

                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable)
                    {
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerб.close();
            fontTableFileId.write("{б}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerв = new BufferedReader(new FileReader(inPutFileAdderssв));
            do {
                lineStr = readerв.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerв.close();
            fontTableFileId.write("{в}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerг = new BufferedReader(new FileReader(inPutFileAdderssг));
            do {
                lineStr = readerг.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerг.close();
            fontTableFileId.write("{г}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerд = new BufferedReader(new FileReader(inPutFileAdderssд));
            do {
                lineStr = readerд.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerд.close();
            fontTableFileId.write("{д}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerе = new BufferedReader(new FileReader(inPutFileAdderssе));
            do {
                lineStr = readerе.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerе.close();
            fontTableFileId.write("{е}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerё = new BufferedReader(new FileReader(inPutFileAdderssё));
            do {
                lineStr = readerё.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerё.close();
            fontTableFileId.write("{ё}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerж = new BufferedReader(new FileReader(inPutFileAdderssж));
            do {
                lineStr = readerж.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerж.close();
            fontTableFileId.write("{ж}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerз = new BufferedReader(new FileReader(inPutFileAdderssз));
            do {
                lineStr = readerз.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerз.close();
            fontTableFileId.write("{з}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerи = new BufferedReader(new FileReader(inPutFileAdderssи));
            do {
                lineStr = readerи.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerи.close();
            fontTableFileId.write("{и}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerй = new BufferedReader(new FileReader(inPutFileAdderssй));
            do {
                lineStr = readerй.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerй.close();
            fontTableFileId.write("{й}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerк = new BufferedReader(new FileReader(inPutFileAdderssк));
            do {
                lineStr = readerк.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerк.close();
            fontTableFileId.write("{к}"+fontVectorsStr+"\n");


            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerл = new BufferedReader(new FileReader(inPutFileAdderssл));
            do {
                lineStr = readerл.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerл.close();
            fontTableFileId.write("{л}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerм = new BufferedReader(new FileReader(inPutFileAdderssм));
            do {
                lineStr = readerм.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerм.close();
            fontTableFileId.write("{м}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerн = new BufferedReader(new FileReader(inPutFileAdderssн));
            do {
                lineStr = readerн.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerн.close();
            fontTableFileId.write("{н}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerо = new BufferedReader(new FileReader(inPutFileAdderssо));
            do {
                lineStr = readerо.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerо.close();
            fontTableFileId.write("{о}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerп = new BufferedReader(new FileReader(inPutFileAdderssп));
            do {
                lineStr = readerп.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerп.close();
            fontTableFileId.write("{п}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerр = new BufferedReader(new FileReader(inPutFileAdderssр));
            do {
                lineStr = readerр.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerр.close();
            fontTableFileId.write("{р}"+fontVectorsStr+"\n");


            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerс = new BufferedReader(new FileReader(inPutFileAdderssс));
            do {
                lineStr = readerс.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerс.close();
            fontTableFileId.write("{с}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerт = new BufferedReader(new FileReader(inPutFileAdderssт));
            do {
                lineStr = readerт.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerт.close();
            fontTableFileId.write("{т}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerу = new BufferedReader(new FileReader(inPutFileAdderssу));
            do {
                lineStr = readerу.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerу.close();
            fontTableFileId.write("{у}"+fontVectorsStr+"\n");


            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerф = new BufferedReader(new FileReader(inPutFileAdderssф));
            do {
                lineStr = readerф.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerф.close();
            fontTableFileId.write("{ф}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerх = new BufferedReader(new FileReader(inPutFileAdderssх));
            do {
                lineStr = readerх.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerх.close();
            fontTableFileId.write("{х}"+fontVectorsStr+"\n");


            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerц = new BufferedReader(new FileReader(inPutFileAdderssц));
            do {
                lineStr = readerц.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerц.close();
            fontTableFileId.write("{ц}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerч = new BufferedReader(new FileReader(inPutFileAdderssч));
            do {
                lineStr = readerч.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerч.close();
            fontTableFileId.write("{ч}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerш = new BufferedReader(new FileReader(inPutFileAdderssш));
            do {
                lineStr = readerш.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerш.close();
            fontTableFileId.write("{ш}"+fontVectorsStr+"\n");

            //_______________________________________________________________
            fontVectorsStr="";
            BufferedReader readerщ = new BufferedReader(new FileReader(inPutFileAdderssщ));
            do {
                lineStr = readerщ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerщ.close();
            fontTableFileId.write("{щ}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerъ = new BufferedReader(new FileReader(inPutFileAdderssъ));
            do {
                lineStr = readerъ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerъ.close();
            fontTableFileId.write("{ъ}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerы = new BufferedReader(new FileReader(inPutFileAdderssы));
            do {
                lineStr = readerы.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerы.close();
            fontTableFileId.write("{ы}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerь = new BufferedReader(new FileReader(inPutFileAdderssь));
            do {
                lineStr = readerь.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerь.close();
            fontTableFileId.write("{ь}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerэ = new BufferedReader(new FileReader(inPutFileAdderssэ));
            do {
                lineStr = readerэ.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerэ.close();
            fontTableFileId.write("{э}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerю = new BufferedReader(new FileReader(inPutFileAdderssю));
            do {
                lineStr = readerю.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerю.close();
            fontTableFileId.write("{ю}"+fontVectorsStr+"\n");

            //___________________________________________________________________________
            fontVectorsStr="";
            BufferedReader readerя = new BufferedReader(new FileReader(inPutFileAdderssя));
            do {
                lineStr = readerя.readLine();
                if (null != lineStr) {
                    if(lineStr.contains("<path")){
                        recordingEnable=1;
                    }
                    if(lineStr.contains("</g>")){
                        recordingEnable=0;
                    }
                    if(1==recordingEnable){
                        fontVectorsStr=fontVectorsStr+lineStr;
                    }
                }
            }while(null!=lineStr);
            readerя.close();
            fontTableFileId.write("{я}"+fontVectorsStr+"\n");

            fontTableFileId.close();
// open fonts in notepad
            System.out.println("Done!");

            //Runtime runtime = Runtime.getRuntime();
            //Process process = runtime.exec("C:\\Program Files\\Notepad++\\notepad++.exe "+outPutFontTable );


        }catch(IOException ехс){
            System.out.println("Font Table Error");
            return;
        }
    }

    public void cleanCanva(){
        GraphicsContext gc = cnv.getGraphicsContext2D();
        //System.out.println("clean canva ");
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, cnv.getWidth(), cnv.getHeight());

    }
    public void drawLine(int x1,int y1,int x2,int y2) {
        GraphicsContext gc = cnv.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.setLineWidth(0.5);
        gc.strokeLine(x1, y1, x2, y2);
    }

    public int fontSet = 8;//2 3 4 5 12
    public double DistanceBetweenLines=33.0;

    public  boolean isCirrilic(char inChar)  {
        if( 'А'==inChar || 'Б'==inChar || 'В'==inChar || 'Г'==inChar ||
                'Д'==inChar || 'Е'==inChar || 'Ё'==inChar || 'Ж'==inChar || 'З'==inChar ||
                'И'==inChar || 'Й'==inChar || 'К'==inChar || 'Л'==inChar || 'М'==inChar ||
                'Н'==inChar || 'О'==inChar || 'П'==inChar || 'Р'==inChar || 'С'==inChar ||
                'Т'==inChar || 'У'==inChar || 'Ф'==inChar || 'Х'==inChar || 'Ц'==inChar ||
                'Ч'==inChar || 'Ш'==inChar || 'Щ'==inChar || 'Ъ'==inChar || 'Ы'==inChar ||
                'Ь'==inChar || 'Э'==inChar || 'Ю'==inChar || 'Я'==inChar ||
                'а'==inChar || 'б'==inChar || 'в'==inChar || 'г'==inChar || 'д'==inChar ||
                'е'==inChar || 'ё'==inChar || 'ж'==inChar || 'и'==inChar || 'й'==inChar ||
                'к'==inChar || 'л'==inChar || 'м'==inChar || 'н'==inChar || 'о'==inChar ||
                'п'==inChar || 'р'==inChar || 'с'==inChar || 'т'==inChar || 'у'==inChar ||
                'ф'==inChar || 'х'==inChar || 'ц'==inChar || 'ч'==inChar || 'ш'==inChar ||
                'щ'==inChar || 'ъ'==inChar || 'ы'==inChar || 'ь'==inChar || 'э'==inChar ||
                'ю'==inChar || 'я'==inChar
                ) {
            return true;
        }
        return false;
    }

    public static  boolean isCapital(char inChar)    {
        if( 'A'==inChar || 'B'==inChar || 'C'==inChar || 'D'==inChar || 'E'==inChar || 'F'==inChar ||
                'G'==inChar || 'H'==inChar || 'I'==inChar || 'J'==inChar || 'K'==inChar || 'L'==inChar ||
                'M'==inChar || 'N'==inChar || 'O'==inChar || 'P'==inChar || 'Q'==inChar ||
                'R'==inChar || 'S'==inChar || 'T'==inChar || 'V'==inChar || 'U'==inChar ||
                'W'==inChar || 'X'==inChar || 'Y'==inChar || 'Z'==inChar ||
                'А'==inChar || 'Б'==inChar || 'В'==inChar || 'Г'==inChar ||
                'Д'==inChar || 'Е'==inChar || 'Ё'==inChar || 'Ж'==inChar || 'З'==inChar ||
                'И'==inChar || 'Й'==inChar || 'К'==inChar || 'Л'==inChar || 'М'==inChar ||
                'Н'==inChar || 'О'==inChar || 'П'==inChar || 'Р'==inChar || 'С'==inChar ||
                'Т'==inChar || 'У'==inChar || 'Ф'==inChar || 'Х'==inChar || 'Ц'==inChar ||
                'Ч'==inChar || 'Ш'==inChar || 'Щ'==inChar || 'Ъ'==inChar || 'Ы'==inChar ||
                'Ь'==inChar || 'Э'==inChar || 'Ю'==inChar || 'Я'==inChar
                ) {
            return true;
        }
        return false;
    }

    public Double offsetFont1(char inChar) {
        Double xOffSet = 0.0;

        if (isCapital(inChar)) {
            xOffSet = 2.0;
        } else {
            //xOffSet = 2.0;
        }

        if ('a' == inChar) {
            xOffSet += 1.0;
        }

        if ('I' == inChar) {
            xOffSet -= 5.0;
        }

        if ('l' == inChar || 'i' == inChar) {
            xOffSet -= 2.0;
        }
        if ('x' == inChar || 'u' == inChar || 'b' == inChar ||
                'k' == inChar || 's' == inChar || 'n' == inChar || 'y' == inChar) {
            xOffSet += 2.0;
        }
        if ('M' == inChar)
            xOffSet += 5.0;
        if ('w' == inChar)
            xOffSet += 4.0;
        if ('B' == inChar)
            xOffSet += 11.0;
        if ('U' == inChar)
            xOffSet += 11.0;
        if ('V' == inChar)
            xOffSet += 11.0;
        if ('Y' == inChar)
            xOffSet += 11.0;
        if ('D' == inChar)
            xOffSet += 11.0;
        if ('W' == inChar)
            xOffSet += 11.0;
        if ('m' == inChar)
            xOffSet += 5.0;

        //  ----- Rus Capitals
        if ('А' == inChar) xOffSet = 3.5;
        if ('Г' == inChar) xOffSet = 0.0;
        if ('Д' == inChar) xOffSet = 4.0;
        if ('Ж' == inChar) xOffSet = 4.0;
        if ('Л' == inChar) xOffSet = 3.0;
        if ('М' == inChar) xOffSet = 8.0;
        if ('Х' == inChar) xOffSet = 2.0;
        if ('Ш' == inChar) xOffSet = 3.0;
        if ('Щ' == inChar) xOffSet = 3.0;
        if ('Ь' == inChar) xOffSet = 2.0;
        if ('Ы' == inChar) xOffSet = 3.0;
        if ('Ъ' == inChar) xOffSet = 2.0;
        if ( 'Э' == inChar ) xOffSet = 1.0;
        if ( 'Ю' == inChar ) xOffSet = 6.0;
        //  rus lowcase
        if('а' == inChar  ) xOffSet = 2.0;
        if('ж' == inChar  ) xOffSet = 5.0;
        if('и' == inChar  ) xOffSet = 2.5;
        if('й' == inChar  ) xOffSet = 3.0;
        if('к' == inChar  ) xOffSet = 1.0;
        if('м' == inChar  ) xOffSet = 4.0;
        if('п' == inChar  )  xOffSet = 2.0;
        if( 'т' == inChar  ) xOffSet =3.0;
        if ('ц' == inChar)  xOffSet = 2.0;
        if('x' == inChar  ) xOffSet = 6.0;
        if('ш' == inChar  ) xOffSet = 5.0;
        if( 'щ' == inChar ) xOffSet = 7.0;
        if( 'ъ' == inChar ) xOffSet = 0.0;
        if( 'ь' == inChar ) xOffSet = 0.0;
        if ('ю' == inChar)  xOffSet = 3.0;

        //----------------
        if ( '0' == inChar  ) xOffSet = 0.0;
        if ( '1' == inChar  ) xOffSet = -1.0;
        if ( '2' == inChar  ) xOffSet = 0.0;
        if ( '3' == inChar  ) xOffSet = 0.0;
        if ( '4' == inChar  ) xOffSet = 0.0;
        if ( '5' == inChar  ) xOffSet = 0.0;
        if ( '6' == inChar  ) xOffSet = 0.0;
        if ( '7' == inChar  ) xOffSet = 0.0;
        if ( '8' == inChar  ) xOffSet = 0.0;
        if ( '9' == inChar  ) xOffSet = 0.0;
        if ( '0' == inChar  ) xOffSet = 0.0;

        return xOffSet;
    }

    public Double offsetFont2(char inChar) {
        Double xOffSet = 0.0;

        if (isCapital(inChar)) {
            xOffSet = 10.0;
            if(isCirrilic(inChar)){
                xOffSet += 1.0;
            }
        } else {
            // xOffSet = 2.0;
        }

//--------------Eng lower case------------

        if ('a' == inChar) {
            xOffSet = 3.0;
        }
        if ('b' == inChar) {
            xOffSet = 3.0;
        }
        if ('c' == inChar) {
            xOffSet = 1.0;
        }
        if ('d' == inChar) {
            xOffSet = 2.0;
        }
        if ('e' == inChar) {
            xOffSet = -1.0;
        }
        if ('g' == inChar) {
            xOffSet = 3.0;
        }
        if ('h' == inChar) {
            xOffSet = 3.0;
        }
        if ('i' == inChar) {
            xOffSet = -2.0;
        }
        if ('j' == inChar ) {
            xOffSet = -2.0;
        }
        if ( 'k' == inChar ) {
            xOffSet = 3.0;
        }

        if ('l' == inChar ) {
            xOffSet = -1.0;
        }
        if ('m' == inChar) {
            xOffSet = 7.0;
        }
        if ('n' == inChar ) {
            xOffSet = 3.0;
        }
        if ('o' == inChar ) {
            xOffSet = 3.0;
        }
        if ('p' == inChar ) {
            xOffSet = 2.0;
        }
        if ('r' == inChar ) {
            xOffSet = 3.0;
        }
        if ('s' == inChar ) {
            xOffSet = 3.0;
        }
        if ('t' == inChar ) {
            xOffSet = 0.0;
        }
        if ('q' == inChar ) {
            xOffSet = 2.0;
        }
        if ('u' == inChar ) {
            xOffSet = 4.0;
        }
        if ('v' == inChar) {
            xOffSet = 2.0;
        }
        if ('w' == inChar) {
            xOffSet = 6.0;
        }
        if ('x' == inChar ) {
            xOffSet = 3.0;
        }
        if ( 'y' == inChar) {
            xOffSet = 3.0;
        }
        if ( 'z' == inChar) {
            xOffSet = 2.0;
        }



//--------------Eng upper case------------
        if ('A' == inChar) {
            xOffSet = 17.0;
        }
        if ('B' == inChar) xOffSet = 19.0;

        if ('C' == inChar) {
            xOffSet = 6.0;
        }
        if ('D' == inChar) {
            xOffSet += 11.0;
        }

        if ('M' == inChar) {
            xOffSet += 5.0;
        }
        if ('N' == inChar) {
            xOffSet += 7.0;
        }

        if ('I' == inChar) {
            xOffSet = 3.0;
        }
        if ('J' == inChar) {
            xOffSet = 3.0;
        }


        if ('Q' == inChar) {
            xOffSet = 8.0;
        }

        if ('U' == inChar) {
            xOffSet = 14.0;
        }
        if ('V' == inChar) {
            xOffSet = 13.0;
        }

        if ('W' == inChar) {
            xOffSet += 11.0;
        }
        if ('X' == inChar) {
            xOffSet = 17.0;
        }
        if ('Y' == inChar) {
            xOffSet = 13.0;
        }



//--------------Rus-lower case-------
        if ( 'a' == inChar  ) {
            xOffSet = 3.5;
        }
        if ( 'в' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( 'г' == inChar  ) {
            xOffSet -= 2.0;
        }
        if ( 'д' == inChar  ) {
            xOffSet -= 1.0;
        }
        if ( 'е' == inChar  ) {
            xOffSet = -1.0;
        }
        if ( 'ё' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( 'ж' == inChar  ) {
            xOffSet = 12.0;
        }
        if ( 'з' == inChar  ) {
            xOffSet += 3.0;
        }
        if ( 'и' == inChar  ) {
            xOffSet = 6.5;
        }
        if ( 'й' == inChar  ) {
            xOffSet = 6.5;
        }
        if ( 'к' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( 'л' == inChar  ) {
            xOffSet = 5.0;
        }
        if ( 'м' == inChar ) {
            xOffSet = 12.0;
        }
        if ( 'н' == inChar ) {
            xOffSet += 4.0;
        }
        if ('о' == inChar) xOffSet = 0.5;

        if ( 'п' == inChar ) {
            xOffSet = 6.0;
        }
        if ( 'р' == inChar ) {
            xOffSet += 4.0;
        }
        if ( 'с' == inChar ) {
            xOffSet -= 1.0;
        }
        if ( 'т' == inChar ) {
            xOffSet = 14.0;
        }
        if ( 'у' == inChar ) {
            xOffSet = 5.0;
        }
        if ( 'ф' == inChar ) {
            xOffSet += 8.0;
        }
        if ('х' == inChar) xOffSet = 3.5;

        if ( 'ц' == inChar) {
            xOffSet += 2.0;
        }
        if ( 'ч' == inChar) {
            xOffSet = 0.0;
        }
        if ( 'ь' == inChar ) {
            xOffSet -= 3.0;
        }
        if ( 'ы' == inChar   ) {
            xOffSet += 4.0;
        }
        if ( 'ъ' == inChar   ) {
            xOffSet = 3.0;
        }
        if( 'ш' == inChar  ) {
            xOffSet += 8.0;
        }
        if( 'щ' == inChar  ) {
            xOffSet += 8;
        }
        if( 'ю' == inChar  ) {
            xOffSet = 7.0;
        }
        if( 'я' == inChar  ) {
            xOffSet = 7.0;
        }
        //------------------
        if ( 'А' == inChar) {
            xOffSet = 9.0;
        }
        if ( 'Б' == inChar) {
            xOffSet += 1.0;
        }
        if ( 'В' == inChar) {
            xOffSet += 1.0;
        }
        if ( 'Г' == inChar ) {
            xOffSet = 6.0;
        }
        if ( 'Д' == inChar ) {
            xOffSet  += 5.0;
        }

        if ( 'Е' == inChar)  xOffSet = 3.0;
        if ( 'Ё' == inChar)  xOffSet = 4.0;

        if ( 'Ж' == inChar ) {
            xOffSet += 5.0;
        }

        if ( 'З' == inChar) xOffSet = 4.0;

        if ( 'И' == inChar) {
            xOffSet -= 4.0;
        }
        if ( 'Й' == inChar) {
            xOffSet -= 4.0;
        }
        if ('К' == inChar) xOffSet = 11.0;
        if ('Л' == inChar) xOffSet = 12.0;
        if ('М' == inChar) xOffSet = 14.5;
        if ('О' == inChar) xOffSet = 5.0;
        if ('П' == inChar  ) {
            xOffSet -= 2.0;
        }
        if ('Р' == inChar) xOffSet = 6.0;


        if ( 'С' == inChar) xOffSet = 2.5;

        if ( 'Т' == inChar) {
            xOffSet += 1.0;
        }
        if ('У' == inChar) xOffSet = 4.5;
        if ( 'Ф' == inChar) {
            xOffSet -= 1.0;
        }
        if ( 'Х' == inChar) {
            xOffSet += 3.0;
        }
        if ( 'Ц' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Ч' == inChar) xOffSet = 7.0;

        if ( 'Ш' == inChar) {
            xOffSet += 1.0;
        }
        if ( 'Щ' == inChar) xOffSet = 15.0;


        if ( 'Ъ' == inChar  ) {
            xOffSet -= 5.0;
        }

        if ( 'Ы' == inChar  ) {
            xOffSet -= 5.0;
        }
        if ( 'Ь' == inChar  ) {
            xOffSet  -= 5.0;
        }
        if ( 'Э' == inChar  ) xOffSet =6.0;

        if ( 'Ю' == inChar  ) {
            xOffSet += 5.0;
        }
        if ( 'Я' == inChar  )      xOffSet = 11.0;

        //----------------
        if ( '0' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( '1' == inChar  ) {
            xOffSet = -1.0;
        }
        if ( '2' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '3' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '4' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '5' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '6' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '7' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '8' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '9' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '0' == inChar  ) {
            xOffSet = 0.0;
        }
        return xOffSet;
    }

    public Double offsetFont3(char inChar) {
        Double xOffSet = 0.0;

        if (isCapital(inChar)) {
            xOffSet = 10.0;
            if(isCirrilic(inChar)){
                xOffSet -= 4.0;
            }
        } else {
            xOffSet = 2.0;
        }

        if ('a' == inChar) {
            xOffSet += 1.0;
        }


        if ('I' == inChar) {
            xOffSet -= 5.0;
        }

        if ('l' == inChar || 'i' == inChar) {
            xOffSet -= 2.0;
        }
        if ('x' == inChar || 'u' == inChar || 'b' == inChar ||
                'k' == inChar || 's' == inChar || 'n' == inChar || 'y' == inChar) {
            xOffSet += 2.0;
        }
        if ('A' == inChar) {
            xOffSet += 5.0;
        }
        if ('M' == inChar) {
            xOffSet += 5.0;
        }
        if ('N' == inChar) {
            xOffSet += 7.0;
        }

        if ('w' == inChar) {
            xOffSet += 4.0;
        }
        if ('B' == inChar) {
            xOffSet += 11.0;
        }
        if ('U' == inChar) {
            xOffSet += 11.0;
        }
        if ('V' == inChar) {
            xOffSet += 11.0;
        }
        if ('Y' == inChar) {
            xOffSet += 11.0;
        }
        if ('D' == inChar) {
            xOffSet += 11.0;
        }
        if ('W' == inChar) {
            xOffSet += 11.0;
        }
        if ('m' == inChar) {
            xOffSet += 5.0;
        }
        if ('X' == inChar) {
            xOffSet += 6.0;
        }

        if ('w' == inChar) {
            xOffSet += 5.0;
        }


//----------------------
        if ('г' == inChar  ) {
            //xOffSet -= 0;
        }
        if ('д' == inChar  ) {
            //xOffSet -= 1.0;
        }
        if ('е' == inChar  ) {
            // xOffSet -= 1.0;
        }
        if ('ё' == inChar  ) {
            //  xOffSet -= 1.0;
        }
        if ('ж' == inChar  ) {
            xOffSet += 3.0;
        }
        if ('з' == inChar  ) {
            // xOffSet += 3.0;//-1
        }
        if ( 'и' == inChar  ) {
            //xOffSet += 3.0;
        }
        if ( 'й' == inChar  ) {
            // xOffSet += 3.0;
        }
        if ( 'л' == inChar  ) {
            // xOffSet += 2.0;
        }
        if ( 'м' == inChar ) {
            // xOffSet += 8.0;
        }
        if ( 'н' == inChar ) {
            // xOffSet += 4.0;
        }
        if ( 'п' == inChar ) {
            //xOffSet += 6.0;
        }
        if ( 'р' == inChar ) {
            //xOffSet += 4.0;
        }
        if ( 'с' == inChar ) {
            // xOffSet -= 1.0;
        }
        if ( 'т' == inChar ) {
            // xOffSet += 10.0;
        }
        if ( 'у' == inChar ) {
            // xOffSet += 1.0;
        }
        if ( 'ф' == inChar ) {
            // xOffSet += 8.0;
        }
        if ( 'х' == inChar ) {
            // xOffSet += 3.0;
        }

        if ('ц' == inChar) {
            // xOffSet += 2.0;
        }
        if ( 'ь' == inChar ) {
            xOffSet += 1.0;
        }
        if('ы' == inChar   ) {
            xOffSet += 4.0;
        }
        if('ъ' == inChar   ) {
            xOffSet += 1.0;
        }
        if ( 'ю' == inChar ) {
            // xOffSet += 3.0;
        }

        if('ш' == inChar  ) {
            // xOffSet += 8.0;
        }
        if( 'щ' == inChar  ) {
            // xOffSet += 8.0;
        }
        if( 'я' == inChar  ) {
            // xOffSet += 4.0;
        }
        //------------------
        if ('Б' == inChar) {
            // xOffSet += 1.0;
        }
        if (  'Д' == inChar ) {
            // xOffSet += 5.0;
        }

        if ('Е' == inChar) {
            // xOffSet -= 4.0;
        }
        if ('Ё' == inChar) {
            //xOffSet -= 4.0;
        }
        if ( 'Ж' == inChar ) {
            xOffSet += 5.0;
        }

        if ('З' == inChar) {
            xOffSet += 0.0;
        }
        if ('И' == inChar) {
            // xOffSet -= 4.0;
        }
        if ('Й' == inChar) {
            // xOffSet -= 4.0;
        }
        if ( 'М' == inChar   ) {
            // xOffSet += 5.0;
        }

        if ('О' == inChar) {
            // xOffSet -= 3.0;
        }
        if (  'П' == inChar  ) {
            // xOffSet -= 2.0;
        }

        if ('С' == inChar) {
            // xOffSet -= 5.0;
        }
        if ('Т' == inChar) {
            // xOffSet += 1.0;
        }
        if ('У' == inChar) {
            // xOffSet -= 3.0;
        }
        if ('Ф' == inChar) {
            //  xOffSet -= 1.0;
        }
        if ('Х' == inChar) {
            xOffSet += 4.0;
        }
        if ('Ч' == inChar) {
            // xOffSet -= 3.0;
        }
        if ('Щ' == inChar) {
            // xOffSet += 2.0;
        }

        if ('Ъ' == inChar  ) {
            xOffSet += 5.0;
        }

        if ('Ы' == inChar  ) {
            xOffSet += 5.0;
        }
        if ( 'Ю' == inChar  ) {
            //xOffSet += 5.0;
        }
        //----------------
        if ( '0' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '1' == inChar  ) {
            xOffSet = -1.0;
        }
        if ( '2' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '3' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '4' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '5' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '6' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '7' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '8' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '9' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '0' == inChar  ) {
            xOffSet = 0.0;
        }
        return xOffSet;
    }

    public Double offsetFont4(char inChar) {
        Double xOffSet = 0.0;

        if (isCapital(inChar)) {
            xOffSet = 15.0;
            if(isCirrilic(inChar)){
                //xOffSet -= 4.0;
            }
        } else {
            xOffSet = 2.0;
        }
//--------------Rus-lower case-------
        if ( 'a' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( 'б' == inChar  ) {
            xOffSet = 2.0;
        }
        if ( 'в' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( 'г' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( 'д' == inChar  ) {
            //xOffSet -= 1.0;
        }
        if ( 'е' == inChar  ) {
            xOffSet = -1.0;
        }
        if ( 'ё' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( 'ж' == inChar  ) {
            xOffSet = 8.0;
        }
        if ( 'з' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( 'и' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( 'й' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( 'к' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( 'л' == inChar  ) {
            xOffSet = 5.0;
        }
        if ( 'м' == inChar ) {
            xOffSet = 9.0;
        }
        if ( 'н' == inChar ) {
            xOffSet = 4.0;
        }
        if ( 'о' == inChar ) {
            xOffSet = 0.0;
        }
        if ( 'п' == inChar ) {
            xOffSet = 5.0;
        }
        if ( 'р' == inChar ) {
            xOffSet = 6.0;
        }
        if ( 'с' == inChar ) {
            xOffSet = 1.0;
        }
        if ( 'т' == inChar ) {
            xOffSet = 9.0;
        }
        if ( 'у' == inChar ) {
            xOffSet = 5.0;
        }
        if ( 'ф' == inChar ) {
            xOffSet = 7.0;
        }
        if ( 'х' == inChar ) {
            xOffSet = 6.0;
        }
        if ( 'ц' == inChar) {
            xOffSet = 6.0;
        }
        if ( 'ч' == inChar) {
            xOffSet = 0.0;
        }
        if ( 'ь' == inChar ) {
            xOffSet = 1.0;
        }
        if ( 'ы' == inChar   ) {
            xOffSet = 4.0;
        }
        if ( 'ъ' == inChar   ) {
            //xOffSet += 1.0;
        }
        if ( 'ю' == inChar ) {
            // xOffSet += 3.0;
        }
        if( 'ш' == inChar  ) {
            xOffSet = 10.0;
        }
        if( 'щ' == inChar  ) {
            xOffSet = 10.0;
        }
        if( 'ю' == inChar  ) {
            xOffSet = 7.0;
        }
        if( 'я' == inChar  ) {
            xOffSet = 4.0;
        }
        //------------------
        if ( 'A' == inChar) {
            xOffSet = 9.0;
        }
        if ( 'Б' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'В' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Г' == inChar ) {
            xOffSet = 6.0;
        }
        if ( 'Д' == inChar ) {
            xOffSet  = 11.0;
        }

        if ( 'Е' == inChar) {
            xOffSet = 4.0;
        }
        if ( 'Ё' == inChar) {
            xOffSet = 4.0;
        }
        if ( 'Ж' == inChar ) {
            xOffSet = 16.0;
        }

        if ( 'З' == inChar) {
            xOffSet = 11.0;
        }
        if ( 'И' == inChar) {
            xOffSet = 9.0;
        }
        if ( 'Й' == inChar) {
            xOffSet = 9.0;
        }
        if ( 'К' == inChar) {
            xOffSet = 12.0;
        }
        if ( 'Л' == inChar) {
            xOffSet = 14.0;
        }
        if ( 'М' == inChar ) {
            xOffSet = 20.0;
        }

        if ( 'О' == inChar) {
            xOffSet = 5.0;
        }
        if ( 'П' == inChar  ) {
            //xOffSet -= 3.0;
        }
        if ( 'Р' == inChar  ) {
            xOffSet = 4.0;
        }

        if ( 'С' == inChar) {
            xOffSet = 6.0;
        }
        if ( 'Т' == inChar) {
            xOffSet = 17.0;
        }
        if ( 'У' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Ф' == inChar) {
            xOffSet = 8.0;
        }
        if ( 'Х' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Ц' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Ч' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Ш' == inChar) {
            xOffSet = 12.0;
        }
        if ( 'Щ' == inChar) {
            xOffSet = 13.0;
        }

        if ( 'Ъ' == inChar  ) {
            xOffSet = 8.0;
        }

        if ( 'Ы' == inChar  ) {
            //xOffSet += 3.0;
        }
        if ( 'Ь' == inChar  ) {
            xOffSet  = 7.0;
        }
        if ( 'Э' == inChar  ) {
            xOffSet =6.0;
        }
        if ( 'Ю' == inChar  ) {
            xOffSet = 17.0;
        }
        if ( 'Я' == inChar  ) {
            xOffSet = 13.0;
        }
        //----------------
        if ( '0' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '1' == inChar  ) {
            xOffSet = -1.0;
        }
        if ( '2' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '3' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '4' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '5' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '6' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '7' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '8' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '9' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '0' == inChar  ) {
            xOffSet = 0.0;
        }
        return xOffSet;
    }

    public Double offsetFont5(char inChar) {
        Double xOffSet = 0.0;

        if (isCapital(inChar)) {
            xOffSet = 10.0;
            if(isCirrilic(inChar)){
            }
        } else {
            xOffSet = 2.0;
        }
        if ('a' == inChar) {
            xOffSet = 0.0;
        }
        if ('b' == inChar) {
            xOffSet = 1.0;
        }
        if ('c' == inChar) {
            xOffSet = 0.0;
        }
        if ('d' == inChar) {
            xOffSet = 3.0;
        }
        if ('e' == inChar) {
            xOffSet = 0.0;
        }
        if ('f' == inChar) {
            xOffSet = 5.0;
        }
        if ('g' == inChar) {
            xOffSet = 0.0;
        }
        if ('h' == inChar) {
            xOffSet = 3.0;
        }
        if ('i' == inChar) {
            xOffSet = -1.0;
        }
        if ('j' == inChar) {
            xOffSet = -3.0;
        }
        if ('k' == inChar) {
            xOffSet = 3.0;
        }
        if ('l' == inChar) {
            xOffSet = -1.0;
        }
        if ('m' == inChar) {
            xOffSet = 9.0;
        }
        if ('n' == inChar) {
            xOffSet = 4.0;
        }
        if ('o' == inChar) {
            xOffSet = 0.0;
        }
        if ('p' == inChar) {
            xOffSet = 2.0;
        }
        if ('q' == inChar) {
            xOffSet = 0.0;
        }
        if ('r' == inChar) {
            xOffSet = 1.0;
        }
        if ('s' == inChar) {
            xOffSet = 0.0;
        }
        if ('t' == inChar) {
            xOffSet = 0.0;
        }
        if ('u' == inChar) {
            xOffSet = 2.0;
        }
        if ('v' == inChar) {
            xOffSet = 0.0;
        }
        if ('w' == inChar) {
            xOffSet = 7.0;
        }
        if ('x' == inChar) {
            xOffSet = 5.0;
        }
        if ('y' == inChar) {
            xOffSet = 0.0;
        }
        if ('z' == inChar) {
            xOffSet = 0.0;
        }

//------------------
        if ('A' == inChar) {
            xOffSet = 13.0;
        }
        if ('B' == inChar) {
            xOffSet = 20.0;
        }
        if ('C' == inChar) {
            xOffSet = 10.0;
        }
        if ('D' == inChar) {
            xOffSet = 24.0;
        }
        if ('E' == inChar) {
            xOffSet = 10.0;
        }
        if ('F' == inChar) {
            xOffSet = 11.0;
        }
        if ('G' == inChar) {
            xOffSet = 10.0;
        }
        if ('H' == inChar) {
            xOffSet = 15.0;
        }
        if ('I' == inChar) {
            xOffSet = 6.0;
        }
        if ('J' == inChar) {
            xOffSet = 10.0;
        }
        if ('K' == inChar) {
            xOffSet = 20.0;
        }
        if ('L' == inChar) {
            xOffSet = 16.0;
        }
        if ('M' == inChar) {
            xOffSet = 22.0;
        }
        if ('N' == inChar) {
            xOffSet = 19.0;
        }
        if ('O' == inChar) {
            xOffSet = 10.0;
        }
        if ('P' == inChar) {
            xOffSet = 21.0;
        }
        if ('Q' == inChar) {
            xOffSet = 12.0;
        }
        if ('R' == inChar) {
            xOffSet = 24.0;
        }
        if ('S' == inChar) {
            xOffSet = 10.0;
        }
        if ('T' == inChar) {
            xOffSet = 10.0;
        }
        if ('U' == inChar) {
            xOffSet = 15.0;
        }
        if ('V' == inChar) {
            xOffSet = 12.0;
        }
        if ('W' == inChar) {
            xOffSet = 22.0;
        }
        if ('X' == inChar) {
            xOffSet = 23.0;
        }
        if ('Y' == inChar) {
            xOffSet = 11.0;
        }
        if ('Z' == inChar) {
            xOffSet = 13.0;
        }



//--------------Rus-lower case-------
        if ('a' == inChar  ) {
            xOffSet = 3.0;
        }
        if ('в' == inChar  ) {
            xOffSet = 0.0;
        }
        if ('г' == inChar  ) {
            xOffSet = 0.0;
        }
        if ('д' == inChar  ) {
            //xOffSet -= 1.0;
        }
        if ('е' == inChar  ) {
            xOffSet = 1.0;
        }
        if ('ё' == inChar  ) {
            xOffSet = 1.0;
        }
        if ('ж' == inChar  ) {
            xOffSet = 9.0;
        }
        if ('з' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( 'и' == inChar  ) {
            xOffSet += 3.0;
        }
        if ( 'й' == inChar  ) {
             xOffSet += 3.0;
        }
        if ( 'к' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( 'л' == inChar  ) {
            xOffSet = 5.0;
        }
        if ( 'м' == inChar ) {
            xOffSet = 9.0;
        }
        if ( 'н' == inChar ) {
            xOffSet = 8.0;
        }
        if ( 'п' == inChar ) {
            xOffSet = 5.0;
        }
        if ( 'р' == inChar ) {
            xOffSet = 9.0;
        }
        if ( 'с' == inChar ) {
            // xOffSet -= 1.0;
        }
        if ( 'т' == inChar ) {
            xOffSet = 3.0;
        }
        if ( 'у' == inChar ) {
            // xOffSet += 1.0;
        }
        if ( 'ф' == inChar ) {
            xOffSet = 8.0;
        }
        if ( 'х' == inChar ) {
            xOffSet = 6.0;
        }

        if ( 'ц' == inChar) {
            // xOffSet += 2.0;
        }
        if ( 'ь' == inChar ) {
            xOffSet = 1.0;
        }
        if( 'ы' == inChar   ) {
            xOffSet = 7.0;
        }
        if( 'ъ' == inChar   ) {
            //xOffSet += 1.0;
        }
        if ( 'ю' == inChar ) {
            // xOffSet += 3.0;
        }

        if( 'ш' == inChar  ) {
            xOffSet = 10.0;
        }
        if( 'щ' == inChar  ) {
            xOffSet = 10.0;
        }
        if( 'ю' == inChar  ) {
            xOffSet = 7.0;
        }
        if( 'я' == inChar  ) {
            xOffSet = 3.0;
        }
        //------------------
        if ( 'А' == inChar) {
            xOffSet = 13.0;
        }
        if ( 'Б' == inChar) {
            xOffSet = 20.0;
        }
        if ( 'В' == inChar) {
            xOffSet = 19.0;
        }
        if ( 'Д' == inChar ) {
            xOffSet = 23.0;
        }
        if ( 'Е' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Ё' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Ж' == inChar ) {
            xOffSet = 34.0;
        }
        if ( 'З' == inChar) {
            xOffSet = 15.0;
        }
        if ( 'И' == inChar) {
            xOffSet = 15.0;
        }
        if ( 'Й' == inChar) {
            xOffSet = 18.0;
        }
        if ( 'К' == inChar) {
            xOffSet = 19.0;
        }
        if ( 'Л' == inChar) {
            xOffSet = 11.0;
        }
        if ( 'М' == inChar   ) {
            xOffSet = 19.0;
        }
        if ( 'Н' == inChar   ) {
            xOffSet = 15.0;
        }
        if ( 'О' == inChar) {
            xOffSet = 9.5;
        }
        if ( 'П' == inChar  ) {
            xOffSet = 8.5;
        }
        if ( 'Р' == inChar  ) {
            xOffSet = 20.0;
        }
        if ( 'С' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Т' == inChar) {
            xOffSet = 13.0;
        }
        if ( 'У' == inChar) {
            xOffSet = 13.0;
        }
        if ( 'Ф' == inChar) {
            xOffSet = 8.0;
        }
        if ( 'Х' == inChar) {
            xOffSet = 23.0;
        }
        if ( 'Ц' == inChar) {
            xOffSet = 20.0;
        }
        if ( 'Ч' == inChar) {
            xOffSet = 16.0;
        }
        if ( 'Ш' == inChar) {
            xOffSet = 33.0;
        }
        if ( 'Щ' == inChar) {
            xOffSet = 31.0;
        }
        if ( 'Ъ' == inChar  ){
            xOffSet = 20.0;
        }
        if ( 'Ы' == inChar  ){
            xOffSet = 28.0;
        }
        if ( 'Ь' == inChar  ){
            xOffSet = 19.0;
        }
        if ( 'Э' == inChar  ) {
            xOffSet = 13.0;
        }
        if ( 'Ю' == inChar  ) {
            xOffSet = 17.0;
        }
        if ( 'Я' == inChar  ) {
            xOffSet = 12.0;
        }
        //----------------
        if ( '0' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( '1' == inChar  ) {
            xOffSet = -1.0;
        }
        if ( '2' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( '3' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '4' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '5' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '6' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '7' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '8' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '9' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '0' == inChar  ) {
            xOffSet = 0.0;
        }
        return xOffSet;
    }

    public Double offsetFont7(char inChar) {
        Double xOffSet = 0.0;

        if (isCapital(inChar)) {
            xOffSet = 15.0;
            if(isCirrilic(inChar)){
                //xOffSet -= 4.0;
            }
        } else {
            xOffSet = -1.0;
        }



//--------------Rus-lower case-------
        if ( 'а' == inChar  ) {
            xOffSet = 2.0;
        }
        if ( 'в' == inChar  ) {
            xOffSet = -1.0;
        }
        if ( 'г' == inChar  ) {
            xOffSet = -1.0;
        }
        if ( 'д' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( 'е' == inChar  ) {
            xOffSet = -1.0;
        }
        if ( 'ё' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( 'ж' == inChar  ) {
            xOffSet = 7.0;
        }
        if ( 'з' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( 'и' == inChar  ) {
            xOffSet = 2.0;
        }
        if ( 'й' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( 'к' == inChar  ) {
            xOffSet = 2.0;
        }
        if ( 'л' == inChar  ) {
            xOffSet = 2.0;
        }
        if ( 'м' == inChar ) {
            xOffSet = 6.0;
        }
        if ( 'н' == inChar ) {
            xOffSet = 1.0;
        }
        if ( 'о' == inChar ) {
            xOffSet = -2.0;
        }
        if ( 'п' == inChar ) {
            xOffSet = 2.0;
        }
        if ( 'р' == inChar ) {
            xOffSet = 3.0;
        }
        if ( 'с' == inChar ) {
            xOffSet = -1.0;
        }
        if ( 'т' == inChar ) {
            xOffSet = 5.0;
        }
        if ( 'у' == inChar ) {
            xOffSet =1.0;
        }
        if ( 'ф' == inChar ) {
            xOffSet = 4.0;
        }
        if ( 'х' == inChar ) {
            xOffSet = 3.0;
        }
        if ( 'ц' == inChar) {
            xOffSet = 4.0;
        }
        if ( 'ч' == inChar) {
            xOffSet = -1.0;
        }
        if ( 'ь' == inChar ) {
            xOffSet = -1.0;
        }
        if ( 'ы' == inChar   ) {
            xOffSet = 3.0;
        }
        if ( 'ъ' == inChar   ) {
            xOffSet = 1.0;
        }
        if ( 'ю' == inChar ) {
            xOffSet = 1.5;
        }
        if( 'ш' == inChar  ) {
            xOffSet = 6.0;
        }
        if( 'щ' == inChar  ) {
            xOffSet = 10.0;
        }
        if( 'ю' == inChar  ) {
            xOffSet = 7.0;
        }
        if( 'я' == inChar  ) {
            xOffSet = 3.0;
        }
        //------------------
        if ( 'А' == inChar) {
            xOffSet = 20.0; //
        }
        if ( 'Б' == inChar) {
            xOffSet = 17.0;
        }
        if ( 'В' == inChar) {
            xOffSet = 15.0;
        }
        if ( 'Г' == inChar ) {
            xOffSet = 9.0;
        }
        if ( 'Д' == inChar ) {
            xOffSet  = 11.0;
        }

        if ( 'Е' == inChar) {
            xOffSet = 9.0;
        }
        if ( 'Ё' == inChar) {
            xOffSet = 9.0;
        }
        if ( 'Ж' == inChar ) {
            xOffSet = 20.0;
        }

        if ( 'З' == inChar) {
            xOffSet = 11.0;
        }
        if ( 'И' == inChar) {
            xOffSet = 9.0;
        }
        if ( 'Й' == inChar) {
            xOffSet = 9.0;
        }
        if ( 'К' == inChar) {
            xOffSet = 15.0;
        }
        if ( 'Л' == inChar) {
            xOffSet = 14.0;
        }
        if ( 'М' == inChar   ) {
            xOffSet = 20.0;
        }
        if ( 'Н' == inChar   ) {
            xOffSet = 14.0;
        }

        if ( 'О' == inChar) {
            xOffSet = 11.0;
        }
        if ( 'П' == inChar  ) {
            xOffSet = 15.0;
        }
        if ( 'Р' == inChar  ) {
            xOffSet = 8.0;
        }

        if ( 'С' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Т' == inChar) {
            xOffSet = 17.0;
        }
        if ( 'У' == inChar) {
            xOffSet = 15.0;
        }
        if ( 'Ф' == inChar) {
            xOffSet = 15.0;
        }
        if ( 'Х' == inChar) {
            xOffSet = 13.0;
        }
        if ( 'Ц' == inChar) {
            xOffSet = 16.0;
        }
        if ( 'Ч' == inChar) {
            xOffSet = 7.0;
        }
        if ( 'Ш' == inChar) {
            xOffSet = 12.0;
        }
        if ( 'Щ' == inChar) {
            xOffSet = 19.0;
        }

        if ( 'Ъ' == inChar  ) {
            xOffSet = 8.0;
        }

        if ( 'Ы' == inChar  ) {
            //xOffSet += 3.0;
        }
        if ( 'Ь' == inChar  ) {
            xOffSet  = 7.0;
        }
        if ( 'Э' == inChar  ) {
            xOffSet =10.0;
        }
        if ( 'Ю' == inChar  ) {
            xOffSet = 26.0;
        }
        if ( 'Я' == inChar  ) {
            xOffSet = 18.0;
        }
        //----------------
        if ( '0' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '1' == inChar  ) {
            xOffSet = -1.0;
        }
        if ( '2' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '3' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '4' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '5' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '6' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '7' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '8' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '9' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '0' == inChar  ) {
            xOffSet = 0.0;
        }
        return xOffSet;
    }

    public Double offsetFont8(char inChar) {
        Double xOffSet = 0.0;

        if (isCapital(inChar)) {
            xOffSet = 11.0;
            if(isCirrilic(inChar)){
                //xOffSet -= 4.0;
            }
        } else {
            xOffSet = -1.0;
        }



//--------------Rus-lower case-------
        if ( 'а' == inChar  ) {
            xOffSet = 2.0;
        }
        if ( 'в' == inChar  ) {
            xOffSet = -1.0;
        }
        if ( 'г' == inChar  ) {
            xOffSet = -1.0;
        }
        if ( 'д' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( 'е' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( 'ё' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( 'ж' == inChar  ) {
            xOffSet = 10.0;///..11
        }
        if ( 'з' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( 'и' == inChar  ) {
            xOffSet = 2.0;
        }
        if ( 'й' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( 'к' == inChar  ) {
            xOffSet = 2.0;
        }
        if ( 'л' == inChar  ) {
            xOffSet = 3.0;//2
        }
        if ( 'м' == inChar ) {
            xOffSet = 7.5;//6<
        }
        if ( 'н' == inChar ) {
            xOffSet = 2.0;
        }
        if ( 'о' == inChar ) {
            xOffSet = -1.0;
        }
        if ( 'п' == inChar ) {
            xOffSet = 3.0;//2...
        }
        if ( 'р' == inChar ) {
            xOffSet = 3.0;
        }
        if ( 'с' == inChar ) {
            xOffSet = -1.0;
        }
        if ( 'т' == inChar ) {
            xOffSet = 7.0;//5
        }
        if ( 'у' == inChar ) {
            xOffSet =1.0;
        }
        if ( 'ф' == inChar ) {
            xOffSet = 3.5;
        }
        if ( 'х' == inChar ) {
            xOffSet = 3.0;
        }
        if ( 'ц' == inChar) {
            xOffSet = 4.0;
        }
        if ( 'ч' == inChar) {
            xOffSet = -1.0;
        }
        if ( 'ь' == inChar ) {
            xOffSet = -1.0;
        }
        if ( 'ы' == inChar   ) {
            xOffSet = 2.5;
        }
        if ( 'ъ' == inChar   ) {
            xOffSet = 1.0;
        }
        if ( 'ю' == inChar ) {
            xOffSet = 1.5;
        }
        if( 'ш' == inChar  ) {
            xOffSet = 6.0;
        }
        if( 'щ' == inChar  ) {
            xOffSet = 8.0;
        }
        if( 'ю' == inChar  ) {
            xOffSet = 7.0;
        }
        if( 'я' == inChar  ) {
            xOffSet = 3.0;
        }
        //------------------
        if ( 'А' == inChar) {
            xOffSet = 9.0; //
        }
        if ( 'Б' == inChar) {
            xOffSet = 6.0;//..8
        }
        if ( 'В' == inChar) {
            xOffSet = 8.0;//...10
        }
        if ( 'Г' == inChar ) {
            xOffSet = 7.0;
        }
        if ( 'Д' == inChar ) {
            xOffSet  = 12.0;
        }

        if ( 'Е' == inChar) {
            xOffSet = 6.0;
        }
        if ( 'Ё' == inChar) {
            xOffSet = 6.0;
        }
        if ( 'Ж' == inChar ) {
            xOffSet = 20.0;
        }
        if ( 'З' == inChar) {
            xOffSet = 7.0;
        }
        if ( 'И' == inChar) {
            xOffSet = 8.0;
        }
        if ( 'Й' == inChar) {
            xOffSet = 8.0;
        }
        if ( 'К' == inChar) {
            xOffSet = 9.0;
        }
        if ( 'Л' == inChar) {
            xOffSet = 10.0; //
        }
        if ( 'М' == inChar   ) {
            xOffSet = 17.0;
        }
        if ( 'Н' == inChar   ) {
            xOffSet = 8.0;//...10
        }
        if ( 'О' == inChar) {
            xOffSet = 4.0;//..6
        }
        if ( 'П' == inChar  ) {
            xOffSet = 14.0;//...17
        }
        if ( 'Р' == inChar  ) {
            xOffSet = 5.0;
        }
        if ( 'С' == inChar) {
            xOffSet = 5.0;//...C
        }
        if ( 'Т' == inChar) {
            xOffSet = 19.0;
        }
        if ( 'У' == inChar) {
            xOffSet = 4.0;
        }
        if ( 'Ф' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Х' == inChar) {
            xOffSet = 12.0;
        }
        if ( 'Ц' == inChar) {
            xOffSet = 9.0;
        }
        if ( 'Ч' == inChar) {
            xOffSet = 6.0;
        }
        if ( 'Ш' == inChar) {
            xOffSet = 13.0;
        }
        if ( 'Щ' == inChar) {
            xOffSet = 14.0;
        }

        if ( 'Ъ' == inChar  ) {
            xOffSet = 8.0;
        }

        if ( 'Ы' == inChar  ) {
            //xOffSet += 3.0;
        }
        if ( 'Ь' == inChar  ) {
            xOffSet = 7.0;
        }
        if ( 'Э' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( 'Ю' == inChar  ) {
            xOffSet = 14.0;//...20
        }
        if ( 'Я' == inChar  ) {
            xOffSet = 7.0;
        }
        //----------------
        if ( '0' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '1' == inChar  ) {
            xOffSet = -2.0;
        }
        if ( '2' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '3' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '4' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '5' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '6' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( '7' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '8' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '9' == inChar  ) {
            xOffSet = 0.0;
        }
        if ( '0' == inChar  ) {
            xOffSet = 1.0;
        }
        return xOffSet;
    }

    public Double offsetFont9(char inChar) {
        Double xOffSet = 0.0;

        if (isCapital(inChar)) {
            xOffSet = 10.0;
            if(isCirrilic(inChar)){
            }
        } else {
            xOffSet = 2.0;
        }
        if ('a' == inChar) {
            xOffSet = 0.0;
        }
        if ('b' == inChar) {
            xOffSet = 1.0;
        }
        if ('c' == inChar) {
            xOffSet = 0.0;
        }
        if ('d' == inChar) {
            xOffSet = 3.0;
        }
        if ('e' == inChar) {
            xOffSet = 0.0;
        }
        if ('f' == inChar) {
            xOffSet = 5.0;
        }
        if ('g' == inChar) {
            xOffSet = 0.0;
        }
        if ('h' == inChar) {
            xOffSet = 3.0;
        }
        if ('i' == inChar) {
            xOffSet = -1.0;
        }
        if ('j' == inChar) {
            xOffSet = -3.0;
        }
        if ('k' == inChar) {
            xOffSet = 3.0;
        }
        if ('l' == inChar) {
            xOffSet = -1.0;
        }
        if ('m' == inChar) {
            xOffSet = 9.0;
        }
        if ('n' == inChar) {
            xOffSet = 4.0;
        }
        if ('o' == inChar) {
            xOffSet = 0.0;
        }
        if ('p' == inChar) {
            xOffSet = 2.0;
        }
        if ('q' == inChar) {
            xOffSet = 0.0;
        }
        if ('r' == inChar) {
            xOffSet = 1.0;
        }
        if ('s' == inChar) {
            xOffSet = 0.0;
        }
        if ('t' == inChar) {
            xOffSet = 0.0;
        }
        if ('u' == inChar) {
            xOffSet = 2.0;
        }
        if ('v' == inChar) {
            xOffSet = 0.0;
        }
        if ('w' == inChar) {
            xOffSet = 7.0;
        }
        if ('x' == inChar) {
            xOffSet = 5.0;
        }
        if ('y' == inChar) {
            xOffSet = 0.0;
        }
        if ('z' == inChar) {
            xOffSet = 0.0;
        }

//------------------
        if ('A' == inChar) {
            xOffSet = 13.0;
        }
        if ('B' == inChar) {
            xOffSet = 20.0;
        }
        if ('C' == inChar) {
            xOffSet = 10.0;
        }
        if ('D' == inChar) {
            xOffSet = 24.0;
        }
        if ('E' == inChar) {
            xOffSet = 10.0;
        }
        if ('F' == inChar) {
            xOffSet = 11.0;
        }
        if ('G' == inChar) {
            xOffSet = 10.0;
        }
        if ('H' == inChar) {
            xOffSet = 15.0;
        }
        if ('I' == inChar) {
            xOffSet = 6.0;
        }
        if ('J' == inChar) {
            xOffSet = 10.0;
        }
        if ('K' == inChar) {
            xOffSet = 20.0;
        }
        if ('L' == inChar) {
            xOffSet = 16.0;
        }
        if ('M' == inChar) {
            xOffSet = 22.0;
        }
        if ('N' == inChar) {
            xOffSet = 19.0;
        }
        if ('O' == inChar) {
            xOffSet = 10.0;
        }
        if ('P' == inChar) {
            xOffSet = 21.0;
        }
        if ('Q' == inChar) {
            xOffSet = 12.0;
        }
        if ('R' == inChar) {
            xOffSet = 24.0;
        }
        if ('S' == inChar) {
            xOffSet = 10.0;
        }
        if ('T' == inChar) {
            xOffSet = 10.0;
        }
        if ('U' == inChar) {
            xOffSet = 15.0;
        }
        if ('V' == inChar) {
            xOffSet = 12.0;
        }
        if ('W' == inChar) {
            xOffSet = 22.0;
        }
        if ('X' == inChar) {
            xOffSet = 23.0;
        }
        if ('Y' == inChar) {
            xOffSet = 11.0;
        }
        if ('Z' == inChar) {
            xOffSet = 13.0;
        }



//--------------Rus-lower case-------
        if ('а' == inChar  ) {
            xOffSet = 6.0;
        }
        if ('б' == inChar  ) {
            xOffSet = 3.0;
        }
        if ('в' == inChar  ) {
            xOffSet = 0.0;
        }
        if ('г' == inChar  ) {
            xOffSet = 0.0;
        }
        if ('д' == inChar  ) {
            xOffSet = 3.0;
        }
        if ('е' == inChar  ) {
            xOffSet = 1.0;
        }
        if ('ё' == inChar  ) {
            xOffSet = 1.0;
        }
        if ('ж' == inChar  ) {
            xOffSet = 11.0;
        }
        if ('з' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( 'и' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( 'й' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( 'к' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( 'л' == inChar  ) {
            xOffSet = 7.0;
        }
        if ( 'м' == inChar ) {
            xOffSet = 14.0;
        }
        if ( 'н' == inChar ) {
            xOffSet = 6.0;
        }
        if ( 'о' == inChar ) {
            xOffSet = 3.0;
        }
        if ( 'п' == inChar ) {
            xOffSet = 5.0;
        }
        if ( 'р' == inChar ) {
            xOffSet = 5.0;
        }
        if ( 'с' == inChar ) {
            xOffSet = 3.0;
        }
        if ( 'т' == inChar ) {
            xOffSet = 9.0;
        }
        if ( 'у' == inChar ) {
            xOffSet = 3.0;
        }
        if ( 'ф' == inChar ) {
            xOffSet = 8.0;
        }
        if ( 'х' == inChar ) {
            xOffSet = 4.0;
        }

        if ( 'ц' == inChar) {
            xOffSet = 4.0;
        }
        if ( 'ч' == inChar) {
            xOffSet = 3.0;
        }

        if( 'ш' == inChar  ) {
            xOffSet = 10.0;
        }
        if( 'щ' == inChar  ) {
            xOffSet = 10.0;
        }
        if ( 'ь' == inChar ) {
            xOffSet = 2.0;
        }
        if( 'ы' == inChar   ) {
            xOffSet = 7.0;
        }
        if( 'ъ' == inChar   ) {
            xOffSet = 2.0;
        }

        if( 'ю' == inChar  ) {
            xOffSet = 7.0;
        }
        if( 'я' == inChar  ) {
            xOffSet = 3.0;
        }
        //------------------
        if ( 'А' == inChar) {
            xOffSet = 9.0;
        }
        if ( 'Б' == inChar) {
            xOffSet = 11.0;
        }
        if ( 'В' == inChar) {
            xOffSet = 18.0;
        }
        if ( 'Д' == inChar ) {
            xOffSet = 17.0;
        }
        if ( 'Е' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Ё' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Ж' == inChar ) {
            xOffSet = 22.0;
        }
        if ( 'З' == inChar) {
            xOffSet = 12.0;
        }
        if ( 'И' == inChar) {
            xOffSet = 17.0;
        }
        if ( 'Й' == inChar) {
            xOffSet = 18.0;
        }
        if ( 'К' == inChar) {
            xOffSet = 16.0;
        }
        if ( 'Л' == inChar) {
            xOffSet = 20.0;
        }
        if ( 'М' == inChar   ) {
            xOffSet = 34.0;
        }
        if ( 'Н' == inChar   ) {
            xOffSet = 18.0;
        }
        if ( 'О' == inChar) {
            xOffSet = 9.5;
        }
        if ( 'П' == inChar  ) {
            xOffSet = 16.0;
        }
        if ( 'Р' == inChar  ) {
            xOffSet = 16.0;
        }
        if ( 'С' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Т' == inChar) {
            xOffSet = 25.0;
        }
        if ( 'У' == inChar) {
            xOffSet = 18.0;
        }
        if ( 'Ф' == inChar) {
            xOffSet = 11.0;
        }
        if ( 'Х' == inChar) {
            xOffSet = 18.0;
        }
        if ( 'Ц' == inChar) {
            xOffSet = 22.0;
        }
        if ( 'Ч' == inChar) {
            xOffSet = 16.0;
        }
        if ( 'Ш' == inChar) {
            xOffSet = 31.0;
        }
        if ( 'Щ' == inChar) {
            xOffSet = 34.0;
        }
        if ( 'Ъ' == inChar  ){
            xOffSet = 13.0;
        }
        if ( 'Ы' == inChar  ){
            xOffSet = 23.0;
        }
        if ( 'Ь' == inChar  ){
            xOffSet = 12.0;
        }
        if ( 'Э' == inChar  ) {
            xOffSet = 11.0;
        }
        if ( 'Ю' == inChar  ) {
            xOffSet = 27.0;
        }
        if ( 'Я' == inChar  ) {
            xOffSet = 12.0;
        }
        //----------------
        if ( '0' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '1' == inChar  ) {
            xOffSet = 2.0;
        }
        if ( '2' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( '3' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( '4' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( '5' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( '6' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( '7' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( '8' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( '9' == inChar  ) {
            xOffSet = 3.0;
        }
        return xOffSet;
    }

    public Double offsetFont10(char inChar) {
        Double xOffSet = 0.0;

        if (isCapital(inChar)) {
            xOffSet = 10.0;
            if(isCirrilic(inChar)){
            }
        } else {
            xOffSet = 2.0;
        }
        if ('a' == inChar) {
            xOffSet = 0.0;
        }
        if ('b' == inChar) {
            xOffSet = 1.0;
        }
        if ('c' == inChar) {
            xOffSet = 0.0;
        }
        if ('d' == inChar) {
            xOffSet = 3.0;
        }
        if ('e' == inChar) {
            xOffSet = 0.0;
        }
        if ('f' == inChar) {
            xOffSet = 5.0;
        }
        if ('g' == inChar) {
            xOffSet = 0.0;
        }
        if ('h' == inChar) {
            xOffSet = 3.0;
        }
        if ('i' == inChar) {
            xOffSet = -1.0;
        }
        if ('j' == inChar) {
            xOffSet = -3.0;
        }
        if ('k' == inChar) {
            xOffSet = 3.0;
        }
        if ('l' == inChar) {
            xOffSet = -1.0;
        }
        if ('m' == inChar) {
            xOffSet = 9.0;
        }
        if ('n' == inChar) {
            xOffSet = 4.0;
        }
        if ('o' == inChar) {
            xOffSet = 0.0;
        }
        if ('p' == inChar) {
            xOffSet = 2.0;
        }
        if ('q' == inChar) {
            xOffSet = 0.0;
        }
        if ('r' == inChar) {
            xOffSet = 1.0;
        }
        if ('s' == inChar) {
            xOffSet = 0.0;
        }
        if ('t' == inChar) {
            xOffSet = 0.0;
        }
        if ('u' == inChar) {
            xOffSet = 2.0;
        }
        if ('v' == inChar) {
            xOffSet = 0.0;
        }
        if ('w' == inChar) {
            xOffSet = 7.0;
        }
        if ('x' == inChar) {
            xOffSet = 5.0;
        }
        if ('y' == inChar) {
            xOffSet = 0.0;
        }
        if ('z' == inChar) {
            xOffSet = 0.0;
        }

//------------------
        if ('A' == inChar) {
            xOffSet = 13.0;
        }
        if ('B' == inChar) {
            xOffSet = 20.0;
        }
        if ('C' == inChar) {
            xOffSet = 10.0;
        }
        if ('D' == inChar) {
            xOffSet = 24.0;
        }
        if ('E' == inChar) {
            xOffSet = 10.0;
        }
        if ('F' == inChar) {
            xOffSet = 11.0;
        }
        if ('G' == inChar) {
            xOffSet = 10.0;
        }
        if ('H' == inChar) {
            xOffSet = 15.0;
        }
        if ('I' == inChar) {
            xOffSet = 6.0;
        }
        if ('J' == inChar) {
            xOffSet = 10.0;
        }
        if ('K' == inChar) {
            xOffSet = 20.0;
        }
        if ('L' == inChar) {
            xOffSet = 16.0;
        }
        if ('M' == inChar) {
            xOffSet = 22.0;
        }
        if ('N' == inChar) {
            xOffSet = 19.0;
        }
        if ('O' == inChar) {
            xOffSet = 10.0;
        }
        if ('P' == inChar) {
            xOffSet = 21.0;
        }
        if ('Q' == inChar) {
            xOffSet = 12.0;
        }
        if ('R' == inChar) {
            xOffSet = 24.0;
        }
        if ('S' == inChar) {
            xOffSet = 10.0;
        }
        if ('T' == inChar) {
            xOffSet = 10.0;
        }
        if ('U' == inChar) {
            xOffSet = 15.0;
        }
        if ('V' == inChar) {
            xOffSet = 12.0;
        }
        if ('W' == inChar) {
            xOffSet = 22.0;
        }
        if ('X' == inChar) {
            xOffSet = 23.0;
        }
        if ('Y' == inChar) {
            xOffSet = 11.0;
        }
        if ('Z' == inChar) {
            xOffSet = 13.0;
        }

//--------------Rus-lower case-------
        if (' ' == inChar  ) {
            xOffSet = 5.0;
        }
        if ('а' == inChar  ) {
            xOffSet = 9.0;
        }
        if ('б' == inChar  ) {
            xOffSet = 9.0;
        }
        if ('в' == inChar  ) {
            xOffSet = 7.0;
        }
        if ('г' == inChar  ) {
            xOffSet = 1.0;
        }
        if ('д' == inChar  ) {
            xOffSet = 10.0;
        }
        if ('е' == inChar  ) {
            xOffSet = 7.0;
        }
        if ('ё' == inChar  ) {
            xOffSet = 7.0;
        }
        if ('ж' == inChar  ) {
            xOffSet = 22.0;
        }
        if ('з' == inChar  ) {
            xOffSet = 8.0;
        }
        if ('и' == inChar  ) {
            xOffSet = 9.0;
        }
        if ( 'й' == inChar  ) {
            xOffSet = 9.0;
        }
        if ( 'к' == inChar  ) {
            xOffSet = 6.0;
        }
        if ( 'л' == inChar  ) {
            xOffSet = 9.0;
        }
        if ( 'м' == inChar ) {
            xOffSet = 15.0;
        }
        if ( 'н' == inChar ) {
            xOffSet = 7.0;
        }
        if ( 'о' == inChar ) {
            xOffSet = 9.0;
        }
        if ( 'п' == inChar ) {
            xOffSet = 5.0;
        }
        if ( 'р' == inChar ) {
            xOffSet = 12.0;
        }
        if ( 'с' == inChar ) {
            xOffSet = 8.0;
        }
        if ( 'т' == inChar ) {
            xOffSet = 10.0;
        }
        if ( 'у' == inChar ) {
            xOffSet = 7.0;
        }
        if ( 'ф' == inChar ) {
            xOffSet = 20.0;
        }
        if ( 'х' == inChar ) {
            xOffSet = 7.0;
        }
        if ( 'ц' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'ч' == inChar) {
            xOffSet = 7.0;
        }
        if( 'ш' == inChar  ) {
            xOffSet = 13.0;
        }
        if( 'щ' == inChar  ) {
            xOffSet = 16.0;
        }
        if ( 'ь' == inChar ) {
            xOffSet = 5.0;
        }
        if( 'ы' == inChar   ) {
            xOffSet = 11.0;
        }
        if( 'ъ' == inChar   ) {
            xOffSet = 9.0;
        }
        if( 'э' == inChar  ) {
            xOffSet = 7.0;
        }
        if( 'ю' == inChar  ) {
            xOffSet = 10.0;
        }
        if( 'я' == inChar  ) {
            xOffSet = 12.0;
        }
        //------------------
        if ( 'А' == inChar) {
            xOffSet = 21.0;
        }
        if ( 'Б' == inChar) {
            xOffSet = 16.0;
        }
        if ( 'В' == inChar) {
            xOffSet = 16.0;
        }
        if ( 'Д' == inChar ) {
            xOffSet = 17.0;
        }
        if ( 'Е' == inChar) {
            xOffSet = 7.0;
        }
        if ( 'Ё' == inChar) {
            xOffSet = 7.0;
        }
        if ( 'Ж' == inChar ) {
            xOffSet = 35.0;
        }
        if ( 'З' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'И' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Й' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'К' == inChar) {
            xOffSet = 16.0;
        }
        if ( 'Л' == inChar) {
            xOffSet = 18.0;
        }
        if ( 'М' == inChar   ) {
            xOffSet = 25.0;
        }
        if ( 'Н' == inChar   ) {
            xOffSet = 18.0;
        }
        if ( 'О' == inChar) {
            xOffSet = 9.5;
        }
        if ( 'П' == inChar  ) {
            xOffSet = 16.0;
        }
        if ( 'Р' == inChar  ) {
            xOffSet = 12.0;
        }
        if ( 'С' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'Т' == inChar) {
            xOffSet = 24.0;
        }
        if ( 'У' == inChar) {
            xOffSet = 14.0;
        }
        if ( 'Ф' == inChar) {
            xOffSet = 14.0;
        }
        if ( 'Х' == inChar) {
            xOffSet = 22.0;
        }
        if ( 'Ц' == inChar) {
            xOffSet = 12.0;
        }
        if ( 'Ч' == inChar) {
            xOffSet = 12.0;
        }
        if ( 'Ш' == inChar) {
            xOffSet = 25.0;
        }
        if ( 'Щ' == inChar) {
            xOffSet = 26.0;
        }
        if ( 'Ъ' == inChar  ){
            xOffSet = 30.0;
        }
        if ( 'Ы' == inChar  ){
            xOffSet = 30.0;
        }
        if ( 'Ь' == inChar  ){
            xOffSet = 30.0;
        }
        if ( 'Э' == inChar  ) {
            xOffSet = 12.0;
        }
        if ( 'Ю' == inChar  ) {
            xOffSet = 27.0;
        }
        if ( 'Я' == inChar  ) {
            xOffSet = 12.0;
        }
        //----------------
        if ( '0' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '1' == inChar  ) {
            xOffSet = 2.0;
        }
        if ( '2' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '3' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '4' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '5' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '6' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '7' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '8' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '9' == inChar  ) {
            xOffSet = 5.0;
        }
        return xOffSet;
    }

    public Double offsetFont11(char inChar) {
        Double xOffSet = 0.0;

        if (isCapital(inChar)) {
            xOffSet = 10.0;
            if(isCirrilic(inChar)){
            }
        } else {
            xOffSet = 2.0;
        }
        if ('a' == inChar) {
            xOffSet = 0.0;
        }
        if ('b' == inChar) {
            xOffSet = 1.0;
        }
        if ('c' == inChar) {
            xOffSet = 0.0;
        }
        if ('d' == inChar) {
            xOffSet = 3.0;
        }
        if ('e' == inChar) {
            xOffSet = 0.0;
        }
        if ('f' == inChar) {
            xOffSet = 5.0;
        }
        if ('g' == inChar) {
            xOffSet = 0.0;
        }
        if ('h' == inChar) {
            xOffSet = 3.0;
        }
        if ('i' == inChar) {
            xOffSet = -1.0;
        }
        if ('j' == inChar) {
            xOffSet = -3.0;
        }
        if ('k' == inChar) {
            xOffSet = 3.0;
        }
        if ('l' == inChar) {
            xOffSet = -1.0;
        }
        if ('m' == inChar) {
            xOffSet = 9.0;
        }
        if ('n' == inChar) {
            xOffSet = 4.0;
        }
        if ('o' == inChar) {
            xOffSet = 0.0;
        }
        if ('p' == inChar) {
            xOffSet = 2.0;
        }
        if ('q' == inChar) {
            xOffSet = 0.0;
        }
        if ('r' == inChar) {
            xOffSet = 1.0;
        }
        if ('s' == inChar) {
            xOffSet = 0.0;
        }
        if ('t' == inChar) {
            xOffSet = 0.0;
        }
        if ('u' == inChar) {
            xOffSet = 2.0;
        }
        if ('v' == inChar) {
            xOffSet = 0.0;
        }
        if ('w' == inChar) {
            xOffSet = 7.0;
        }
        if ('x' == inChar) {
            xOffSet = 5.0;
        }
        if ('y' == inChar) {
            xOffSet = 0.0;
        }
        if ('z' == inChar) {
            xOffSet = 0.0;
        }

//------------------
        if ('A' == inChar) {
            xOffSet = 13.0;
        }
        if ('B' == inChar) {
            xOffSet = 20.0;
        }
        if ('C' == inChar) {
            xOffSet = 10.0;
        }
        if ('D' == inChar) {
            xOffSet = 24.0;
        }
        if ('E' == inChar) {
            xOffSet = 10.0;
        }
        if ('F' == inChar) {
            xOffSet = 11.0;
        }
        if ('G' == inChar) {
            xOffSet = 10.0;
        }
        if ('H' == inChar) {
            xOffSet = 15.0;
        }
        if ('I' == inChar) {
            xOffSet = 6.0;
        }
        if ('J' == inChar) {
            xOffSet = 10.0;
        }
        if ('K' == inChar) {
            xOffSet = 20.0;
        }
        if ('L' == inChar) {
            xOffSet = 16.0;
        }
        if ('M' == inChar) {
            xOffSet = 22.0;
        }
        if ('N' == inChar) {
            xOffSet = 19.0;
        }
        if ('O' == inChar) {
            xOffSet = 10.0;
        }
        if ('P' == inChar) {
            xOffSet = 21.0;
        }
        if ('Q' == inChar) {
            xOffSet = 12.0;
        }
        if ('R' == inChar) {
            xOffSet = 24.0;
        }
        if ('S' == inChar) {
            xOffSet = 10.0;
        }
        if ('T' == inChar) {
            xOffSet = 10.0;
        }
        if ('U' == inChar) {
            xOffSet = 15.0;
        }
        if ('V' == inChar) {
            xOffSet = 12.0;
        }
        if ('W' == inChar) {
            xOffSet = 22.0;
        }
        if ('X' == inChar) {
            xOffSet = 23.0;
        }
        if ('Y' == inChar) {
            xOffSet = 11.0;
        }
        if ('Z' == inChar) {
            xOffSet = 13.0;
        }

//--------------Rus-lower case-------
        if (' ' == inChar  ) {
            xOffSet = 5.0;
        }
        if ('а' == inChar  ) {
            xOffSet = 5.0;
        }
        if ('б' == inChar  ) {
            xOffSet = 1.0;
        }
        if ('в' == inChar  ) {
            xOffSet = -1.0;
        }
        if ('г' == inChar  ) {
            xOffSet = 1.5;
        }
        if ('д' == inChar  ) {
            xOffSet = 3.0;
        }
        if ('е' == inChar  ) {
            xOffSet = 0.0;
        }
        if ('ё' == inChar  ) {
            xOffSet = 0.0;
        }
        if ('ж' == inChar) {
            xOffSet = 11.0;
        }
        if ('з' == inChar) {
            xOffSet = 3.0;
        }
        if ('и' == inChar) {
            xOffSet = 4.0;
        }
        if ('й' == inChar) {
            xOffSet = 4.0;
        }
        if ('к' == inChar) {
            xOffSet = 2.0;
        }
        if ('л' == inChar) {
            xOffSet = 7.0;
        }
        if ('м' == inChar ) {
            xOffSet = 9.0;
        }
        if ('н' == inChar ) {
            xOffSet = 4.0;
        }
        if ('о' == inChar ) {
            xOffSet = 0.0;
        }
        if ('п' == inChar ) {
            xOffSet = 3.0;
        }
        if ('р' == inChar ) {
            xOffSet = 0.0;
        }
        if ( 'с' == inChar ) {
            xOffSet = -1.0;
        }
        if ( 'т' == inChar ) {
            xOffSet = 7.0;
        }
        if ( 'у' == inChar ) {
            xOffSet = 2.0;
        }
        if ( 'ф' == inChar ) {
            xOffSet = 4.0;
        }
        if ( 'х' == inChar ) {
            xOffSet = 2.0;
        }
        if ( 'ц' == inChar) {
            xOffSet = 5.0;
        }
        if ( 'ч' == inChar) {
            xOffSet = 2.0;
        }
        if( 'ш' == inChar  ) {
            xOffSet = 10.0;
        }
        if( 'щ' == inChar  ) {
            xOffSet = 11.0;
        }
        if ('ь' == inChar ) {
            xOffSet = 0.0;
        }
        if ('ы' == inChar) {
            xOffSet = 5.0;
        }
        if ('ъ' == inChar) {
            xOffSet = 1.0;
        }
        if ('э' == inChar  ) {
            xOffSet = -0.5;
        }
        if('ю' == inChar  ) {
            xOffSet = 4.0;
        }
        if('я' == inChar  ) {
            xOffSet = 5.0;
        }
        //------------------
        if ( 'А' == inChar) {
            xOffSet = 21.0;
        }
        if ( 'Б' == inChar) {
            xOffSet = 23.0;
        }
        if ( 'В' == inChar) {
            xOffSet = 16.0;
        }
        if ( 'Г' == inChar ) {
            xOffSet = 17.0;
        }
        if ( 'Д' == inChar ) {
            xOffSet = 21.0;
        }
        if ( 'Е' == inChar) {
            xOffSet = 12.0;
        }
        if ( 'Ё' == inChar) {
            xOffSet = 12.0;
        }
        if ( 'Ж' == inChar ) {
            xOffSet = 35.0;
        }
        if ( 'З' == inChar) {
            xOffSet = 10.0;
        }
        if ( 'И' == inChar) {
            xOffSet = 17.0;
        }
        if ( 'Й' == inChar) {
            xOffSet = 17.0;
        }
        if ( 'К' == inChar) {
            xOffSet = 22.0;
        }
        if ( 'Л' == inChar) {
            xOffSet = 21.0;
        }
        if ( 'М' == inChar   ) {
            xOffSet = 25.0;
        }
        if ( 'Н' == inChar   ) {
            xOffSet = 24.0;
        }
        if ( 'О' == inChar) {
            xOffSet = 19.0;
        }
        if ( 'П' == inChar  ) {
            xOffSet = 22.0;
        }
        if ( 'Р' == inChar  ) {
            xOffSet = 12.0;
        }
        if ( 'С' == inChar) {
            xOffSet = 16.0;
        }
        if ( 'Т' == inChar) {
            xOffSet = 21.0;
        }
        if ( 'У' == inChar) {
            xOffSet = 14.0;
        }
        if ( 'Ф' == inChar) {
            xOffSet = 13.0;
        }
        if ( 'Х' == inChar) {
            xOffSet = 21.0;
        }
        if ( 'Ц' == inChar) {
            xOffSet = 19.0;
        }
        if ( 'Ч' == inChar) {
            xOffSet = 16.0;
        }
        if ( 'Ш' == inChar) {
            xOffSet = 28.0;
        }
        if ( 'Щ' == inChar) {
            xOffSet = 31.0;
        }
        if ( 'Ъ' == inChar  ){
            xOffSet = 30.0;
        }
        if ( 'Ы' == inChar  ){
            xOffSet = 30.0;
        }
        if ( 'Ь' == inChar  ){
            xOffSet = 30.0;
        }
        if ( 'Э' == inChar  ) {
            xOffSet = 12.0;
        }
        if ( 'Ю' == inChar  ) {
            xOffSet = 37.0;
        }
        if ( 'Я' == inChar  ) {
            xOffSet = 17.0;
        }
        //----------------
        if ( '0' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '1' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( '2' == inChar  ) {
            xOffSet = 6.0;
        }
        if ( '3' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '4' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( '5' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '6' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '7' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( '8' == inChar  ) {
            xOffSet = 5.0;
        }
        if ( '9' == inChar  ) {
            xOffSet = 4.0;
        }
        return xOffSet;
    }

    public Double offsetFont12(char inChar) {
        Double xOffSet = 0.0;

        if (isCapital(inChar)) {
            xOffSet = 10.0;
            if(isCirrilic(inChar)){
            }
        } else {
            xOffSet = 2.0;
        }
        if ('a' == inChar) {
            xOffSet = 0.0;
        }
        if ('b' == inChar) {
            xOffSet = 1.0;
        }
        if ('c' == inChar) {
            xOffSet = 0.0;
        }
        if ('d' == inChar) {
            xOffSet = 3.0;
        }
        if ('e' == inChar) {
            xOffSet = 0.0;
        }
        if ('f' == inChar) {
            xOffSet = 5.0;
        }
        if ('g' == inChar) {
            xOffSet = 0.0;
        }
        if ('h' == inChar) {
            xOffSet = 3.0;
        }
        if ('i' == inChar) {
            xOffSet = -1.0;
        }
        if ('j' == inChar) {
            xOffSet = -3.0;
        }
        if ('k' == inChar) {
            xOffSet = 3.0;
        }
        if ('l' == inChar) {
            xOffSet = -1.0;
        }
        if ('m' == inChar) {
            xOffSet = 9.0;
        }
        if ('n' == inChar) {
            xOffSet = 4.0;
        }
        if ('o' == inChar) {
            xOffSet = 0.0;
        }
        if ('p' == inChar) {
            xOffSet = 2.0;
        }
        if ('q' == inChar) {
            xOffSet = 0.0;
        }
        if ('r' == inChar) {
            xOffSet = 1.0;
        }
        if ('s' == inChar) {
            xOffSet = 0.0;
        }
        if ('t' == inChar) {
            xOffSet = 0.0;
        }
        if ('u' == inChar) {
            xOffSet = 2.0;
        }
        if ('v' == inChar) {
            xOffSet = 0.0;
        }
        if ('w' == inChar) {
            xOffSet = 7.0;
        }
        if ('x' == inChar) {
            xOffSet = 5.0;
        }
        if ('y' == inChar) {
            xOffSet = 0.0;
        }
        if ('z' == inChar) {
            xOffSet = 0.0;
        }

//------------------
        if ('A' == inChar) {
            xOffSet = 13.0;
        }
        if ('B' == inChar) {
            xOffSet = 20.0;
        }
        if ('C' == inChar) {
            xOffSet = 10.0;
        }
        if ('D' == inChar) {
            xOffSet = 24.0;
        }
        if ('E' == inChar) {
            xOffSet = 10.0;
        }
        if ('F' == inChar) {
            xOffSet = 11.0;
        }
        if ('G' == inChar) {
            xOffSet = 10.0;
        }
        if ('H' == inChar) {
            xOffSet = 15.0;
        }
        if ('I' == inChar) {
            xOffSet = 6.0;
        }
        if ('J' == inChar) {
            xOffSet = 10.0;
        }
        if ('K' == inChar) {
            xOffSet = 20.0;
        }
        if ('L' == inChar) {
            xOffSet = 16.0;
        }
        if ('M' == inChar) {
            xOffSet = 22.0;
        }
        if ('N' == inChar) {
            xOffSet = 19.0;
        }
        if ('O' == inChar) {
            xOffSet = 10.0;
        }
        if ('P' == inChar) {
            xOffSet = 21.0;
        }
        if ('Q' == inChar) {
            xOffSet = 12.0;
        }
        if ('R' == inChar) {
            xOffSet = 24.0;
        }
        if ('S' == inChar) {
            xOffSet = 10.0;
        }
        if ('T' == inChar) {
            xOffSet = 10.0;
        }
        if ('U' == inChar) {
            xOffSet = 15.0;
        }
        if ('V' == inChar) {
            xOffSet = 12.0;
        }
        if ('W' == inChar) {
            xOffSet = 22.0;
        }
        if ('X' == inChar) {
            xOffSet = 23.0;
        }
        if ('Y' == inChar) {
            xOffSet = 11.0;
        }
        if ('Z' == inChar) {
            xOffSet = 13.0;
        }

//--------------Rus-lower case-------
        if (' ' == inChar  ) {
            xOffSet = 5.0;
        }
        if ('а' == inChar  ) {
            xOffSet = 4.5;
        }
        if ('б' == inChar  ) {
            xOffSet = 1.0;
        }
        if ('в' == inChar  ) {
            xOffSet = 0.0;
        }
        if ('г' == inChar  ) {
            xOffSet = 1.5;
        }
        if ('д' == inChar  ) {
            xOffSet = 3.0;
        }
        if ('е' == inChar  ) {
            xOffSet = 0.0;
        }
        if ('ё' == inChar  ) {
            xOffSet = 0.0;
        }
        if ('ж' == inChar) {
            xOffSet = 8.0;
        }
        if ('з' == inChar) {
            xOffSet = 7.0;//3...
        }
        if ('и' == inChar) {
            xOffSet = 4.0;
        }
        if ('й' == inChar) {
            xOffSet = 3.0;
        }
        if ('к' == inChar) {
            xOffSet = 4.5;
        }
        if ('л' == inChar) {
            xOffSet = 7.0;
        }
        if ('м' == inChar ) {
            xOffSet = 9.0;
        }
        if ('н' == inChar ) {
            xOffSet = 5.5;
        }
        if ('о' == inChar ) {
            xOffSet = 0.0;
        }
        if ('п' == inChar ) {
            xOffSet = 3.5;
        }
        if ('р' == inChar ) {
            xOffSet = 4.0;//0...
        }
        if ( 'с' == inChar ) {
            xOffSet = -1.0;
        }
        if ( 'т' == inChar ) {
            xOffSet = 8.0;///  0...7
        }
        if ( 'у' == inChar ) {
            xOffSet = 4.0;//2...
        }
        if ( 'ф' == inChar ) {
            xOffSet = 4.0;
        }
        if ( 'х' == inChar ) {
            xOffSet = 2.0;
        }
        if ( 'ц' == inChar) {
            xOffSet = 5.0;
        }
        if ( 'ч' == inChar) {
            xOffSet = 1.0;
        }
        if( 'ш' == inChar  ) {
            xOffSet = 8.0;//....10
        }
        if( 'щ' == inChar  ) {
            xOffSet = 9.0;//...11
        }
        if ('ь' == inChar ) {
            xOffSet = 0.0;
        }
        if ('ы' == inChar) {
            xOffSet = 5.0;
        }
        if ('ъ' == inChar) {
            xOffSet = 1.0;
        }
        if ('э' == inChar  ) {
            xOffSet = 3.0;
        }
        if('ю' == inChar  ) {
            xOffSet = 4.0;
        }
        if('я' == inChar  ) {
            xOffSet = 5.0;
        }
        //------------------
        if ( 'А' == inChar) {
            xOffSet = 10.0;//....21
        }
        if ( 'Б' == inChar) {
            xOffSet = 11.0;//23.0;
        }
        if ( 'В' == inChar) {
            xOffSet = 10.0;//...16
        }
        if ( 'Г' == inChar ) {
            xOffSet = 8.0; //17.0;
        }
        if ( 'Д' == inChar ) {
            xOffSet = 14.0;//21
        }
        if ( 'Е' == inChar) {
            xOffSet = 7.0;//12
        }
        if ( 'Ё' == inChar) {
            xOffSet = 7.0;//12
        }
        if ( 'Ж' == inChar ) {
            xOffSet = 20.0;//17....35.0;
        }
        if ( 'З' == inChar) {
            xOffSet = 7.0;//10
        }
        if ( 'И' == inChar) {
            xOffSet = 10.0;//17
        }
        if ( 'Й' == inChar) {
            xOffSet = 17.0;
        }
        if ( 'К' == inChar) {
            xOffSet = 11.0;//22.0;
        }
        if ( 'Л' == inChar) {
            xOffSet = 14.0;//10... 21.0;
        }
        if ( 'М' == inChar   ) {
            xOffSet = 17.0;//12...25.0;
        }
        if ( 'Н' == inChar   ) {
            xOffSet = 9.0;//0....12;
        }
        if ( 'О' == inChar) {
            xOffSet = 7.0; //10.0;
        }
        if ( 'П' == inChar  ) {
            xOffSet =7.0;// 11.0;
        }
        if ( 'Р' == inChar  ) {
            xOffSet =6.0; //12.0;
        }
        if ( 'С' == inChar) {
            xOffSet =7.0; //16.0;
        }
        if ( 'Т' == inChar) {
            xOffSet =6.0; //10.0;
        }
        if ( 'У' == inChar) {
            xOffSet =5.0; //7.0;
        }
        if ( 'Ф' == inChar) {
            xOffSet = 9.0;//13
        }
        if ( 'Х' == inChar) {
            xOffSet = 10.0;//21.0;
        }
        if ( 'Ц' == inChar) {
            xOffSet = 15.0;//....19
        }
        if ( 'Ч' == inChar) {
            xOffSet =8.0;// 16.0;
        }
        if ( 'Ш' == inChar) {
            xOffSet =14.0; //28.0;
        }
        if ( 'Щ' == inChar) {
            xOffSet = 20.0;//15...31.0;
        }
        if ( 'Ъ' == inChar  ){
            xOffSet = 15.0;
        }
        if ( 'Ы' == inChar  ){
            xOffSet = 15.0;
        }
        if ( 'Ь' == inChar  ){
            xOffSet = 13.0;
        }
        if ( 'Э' == inChar  ) {
            xOffSet = 7.0;//12
        }
        if ( 'Ю' == inChar  ) {
            xOffSet =15.0;// 37.0;
        }
        if ( 'Я' == inChar  ) {
            xOffSet = 10.0;//17
        }
        //----------------
        if ( '0' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '1' == inChar  ) {
            xOffSet = 1.0;
        }
        if ( '2' == inChar  ) {
            xOffSet = 6.0;
        }
        if ( '3' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '4' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( '5' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '6' == inChar  ) {
            xOffSet = 4.0;
        }
        if ( '7' == inChar  ) {
            xOffSet = 3.0;
        }
        if ( '8' == inChar  ) {
            xOffSet = 5.0;
        }
        if ( '9' == inChar  ) {
            xOffSet = 4.0;
        }
        return xOffSet;
    }

    public Double calcInterval(char inChar) {
        Double xOffSet = 0.0;
        if (1 == fontSet) {
            xOffSet = offsetFont1(inChar);
        }

        if (2 == fontSet) {
            xOffSet = offsetFont2(inChar);
        }

        if (3 == fontSet) {
            xOffSet = offsetFont3(inChar);
        }
        if (4 == fontSet) {
            xOffSet = offsetFont4(inChar);
        }
        if (5 == fontSet) {
            xOffSet = offsetFont5(inChar);
        }
        if (7 == fontSet) {
            xOffSet = offsetFont7(inChar);
        }
        if (8 == fontSet) {
            xOffSet = offsetFont8(inChar);
        }
        if (9 == fontSet) {
            xOffSet = offsetFont9(inChar);
        }
        if (10 == fontSet) {
            xOffSet = offsetFont10(inChar);
        }
        if (11 == fontSet) {
            xOffSet = offsetFont11(inChar);
        }
        if (12 == fontSet) {
            xOffSet = offsetFont12(inChar);
        }
        return xOffSet;
    }

    public ArrayList FillInListOfLetters(String fontTableFileName)    {
        int numOfLineInFile;
        String tempStr;
        ArrayList tempFontTable = new ArrayList();

        try{
            BufferedReader fontTableFile = new BufferedReader(new FileReader(fontTableFileName));
            numOfLineInFile=0;
            do {
                tempStr = fontTableFile.readLine();
                if (null != tempStr) {
                    tempFontTable.add(tempStr);
                    numOfLineInFile++;
                }else{
                    //System.out.println("numOfSamplesInWavFile: "+numOfSamplesInWavFile);
                }
            }while(null!=tempStr);

            fontTableFile.close();

        }catch(IOException ехс){
            System.out.println("Error");
        }


        return tempFontTable;
    }

    public int readAmoutOfLineInFile(String inFileName)    {
        String tempStr;
        int cnt=0;
        try{
            BufferedReader reader = new BufferedReader(new FileReader(inFileName));
            do {
                tempStr = reader.readLine();
                if (null != tempStr) {
                    cnt++;
                }else{
                    //System.out.println("numOfSamplesInWavFile: "+numOfSamplesInWavFile);
                }
            }while(null!=tempStr);
            reader.close();

        }catch(IOException ехс){
            System.out.println("Cannot evaluate an ammout of line in file");
            return 0;
        }
        return cnt;
    }

    public static String replasePositions(String tempString, Double xPosition, Double yPosition)    {
        //System.out.println(j);
        ArrayList xPosListStr= new ArrayList();
        ArrayList xPosListDobl= new ArrayList();

        ArrayList yPosListStr= new ArrayList();
        ArrayList yPosListDobl= new ArrayList();

        xPosListStr.clear();
        xPosListDobl.clear();

        yPosListStr.clear();
        yPosListDobl.clear();

        //System.out.println(tempString);
        //write in file

        //split string   d="[mM] ([-\d\.]+),([-\d\.]+)
        //Pattern p = Pattern.compile("d=\"[mM] ([-\\d\\.]+)");

        Pattern p = Pattern.compile("d=\"[mM] ([-\\d\\.]+),([-\\d\\.]+)");
        Matcher m = p.matcher(tempString);

        // может быть несколько
        while (m.find()) {
            //System.out.println(m.group());
            String numStr = m.group();
            numStr = numStr.substring(5);
            //System.out.println(numStr);
            String[] parts = numStr.split(",");
            xPosListStr.add(parts[0]);
            xPosListDobl.add(Double.parseDouble(parts[0]));

            yPosListStr.add(parts[1]);
            yPosListDobl.add(Double.parseDouble(parts[1]));
        }
        //System.out.println(xPosListStr);
        //System.out.println(yPosListStr);


        for (int n = 0; n < xPosListDobl.size(); n++) {
            xPosListDobl.set(n, ((Double) xPosListDobl.get(n)) + xPosition);
            yPosListDobl.set(n, ((Double) yPosListDobl.get(n)) + yPosition);
        }
        // convert to float
        //System.out.println(xPosListDobl);

        String outString = "";
        for (int n = 0; n < xPosListDobl.size(); n++) {
            tempString = tempString.replaceAll((String) xPosListStr.get(n), xPosListDobl.get(n).toString());
            tempString = tempString.replaceAll((String) yPosListStr.get(n), yPosListDobl.get(n).toString());
        }

        return tempString.substring(4);
        //embed new x coordinate
    }

    public void composeSVGfile(String fontNumber) {
        System.setProperty("console.encoding","utf-8");

        fontSet=Integer.parseInt(fontNumber);
        System.out.println("Text Genegator Font Set: "+fontSet);
        if(1 == fontSet){
            DistanceBetweenLines=23.0;
        }
        if(2 == fontSet){
            DistanceBetweenLines=25.0;
        }
        if(3 == fontSet){
            DistanceBetweenLines=18.0;
        }

        if(4 == fontSet){
            DistanceBetweenLines=21.0;
        }
        if(5 == fontSet){
            DistanceBetweenLines=27.0;
        }
        if(7 == fontSet){
            DistanceBetweenLines=22.0;
        }
        if(8 == fontSet){
            DistanceBetweenLines=26.0;
        }
        if(9 == fontSet){
            DistanceBetweenLines=26.0;
        }
        if(10 == fontSet){
            DistanceBetweenLines=26.0;
        }
        if(11 == fontSet){
            DistanceBetweenLines=26.0;
        }
        if(12 == fontSet){
            DistanceBetweenLines=26.0;
        }
        if(13 == fontSet){
            DistanceBetweenLines=26.0;
        }
        DistanceBetweenLines= line_dist_slider.getValue();

        CustomFont fontData = new CustomFont(fontSet);
        fontData.load_font(fontNumber);

        Double xPosition = 5.0, xOffset=0.0;
        Double yPosition = -270.0;

        Double yStep=DistanceBetweenLines;

        //if(10 == fontSet){
        //    xStep= 12.0;
        //}
        ArrayList FontTable = new ArrayList();
        ArrayList xPosListStr= new ArrayList();
        ArrayList xPosListDobl= new ArrayList();
        int SuccessCnt = 0, amoutOfLineInFile=0,presenceFlag=0;
        String tempStr;
        String inPutFileAdderss     = "C:\\!!docs\\1_my_prj\\1_plotter\\2_txtToSvg\\1_inPutText.txt";
        String fontTableFileAdderss = "C:\\!!docs\\1_my_prj\\1_plotter\\2_txtToSvg\\FontTableAuto.txt";
        String svgHeaderFileAdderss = "C:\\!!docs\\1_my_prj\\1_plotter\\2_txtToSvg\\svgHeader.txt";
        outSvgFileName = "C:\\!!docs\\1_my_prj\\1_plotter\\2_txtToSvg\\vector_"+Integer.toString(fontSet,10)+"_Test.svg";

        int numOfLineInFile=0;
        int numOfreadChars=0, numOftransformedChars=0;

        BufferedWriter bwLines = null;

        try {
            //fwLines = new FileWriter(outSvgFileName);
            bwLines = new BufferedWriter(new FileWriter(outSvgFileName));
            //write svg header

            //BufferedReader fontTableFile = new BufferedReader(new FileReader(fontTableFileAdderss));
            BufferedReader svgHeaderFile = new BufferedReader(new FileReader(svgHeaderFileAdderss));

            do {
                tempStr = svgHeaderFile.readLine();
                if (null != tempStr) {
                    bwLines.write(tempStr + "\n");
                } else {
                    //System.out.println("numOfSamplesInWavFile: "+numOfSamplesInWavFile);
                }
            }while(null!=tempStr);
            svgHeaderFile.close();

            FontTable= FillInListOfLetters(fontTableFileAdderss);
            System.out.println("FontTable.size : " +FontTable.size());

            BufferedReader reader = new BufferedReader(new FileReader(inPutFileAdderss));
            // how much symbols in the file?
            int chi;
            do{
                chi= reader.read();
                System.out.print((char ) chi);// OK
                numOfreadChars++;

                //System.out.println("--------------------------------------------------------");
                //xPosition += xStep;
                //System.out.println(textData.charAt(i));
                char curChar;
                curChar = (char ) chi;
                //System.out.println(curChar);
                //find that font
                //if (' ' != curChar && '﻿' != curChar) {
                if ('﻿' != curChar) {
                    if (0x0A == curChar) {
                        yPosition += yStep;
                        numOfLineInFile++;
                        xPosition = 0.0;
                        numOftransformedChars++;
                        //System.out.println("Next Line");
                        //continue;
                    }
                    if (0x0D == curChar) {
                        xPosition = 5.0;
                        numOftransformedChars++;
                        //System.out.println("Caret return");
                        //continue;
                    }

                    // find char in Font table:
                    presenceFlag=0;
                    for (int j = 0; j < FontTable.size(); j++) {
                        String tempString;
                        tempString = (String) FontTable.get(j);
                        String pattern = "{" + curChar + "}";
                        int index = tempString.indexOf(pattern);
                        //System.out.println(index);
                        if (-1 != index) {
                            //System.out.println("Font presents. Line:" + j + " ");
                            SuccessCnt++;
                            String outStr;
                            outStr = replasePositions(tempString, xPosition, yPosition);
                            bwLines.write(outStr + "\n");
                            numOftransformedChars++;
                            presenceFlag=1;
                            break;
                        }
                    }//for

                    if (0==presenceFlag) {
                        if (0x0A != curChar && 0x0D != curChar) {
                            System.out.println(" (Unknown symbol: "+curChar+" )" );
                        }
                    }
                    xOffset = fontData.calcInterval( curChar);
                    //xOffset = calcInterval( curChar);
                    xPosition += xOffset;

                }
                //else if(' ' == curChar) {
                //    numOftransformedChars++;
                //}
            }while (chi != (-1));

            bwLines.write(" </svg> "+ "\n");
            reader.close();

            bwLines.close();

            System.out.println( );
            System.out.println("read: "+numOfreadChars);
            System.out.println("transformed: "+numOftransformedChars);
            System.out.println("unknown: "+(numOfreadChars-numOftransformedChars));

            String cmdLine = "C:\\Program Files\\Inkscape\\inkscape.exe "+outSvgFileName ;

            System.out.println(cmdLine);
            //Process p = Runtime.getRuntime().exec(cmdLine );

        }catch(IOException ехс){
            System.out.println("Error");
            return;
        }


        System.out.println("Done!");
    }

    String inputFile ="C:\\!!docs\\1_my_prj\\1_plotter\\2_txtToSvg\\1_inPutText.txt";
    public GraphicsContext gc;
    public Double zoom=2.0;

    public void proc(){
        if (10<input) {
            System.out.println("Error input: "+input);
            return;
        }
        if(12<curState) {
            System.out.println("Error state: "+curState);
            return;
        }
        action=actionTable[input][curState];
        curState=stateTable[input][curState];

        switch(action) {
            case 0:
                break;
            case 44:
                //System.out.println("[d=\" ]");
                break;


            case save_first_x:
                num_x = Double.parseDouble(str_x);
                //System.out.print(num_x+",");
                str_x="";
                break;

            case save_first_dot://save_first_y
                num_y = Double.parseDouble(str_y);
               // System.out.print("["+num_x+","+ num_y+"]");
                str_y="";
                break;

            case save_diff_x:
                num_diff_x = Double.parseDouble(str_x);
                //System.out.print(num_diff_x+",");
                num_x2=num_x+num_diff_x;
                str_x="";
                break;

            case save_diff_y:
                num_diff_y = Double.parseDouble(str_y);
                //System.out.print(num_diff_y+"\n");
                num_y2=num_y+num_diff_y;
                //System.out.print("["+num_x2 +","+num_y2+"]\n");
                num_x=num_x2;
                num_y=num_y2;
                str_y="";
                break;

            case stack_digit_x:
                str_x=str_x+inChar;
                break;

            case stack_digit_y:
                str_y=str_y+inChar;
                break;

            case path_done:
                break;

            case draw_line:
                num_diff_y = Double.parseDouble(str_y);
                num_y2=num_y+num_diff_y;
                //System.out.println("[("+num_x +","+num_y+")"+"("+num_x2 +","+num_y2+")]");
                Double x1,y1,x2,y2;
                x1=num_x*zoom;
                y1=num_y*zoom;
                x2=num_x2*zoom;
                y2=num_y2*zoom;
                drawLine(x1.intValue(),  y1.intValue(), x2.intValue(), y2.intValue());
                num_x=num_x2;
                num_y=num_y2;
                str_y="";
                break;

            default:
                //System.out.println("\n\nUnknown action.");
        }

    }


    final int in2_quote =2;
    final int in5_minus=5;
    final int in6_digit=6;
    final int in7_dot=7;

    final int path_done = 99;
    final int save_first_x = 86;

    final int save_diff_x = 810;
    final int stack_digit_x =65;
    final int save_first_dot = 48;
    final int draw_line=2412 ;

    final int save_diff_y =412;
    final int stack_digit_y=57;

    int curState;
    int input;
    int action;
    int stateTable[][];
    int actionTable[][];
    char inChar;

    String str_x;
    String str_y;

    Double num_x;
    Double num_y;

    Double num_diff_x;
    Double num_diff_y;

    Double num_x2;
    Double num_y2;

    public void init_stateTable(){
        stateTable[0][0] = 1;
        stateTable[1][0] = 0;
        stateTable[in2_quote][0] = 0;
        stateTable[3][0] = 0;
        stateTable[4][0] = 0;
        stateTable[in5_minus][0] = 0;
        stateTable[in6_digit][0] = 0;
        stateTable[in7_dot][0] = 0;
        stateTable[8][0] = 0;
        stateTable[9][0] = 0;

        stateTable[0][1] = 0;
        stateTable[1][1] = 2;
        stateTable[in2_quote][1] = 0;
        stateTable[3][1] = 0;
        stateTable[4][1] = 0;
        stateTable[in5_minus][1] = 0;
        stateTable[in6_digit][1] = 0;
        stateTable[in7_dot][1] = 0;
        stateTable[8][1] = 0;
        stateTable[9][1] = 0;

        stateTable[0][2] = 0;
        stateTable[1][2] = 0;
        stateTable[in2_quote][2] = 3;
        stateTable[3][2] = 0;
        stateTable[4][2] = 0;
        stateTable[in5_minus][2] = 0;
        stateTable[in6_digit][2] = 0;
        stateTable[in7_dot][2] = 0;
        stateTable[8][2] = 0;
        stateTable[9][2] = 0;

        stateTable[0][3] = 0;
        stateTable[1][3] = 0;
        stateTable[in2_quote][3] = 0;
        stateTable[3][3] = 4;
        stateTable[4][3] = 0;
        stateTable[in5_minus][3] = 0;
        stateTable[in6_digit][3] = 0;
        stateTable[in7_dot][3] = 0;
        stateTable[8][3] = 0;
        stateTable[9][3] = 0;

        stateTable[0][4] = 0;
        stateTable[1][4] = 0;
        stateTable[in2_quote][4] = 0;
        stateTable[3][4] = 0;
        stateTable[4][4] = 5;
        stateTable[in5_minus][4] = 0;
        stateTable[in6_digit][4] = 0;
        stateTable[in7_dot][4] = 0;
        stateTable[8][4] = 0;
        stateTable[9][4] = 0;

        stateTable[0][5] = 0;
        stateTable[1][5] = 0;
        stateTable[in2_quote][5] = 0;
        stateTable[3][5] = 0;
        stateTable[4][5] = 0;
        stateTable[in5_minus][5] = 6;
        stateTable[in6_digit][5] = 6;
        stateTable[in7_dot][5] = 0;
        stateTable[8][5] = 0;
        stateTable[9][5] = 0;

        stateTable[0][6] = 0;
        stateTable[1][6] = 0;
        stateTable[in2_quote][6] = 0;
        stateTable[3][6] = 0;
        stateTable[4][6] = 0;
        stateTable[in5_minus][6] = 0;
        stateTable[in6_digit][6] = 6;
        stateTable[in7_dot][6] = 6;
        stateTable[8][6] = 7;
        stateTable[9][6] = 0;

        stateTable[0][7] = 0;
        stateTable[1][7] = 0;
        stateTable[in2_quote][7] = 0;
        stateTable[3][7] = 0;
        stateTable[4][7] = 0;
        stateTable[in5_minus][7] = 8;
        stateTable[in6_digit][7] = 8;
        stateTable[in7_dot][7] = 0;
        stateTable[8][7] = 0;
        stateTable[9][7] = 0;

        stateTable[0][8] = 0;
        stateTable[1][8] = 0;
        stateTable[in2_quote][8] = 0;
        stateTable[3][8] = 0;
        stateTable[4][8] = 9;
        stateTable[in5_minus][8] = 0;
        stateTable[in6_digit][8] = 8;
        stateTable[in7_dot][8] = 8;
        stateTable[8][8] = 0;
        stateTable[9][8] = 0;

        stateTable[0][9] = 0;
        stateTable[1][9] = 0;
        stateTable[in2_quote][9] = 0;
        stateTable[3][9] = 0;
        stateTable[4][9] = 0;
        stateTable[in5_minus][9] = 10;
        stateTable[in6_digit][9] = 10;
        stateTable[in7_dot][9] = 0;
        stateTable[8][9] = 0;
        stateTable[9][9] = 0;

        stateTable[0][10] = 0;
        stateTable[1][10] = 0;
        stateTable[in2_quote][10] = 0;
        stateTable[3][10] = 0;
        stateTable[4][10] = 0;
        stateTable[in5_minus][10] = 0;
        stateTable[in6_digit][10] = 10;
        stateTable[in7_dot][10] = 10;
        stateTable[8][10] = 11;
        stateTable[9][10] = 0;

        stateTable[0][11] = 0;
        stateTable[1][11] = 0;
        stateTable[in2_quote][11] = 0;
        stateTable[3][11] = 0;
        stateTable[4][11] = 0;
        stateTable[in5_minus][11] = 12;
        stateTable[in6_digit][11] = 12;
        stateTable[in7_dot][11] = 0;
        stateTable[8][11] = 0;
        stateTable[9][11] = 0;

        stateTable[0][12] = 0;
        stateTable[1][12] = 0;
        stateTable[in2_quote][12] = 0;
        stateTable[3][12] = 0;
        stateTable[4][12] = 9;
        stateTable[in5_minus][12] = 0;
        stateTable[in6_digit][12] = 12;
        stateTable[in7_dot][12] = 12;
        stateTable[8][12] = 0;
        stateTable[9][12] = 0;
    }

    void init(){
        str_x="";
        str_y="";

        //drawLine(0, 0, 0, 400);
        //drawLine(0, sizeOfCanva, sizeOfCanva, sizeOfCanva);
        //drawLine(sizeOfCanva, sizeOfCanva, sizeOfCanva, 0);
        //drawLine(sizeOfCanva, 0, 0, 0);

        input    = 0 ;
        action   = 0 ;
        curState = 0 ;

        stateTable= new int[10][13];
        actionTable= new int[10][13];

        init_stateTable();

        //--------------------------------
        actionTable[0][0] = 2;
        actionTable[1][0] = 0;
        actionTable[in2_quote][0] = 0;
        actionTable[3][0] = 0;
        actionTable[4][0] = 0;
        actionTable[in5_minus][0] = 0;
        actionTable[in6_digit][0] = 0;
        actionTable[in7_dot][0] = 0;
        actionTable[8][0] = 0;
        actionTable[9][0] = 0;

        actionTable[0][1] = 0;
        actionTable[1][1] = 4;
        actionTable[in2_quote][1] = 0;
        actionTable[3][1] = 0;
        actionTable[4][1] = 0;
        actionTable[in5_minus][1] = 0;
        actionTable[in6_digit][1] = 0;
        actionTable[in7_dot][1] = 0;
        actionTable[8][1] = 0;
        actionTable[9][1] = 0;

        actionTable[0][2] = 0;
        actionTable[1][2] = 0;
        actionTable[in2_quote][2] = 0;
        actionTable[3][2] = 0;
        actionTable[4][2] = 0;
        actionTable[in5_minus][2] = 0;
        actionTable[in6_digit][2] = 0;
        actionTable[in7_dot][2] = 0;
        actionTable[8][2] = 0;
        actionTable[9][2] = 0;

        actionTable[0][3] = 0;
        actionTable[1][3] = 0;
        actionTable[in2_quote][3] = 0;
        actionTable[3][3] = 5;
        actionTable[4][3] = 0;
        actionTable[in5_minus][3] = 0;
        actionTable[in6_digit][3] = 0;
        actionTable[in7_dot][3] = 0;
        actionTable[8][3] = 0;
        actionTable[9][3] = 0;

        actionTable[0][4] = 0;
        actionTable[1][4] = 0;
        actionTable[in2_quote][4] = 0;
        actionTable[3][4] = 0;
        actionTable[4][4] = 44;
        actionTable[in5_minus][4] = 0;
        actionTable[in6_digit][4] = 0;
        actionTable[in7_dot][4] = 0;
        actionTable[8][4] = 0;
        actionTable[9][4] = 0;

        actionTable[0][5] = 0;
        actionTable[1][5] = 0;
        actionTable[in2_quote][5] = 0;
        actionTable[3][5] = 0;
        actionTable[4][5] = 0;
        actionTable[in5_minus][5] = stack_digit_x;
        actionTable[in6_digit][5] = stack_digit_x;//65
        actionTable[in7_dot][5] = 0;
        actionTable[8][5] = 0;
        actionTable[9][5] = 0;

        actionTable[0][6] = 0;
        actionTable[1][6] = 0;
        actionTable[in2_quote][6] = 0;
        actionTable[3][6] = 0;
        actionTable[4][6] = 0;
        actionTable[in5_minus][6] = 0;
        actionTable[in6_digit][6] = stack_digit_x;
        actionTable[in7_dot][6] = stack_digit_x;
        actionTable[8][6] = save_first_x;
        actionTable[9][6] = 0;

        actionTable[0][7] = 0;
        actionTable[1][7] = 0;
        actionTable[in2_quote][7] = 0;
        actionTable[3][7] = 0;
        actionTable[4][7] = 0;
        actionTable[in5_minus][7] = stack_digit_y;
        actionTable[in6_digit][7] = stack_digit_y;
        actionTable[in7_dot][7] = 0;
        actionTable[8][7] = 0;
        actionTable[9][7] = 0;

        actionTable[0][8] = 0;
        actionTable[1][8] = 0;
        actionTable[in2_quote][8] = 0;
        actionTable[3][8] = 0;
        actionTable[4][8] = save_first_dot;
        actionTable[in5_minus][8] = 0;
        actionTable[in6_digit][8] = stack_digit_y;
        actionTable[in7_dot][8] = stack_digit_y;
        actionTable[8][8] = 0;
        actionTable[9][8] = 0;

        actionTable[0][9] = 0;
        actionTable[1][9] = 0;
        actionTable[in2_quote][9] = 0;
        actionTable[3][9] = 0;
        actionTable[4][9] = 0;
        actionTable[in5_minus][9] = stack_digit_x;
        actionTable[in6_digit][9] = stack_digit_x;
        actionTable[in7_dot][9] = 0;
        actionTable[8][9] = 0;
        actionTable[9][9] = 0;

        actionTable[0][10] = 0;
        actionTable[1][10] = 0;
        actionTable[in2_quote][10] = 1;
        actionTable[3][10] = 0;
        actionTable[4][10] = 0;
        actionTable[in5_minus][10] = 0;
        actionTable[in6_digit][10] = stack_digit_x;
        actionTable[in7_dot][10] = stack_digit_x;
        actionTable[8][10] = save_diff_x;
        actionTable[9][10] = 0;
        actionTable[0][10] = 0;

        actionTable[1][11] = 0;
        actionTable[in2_quote][11] = 1;
        actionTable[3][11] = 0;
        actionTable[4][11] = 0;
        actionTable[in5_minus][11] = stack_digit_y;
        actionTable[in6_digit][11] = stack_digit_y;
        actionTable[in7_dot][11] = 0;
        actionTable[8][11] = 0;
        actionTable[9][11] = 0;

        actionTable[0][12] = 0;
        actionTable[1][12] = 0;
        actionTable[in2_quote][12] = draw_line;//save_diff_y;
        actionTable[3][12] = 0;
        actionTable[4][12] = draw_line;//save_diff_y;
        actionTable[in5_minus][12] = 0;
        actionTable[in6_digit][12] = stack_digit_y;
        actionTable[in7_dot][12] = stack_digit_y;
        actionTable[8][12] = 0;
        actionTable[9][12] = 0;

/*
        GraphicsContext gc = cnv.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.setLineWidth(1);
        int sizeOfCanva=400;
        gc.strokeLine(0, 0, 0, sizeOfCanva);
        gc.strokeLine(0, sizeOfCanva, sizeOfCanva, sizeOfCanva);
        gc.strokeLine(sizeOfCanva, sizeOfCanva, sizeOfCanva, 0);

*/
    }

    public boolean isDigit(char ch) {
        if('0'==ch){
            return true;
        }
        if('1'==ch){
            return true;
        }
        if('2'==ch){
            return true;
        }
        if('3'==ch){
            return true;
        }
        if('4'==ch){
            return true;
        }
        if('5'==ch){
            return true;
        }
        if('6'==ch){
            return true;
        }
        if('7'==ch){
            return true;
        }
        if('8'==ch){
            return true;
        }
        if('9'==ch){
            return true;
        }
        return false;
    }

    public void input(int ich){
        char ch=(char) ich;
        inChar = ch;
        if('d'==ch){
            input=0;
        }else if('='==ch){
            input=1;
        }else if('\"'==ch){
            input=2;
        }else if('m'==ch){
            input=3;
        }else if(' '==ch){
            input=4;
        }else if('-'==ch){
            input=5;
        }else if(isDigit(ch)) {
            input=6;
        }else if('.'==ch){
            input=7;
        }else if(','==ch){
            input=8;
        }else{
            input=9;
        }

    }


}
