package com.friday.thread.task;

import java.util.Arrays;

import com.friday.thread.TaskSource;

public class CalcDelayValTask extends BasicTask  {

    public CalcDelayValTask(TaskSource taskSrc){
        super(taskSrc);
    }

    @Override
    public void taskRun() {
        String msg = taskSrc.getSource().getContent();
        //String a = "Delay 8 5 10.1.10.101:80 10.1.20.11:80";
        String[] parts =  msg.split(" ");
        if(parts.length < 5){
            throw new RuntimeException("Messge is inccomplete. => " + msg);
        }
        String vsVal = parts[1];
        String serverVal = parts[2];
        String vsAddr = parts[3];
        String serverAddr = parts[4];
        
        
        
    }
    
}