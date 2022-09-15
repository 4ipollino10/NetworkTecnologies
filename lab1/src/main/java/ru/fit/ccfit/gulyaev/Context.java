package ru.fit.ccfit.gulyaev;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private final HashMap<String, Boolean> usersTable = new HashMap<>();



    public void setUser(String addr) {
        this.usersTable.put(addr, true);
        for (Map.Entry<String, Boolean> entry : this.usersTable.entrySet()) {
            System.out.println(entry.getKey() + entry.getValue());
        }
    }
    public void checkUsers(){
        for (Map.Entry<String, Boolean> entry : this.usersTable.entrySet()) {
            if (entry.getValue()) {
                System.out.println(entry.getKey() + "123");
                this.usersTable.put(entry.getKey(), false);
            } else {
                deleteUser(entry.getKey());
                System.out.println("pizdec");
            }
        }
    }

    public void deleteUser(String addr){
        this.usersTable.remove(addr, this.usersTable.get(addr));
        System.out.println(addr + " otvalivsya");
    }

}
