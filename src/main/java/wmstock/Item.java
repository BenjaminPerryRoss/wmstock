package wmstock;

import java.io.Serializable;

public class Item implements Serializable {
    private String name;
    private int quantity;

    public Item(String n, int q)
    {
        name = n;
        quantity = q;
    }

    public String getName()
    {
        return name;
    }

    public int getQuantity()
    {
        return quantity;
    }
}
