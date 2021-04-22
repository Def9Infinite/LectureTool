package nl.tudelft.oopp.project;

import nl.tudelft.oopp.project.views.CreateSessionDisplay;

public class MainApp {
    public static void main(String[] args) {
        System.setProperty("java.net.useSystemProxies", "true");
        CreateSessionDisplay.main(new String[0]);
    }
}
