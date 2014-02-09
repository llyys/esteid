package esteid.gwt.client;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.user.client.Element;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;

public class VIdCardComponent extends Widget implements Paintable {

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-mycomponent";
    private final com.google.gwt.dom.client.Element element;

    /** The client side widget identifier */
    protected String paintableId;

    /** Reference to the server connection object. */
    ApplicationConnection client;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VIdCardComponent() {
        // TODO Example code is extending GWT Widget so it must set a root element.
        // Change to proper element or remove if extending another widget
        element = Document.get().createDivElement();
        element.setId("pluginLocation");
        element.setInnerHTML("Id kaardi plugina initsialiseerimine.");
        setElement(element);
        
        // This method call of the Paintable interface sets the component
        // style name in DOM tree
        setStyleName(CLASSNAME);
    }

    /**
     * Called whenever an update is received from the server 
     */
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first. 
        // It handles sizes, captions, tooltips, etc. automatically.
        if (client.updateComponent(this, uidl, true)) {
            // If client.updateComponent returns true there has been no changes and we
            // do not need to update anything.
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the client side identifier (paintable id) for the widget
        paintableId = uidl.getId();

        if(uidl.getBooleanAttribute("loadPlugin"))
        {
           loadPlugin(this);
        }
        if(uidl.getBooleanAttribute("loadCert"))
        {
            loadCert(this);
        }
        if(uidl.getBooleanAttribute("doSign"))
        {
            String signCertId=uidl.getStringAttribute("signCertId");
            String hashHex=uidl.getStringAttribute("hashHex");
            doSign(this, signCertId, hashHex);
        }
    }

    public void onCert(String certHex, String certId){

        client.updateVariable(paintableId, "onCert", new String[]{certHex, certId}, true);
    }

    public void onError(String msg) {
        client.updateVariable(paintableId, "onError", msg, true);
    }

    public void onCardInserted() {
        client.updateVariable(paintableId, "onCardInserted", "", true);
    }

    public void onPluginReady(String version) {
        client.updateVariable(paintableId, "onPluginReady", version, true);
    }

    public void onCardRemoved() {
        client.updateVariable(paintableId, "onCardRemoved", "", true);
    }

    public void onSigningCompleted(String msg) {
        client.updateVariable(paintableId, "onSigningCompleted", msg, true);
    }

    public native void loadPlugin (VIdCardComponent component) /*-{
		try
        {
            $wnd.pluginHandler=$wnd.loadSigningPlugin('est', {
                onError:function(message){
                    component.@esteid.gwt.client.VIdCardComponent::onError(Ljava/lang/String;) (message);
                },
                onCardInserted: function(cert) {
					component.@esteid.gwt.client.VIdCardComponent::onCardInserted() ();
				},
				onCardRemoved: function() {
					component.@esteid.gwt.client.VIdCardComponent::onCardRemoved() ();
				},
				onPluginReady: function(version){
				    component.@esteid.gwt.client.VIdCardComponent::onPluginReady(Ljava/lang/String;) (version);
				},
            });
        }
        catch (ex)
        {
            if (ex instanceof $wnd.IdCardException) {
                component.@esteid.gwt.client.VIdCardComponent::onError(Ljava/lang/String;) ('{code: ' + ex.returnCode + '; message: "' + ex.message + '"}');
            } else {
                component.@esteid.gwt.client.VIdCardComponent::onError(Ljava/lang/String;) ('{message: "' + ex.message != undefined ? ex.message : ex+ '"}');
            }
        }
	}-*/;

    public native void loadCert(VIdCardComponent component) /*-{
		try
        {
            debugger;
            var selectedCertificate = $wnd.pluginHandler.getCertificate();
            component.@esteid.gwt.client.VIdCardComponent::onCert(Ljava/lang/String;Ljava/lang/String;) (selectedCertificate.certHex, selectedCertificate.id);
        }
        catch (ex)
        {
           if (ex instanceof $wnd.IdCardException) {
                component.@esteid.gwt.client.VIdCardComponent::onError(Ljava/lang/String;) ('{code: ' + ex.returnCode + '; message: "' + ex.message + '"}');
            } else {
                component.@esteid.gwt.client.VIdCardComponent::onError(Ljava/lang/String;) ('{message: "' + ex.message != undefined ? ex.message : ex+ '"}');
            }
        }
	}-*/;

    public native void doSign(VIdCardComponent component, String signCertId, String hashHex)/*-{
		try
        {
            $wnd.pluginHandler=$wnd.loadSigningPlugin('est', {
                onError:function(message){
                    component.@esteid.gwt.client.VIdCardComponent::onError(Ljava/lang/String;) (message);
                },
                onCardInserted: function(cert) {
					component.@esteid.gwt.client.VIdCardComponent::onCardInserted() ();
				},
				onCardRemoved: function() {
					component.@esteid.gwt.client.VIdCardComponent::onCardRemoved() ();
				},
				onPluginReady: function(version){
				    component.@esteid.gwt.client.VIdCardComponent::onPluginReady(Ljava/lang/String;) (version);
				},
            });
            var signValueHex = $wnd.pluginHandler.sign(signCertId, hashHex);
            component.@esteid.gwt.client.VIdCardComponent::onSigningCompleted(Ljava/lang/String;) (signValueHex);
        }
        catch (ex)
        {
           if (ex instanceof $wnd.IdCardException) {
                component.@esteid.gwt.client.VIdCardComponent::onError(Ljava/lang/String;) ('{code: ' + ex.returnCode + '; message: "' + ex.message + '"}');
            } else {
                component.@esteid.gwt.client.VIdCardComponent::onError(Ljava/lang/String;) ('{message: "' + ex.message != undefined ? ex.message : ex+ '"}');
            }
        }
	}-*/;

}
