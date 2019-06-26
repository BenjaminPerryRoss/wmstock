package wmstock;

import java.io.Serializable;

import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxWriter;

public class ItemV2 extends Item {

    public int integer;

    public ItemV2() {

    }

    public ItemV2(String n, int q, int integer)
    {
        setName(n);
        setQuantity(q);
        this.integer = integer;
    }

    @Override
    public void toData(PdxWriter writer) {
        writer.writeString("name",this.getName());
        writer.writeInt("quantity",this.getQuantity());
        writer.writeInt("integer", integer);
    }

    @Override
    public void fromData(PdxReader reader) {
        setName(reader.readString("name"));
        setQuantity(reader.readInt("quantity"));
        integer = reader.readInt("integer");
    }

}
