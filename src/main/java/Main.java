package main.java;

import main.java.application.Application;
import main.java.logger.StdLogger;
import main.java.reader.FilesReader;

public class Main {
    public static void main(String[] args) {

        var logger = new StdLogger();

        var application = new Application(logger, new FilesReader(logger));

        if (args.length < 1) {
            logger.Errorf("directory was not provided");
            return;
        }

        application.Process(args[0]);
    }
}