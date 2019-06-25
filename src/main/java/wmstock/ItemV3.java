package wmstock;

import java.io.Serializable;

public class ItemV3 extends Item {

    public String string;

    public ItemV3(String n, int q, String string)
    {
        setName(n);
        setQuantity(q);
        this.string = string;
    }
}
