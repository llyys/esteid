package esteid;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
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


        final Button btn = new Button("Alusta allkirjastamist");
        final Button btnSign = new Button("Alkirjasta dokument");
        btnSign.setVisible(false);
        VerticalLayout layout=new VerticalLayout();


        final IdCardComponent idCardComponent = new IdCardComponent();
        idCardComponent.addListener(new IdCardComponent.IdCardComponentListener() {
            public void onError(String code, String message) {

            }

            public void onEvent(IdCardComponent.IdCardEventType event, IdCardComponent component) {
                switch (event){
                    case CARD_INSERTED:
                        break;
                    case CARD_REMOVED:
                        break;
                    case ON_ERROR:
                        break;
                    case CERT_LOADED:
                        if(component.getCertHex()!=null){
                            btnSign.setVisible(true);
                        }
                        break;
                    case PLUGIN_READY:
                        btn.setEnabled(true);
                        break;
                    case SIGN_SUCCESS:
                        window.showNotification("success");
                        break;
                }
            }
        });

        idCardComponent.init();

        setMainWindow(window);

        layout.addComponent(idCardComponent);
        layout.addComponent(btn);
        layout.addComponent(btnSign);
        btn.setEnabled(false);

        btn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                idCardComponent.loadCert();

            }
        });
        window.addComponent(layout);
    }
    
}
