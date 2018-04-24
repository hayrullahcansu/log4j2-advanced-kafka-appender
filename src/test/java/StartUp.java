import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Console;
import java.util.Scanner;

public class StartUp {
    private static final Logger Logger = LogManager.getLogger("AdvencedKafkaAppender");

    public static void main(String[] args){

        System.out.println("TESTTT");
        Logger.debug("Debug Message Logged !!!");
        Logger.info("Info Message Logged !!!");
        Logger.error("Error Message Logged !!!");

        Scanner s = new Scanner(System.in);
        System.out.println(s.nextInt());
        System.out.println(s.nextInt());
        System.out.println(s.next());
        System.out.println(s.next());
        return;
    }
}
