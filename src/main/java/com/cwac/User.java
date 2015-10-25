package com.cwac;

import org.bson.Document;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David on 10/24/2015.
 */
public class User extends Document {
    User(String kerberos, List<Pair> history, boolean isActive, String location){
        this.append("_id", kerberos)
                .append("history", history)
                .append("isActive", isActive)
                .append("location", location);
    }

    public List<Pair> getHistory(){
        return this.get("history", ArrayList.class);
    }

    public void addToHistory(Pair pair){
       List<Pair> history = this.getHistory();
        history.add(pair);
        this.append("history", history);
    }

    public boolean hasMet(User otherUser){
        List<String> usersIveMet = this.getUsersMet(),
                     usersTheyMet = otherUser.getUsersMet();
        return !Collections.disjoint(usersIveMet, usersTheyMet);
    }

    public List<String> getUsersMet() {
        List<Pair> usersHistory = this.getHistory();
        List<String> usersMet = usersHistory.stream()
                .map(Pair::getUsers)
                .flatMap(Collection::stream)
                .filter(u -> !u.equals(this.getKerberos()))
                .collect(Collectors.toList());
        return usersMet;
    }

    public String getKerberos(){
        return this.getString("_id");
    }
}
