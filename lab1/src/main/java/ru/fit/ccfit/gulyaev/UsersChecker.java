package ru.fit.ccfit.gulyaev;

import java.util.TimerTask;

public class UsersChecker extends TimerTask {

    Context context;

    public UsersChecker(Context context){
        this.context = context;
    }

    @Override
    public void run() {
        this.context.checkUsers();
    }



}
