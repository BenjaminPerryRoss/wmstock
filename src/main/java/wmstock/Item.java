package wmstock;

import java.io.Serializable;

import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;

public class Item implements PdxSerializable {
    private String name;
    private int quantity;

    public Item() {

    }

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

    public void setName(String name){
        this.name = name;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    @Override
    public void toData(PdxWriter writer) {

    }

    @Override
    public void fromData(PdxReader reader) {

    }
}

