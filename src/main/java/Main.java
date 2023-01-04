package main.java;

import main.java.application.Application;
import main.java.logger.StdLogger;
import main.java.reader.CachedReader;
import main.java.reader.LocalFilesReader;

public class Main {
    public static void main(String[] args) {

        var logger = new StdLogger();

        var localFileReader = new LocalFilesReader(logger);
        var cachedFileReader = new CachedReader(localFileReader); // TODO: use cache just for one Process call (or invalidate)
        var application = new Application(logger, cachedFileReader);

        if (args.length < 1) {
            logger.Errorf("directory was not provided");
            return;
        }

        application.Process(args[0]);
    }
}