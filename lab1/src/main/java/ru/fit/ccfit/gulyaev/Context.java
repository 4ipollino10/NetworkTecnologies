package ru.fit.ccfit.gulyaev;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Context {
    private final Map<String, Boolean> usersTable = new ConcurrentHashMap<>();



    public void setUser(String addr) {
        this.usersTable.put(addr, true);
    }
    public void checkUsers(){
        for (String add : this.usersTable.keySet()) {
            if (this.usersTable.get(add)) {
                this.usersTable.put(add, false);
            } else {
                this.usersTable.remove(add);
                System.out.println("USER " + add + " OTVALILSYA");
            }
        }
        System.out.println("!!!ATTENTION!!!\n" + "There are " + usersTable.size() + " copies of me\n" + "!!!ATTENTION!!!");
    }

}
