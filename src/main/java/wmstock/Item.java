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

    @Override
    public boolean equals(Object o) {

        return (((Item)o).getName() == name && ((Item)o).getQuantity() == quantity);
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
