package ru.fit.ccfit.gulyaev;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private final HashMap<String, Boolean> usersTable = new HashMap<>();



    public void setUser(String addr) {
        this.usersTable.put(addr, true);
    }
    public void checkUsers(){
        for (Map.Entry<String, Boolean> entry : this.usersTable.entrySet()) {
            if (entry.getValue()) {
                this.usersTable.put(entry.getKey(), false);
            } else {
                deleteUser(entry.getKey());
                System.out.println("USER " + entry.getKey() + " OTVALILSYA");
            }
        }
    }

    public void deleteUser(String addr){
        this.usersTable.remove(addr);
        System.out.println(addr + " otvalivsya");
    }

}
