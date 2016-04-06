package com.cookiescode.trendsmap;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MapController implements Initializable, MapComponentInitializedListener {

    @FXML private GoogleMapView mapView;
    @FXML private Label lblCenter;
    @FXML private Label lblZoom;
    @FXML private TextField txtSearch;
    @FXML private ListView<TagInfo> listHashs;
    @FXML private Label lblTweetsCount;
    @FXML private VBox panelLoading;
    @FXML private HBox panelMap;
    @FXML private ListView<TagInfo> listSelectedTags;
    @FXML private ColorPicker cpTagColors;
    @FXML private Label lblTweets;
    @FXML private Label lblPosition;


    private GoogleMap map;
    private DirectionsPane directions;
    private Graph<HashTag> mapGraph;

    private List<Marker> mapMarkers;
    private List<MapShape> mapShapes;
    private ObservableList<TagInfo> obSearchList;
    private HashMap<TagInfo, Graph<Location>> tagGraphs;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        // add listener to data loader.
        Main.dataLoaderFinished.addListener(this, this::dataLoaded);
        Main.dataLoaderStarted.addListener(this, this::loaderStarted);

        panelMap.setVisible(false);
        panelLoading.setVisible(true);


        // add this instance as listener to notify when map initialized.
        mapView.addMapInializedListener(this);

        mapMarkers = new ArrayList<>();
        mapShapes = new ArrayList<>();
        tagGraphs = new HashMap<>();

        listSelectedTags.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @Override
    public void mapInitialized() {
        //Once the map has been loaded by the Webview, initialize the map details.
        LatLong center = new LatLong(40.165185, 364.45708);
        MapOptions options = new MapOptions();
        options.center(center)
                .mapMarker(true)
                .zoom(2)
                .overviewMapControl(true)
                .panControl(true)
                .rotateControl(true)
                .scaleControl(true)
                .streetViewControl(true)
                .zoomControl(true)
                .mapType(MapTypeIdEnum.ROADMAP);

        map = mapView.createMap(options);
        directions = mapView.getDirec();
        map.setHeading(123.2);


        map.fitBounds(new LatLongBounds(new LatLong(30, 120), center));

        // Debug
        lblCenter.setText(map.getCenter().toString());
        map.centerProperty().addListener((ObservableValue<? extends LatLong> obs, LatLong o, LatLong n) -> {
            lblCenter.setText(n.toString());
        });

        lblZoom.setText(Integer.toString(map.getZoom()));
        map.zoomProperty().addListener((ObservableValue<? extends Number> obs, Number o, Number n) -> {
            lblZoom.setText(n.toString());
        });

        map.addUIEventHandler(UIEventType.click, (JSObject obj) -> {
            LatLong ll = new LatLong((JSObject) obj.getMember("latLng"));
            lblPosition.setText(ll.toString());
        });
    }

    public void btnViewOnAction(){
        if(listSelectedTags.getSelectionModel().isEmpty())
            return;

        // clean the map.
        // remove any marker on the map.
        mapMarkers.forEach(marker -> map.removeMarker(marker));
        mapMarkers.clear();
        // remove any shape on the map
        mapShapes.forEach(shape -> map.removeMapShape(shape));
        mapShapes.clear();

        List<TagInfo> selectedTag = listSelectedTags.getSelectionModel().getSelectedItems();
        selectedTag.forEach(tag -> {
            tagGraphs.get(tag).getVertices().forEach(vertex -> {
                // Create Location Point.
                LatLong latLong = new LatLong(
                        vertex.getLatitude(), vertex.getLongitude());
                // Create Marker Options.
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLong)
                        .title(tag.getName())
                        .animation(Animation.BOUNCE)
                        .visible(true);
                final Marker marker = new Marker(markerOptions);
                map.addMarker(marker);
                mapMarkers.add(marker);
            });

            tagGraphs.get(tag).exportGraph().forEach((vertex, edges) ->
                    edges.forEach(edge -> {
                        LatLong loc1 = new LatLong(vertex.getLatitude(), vertex.getLongitude());
                        LatLong loc2 = new LatLong(edge.getLatitude(), edge.getLongitude());
                        MVCArray mvc = new MVCArray(new LatLong[]{loc1, loc2});
                        PolylineOptions polyOpts = new PolylineOptions();
                        polyOpts.path(mvc).strokeColor(tag.getColor()).strokeWeight(1);
                        Polyline polyline = new Polyline(polyOpts);
                        map.addMapShape(polyline);
                        mapShapes.add(polyline);
                    })
            );
        });
    }

    private void dataLoaded(DataSet dataSet){
        // show Map panel.
        panelMap.setVisible(true);
        panelLoading.setVisible(false);

        // show hash tags. --------->
        // Initialize List View to show Hash Tag and count.
        listHashs.setCellFactory(new Callback<ListView<TagInfo>, ListCell<TagInfo>>() {
            @Override
            public ListCell<TagInfo> call(ListView<TagInfo> param) {
                ListCell<TagInfo> cell = new ListCell<TagInfo>(){
                    @Override
                    protected void updateItem(TagInfo item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null){
                            setText(item.getAmount() + "\t" + item.getName());
                        }
                    }
                };
                return cell;
            }
        });

        // Initialize List View to show selected Hash Tags.
        listSelectedTags.setCellFactory(new Callback<ListView<TagInfo>, ListCell<TagInfo>>() {
            @Override
            public ListCell<TagInfo> call(ListView<TagInfo> param) {
                ListCell<TagInfo> cell = new ListCell<TagInfo>(){
                    @Override
                    protected void updateItem(TagInfo item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null){
                            setText(item.getName());
                            setTextFill(Paint.valueOf(item.getColor()));
                        }
                        else {
                            setText(null);
                            setTextFill(null);
                        }
                    }
                };
                return cell;
            }
        });

        // 1. add all hash tags into observable list of TagInfo.
        ObservableList<TagInfo> obHashTags = FXCollections.observableArrayList();
        obHashTags.addAll(dataSet.getTweets().keySet().stream().map(
                hash -> new TagInfo(hash, dataSet.getTweets().get(hash).size())
        ).collect(Collectors.toList()));

        // 2. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<TagInfo> filteredList = new FilteredList<>(obHashTags, p -> true);

        // 3. Set the filter Predicate whenever the filter changes.
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(hash -> {
                // If filter text is empty, display all Data.
                if(newValue == null || newValue.isEmpty())
                    return true;

                // Compare Item Name with filter text.
                String strFilter = newValue;

                if(hash.getName().contains(strFilter))
                    return true;  // Filter matches this item.

                return false;     // Does not match.
            });
        });

        // 4. Wrap the FilteredList in a SortedList.
        SortedList<TagInfo> sortedList = new SortedList<>(filteredList);

        // 5. Add sorted (and filtered) data to the list.
        listHashs.setItems(sortedList.sorted());

        obSearchList = obHashTags;
        Platform.runLater(() -> lblTweets.setText(lblTweetsCount.getText()));
    }

    private void loaderStarted(SimpleIntegerProperty count){
        lblTweetsCount.textProperty().bind(count.asString());
    }

    @FXML
    public void cpTagColorsOnHidden(){
        if(listSelectedTags.getItems().size() > 0 && !listSelectedTags.getSelectionModel().isEmpty()){
            listSelectedTags.getSelectionModel().getSelectedItem().setColor(cpTagColors.getValue().toString());
            listSelectedTags.refresh();
        }
    }

    @FXML
    public void btnForwardOnAction(){
        if(!listHashs.getSelectionModel().isEmpty()){
            TagInfo selectedTag = listHashs.getSelectionModel().getSelectedItem();
            if(!listSelectedTags.getItems().contains(selectedTag)){
                if(!tagGraphs.containsKey(selectedTag)) {
                    // load tweets graph.
                    tagGraphs.put(selectedTag, GraphLoader.loadGraph(selectedTag));
                    // add this tag to selection list.
                    listSelectedTags.getItems().add(selectedTag);
                }
            }
        }
    }

    @FXML
    public void btnBackwardOnAction(){
        if(!listSelectedTags.getSelectionModel().isEmpty()){
            listSelectedTags.getItems().remove(listSelectedTags.getSelectionModel().getSelectedItem());
            listSelectedTags.refresh();
        }
    }
}
