package pl.anicos.snapshot.fx;

import javafx.application.Application;

/**
 * Created by anicos on 12/14/15.
 */
public class MainFx {
    public static void main(String[] args) {
        Application.launch(FxApplication.class, args[0].split("\\s"));
    }
}
