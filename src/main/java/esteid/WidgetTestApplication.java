package esteid;

import com.vaadin.Application;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class WidgetTestApplication extends Application
{
    private Window window;

    @Override
    public void init()
    {
        window = new Window("Widget Test");
        setMainWindow(window);
        IdCardComponent c = new IdCardComponent();
        c.init();
        window.addComponent(c);
    }
    
}
