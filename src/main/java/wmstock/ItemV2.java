package wmstock;

import java.io.Serializable;

public class ItemV2 extends Item {

    public int integer;

    public ItemV2(String n, int q, int integer)
    {
        setName(n);
        setQuantity(q);
        this.integer = integer;
    }
}
