import models.Bill;
import models.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import builder.BillBuilder;
import java.sql.Date;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BillBuilderTest {
    private BillBuilder billBuilder;

    @BeforeEach
    public void setUp() {
        billBuilder = new BillBuilder();
    }

    @Test
    public void should_BuildBillWithCorrectValues_when_BuildCalled() {
        Item item = new Item("I001", "ItemName");
        Date date = new Date(System.currentTimeMillis());
        Bill bill = billBuilder.setBillId(1)
                .addItem(item)
                .setDate(date)
                .build();
        assertEquals(1, bill.getBillId());
        assertEquals(Collections.singletonList(item), bill.getItems());
        assertEquals(date, bill.getDate());
    }
}