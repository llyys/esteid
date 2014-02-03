package esteid;

import com.google.gson.Gson;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.Window;

import java.util.Map;

/**
 * Server side component for the VIdCardComponent widget.
 */
@ClientWidget(esteid.gwt.client.VIdCardComponent.class)
public class IdCardComponent extends AbstractComponent {
    private String action=null;
    private String signCertId;
    private String certHex;
    private String hashHex;
    private String error;
    private static final long serialVersionUID = 1501433173868836522L;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        // TODO Paint any component specific content by setting attributes
        // These attributes can be read in updateFromUIDL in the widget.
        target.addAttribute(action, true);
        if(action=="sign" && this.hashHex!=null)
        {
            target.addAttribute("signCertId", this.signCertId);
            target.addAttribute("hashHex", this.hashHex);
        }
    }

    public IdCardComponent addListener(){
        return this;
    }


    public void init() {
        action="loadPlugin";
    }
    /*
     * The server-side component will receive state changes from the client-side widget
     */
    @Override
    public void changeVariables(Object source, Map variables) {
        IdCardEvent event = new IdCardEvent(this);
        if(variables.containsKey("onGetCert")){
            String[] result= (String[]) variables.get("onGetCert");
            this.certHex=result[0];
            this.signCertId=result[1];
            event.setEventType(IdCardEventType.CERT_LOADED);
        }
        if(variables.containsKey("onError")){
            error= (String) variables.get("onError");
            Gson gson = new Gson();
            IdCardException ex=gson.fromJson(error, IdCardException.class);
            event.setEventType(IdCardEventType.ON_ERROR);
            getWindow().showNotification("Id kaardi viga "+ex.code, ex.message, Window.Notification.TYPE_ERROR_MESSAGE);
        }
        if(variables.containsKey("onSigningCompleted")){
            event.setEventType(IdCardEventType.SIGN_SUCCESS);
        }
        if(variables.containsKey("onCardInserted")){
            event.setEventType(IdCardEventType.CARD_INSERTED);
        }
        if(variables.containsKey("onCardRemoved")){
            event.setEventType(IdCardEventType.CARD_REMOVED);
        }
        if(event.getEventType()!=null)
            fireEvent(event);
    }

    public void startSign(String signCertId, String hashHex)
    {
        this.signCertId = signCertId;
        this.hashHex = hashHex;
        action="sign";
        requestRepaint();
    }

    public String getCert()
    {
        action="getCert";
        requestRepaint();
        return "";
    }

    public interface IdCardListener{
        public void onEvent(IdCardEvent event);
    }

    public enum IdCardEventType { CARD_INSERTED, CARD_REMOVED, ON_ERROR, CERT_LOADED, SIGN_SUCCESS}


}
