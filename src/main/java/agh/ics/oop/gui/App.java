package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class App extends Application implements IPositionChangeObserver {
    AbstractWorldMap map;
    private double width =40;
    private double height=40;
    private ImageLoader imageLoader;
    private Vector2d borderSW = new Vector2d(0,0);
    private Vector2d borderNE = new Vector2d(0,0);
    private GridPane gridPane;
    private Scene scene;
    private Thread engineThread;

    boolean recalculateBorders()
    {

        Vector2d newBorderNE = map.getBorderNE();
        Vector2d newBorderSW = map.getBorderSW();

        boolean out =((newBorderNE!= borderNE || newBorderSW!= borderSW));
        borderNE = newBorderNE;
        borderSW = newBorderSW;

        return out;
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        gridPane = new GridPane();


        Button button1 = new Button("Start");
        Button button2 = new Button("Button Number 2");


        TextField txtField = new TextField();
        TextField output = new TextField();
        HBox h1 = new HBox(button1,txtField);
        HBox h2 = new HBox(output);


        VBox vbox = new VBox(h1, h2,gridPane);



        scene = new Scene(vbox, 400, 400);
        imageLoader = new ImageLoader();
        recalculateBorders();
        drawMap();

        primaryStage.setScene(scene);
        primaryStage.show();
        gridPane.setGridLinesVisible(true);


        button1.setOnAction(value ->  {
            try {
                startThread(txtField.getText().split(" "));
            }catch (IllegalArgumentException e)
            {
                output.setText(e.getMessage());

            }
        });

    }

    public void init()
    {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        startThread(args);

    }


    Vector2d convertToMapPosition(Vector2d from){
        return new Vector2d(from.x - borderSW.x + 1,  borderNE.y-borderSW.y-(from.y - borderSW.y )+ 1);
    }


    void drawMap()
    {
        gridPane.setGridLinesVisible(true);
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
        gridPane.add(new Label("y\\x"),0,0,1,1);
        gridPane.getColumnConstraints().add(new ColumnConstraints(width));
        gridPane.getRowConstraints().add(new RowConstraints(height));

        for (int i = borderSW.x; i<= borderNE.x; i++)
        {
            Label _tmplabel = new Label(Integer.toString(i));
            GridPane.setHalignment(_tmplabel, HPos.CENTER);
            gridPane.add(_tmplabel,i- borderSW.x+1,0,1,1);

            gridPane.getColumnConstraints().add(new ColumnConstraints(width));
        }
        for (int i = borderSW.y; i<= borderNE.y; i++)
        {
            Label _tmplabel = new Label(Integer.toString(borderNE.y-borderSW.y-i));
            GridPane.setHalignment(_tmplabel, HPos.CENTER);
            gridPane.add(_tmplabel,0,i- borderSW.y+1,1,1);

            gridPane.getRowConstraints().add(new RowConstraints(height));
        }
        for (int y = borderSW.y; y<= borderNE.y; y++)
        {
            for (int x = borderSW.x; x<= borderNE.x; x++)
            {
                String print = "";
                IMapElement el = map.objectAt(new Vector2d(x,y));
                Vector2d mapPosition = convertToMapPosition(new Vector2d(x,y));

                if (el != null) {
                    try{
                        GuiElementBox gui = new GuiElementBox(el,20,20,imageLoader);
                        VBox box = gui.createBox();
                        GridPane.setHalignment(box, HPos.CENTER);
                        gridPane.add(box, mapPosition.x, mapPosition.y, 1, 1);
                    }catch(FileNotFoundException e){
                        System.out.println(e.getMessage()+ "File from object:"+el.toString()+" not found. Looked for "+el.getFilename());
                        print= el.toString();
                        Label _tmplabel = new Label(print);
                        GridPane.setHalignment(_tmplabel, HPos.CENTER);
                        gridPane.add(_tmplabel, mapPosition.x, mapPosition.y, 1, 1);
                    }
                }
                else{
                    Label _tmplabel = new Label(print);
                    GridPane.setHalignment(_tmplabel, HPos.CENTER);
                    gridPane.add(_tmplabel, mapPosition.x, mapPosition.y, 1, 1);
                }
            }
        }
        gridPane.setGridLinesVisible(true);

    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement e) {

        Platform.runLater(()->{
            recalculateBorders();
            drawMap();
        });

    }

    void startThread(String[] args) throws IllegalArgumentException{
        if(engineThread != null)
            engineThread.interrupt();
        MoveDirection[] directions1;
        try {
            directions1 = OptionsParser.parse(args);
        }catch(IllegalArgumentException e){
            throw e;
        }

        System.out.println("system wystartował");
        System.out.println(directions1);
        GrassField map1 = new GrassField(20);
        map = map1;

        Vector2d[] positions1 = { new Vector2d(2,2), new Vector2d(2,10) };
        SimulationEngine engine1 = new SimulationEngine(directions1, map1, positions1);
        engine1.addListener(this);
        engineThread = new Thread(engine1);
        engineThread.start();
    }
}
