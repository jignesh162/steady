package com.steady.nifty.strategy.util;

public class RunThreads {
    public static void main(String[] args) 
    { 
        LoadDatabase janLoadDatabase = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN");
        janLoadDatabase.start();
        LoadDatabase febLoadDatabase = new LoadDatabase("C://Learn//OriginalData//INDICES//02FEB", "C://Learn//OriginalData//NIFTY_OPTIONS//02FEB");
        febLoadDatabase.start();
        LoadDatabase marLoadDatabase = new LoadDatabase("C://Learn//OriginalData//INDICES//03MAR", "C://Learn//OriginalData//NIFTY_OPTIONS//03MAR");
        marLoadDatabase.start();
        LoadDatabase aprLoadDatabase = new LoadDatabase("C://Learn//OriginalData//INDICES//04APR", "C://Learn//OriginalData//NIFTY_OPTIONS//04APR");
        aprLoadDatabase.start();
        LoadDatabase mayLoadDatabase = new LoadDatabase("C://Learn//OriginalData//INDICES//05MAY", "C://Learn//OriginalData//NIFTY_OPTIONS//05MAY");
        mayLoadDatabase.start();
        LoadDatabase junLoadDatabase = new LoadDatabase("C://Learn//OriginalData//INDICES//06JUN", "C://Learn//OriginalData//NIFTY_OPTIONS//06JUN");
        junLoadDatabase.start();

        /*LoadDatabase julLoadDatabase = new LoadDatabase("C://Learn//OriginalData//INDICES//07JUL", "C://Learn//OriginalData//NIFTY_OPTIONS//07JUL");
        julLoadDatabase.start();
        LoadDatabase augLoadDatabase = new LoadDatabase("C://Learn//OriginalData//INDICES//08AUG", "C://Learn//OriginalData//NIFTY_OPTIONS//08AUG");
        augLoadDatabase.start();
        LoadDatabase sepLoadDatabase = new LoadDatabase("C://Learn//OriginalData//INDICES//09SEP", "C://Learn//OriginalData//NIFTY_OPTIONS//09SEP");
        sepLoadDatabase.start();
        LoadDatabase octLoadDatabase = new LoadDatabase("C://Learn//OriginalData//INDICES//10OCT", "C://Learn//OriginalData//NIFTY_OPTIONS//10OCT");
        octLoadDatabase.start();
        LoadDatabase novLoadDatabase = new LoadDatabase("C://Learn//OriginalData//INDICES//11NOV", "C://Learn//OriginalData//NIFTY_OPTIONS//11NOV");
        novLoadDatabase.start();
        LoadDatabase decLoadDatabase = new LoadDatabase("C://Learn//OriginalData//INDICES//12DEC", "C://Learn//OriginalData//NIFTY_OPTIONS//12DEC");
        decLoadDatabase.start(); 

        LoadDatabase janLoadDatabase1 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_01012018");
        janLoadDatabase1.start();

        LoadDatabase janLoadDatabase2 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_02012018");
        janLoadDatabase2.start();

        LoadDatabase janLoadDatabase3 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_03012018");
        janLoadDatabase3.start();

        LoadDatabase janLoadDatabase4 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_04012018");
        janLoadDatabase4.start();

        LoadDatabase janLoadDatabase5 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_05012018");
        janLoadDatabase5.start();

        LoadDatabase janLoadDatabase8 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_08012018");
        janLoadDatabase8.start();

        LoadDatabase janLoadDatabase9 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_09012018");
        janLoadDatabase9.start();

        LoadDatabase janLoadDatabase10 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_10012018");
        janLoadDatabase10.start();

        LoadDatabase janLoadDatabase11 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_11012018");
        janLoadDatabase11.start();

        LoadDatabase janLoadDatabase12 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_12012018");
        janLoadDatabase12.start();

        LoadDatabase janLoadDatabase15 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_15012018");
        janLoadDatabase15.start();

        LoadDatabase janLoadDatabase16 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_16012018");
        janLoadDatabase16.start();

        LoadDatabase janLoadDatabase17 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_17012018");
        janLoadDatabase17.start();

        /*LoadDatabase janLoadDatabase18 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_18012018");
        janLoadDatabase18.start();

        LoadDatabase janLoadDatabase19 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_19012018");
        janLoadDatabase19.run();

        LoadDatabase janLoadDatabase20 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_22012018");
        janLoadDatabase20.run();

        LoadDatabase janLoadDatabase23 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_23012018");
        janLoadDatabase23.start();

        LoadDatabase janLoadDatabase24 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_24012018");
        janLoadDatabase24.start();

        LoadDatabase janLoadDatabase25 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_25012018");
        janLoadDatabase25.start();

        LoadDatabase janLoadDatabase29 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_29012018");
        janLoadDatabase29.start();

        LoadDatabase janLoadDatabase30 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_30012018");
        janLoadDatabase30.start();

        LoadDatabase janLoadDatabase31 = new LoadDatabase("C://Learn//OriginalData//INDICES//01JAN", "C://Learn//OriginalData//NIFTY_OPTIONS//01JAN//GFDLNFO_OPTIONS_31012018");
        janLoadDatabase31.start();*/
    }
    
}