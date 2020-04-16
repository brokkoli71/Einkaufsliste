package com.example.einkaufsliste;

import android.util.Log;

public class NutzerListe {
    static private String[] nutzer;
    static private int[] indizes;
    static private int currentNutzer;


    static public String getNutzerFromId(int id) throws InternetProblemException{
        if (indizes.length == 0)
            update();

        for (int i = 0; i <indizes.length; i++)
            if (id==indizes[i])
                return nutzer[i];
        return null;
    }
    static public int getIdFromName(String name) throws InternetProblemException{
        if (indizes.length == 0)
            update();

        for (int i = 0; i <indizes.length; i++) {
            Log.d("getIdFromName", name + " " + nutzer[i]);
            if (name.equals(nutzer[i]))
                return indizes[i];
        }
        return -1;
    }
    static public int createNutzer(String name) throws InternetProblemException{
        if (getIdFromName(name)==-1) {
            new MyRequest("INSERT INTO nutzer (name) VALUES ('" + name + "')");
            update();
            return getIdFromName(name);
        }else return -2;
    }

    static public void update() throws InternetProblemException{
        MyRequest req = new MyRequest("select id, name from nutzer");
        nutzer = req.getAllContents("name");
        String[] ids = req.getAllContents("id");
        indizes = new int[ids.length];
        for (int i = 0; i < ids.length; i++) {
            indizes[i] = Integer.parseInt(ids[i]);
        }
    }

    public static int getCurrentNutzer() {
        return currentNutzer;
    }

    public static void setCurrentNutzer(int currentNutzer) {
        NutzerListe.currentNutzer = currentNutzer;
    }

}
