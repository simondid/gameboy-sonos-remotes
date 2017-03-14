package com.simon.sonos;

import java.util.ArrayList;

public class browse{
   public int numberReturned,TotalMatches,updateID;
    public String result;

    public ArrayList<trackData> item = new ArrayList<>();
    public ArrayList<browseContainer> container = new ArrayList<>();

    public void print() {
        System.out.println("Number Returned : "+numberReturned);
        System.out.println("Total matches : "+TotalMatches);
        System.out.println("update id : "+updateID);

        for (int i = 0; i < container.size(); i++) {
            container.get(i).print();
        }
        System.out.println();
        for (int i = 0; i < item.size(); i++) {
            item.get(i).print();
        }
        System.out.println();
    }
}
