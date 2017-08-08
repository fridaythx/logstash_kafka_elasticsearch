package com.friday;

import com.friday.consumer.MessageConsumer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try{
            System.out.println("Consumer starts consuming...");
            MessageConsumer.start();
        }catch(Exception e){
            System.out.println("There's en error occured.");
        }finally{
            System.out.println("Consumer stops consuming...");
            MessageConsumer.stop();
        }
        
    }
}
