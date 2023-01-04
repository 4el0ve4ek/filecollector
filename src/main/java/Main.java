package main.java;

import main.java.application.Application;
import main.java.reader.FilesReader;

public class Main {
    public static void main(String[] args) {
        var application = new Application(new FilesReader());
        if (args.length < 1) {
            System.out.println("directory was not provided");
            return;
        }

        application.Process(args[0]);
    }
}