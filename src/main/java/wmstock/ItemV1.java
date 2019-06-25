package wmstock;

import java.io.Serializable;

public class ItemV1 extends Item  {

    public boolean isTrue;

    public ItemV1(String n, int q, boolean isTrue)
    {
        setName(n);
        setQuantity(q);
        this.isTrue = isTrue;
    }
}
