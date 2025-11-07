package fr.paris.lutece.portal.util.mvc.utils;

import java.io.Serializable;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.util.mvc.binding.ParamError;
import fr.paris.lutece.portal.web.cdi.mvc.RedirectScoped;
import jakarta.inject.Named;

/**
 * Represents alert messages with different types (errors, warnings, infos) 
 * and supports redirect handling.
 */
@RedirectScoped
@Named("flashMessage")
public class AlertMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Types of messages */
    public enum MessageType {
        ERROR, WARNING, INFO, SUCCESS, DANGER
    }

    /** Map storing messages by type */
    private final EnumMap<MessageType, Set<String>> messages = new EnumMap<>(MessageType.class);
    private final Set<ParamError> paramErrors= new LinkedHashSet<>( );
    
    /** Flag indicating if this AlertMessage is managed by RedirectAlertMessageHandler */
    private boolean redirectAlertMessageHandler;

    // ---------- Constructor ----------

    public AlertMessage() {
        // Initialize sets for each message type
        for (MessageType messageType : MessageType.values()) {
            messages.put(messageType, new LinkedHashSet<>());
        }
    }

    // ---------- Generic message operations ----------

    /**
     * Adds a simple message of the given type.
     *
     * @param messageType    the message type
     * @param message the message text
     * @return this AlertMessage instance for chaining
     * @throws NullPointerException if {@code messageType} or {@code message} is {@code null}
     */
    public AlertMessage addMessage(MessageType messageType, String message) {
        Objects.requireNonNull(message, "message must not be null");
        getMessageSet(messageType).add(message);
        return this;
    }

    /**
     * Adds a localized message of the given type.
     *
     * @param messageType       the message type
     * @param messageKey the key for localization
     * @param locale     the locale to use
     * @return this AlertMessage instance for chaining
     * @throws NullPointerException if {@code messageType} or {@code messageKey} is {@code null}
     */
    public AlertMessage addMessage(MessageType messageType, String messageKey, Locale locale) {
    	 Objects.requireNonNull(messageKey, "messageKey must not be null");
         getMessageSet(messageType).add(I18nService.getLocalizedString(messageKey, locale));

        return this;
    }

    /**
     * Adds multiple messages of the given type.
     *
     * @param messageType       the message type
     * @param newMessages set of messages to add
     * @return this AlertMessage instance for chaining
     * @throws NullPointerException if {@code messageType} or {@code listMessages} is {@code null}
     */
    public AlertMessage addMessages(MessageType messageType, Set<String> listMessages) {
    	  Objects.requireNonNull(listMessages, "listMessages must not be null");
    	  getMessageSet(messageType).addAll(listMessages);
        
        return this;
    }

    /**
     * Returns an unmodifiable view of messages of the given type.
     *
     * @param messageType the message type
     * @return unmodifiable set of messages
     */
    public Set<String> getMessages(MessageType messageType) {
        return Collections.unmodifiableSet(getMessageSet(messageType));
    }

    /**
     * Checks whether any messages exist for the given type.
     *
     * @param messageType the message type
     * @return true if there are messages, false otherwise
     */
    public boolean hasMessages(MessageType messageType) {
        return !getMessageSet(messageType).isEmpty();
    }
    /**
     * Checks whether any paramErrors exist for the given type.
     *
     * @return true if there are paramError, false otherwise
     */
    public boolean hasParamErros( ) {
        return !paramErrors.isEmpty();
    }
    /**
     * Returns an unmodifiable view of all ParamError object.
     *
     * @return an unmodifiable {@code Set} of ParamError object
     */
    public Set<ParamError> getParamErrors() {
        return Collections.unmodifiableSet(paramErrors);
    }
    /**
     * Adds a Set of ParamError object.
     *
     * @param paramErrors the Set of ParamError object
     * @return this {@code AlertMessage} instance for chaining
     */
    public AlertMessage addParamErrors(Set<ParamError> paramErrors) {
        this.paramErrors.addAll( paramErrors);
        return this;
    }
    /**
     * Adds a ParamError object.
     *
     * @param paramError the ParamError object
     * @return this {@code AlertMessage} instance for chaining
     */
    public AlertMessage addParamError(ParamError paramError) {
        this.paramErrors.add( paramError);
        return this;
    }
    
    // ---------- Redirect handler flag ----------

    public boolean isRedirectAlertMessageHandler() {
        return redirectAlertMessageHandler;
    }

    public void setRedirectAlertMessageHandler(boolean redirectAlertMessageHandler) {
        this.redirectAlertMessageHandler = redirectAlertMessageHandler;
    }
    /**
     * Internal helpers 
     * @param messageType
     * @return Set of message
     */
    private Set<String> getMessageSet(MessageType messageType) {
        Objects.requireNonNull(messageType, "messageType must not be null");
        return messages.get(messageType);
    }
    
    @Override
    public String toString() {
        return "AlertMessage{" +
                "messages=" + messages +
                ", paramErrors=" + paramErrors +
                ", redirectHandler=" + redirectAlertMessageHandler +
                '}';
    }
}
