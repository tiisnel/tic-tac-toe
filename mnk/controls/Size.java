package mnk.controls;


import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;

public class Size extends VBox { //mnk game dimensions
    private final Spinner <Integer> dimension;
    private int min=1;
    private int max=25; //can be changed for extra wide screens
    private int def=4;// overflow disallowed
    public Size(Label label){
        super();
        setPrefWidth(200);

        dimension = new Spinner(min, max, def);
        getChildren().addAll(label, dimension);

    }
    public int value(){
        return dimension.getValue();
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setDef(int def)
    {
        this.def = def;
        this.dimension.getValueFactory().setValue(def);
    }
}
