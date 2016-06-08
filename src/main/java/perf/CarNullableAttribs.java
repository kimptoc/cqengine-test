package perf;

import com.googlecode.cqengine.attribute.Attribute;
import com.googlecode.cqengine.attribute.SimpleNullableAttribute;
import com.googlecode.cqengine.query.option.QueryOptions;

import java.util.Map;

/**
 * Created by kimptoc on 25/05/2016.
 */
public class CarNullableAttribs {
    String colour;
    Double engineSize;
    String make;
    String model;

    CarNullableAttribs(Map m)
    {
        colour = (String) m.get("colour");
        make = (String) m.get("make");
        model = (String) m.get("model");
        engineSize = (Double) m.get("engineSize");
    }

    static Attribute COLOUR = new SimpleNullableAttribute<CarNullableAttribs, String>("colour") {
        public String getValue(CarNullableAttribs car, QueryOptions queryOptions) { return car.colour; }
    };
    static Attribute MAKE = new SimpleNullableAttribute<CarNullableAttribs, String>("make") {
        public String getValue(CarNullableAttribs car, QueryOptions queryOptions) { return car.make; }
    };
    static Attribute MODEL = new SimpleNullableAttribute<CarNullableAttribs, String>("model") {
        public String getValue(CarNullableAttribs car, QueryOptions queryOptions) { return car.model; }
    };
}
