package com.cwac.mongoDocs;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.Date;
import java.util.List;

/**
 * Created by David on 10/24/2015.
 */
public class Meeting extends Document{
    public final static String
        ID = "_id",
        FIRST_USER = "firstUser",
        SECOND_USER = "secondUser",
        LOCATION = "location",
        DATE = "date",
        OCCURED = "occured";

    public Meeting(String firstUser, String secondUser, String location){
        this.append("_id", new ObjectId())
                .append(FIRST_USER, firstUser)
                .append(SECOND_USER, secondUser)
                .append(LOCATION, location)
                .append(DATE, new Date())
                .append(OCCURED, true);
    }
    public String getFirstUser(){
        return this.getString(FIRST_USER);
    }

    public void setFirstUser(String firstUser){
        this.append(FIRST_USER, firstUser);
    }

    public String getSecondUser(){
        return this.getString(SECOND_USER);
    }

    public void setSecondUser(String secondUser){
        this.append(SECOND_USER, secondUser);
    }

    public String getLocation() {
        return this.getString(LOCATION);
    }

    public void setLocation(String location){
        this.append(LOCATION, location);
    }

    public Date getDate() {
        return this.getDate(DATE);
    }

    public void setDate(Date date){
        this.append(DATE, date);
    }

    public boolean getOccured() {
        return this.getBoolean(OCCURED);
    }

    public void setOccured(boolean occured){
        this.append(OCCURED, occured);
    }

    public List<String> getUsers(){
        return Arrays.asList(this.getFirstUser(), this.getSecondUser());
    }
}
