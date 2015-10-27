package com.cwac.mongoDocs;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David on 10/24/2015.
 */
public class User extends Document {
    private final static String
            USER = "_id",
            HISTORY = "history",
            IS_ACTIVE = "isActive",
            LOCATION = "location";

    public User(String kerberos, List<ObjectId> history, String location){
        this.append(USER, kerberos)
                .append(HISTORY, history)
                .append(LOCATION, location)
                .append(IS_ACTIVE, true);
    }

    public User(Document userDoc){
        this(userDoc.getString(USER), userDoc.get(HISTORY, ArrayList.class), userDoc.getString(LOCATION));
        validateDoc(userDoc);
    }

    private void validateDoc(Document userDoc){
        if( userDoc.containsKey(USER) &&
            userDoc.containsKey(HISTORY) &&
            userDoc.containsKey(IS_ACTIVE) &&
            userDoc.containsKey(LOCATION)
        ){
            return;
        }
        else {
            throw new IllegalArgumentException("User Document is missing one of the following fields: _id, history, isActive, location: " + userDoc.toString());
        }
    }

    public String getUser(){
        return this.getString(USER);
    }

    public void setUser(String user){
        this.append(USER, user);
    }

    public List<ObjectId> getHistory(){
        return this.get(HISTORY, ArrayList.class);
    }

    public void setHistory(List<Meeting> history){
        this.append(HISTORY, history);
    }

    public boolean getIsActive(){
        return this.getBoolean(IS_ACTIVE);
    }

    public void setIsActive(boolean isActive){
        this.append(IS_ACTIVE, isActive);
    }

    public String getLocation(){
        return this.getString(LOCATION);
    }

    public void setLocation(String location){
        this.append(LOCATION, location);
    }

    public void addToHistory(Meeting meeting){
       List<ObjectId> history = this.getHistory();
        history.add(meeting.getObjectId(Meeting.ID));
        this.append(HISTORY, history);
    }

    public boolean hasMet(User otherUser){
        List<ObjectId>  usersIveMet = this.getHistory(),
                        usersTheyMet = otherUser.getHistory();
        return !Collections.disjoint(usersIveMet, usersTheyMet);
    }

    /*
    public boolean hasMet(User otherUser, MongoCollection<Document> meetingCollection){
        List<String> usersIveMet = this.getUsersMet(meetingCollection),
                     usersTheyMet = otherUser.getUsersMet(meetingCollection);
        return !Collections.disjoint(usersIveMet, usersTheyMet);
    }

    private List<String> getUsersMet(MongoCollection<Document> meetingCollection) {
        List<ObjectId> usersHistory = this.getHistory();
        Bson usersMeetingIds = Filters.in(Meeting.ID, usersHistory);
        List<Document> userMeetings = meetingCollection.find(usersMeetingIds).into(new ArrayList<>());
        List<String> usersMet = userMeetings.stream()
                .map(meeting -> ((Meeting)meeting).getUsers())
                .flatMap(Collection::stream)
                .filter(u -> !u.equals(this.getUser()))
                .collect(Collectors.toList());
        return usersMet;
    }
    */
}
