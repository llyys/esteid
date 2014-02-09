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
    private String signatureHex;
    private String error;
    private static final long serialVersionUID = 1501433173868836522L;
    private IdCardComponentListener eventListener;


    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        // TODO Paint any component specific content by setting attributes
        // These attributes can be read in updateFromUIDL in the widget.
        target.addAttribute(action, true);
        if(action=="doSign" && this.hashHex!=null)
        {
            target.addAttribute("signCertId", this.signCertId);
            target.addAttribute("hashHex", this.hashHex);
        }
    }


    public IdCardComponent addListener(IdCardComponentListener eventListener){
        this.eventListener = eventListener;
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
        try{

            IdCardEvent event = new IdCardEvent(this);

            if(variables.containsKey("onCert")){
                String[] result= (String[]) variables.get("onCert");
                this.certHex=result[0];
                this.signCertId=result[1];
                event.setEventType(IdCardEventType.CERT_LOADED);
            }
            if(variables.containsKey("onError")){
                error= (String) variables.get("onError");
                if(error!=null && error.contains(":") && error.contains("code:")){

                    Gson gson = new Gson();
                    IdCardException ex=gson.fromJson(error, IdCardException.class);
                    event.setEventType(IdCardEventType.ON_ERROR);

                    if(!event.onError(ex.code, ex.message))
                        getWindow().showNotification("Id kaardi viga " + ex.code, ex.message, Window.Notification.TYPE_ERROR_MESSAGE);

                }
                else {
                    event.setEventType(IdCardEventType.ON_ERROR);
                    if(!event.onError("", error))
                        getWindow().showNotification("Id kaardi viga ", error, Window.Notification.TYPE_ERROR_MESSAGE);
                }

            }
            if(variables.containsKey("onPluginReady")){
                event.setEventType(IdCardEventType.PLUGIN_READY);
            }
            if(variables.containsKey("onSigningCompleted")){
                event.setEventType(IdCardEventType.SIGN_SUCCESS);
                signatureHex= (String) variables.get("onSigningCompleted");
            }
            if(variables.containsKey("onCardInserted")){
                event.setEventType(IdCardEventType.CARD_INSERTED);
            }
            if(variables.containsKey("onCardRemoved")){
                event.setEventType(IdCardEventType.CARD_REMOVED);
            }
            if(eventListener!=null)
            {
                eventListener.onEvent(event.getEventType(), this);
            }

            if(event.getEventType()!=null)
                fireEvent(event);

        } catch (Exception e){
            if(eventListener!=null)
                eventListener.onError("General error", e.getMessage());
        }
    }

    public void startSign(String signCertId, String hashHex)
    {
        this.signCertId = signCertId;
        this.hashHex = hashHex;
        action="doSign";
        requestRepaint();
    }

    public String getCertHex()
    {
        return this.certHex;
    }

    public void loadCert() {
        action="loadCert";
        requestRepaint();
    }

    public String getCertId() {
        return signCertId;
    }

    public String getSignatureHex() {
        return signatureHex;
    }

    public interface IdCardComponentListener{
        void onError(String code, String message);
        void onEvent(IdCardEventType event, IdCardComponent component);

    }

    public enum IdCardEventType { CARD_INSERTED, CARD_REMOVED, ON_ERROR, CERT_LOADED, PLUGIN_READY, SIGN_SUCCESS}


}
