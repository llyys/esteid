package esteid;

import com.vaadin.Application;
import com.vaadin.ui.*;

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


        FormLayout layout=new FormLayout ();

        final Button btn = new Button("Alusta allkirjastamist");
        layout.addComponent(btn);
        btn.setEnabled(false);

        final Label cert=new Label("Cert");
        layout.addComponent(cert);
        cert.setVisible(false);

        final Label certId=new Label("Cert ID");
        layout.addComponent(certId);
        certId.setVisible(false);

        final TextField textField=new TextField("File hash");
        layout.addComponent(textField);
        textField.setVisible(false);

        final Label signHashLabel=new Label();
        layout.addComponent(signHashLabel);
        signHashLabel.setVisible(false);

        final esteid.IdCardComponent idCardComponent = new IdCardComponent();
        layout.addComponent(idCardComponent);

        final Button btnSign = new Button("Alkirjasta dokument");
        layout.addComponent(btnSign);
        btnSign.setVisible(false);

        idCardComponent.addListener(new IdCardComponent.IdCardComponentListener() {
            public void onError(String code, String message) {

            }

            public void onEvent(IdCardComponent.IdCardEventType event, IdCardComponent component) {
                switch (event){
                    case CARD_INSERTED:
                        break;
                    case CARD_REMOVED:
                        btn.setEnabled(false);
                        break;
                    case ON_ERROR:
                        btn.setEnabled(false);
                        break;
                    case CERT_LOADED:
                        if(component.getCertHex()!=null){
                            cert.setValue(component.getCertHex());
                            cert.setVisible(true);

                            certId.setValue(component.getCertId());
                            certId.setVisible(true);

                            btnSign.setVisible(true);

                            textField.setVisible(true);
                            textField.setValue("");
                        }
                        break;
                    case PLUGIN_READY:
                        btn.setEnabled(true);
                        break;
                    case SIGN_SUCCESS:
                        window.showNotification("Sign Success");
                        break;
                }
            }
        });

        idCardComponent.init();

        setMainWindow(window);


        btn.setEnabled(false);

        btnSign.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                idCardComponent.startSign(idCardComponent.getCertId(), textField.getValue().toString());
            }
        });
        btn.addListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                idCardComponent.loadCert();

            }
        });
        window.addComponent(layout);
    }
    
}
