import processing.core.PApplet;
import processing.core.PFont;
import processing.data.Table;
import processing.data.TableRow;
import processing.sound.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main extends PApplet {
    public static PApplet p;
    public ArrayList<Float> gdpVal;
    public ArrayList<Float> year;
    public ArrayList<Float> yearXs;
    public ArrayList<Float> gdpYs;
    public ArrayList<Float> multOf100B;
    public HashMap<Integer, Integer> xMultOf100B;
    public HashMap<Integer, Float> getXSpecificYear;
    public Table table;
    public boolean[] bol;
    int currentX;
    float currentFrek;
    SoundFile sound;
    SawOsc saw;
    int graphR;
    int graphG;
    int graphB;
    int backgroundC;
    PFont f;
    int delayCounter;
    int middleCounter;
    float middDel;
    float aniPos;

    public void settings(){

        size(1400,787);
        pixelDensity(1);

    }
    public void setup(){
    p = this;
    table = loadTable("worldgdp.csv", "header");
    sound = new SoundFile(this, "1sawwhole.aif");
    gdpVal = new ArrayList<>();
    year = new ArrayList<>();
    yearXs = new ArrayList<>();
    gdpYs = new ArrayList<>();
    multOf100B = new ArrayList<>();
    bol = putBolInArr();
    putYearsAndValuesInArrays();
    putXvaluesOfYearsInArray();
    putYValuesOfGDPInArray();
    putMultof100BinArray();
    xMultOf100B = makeHashMapOfXWithSound();
    getXSpecificYear = getXSpecificYear();
/*
        for(HashMap.Entry<Integer, Integer> entry : xMultOf100B.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println(key + " has value of " + value);

            // do what you have to do here
            // In your case, another loop.
        }*/
       // currentX = (int)Math.floor(yearXs.get(0) + 1);
        //currentX = (int)Math.floor(yearXs.get(yearXs.size() - 1));
        currentX = 1200;
        //currentFrek = 12;
        currentFrek = 13;
        saw = new SawOsc(this);
        saw.play();
        saw.freq(currentFrek);
        saw.amp((float) 0);
        graphR = 220;
        graphG = 60;
        graphB = 60;
        backgroundC = 0;
        f = createFont( "TimesNewRomanPS-BoldMT", 64);
        textFont(f);
        middleCounter = 0;
        middDel = 0;
        aniPos = -500;

    }
    public void draw(){
        frameRate(24);
        background(backgroundC);
        noFill();
        stroke(255);
        strokeWeight(2);
        translate(0, height - 40);
        drawGraph();
        drawBackgroundRects();
        drawXAxis();
        drawCircle();
        noFill();
        drawMoneyAboveCircle();
        drawMoneyOnCanvas();
        //makeMiddleSound();
        //drawMoneyOnInMiddle();
        //printCurrentYearOnCanvas();
        makeSound();
        //worldGDPAnimation();
        //visualizeCurrentX();
        visualIzeXurrentFrek();

        if(currentX < yearXs.get(yearXs.size() - 1)) {
            currentX++;
        } else{
            showManyGreatEvents();
            delayCounter++;
        }
        //System.out.println(delayCounter);
        //saveFrame("last/mov_#####.png");
    }
    public void drawGraph(){
        stroke(graphR,graphG,graphB);
        beginShape();
        curveVertex(yearXs.get(0), gdpYs.get(0));
        for (int i = 0; i < yearXs.size(); i++){
            curveVertex(yearXs.get(i), gdpYs.get(i));
        }
        curveVertex(yearXs.get(yearXs.size() - 1), gdpYs.get(gdpYs.size() - 1));
        endShape();
        stroke(255);
    }
    public void drawCircle(){
        noStroke();
        fill(graphR,graphG,graphB);
        circle(currentX, getCurrentYValue(), 6);
        if(xMultOf100B.containsKey(currentX)){
            //circle(currentX, getCurrentYValue(), 9);
        }
        fill(255);
    }
    public void drawBackgroundRects(){
        fill(backgroundC);
        noStroke();
        rect(currentX, -990, 5005, 5005);
        rect(0, getCurrentYValue() - 1, 5000, -5000);
    }

    public void putYearsAndValuesInArrays(){
        for (int i = 0; i < table.getRowCount(); i++){
            TableRow row = table.getRow(i);
            Float currentYear = row.getFloat("Year");
            year.add(currentYear);
            float currentGdpVal = row.getFloat(" (int.-$)");
            gdpVal.add(currentGdpVal);
        }
    }

    public void putXvaluesOfYearsInArray(){
        for (int i = 0; i < year.size(); i++){
            yearXs.add(year.get(i)*80/122 + 40);
        }
    }

    public void putYValuesOfGDPInArray(){
        for (int i = 0; i < gdpVal.size(); i++){
            float gdp = gdpVal.get(i) * 1/900000000;
            float gdp2 = gdp * 1/210;
            gdpYs.add(-gdp2);
        }
    }

    public float getCurrentYValue(){
        for(int i = 0; i < yearXs.size(); i++){
            if (currentX == yearXs.get(i)){
                return gdpYs.get(i);
            } else if(currentX >= yearXs.get(yearXs.size() - 2)){
                return gdpYs.get(gdpYs.size() - 1);
            } else if(currentX > yearXs.get(i) && currentX < yearXs.get(i + 1)){
                    float distanceToI = currentX - yearXs.get(i);
                    float distanceToIPlusOne = yearXs.get(i + 1) - currentX;
                    float YAtI = gdpYs.get(i);
                    float YAtIPlusOne = gdpYs.get(i + 1);
                    float YDifBIAndIPlusOne = YAtIPlusOne - YAtI;

                    return gdpYs.get(i) + YDifBIAndIPlusOne * distanceToI/(distanceToI + distanceToIPlusOne);
                }
            }
        return -1;
    }
    public float getCurrentMoneyValue(){
        for(int i = 0; i < yearXs.size(); i++){
            if (currentX == yearXs.get(i)){
                return gdpVal.get(i);
            } else if(currentX >= yearXs.get(yearXs.size() - 2)){
                return gdpVal.get(gdpVal.size() - 1);
            } else if(currentX > yearXs.get(i) && currentX < yearXs.get(i + 1)){
                float distanceToI = currentX - yearXs.get(i);
                float distanceToIPlusOne = yearXs.get(i + 1) - currentX;
                float MoneyAtI = gdpVal.get(i);
                float MoneyAtIPlusOne = gdpVal.get(i + 1);
                float MoneyDifBIAndIPlusOne = MoneyAtIPlusOne - MoneyAtI;

                return gdpVal.get(i) + MoneyDifBIAndIPlusOne * distanceToI/(distanceToI + distanceToIPlusOne);
            }
        }
        return -1;
    }
    public float getMoneyValueSpecificX(int inputX){
        for(int i = 0; i < yearXs.size(); i++){
            if (inputX == yearXs.get(i)){
                return gdpVal.get(i);
            } else if(inputX >= yearXs.get(yearXs.size() - 2)){
                return gdpVal.get(gdpVal.size() - 1);
            } else if(inputX > yearXs.get(i) && inputX < yearXs.get(i + 1)){
                float distanceToI = inputX - yearXs.get(i);
                float distanceToIPlusOne = yearXs.get(i + 1) - inputX;
                float MoneyAtI = gdpVal.get(i);
                float MoneyAtIPlusOne = gdpVal.get(i + 1);
                float MoneyDifBIAndIPlusOne = MoneyAtIPlusOne - MoneyAtI;

                return gdpVal.get(i) + MoneyDifBIAndIPlusOne * distanceToI/(distanceToI + distanceToIPlusOne);
            }
        }
        return -1;
    }
    public float getYValueSpecificX(int inputX){
        for(int i = 0; i < yearXs.size(); i++){
            if (inputX == yearXs.get(i)){
                return gdpYs.get(i);
            } else if(inputX >= yearXs.get(yearXs.size() - 2)){
                return gdpYs.get(gdpYs.size() - 1);
            } else if(inputX > yearXs.get(i) && inputX < yearXs.get(i + 1)){
                float distanceToI = inputX - yearXs.get(i);
                float distanceToIPlusOne = yearXs.get(i + 1) - inputX;
                float YAtI = gdpYs.get(i);
                float YAtIPlusOne = gdpYs.get(i + 1);
                float YDifBIAndIPlusOne = YAtIPlusOne - YAtI;

                return gdpYs.get(i) + YDifBIAndIPlusOne * distanceToI/(distanceToI + distanceToIPlusOne);
            }
        }
        return -1;
    }
    public void drawMoneyOnCanvas(){
        String s = "";
        String sb = "";
        float f = getCurrentMoneyValue();
        float billions = f * 1/1000000000;
        float trillions = billions * 1/1000;
        int billionsNoDec = (int)Math.floor(billions);
        if(billions < 1000){
            s = String.valueOf(billionsNoDec);
            sb = "$" + s + "Billion";
        } else if(trillions < 10){
            //DecimalFormat formatter = new DecimalFormat("#.0");
            //String tillionsOneDec = formatter.format(trillions);
            float trillionsTimes10 = trillions * 10;
            float trillionsOneDec = (float)Math.floor(trillionsTimes10) * 1/10;
            s = String.valueOf(trillionsOneDec);
            sb = "$" + s + "Trillion";
        } else{
            int trillionsNoDec = (int)Math.floor(trillions);
            s = String.valueOf(trillionsNoDec);
            sb = "$" + s + "Trillion";
        }

        String worldgdpString = "World GDP:";
        textAlign(CENTER);
        textSize(16);
        text(worldgdpString, width/2, -700);
        textSize(54);
        fill(255);
        text(sb, width/2, -650);
        fill(255);

    }
    public void drawMoneyOnInMiddle(){
        String s = "";
        String sb = "";
        //float f = getCurrentMoneyValue();
        //float billions = f * 1/1000000000;
        int billions = middleCounter;
        float trillions = billions * 1/1000;
        //int billionsNoDec = (int)Math.floor(billions);
        if(billions < 1000){
            s = String.valueOf(billions);
            sb = "$" + s + "Billion";
        } else if(trillions < 10){
            //DecimalFormat formatter = new DecimalFormat("#.0");
            //String tillionsOneDec = formatter.format(trillions);
            float trillionsTimes10 = trillions * 10;
            float trillionsOneDec = (float)Math.floor(trillionsTimes10) * 1/10;
            s = String.valueOf(trillionsOneDec);
            sb = "$" + s + "Trillion";
        } else{
            int trillionsNoDec = (int)Math.floor(trillions);
            s = String.valueOf(trillionsNoDec);
            sb = "$" + s + "Trillion";
        }

        String worldgdpString = "World GDP:";
        textAlign(CENTER);
        textSize(16);
        text(worldgdpString, width/2, -500);
        textSize(54);
        fill(255);
        text(sb, width/2, -450);
        fill(255);
        if(middDel < 100){
            middDel++;
        } else if (middleCounter % 100 < 80){
            middleCounter += 2;
        } else {
            middleCounter++;
        }
    }
    public void worldGDPAnimation(){
        String worldgdpString = "World GDP:";
        textAlign(CENTER);
        textSize(16);
        text(worldgdpString, width/2, aniPos);
        if (middDel < 100){
            middDel++;
        } else if (aniPos > -700){
            aniPos--;
        }
    }

    public void drawMoneyAboveCircle(){
        String s = "";
        String sb = "";
        float f = getCurrentMoneyValue();
        float billions = f * 1/1000000000;
        float trillions = billions * 1/1000;
        int billionsNoDec = (int)Math.floor(billions);
        if(billions < 1000){
            s = String.valueOf(billionsNoDec);
            sb = "$" + s + "B";
        } else if(trillions < 10){
            float trillionsTimes10 = trillions * 10;
            float trillionsOneDec = (float)Math.floor(trillionsTimes10) * 1/10;
            s = String.valueOf(trillionsOneDec);
            sb = "$" + s + "T";
        } else{
            int trillionsNoDec = (int)Math.floor(trillions);
            s = String.valueOf(trillionsNoDec);
            sb = "$" + s + "T";
        }
        textAlign(CENTER);
        textSize(12);
        //fill(graphR,graphG,graphB);
        fill(255);
        text(sb, currentX, getCurrentYValue() - 10);
        fill(255);
        textAlign(LEFT);
    }
    public float getCurrentYear(){
        for (int i = 0; i < yearXs.size()-1; i++){
            if(currentX == yearXs.get(i)){
                return year.get(i);
            } else if(currentX >= yearXs.get(yearXs.size() - 2)){
                return year.get(year.size() - 1);
            } else if(currentX < yearXs.get(i + 1) && currentX > yearXs.get(i)){
                float distanceToIPlusOne = yearXs.get(i + 1) - currentX;
                float distanceToI = currentX - yearXs.get(i);
                float yearAtI = year.get(i);
                float yearAtIPlusOne = year.get(i + 1);
                float yearDif = yearAtIPlusOne - yearAtI;

                return year.get(i) + yearDif * distanceToI/(distanceToI + distanceToIPlusOne);
            }
        }
        return -1;
    }
    public float getYearSpecificX(float inputX){
        for (int i = 0; i < yearXs.size()-1; i++){
            if(inputX == yearXs.get(i)){
                return year.get(i);
            } else if(inputX >= yearXs.get(yearXs.size() - 2)){
                return year.get(year.size() - 1);
            } else if(inputX < yearXs.get(i + 1) && inputX > yearXs.get(i)){
                float distanceToIPlusOne = yearXs.get(i + 1) - inputX;
                float distanceToI = inputX - yearXs.get(i);
                float yearAtI = year.get(i);
                float yearAtIPlusOne = year.get(i + 1);
                float yearDif = yearAtIPlusOne - yearAtI;

                return year.get(i) + yearDif * distanceToI/(distanceToI + distanceToIPlusOne);
            }
        }
        return -1;
    }
    public HashMap<Integer, Float> getXSpecificYear(){
        HashMap<Integer, Float> hashMap = new HashMap<>();
        for (int i = 0; i < 1400000; i++){
            int YearAtI = (int) getYearSpecificX(i * 1/1000);
            if (!hashMap.containsKey(YearAtI)){
                hashMap.put(YearAtI, (float) (i * 1/1000));
            }
        }
        return hashMap;
    }
    public void printCurrentYearOnCanvas(){
        String s = "";
        String yearS = "";
        float year = getCurrentYear();
        int formattedYear = (int) Math.floor(year);
        s = String.valueOf(formattedYear);
        yearS = "Year:";
        textSize(16);
        text(yearS, 1100, -700);
        textSize(54);
        text(s, 1100, -650);
    }
    public void drawXAxis(){
        stroke(255);
        strokeWeight(2);
        line(yearXs.get(0), 5, yearXs.get(yearXs.size() - 1), 5);
        float totalXs = yearXs.get(yearXs.size() - 1) - yearXs.get(0);
        float oneYear = totalXs * 1/2020;
        float oneHundredYears = oneYear * 100;
        textSize(12);
        textAlign(CENTER);
        int multOf100 = 0;
        fill(255);
        for (float i = yearXs.get(0); i < yearXs.get(yearXs.size() - 1); i+= oneHundredYears){
            if(i != yearXs.get(0)) {
                line(i, 5, i, 3);
            }
            String current = Integer.toString(multOf100);
            text(current, i, 20);
            multOf100 += 100;
        }
        textAlign(LEFT);
    }
    public void drawYaxis(){
        stroke(255);
        strokeWeight(2);
        line(yearXs.get(0), 12, yearXs.get(0), gdpYs.get(gdpYs.size() - 1) - 30);
        float totalsYs = -1*(gdpYs.get(gdpYs.size() - 1) + gdpYs.get(0));
        float tenTrillion = totalsYs * 10/126;
        textSize(12);
        textAlign(CENTER);
        int multOf10Trill = 0;
        for (float i = gdpYs.get(0); i > gdpYs.get(gdpYs.size() - 1) - 30; i-= tenTrillion){
            line(yearXs.get(0), i, yearXs.get(0) + 4, i);
            String current = Integer.toString(multOf10Trill);
            String output = "$" + current + "T";
            text(output, yearXs.get(0) - 10, i);
            multOf10Trill+= 10;
        }
        drawRectOnYAx();
    }
    public void drawRectOnYAx(){
        fill(10);
        //noStroke();
        rectMode(CENTER);
        rect(yearXs.get(0), gdpYs.get(gdpYs.size() - 1) - 34, 20, 20);
        fill(255);
        stroke(255);
    }
    public void printXWhenMultof100b() {
        for (int i = 0; i < multOf100B.size(); i++) {
            float temp = multOf100B.get(i) * 1000000000;
            float iInBillions = temp * 100;
            if (getMoneyValueSpecificX(currentX) >= iInBillions && getMoneyValueSpecificX(currentX - 1) < iInBillions) {
                System.out.println(currentX);
                //sound.play(2000);
                //saw.freq(87);
                //2000 pretty good
            }
        }
    }
    public void makeMiddleSound(){
            if (middleCounter % 100 == 0 & middleCounter != 0){
                sound.amp((float)0.2);
                sound.play(200);
                String beep = "*click*";
                text(beep, width/2, -300);
            }
    }
    public void makeSound() {
        if(currentX > yearXs.get(yearXs.size() - 1)){
            saw.amp(0);
        }
        for (int i = 0; i < multOf100B.size(); i++) {
            float temp = multOf100B.get(i) * 1000000000;
            float iInBillions = temp * 100;
            if (getMoneyValueSpecificX(currentX) >= iInBillions && getMoneyValueSpecificX(currentX - 1) < iInBillions) {

                /*if(!bol[currentX]){
                    sound.play(2000);
                    bol[currentX] = true;
                }*//*
                sound.play(2000);
*/

                if(currentX < 1267){
                    sound.amp((float)0.2);
                    sound.play(200);
                } else if (currentX >= 1267 && currentX < 1300){
                    saw.amp((float) 0.3);
                    if (xMultOf100B.get(currentX) > currentFrek) {
                        currentFrek = xMultOf100B.get(currentX);
                        saw.freq(currentFrek);
                    }
                    saw.freq(currentFrek);
                    //currentFrek*= 1.0017;
                    currentFrek*= 1.015;
                } else if (currentX >= 1300 && currentX < 1320){
                    saw.amp((float) 0.3);
                    if (xMultOf100B.get(currentX) > currentFrek) {
                        currentFrek = xMultOf100B.get(currentX);
                        saw.freq(currentFrek);
                    }
                    saw.freq(currentFrek);
                    currentFrek*= 1.005;
                } else {
                    saw.freq(currentFrek);
                    currentFrek*=1.003;
                }
            }
        }
    }
    public HashMap<Integer, Integer> makeHashMapOfXWithSound(){
        HashMap<Integer, Integer> hmap = new HashMap<>();
        int posX = 0;
        for(int j = 0; j < 1500; j++) {
            for (int i = 0; i < multOf100B.size(); i++) {
                float temp = multOf100B.get(i) * 1000000000;
                float iInBillions = temp * 100;
                if (getMoneyValueSpecificX(j) >= iInBillions && getMoneyValueSpecificX(j - 1) < iInBillions) {
                    if(!hmap.containsKey(j)){
                        hmap.put(j, 1);
                    } else {
                        hmap.put(j, hmap.get(j) + 1);
                    }
                }
            }
        }
        return hmap;
    }
    public void putMultof100BinArray(){
        for(float i = 1; i < 1300; i++){
            multOf100B.add(i);
        }
    }
    public boolean[] putBolInArr(){
        boolean[] bol = new boolean[1450];
        Arrays.fill(bol, Boolean.FALSE);
        return bol;
    }
    public void visualIzeXurrentFrek(){
        float c = currentFrek;
        String s = String.valueOf(c);
        textSize(32);
        text(s, width/2, -400);
    }
    public void visualizeCurrentX(){
        float c = currentX;
        String s = String.valueOf(c);
        textSize(32);
        text(s, width/2, -300);
    }


    public void showAGreatEvent(String s, int year, int delay){
        textAlign(RIGHT);
        int dif = 0;
        int fillVal = 0;
        if(year > 1920){
            dif = 8;
        } else if (year > 1900){
            dif = 10;
        } else {
            dif = 14;
        }

        int len = 14;
        int difBetTextAndLine = 3;
        float XvalOfEvent = getXSpecificYear.get(year);
        float YValOfEvent = getYValueSpecificX((int) XvalOfEvent);
        if (year == 2020){
            YValOfEvent += 10;
        }


        if (delayCounter > delay){
            fillVal = 11;
        }
        if (delayCounter > delay + 1){
            fillVal = 22;
        }
        if (delayCounter > delay + 2){
            fillVal = 33;
        }
        if (delayCounter > delay + 3){
            fillVal = 44;
        }
        if (delayCounter > delay + 4){
            fillVal = 55;
        }
        if (delayCounter > delay + 5){
            fillVal = 66;
        }
        if (delayCounter > delay + 6){
            fillVal = 77;
        }
        if (delayCounter > delay + 7){
            fillVal = 88;
        }
        if (delayCounter > delay + 8){
            fillVal = 99;
        }
        if (delayCounter > delay + 9){
            fillVal = 110;
        }
        if (delayCounter > delay + 10){
            fillVal = 121;
        }
        if (delayCounter > delay + 11){
            fillVal = 132;
        }
        if (delayCounter > delay + 12){
            fillVal = 143;
        }
        if (delayCounter > delay + 13){
            fillVal = 154;
        }
        if (delayCounter > delay + 14){
            fillVal = 165;
        }
        if (delayCounter > delay + 15){
            fillVal = 176;
        }
        if (delayCounter > delay + 16){
            fillVal = 187;
        }
        if (delayCounter > delay + 17){
            fillVal = 198;
        }
        if (delayCounter > delay + 18){
            fillVal = 209;
        }
        if (delayCounter > delay + 19){
            fillVal = 221;
        }
        if (delayCounter > delay + 20){
            fillVal = 232;
        }
        if (delayCounter > delay + 21){
            fillVal = 243;
        }
        if (delayCounter > delay + 22){
            fillVal = 255;
        }



        stroke(fillVal);
        line (XvalOfEvent - dif, YValOfEvent, XvalOfEvent - dif - len, YValOfEvent);
        noStroke();
        fill(fillVal);
        textSize(12);
        text(s, XvalOfEvent - dif - len - difBetTextAndLine, YValOfEvent + 2);
    }
    public void showManyGreatEvents(){
        showAGreatEvent("Electricity", 1880, 20);
        showAGreatEvent("Aviation", 1915, 40);
        showAGreatEvent("Antibiotics", 1938, 60);
        showAGreatEvent("Modern Computer", 1950, 80);
        showAGreatEvent("Nuclear Power", 1956, 100);
        showAGreatEvent("Moon Landing", 1970, 120);
        //showAGreatEvent("Digital Camera", 1974);
        showAGreatEvent("Internet", 1990, 140);
        showAGreatEvent("Smartphone", 2006, 160);
        showAGreatEvent("Blockchain", 2008, 180);
        showAGreatEvent("CRISPR", 2011, 200);
        showAGreatEvent("Multi-use Rocket", 2017, 220);
        showAGreatEvent("Quantum Computing", 2020, 240);
    }


    public static void main(String[] args) {
        PApplet.main("Main");
    }
}
