import command.AddItemCommand;
import command.Command;
import facade.StoreFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AddItemCommandTest {

    @Mock
    private StoreFacade mockStoreFacade;
    private Command addItemCommand;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        addItemCommand = new AddItemCommand(mockStoreFacade);
    }

    @Test
    public void should_AddItemToStock_when_ExecuteCalled() {
        addItemCommand.execute();
        verify(mockStoreFacade, times(1)).addItemToStock();
    }
}