package us.ATM6SMP.smpBot.api;

import java.util.ArrayList;

public class DataHandler {
    private static DataHandler instance;

    private static ArrayList<String> devList = new ArrayList<>();
    private ArrayList<Long> helperList = new ArrayList<>();


    public static DataHandler getInstance() {
        if(instance == null) {
            instance = new DataHandler();
            addDevs();
        }
        return instance;
    }

    public static ArrayList<String> getDev() {
        return devList;
    }

    public ArrayList<Long> getBotHelperList() {
        return helperList;
    }

    private static void addDevs() {
        devList.add("206383620531683328");
    }
}
