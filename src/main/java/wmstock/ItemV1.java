package wmstock;

import java.io.Serializable;

import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxWriter;

public class ItemV1 extends Item  {

    public boolean isTrue;

    public ItemV1() {

    }

    public ItemV1(String n, int q, boolean isTrue)
    {
        setName(n);
        setQuantity(q);
        this.isTrue = isTrue;
    }

    @Override
    public void toData(PdxWriter writer) {
        writer.writeString("name",this.getName());
        writer.writeInt("quantity",this.getQuantity());
        writer.writeBoolean("isTrue", isTrue);
    }

    @Override
    public void fromData(PdxReader reader) {
        setName(reader.readString("name"));
        setQuantity(reader.readInt("quantity"));
        isTrue = reader.readBoolean("isTrue");
    }

}
