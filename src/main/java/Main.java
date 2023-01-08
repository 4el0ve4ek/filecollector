package main.java;

import main.java.application.Application;
import main.java.logger.ConsoleLogger;
import main.java.reader.CachedFileReader;
import main.java.reader.LocalFileReader;

public class Main {
    public static void main(String[] args) {

        var logger = new ConsoleLogger();

        var localFileReader = new LocalFileReader(logger);
        var cachedFileReader = new CachedFileReader(localFileReader); // TODO: use cache just for one Process call (or invalidate)
        var application = new Application(logger, cachedFileReader);

        if (args.length < 1) {
            logger.Errorf("directory was not provided");
            return;
        }

        application.Process(args[0]);
    }
}