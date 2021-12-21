package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class App extends Application {
    GrassField map1;
    GrassField map2;
    private double width =40;
    private double height=40;
    private ImageLoader imageLoader;
    private Vector2d borderSW = new Vector2d(0,0);
    private Vector2d borderNE = new Vector2d(0,0);
    private GridPane gridPane1;
    private GridPane gridPane2;
    private Scene scene;
    private Thread engineThread1;
    private SimulationEngine simulationEngine1;
    private Thread engineThread2;
    private SimulationEngine simulationEngine2;
    private Stage _primaryStage;

    private TextArea signalMap2;
    private TextArea signalMap1;

    private Animal inspected1 = null;
    private TextArea inspectorMap1;
    private Animal inspected2 = null;
    private TextArea inspectorMap2;

    XYChart.Series animalNumberSeries1 = new XYChart.Series();
    LineChart<Integer,Integer> animalNumberChart1;
    XYChart.Series grassNumberSeries1 =new XYChart.Series();
    LineChart<Integer,Integer> grassNumberChart1;
    XYChart.Series averageEnergySeries1 = new XYChart.Series();
    LineChart<Integer,Integer> averageEnergy1;
    XYChart.Series averageLifespanSeries1 = new XYChart.Series();
    LineChart<Integer,Integer> averageLifespan1;
    XYChart.Series averageChildrenForLivingSeries1 = new XYChart.Series();
    LineChart<Integer,Integer> averageChildrenForLivingCount1;
    Label dominantGenotype1;

    XYChart.Series animalNumberSeries2 = new XYChart.Series();
    LineChart<Integer,Integer> animalNumberChart2;
    XYChart.Series grassNumberSeries2 =new XYChart.Series<>();
    LineChart<Integer,Integer> grassNumberChart2;
    XYChart.Series averageEnergySeries2 = new XYChart.Series();
    LineChart<Integer,Integer> averageEnergy2;
    XYChart.Series averageLifespanSeries2 = new XYChart.Series();
    LineChart<Integer,Integer> averageLifespan2;
    XYChart.Series averageChildrenForLivingSeries2 = new XYChart.Series();
    LineChart<Integer,Integer> averageChildrenForLivingCount2;
    Label dominantGenotype2;


    boolean recalculateBorders(GrassField map)
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

        TextField startingAnimals = new TextField("2");
        HBox StartingAnimals = new HBox(new Label("Number of starting animals:"), startingAnimals);

        TextField mapHeight = new TextField("10");
        HBox MapHeight = new HBox(new Label("Height of the map:"), mapHeight);

        TextField mapWidth = new TextField("20");
        HBox MapWidth = new HBox(new Label("Width of the map:"), mapWidth);

        TextField moveEnergy = new TextField("1");
        HBox MoveEnergy = new HBox(new Label("Energy lost on one move:"), moveEnergy);

        TextField plantEnergy = new TextField("50");
        HBox PlantEnergy = new HBox(new Label("Energy gained by eating plant:"), plantEnergy);

        TextField jungleRatio = new TextField("0.5");
        HBox JungleRatio = new HBox(new Label("Ratio of jungle to map (<1):"), jungleRatio);

        TextField startingEnergy = new TextField("50");
        HBox StartingEnergy = new HBox(new Label("Energy on start:"), startingEnergy);


        CheckBox isMap1Magic = new CheckBox();
        VBox map1 = new VBox(new Label("map1"), isMap1Magic);

        CheckBox isMap2Magic = new CheckBox();
        VBox map2 = new VBox(new Label("map2"), isMap2Magic);

        HBox modes = new HBox(map1,map2);
        VBox Modes = new VBox(new Label ("Choose if simulation type should be \"mAgiCaL\""),modes);


        Button Start = new Button("START");
        TextField output = new TextField();
        HBox h2 = new HBox(output);


        VBox vbox = new VBox(StartingAnimals,MapHeight,MapWidth,MoveEnergy,PlantEnergy,JungleRatio,StartingEnergy,Modes,Start, h2);


        scene = new Scene(vbox, 400, 400);
        _primaryStage = primaryStage;
        _primaryStage.setScene(scene);
        _primaryStage.show();


        Start.setOnAction(value ->  {
            try {
                StartSimulation(
                        Integer.parseInt(startingAnimals.getText()),
                        Integer.parseInt(startingEnergy.getText()),
                        Integer.parseInt(mapHeight.getText()),
                        Integer.parseInt(mapWidth.getText()),
                        Integer.parseInt(moveEnergy.getText()),
                        Integer.parseInt(plantEnergy.getText()),
                        Double.parseDouble(jungleRatio.getText()),
                        isMap1Magic.isSelected(),
                        isMap2Magic.isSelected()
                );
            }catch (IllegalArgumentException e)
            {
                output.setText(e.getMessage());
            }
        });

    }

    private void StartSimulation(int startingAnimals, int startingEnergy ,int mapHeight, int mapWidth, int moveEnergy, int plantEnergy, double jungleRatio, boolean magical1, boolean magical2){
        gridPane1 = new GridPane();
        gridPane1.setGridLinesVisible(true);

        gridPane2 = new GridPane();
        gridPane2.setGridLinesVisible(true);
        imageLoader = new ImageLoader();

        Button pause1 = new Button("Start");
        Button pause2 = new Button("Start");

        pause1.setOnAction( value ->{
            simulationEngine1.interrupt();
        });
        pause2.setOnAction( value ->{
            simulationEngine2.interrupt();
        });

        signalMap1 = new TextArea("----OUTPUT----");
       // signalMap1.setMinWidth(100);
       // signalMap1.setMinHeight(5);
        signalMap2 = new TextArea("----OUTPUT----");
        //signalMap2.setMinWidth(100);
       // signalMap2.setMinHeight(5);

        inspectorMap1 = new TextArea("No inspected, hit pause and choose your animal");
        inspectorMap2 = new TextArea("No inspected, hit pause and choose your animal");

        animalNumberChart1 = new LineChart(new NumberAxis(), new NumberAxis());
        grassNumberChart1= new LineChart(new NumberAxis(), new NumberAxis());
        averageEnergy1= new LineChart(new NumberAxis(), new NumberAxis());
        averageLifespan1= new LineChart(new NumberAxis(), new NumberAxis());
        averageChildrenForLivingCount1= new LineChart(new NumberAxis(), new NumberAxis());
        dominantGenotype1= new Label();

        animalNumberChart2= new LineChart(new NumberAxis(), new NumberAxis());
        grassNumberChart2= new LineChart(new NumberAxis(), new NumberAxis());
        averageEnergy2= new LineChart(new NumberAxis(), new NumberAxis());
        averageLifespan2= new LineChart(new NumberAxis(), new NumberAxis());
        averageChildrenForLivingCount2= new LineChart(new NumberAxis(), new NumberAxis());
        dominantGenotype2= new Label();


        VBox maplabels1 = new VBox(new Label("map1"),gridPane1,pause1, new HBox(signalMap1, inspectorMap1) , new HBox(animalNumberChart1, averageEnergy1,grassNumberChart1), new HBox(averageLifespan1,averageChildrenForLivingCount1,dominantGenotype1));
        VBox maplabels2 = new VBox(new Label("map2"),gridPane2,pause2, new HBox(signalMap2, inspectorMap2) ,  new HBox(animalNumberChart2, averageEnergy2,grassNumberChart2), new HBox(averageLifespan2,averageChildrenForLivingCount2, dominantGenotype2));
        HBox maps = new HBox(maplabels1,maplabels2);

        scene = new Scene(maps, 400 ,800);
        _primaryStage.setScene(scene);
        _primaryStage.show();



        startThread(startingAnimals,startingEnergy,mapHeight,mapWidth,moveEnergy,plantEnergy,jungleRatio,magical1,true);
        startThread(startingAnimals,startingEnergy,mapHeight,mapWidth,moveEnergy,plantEnergy,jungleRatio,magical2,false);

    }
    public void init()
    {

    }

    Vector2d convertToMapPosition(Vector2d from){
        return new Vector2d(from.x - borderSW.x + 1,  borderNE.y-from.y + 1);
    }
    Vector2d convertFromMapPosition(Vector2d from){
        return new Vector2d(from.x + borderSW.x - 1,  borderNE.y-from.y + 1);
    }

    private void setHalignment(GridPane g)
    {
        g.getColumnConstraints().add(new ColumnConstraints(width));
        g.getRowConstraints().add(new RowConstraints(height));
            for (int i = borderSW.x; i<= borderNE.x; i++)
            {
                g.getColumnConstraints().add(new ColumnConstraints(width));
            }
            for (int i = borderSW.y; i<= borderNE.y; i++)
            {
                g.getRowConstraints().add(new RowConstraints(height));
            }
    }

    void drawMap(GridPane gridPane, GrassField map, boolean isMap1)
    {

        gridPane.setGridLinesVisible(true);
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(true);
        gridPane.add(new Label("y\\x"),0,0,1,1);


        for (int i = borderSW.x; i<= borderNE.x; i++)
        {
            Label _tmplabel = new Label(Integer.toString(i));
            GridPane.setHalignment(_tmplabel, HPos.CENTER);
            gridPane.add(_tmplabel,i- borderSW.x+1,0,1,1);

            //gridPane.getColumnConstraints().add(new ColumnConstraints(width));
        }
        for (int i = borderSW.y; i<= borderNE.y; i++)
        {
            Label _tmplabel = new Label(Integer.toString(borderNE.y-borderSW.y-i));
            GridPane.setHalignment(_tmplabel, HPos.CENTER);
            gridPane.add(_tmplabel,0,i- borderSW.y+1,1,1);

            //gridPane.getRowConstraints().add(new RowConstraints(height));
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

                        box.addEventFilter(MouseEvent.MOUSE_CLICKED, (EventHandler<Event>) event -> {
                                Vector2d convertedPos = convertFromMapPosition(new Vector2d(GridPane.getColumnIndex(box),GridPane.getRowIndex(box)));

                                if(isMap1)
                                    inspectAnimal(isMap1, convertedPos);
                                else
                                    inspectAnimal(isMap1,convertedPos);
                        });

                        gridPane.add(box, mapPosition.x, mapPosition.y, 1, 1);
                    }catch(FileNotFoundException e){
                        //System.out.println(e.getMessage()+ "File from object:"+el.toString()+" not found. Looked for "+el.getFilename());
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

    public void drawCall(boolean isMap1) {

        Platform.runLater(()->{
            if(isMap1) {
                drawMap(gridPane1, map1,isMap1);
            }else {
                drawMap(gridPane2, map2,isMap1);

            }});

    }

    void startThread(int startingAnimals, int startingEnergy, int mapHeight, int mapWidth, int moveEnergy, int plantEnergy, double jungleRatio, boolean magical, boolean isMap1) throws IllegalArgumentException{

        System.out.println("system wystartował");

        if(isMap1)
        {
            //if(engineThread1 != null)
                //engineThread1.interrupt();

            map1 = new GrassField(mapWidth,mapHeight ,jungleRatio);
            simulationEngine1 = new SimulationEngine( map1, plantEnergy,startingAnimals, startingEnergy, moveEnergy, true, magical);
            simulationEngine1.addListener(this);
            engineThread1 = new Thread(simulationEngine1);
            recalculateBorders(map1);
            setHalignment(gridPane1);
            drawMap(gridPane1,map1,isMap1);
            engineThread1.start();
        }
        else
        {
            //if(engineThread2 != null)
                //engineThread2.interrupt();

            map2 = new GrassFieldWithoutBorders(mapWidth,mapHeight ,jungleRatio);
            simulationEngine2 = new SimulationEngine( map2, plantEnergy,startingAnimals, startingEnergy, moveEnergy, false, magical);
            simulationEngine2.addListener(this);
            engineThread2 = new Thread(simulationEngine2);
            recalculateBorders(map2);
            setHalignment(gridPane2);
            drawMap(gridPane2,map2,isMap1);
            engineThread2.start();
        }


    }

    public void sendMessage(boolean isMap1, String s) {
        Platform.runLater(()->{
            if(isMap1)
                signalMap1.setText(signalMap1.getText()+s);
            else
                signalMap2.setText(signalMap2.getText()+s);
        });
    }
    public void sendInspection(boolean isMap1, String s) {
        Platform.runLater(()->{
            if(isMap1)
                inspectorMap1.setText(s);
            else
                inspectorMap2.setText(s);
        });
    }

    private void inspectAnimal(boolean isMap1, Vector2d position){
            if(isMap1)
                simulationEngine1.requestInspecting(position);
            else
                simulationEngine2.requestInspecting(position);
    }

}
