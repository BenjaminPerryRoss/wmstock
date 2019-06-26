package wmstock;

import java.io.Serializable;

import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxWriter;

public class ItemV3 extends Item {

    public String string;

    public  ItemV3() {

    }

    public ItemV3(String n, int q, String string)
    {
        setName(n);
        setQuantity(q);
        this.string = string;
    }

    @Override
    public void toData(PdxWriter writer) {
        writer.writeString("name",this.getName());
        writer.writeInt("quantity",this.getQuantity());
        writer.writeString("string", string);
    }

    @Override
    public void fromData(PdxReader reader) {
        setName(reader.readString("name"));
        setQuantity(reader.readInt("quantity"));
        string = reader.readString("string");
    }

}
