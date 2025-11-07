package fr.paris.lutece.portal.util.mvc.utils;

import java.util.LinkedHashSet;
import java.util.Set;

import fr.paris.lutece.portal.util.mvc.binding.ParamError;
import fr.paris.lutece.portal.web.cdi.mvc.Models;
import fr.paris.lutece.portal.web.cdi.mvc.RedirectScopeManager;
import fr.paris.lutece.portal.web.cdi.mvc.event.BeforeControllerEvent;
import fr.paris.lutece.portal.web.cdi.mvc.event.ControllerRedirectEvent;
import fr.paris.lutece.portal.web.cdi.mvc.event.MvcEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

/**
* Handles alert messages during redirect events.
* <p>
* This bean observes controller redirect events and transfers messages
* between the Models and the AlertMessage instance. It supports error, 
* warning, and info messages using the generic MessageType enum.
* <p>
* It ensures that the AlertMessage bean is only used if the RedirectScoped
* context is active.
*/
@ApplicationScoped
public class RedirectAlertMessageHandler {

   @Inject
   private AlertMessage alertMessage;

   @Inject
   private Models models;

   @Inject
   private HttpServletRequest request;

   // ---------- Step 1: Just before redirect ----------

   /**
    * Observes a controller redirect event just before sending a redirect.
    * <p>
    * Transfers messages from the Models into the AlertMessage bean for later use.
    *
    * @param event the controller redirect event
    */
   public void onRedirect(@Observes ControllerRedirectEvent event) {
       processErrorsBeforeRedirect( MVCUtils.MARK_ERRORS );
       processMessagesBeforeRedirect( MVCUtils.MARK_INFOS, AlertMessage.MessageType.INFO );
       processMessagesBeforeRedirect( MVCUtils.MARK_WARNINGS, AlertMessage.MessageType.WARNING );
   }

   /**
    * Retrieves messages from the model for the given key and adds them
    * to the AlertMessage bean under the specified MessageType.
    *
    * @param modelKey the key in the model
    * @param type     the message type in AlertMessage
    */
   private void processMessagesBeforeRedirect(String modelKey, AlertMessage.MessageType type) {
       Set<String> messages = getSetFromModel(modelKey);
       if (messages != null && !messages.isEmpty()) {
    	   alertMessage.setRedirectAlertMessageHandler(true);
           alertMessage.addMessages(type, new LinkedHashSet<>(messages));
       }
   }
   /**
    * Retrieves errors from the model for the given key and adds them
    * to the AlertMessage bean under the specified MessageType.
    *
    * @param modelKey the key in the model
    * @param type     the message type in AlertMessage
    */
   private void processErrorsBeforeRedirect(String modelKey ) {
       Set<ParamError> errors = models.get(modelKey, Set.class);;
       if (errors != null && !errors.isEmpty()) {
    	   alertMessage.setRedirectAlertMessageHandler(true);
           alertMessage.addParamErrors(new LinkedHashSet<>(errors));
       }
   }

   /**
    * Safely retrieves a set of strings from the model.
    * <p>
    * Filters out any non-string elements.
    *
    * @param key the model key
    * @return a LinkedHashSet of strings, or null if none exist
    */
   @SuppressWarnings("unchecked")
   private Set<String> getSetFromModel(String key) {
     
	   return models.get(key, Set.class);
	   /*  Object obj = models.get(key, Set.class);
       if (obj instanceof Set<?> s) {
           return s.stream()
                   .filter(String.class::isInstance)
                   .map(String.class::cast)
                   .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);
       }
       return null;*/
   }

   // ---------- Step 2: Before the next controller after redirect ----------

   /**
    * Observes a BeforeControllerEvent before invoking the next controller
    * after a redirect.
    * <p>
    * Transfers messages from the AlertMessage bean back into the Models
    * for the next request.
    *
    * @param event the BeforeControllerEvent
    */
   public void onBeforeController(@Observes BeforeControllerEvent event) {
       if (!MvcEvent.ControllerInvocationType.ACTION.equals(event.getInvocationType())
               && request.getAttribute(RedirectScopeManager.SCOPE_ID) != null
               && alertMessage.isRedirectAlertMessageHandler( )) {

    	   processErrorsAfterRedirect( MVCUtils.MARK_ERRORS );
           processMessagesAfterRedirect( MVCUtils.MARK_INFOS, AlertMessage.MessageType.INFO );
           processMessagesAfterRedirect( MVCUtils.MARK_WARNINGS, AlertMessage.MessageType.WARNING );
       }
   }

   /**
    * Transfers messages from AlertMessage to the model for the given key and type.
    *
    * @param modelKey the key in the model
    * @param type     the message type
    */
   private void processMessagesAfterRedirect(String modelKey, AlertMessage.MessageType type) {
       if (alertMessage.hasMessages(type)) {
           Set<String> combinedMessages = new LinkedHashSet<>(alertMessage.getMessages(type));
           Set<String> modelMessages = getSetFromModel(modelKey);
           if (modelMessages != null) {
               combinedMessages.addAll(modelMessages);
           }
           models.put(modelKey, combinedMessages);
       }
   }
   /**
    * Transfers paramError from AlertMessage to the model for the given key and type.
    *
    * @param modelKey the key in the model
    */
   private void processErrorsAfterRedirect(String modelKey ) {
       if (alertMessage.hasParamErros( )) {
           Set<ParamError> combinedParamError = new LinkedHashSet<>(alertMessage.getParamErrors( ));
           Set<ParamError> modelParamError = models.get(modelKey, Set.class);;
           if (modelParamError != null) {
               combinedParamError.addAll(modelParamError);
           }
           models.put(modelKey, combinedParamError);
       }
   }
}