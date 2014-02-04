package esteid;

import com.vaadin.ui.Component;

/**
 * EstEid event. This event is thrown, when the event is handled.
 * @author Lauri Lüüs.
 */
public class IdCardEvent extends Component.Event {

    private static final long serialVersionUID = -567533739849771393L;
    private IdCardComponent.IdCardEventType eventType;
    private String message;

    /**
     * New instance of text change event.
     *
     * @param source
     *            the Source of the event.
     */
    public IdCardEvent(Component source) {
        super(source);
    }

    /**
     * Gets the EstEidComponent where the event occurred.
     * @return the Source of the event.
     */
    public IdCardComponent getComponent() {
        return (IdCardComponent) getSource();
    }

    public IdCardComponent.IdCardEventType getEventType() {
        return eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setEventType(IdCardComponent.IdCardEventType eventType) {
        this.eventType = eventType;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public boolean onError(String errorCode, String errorMessage) {

        setEventType(IdCardComponent.IdCardEventType.ON_ERROR);
        return false;
    }
}
