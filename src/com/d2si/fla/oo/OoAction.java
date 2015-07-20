package com.d2si.fla.oo;


import com.iconclude.webservices.extensions.java.interfaces.IAction;
import com.iconclude.webservices.extensions.java.interfaces.IActionRegistry;
import com.iconclude.webservices.extensions.java.interfaces.ISessionContext;
import com.iconclude.webservices.extensions.java.types.ActionRequest;
import com.iconclude.webservices.extensions.java.types.ActionResult;
import com.iconclude.webservices.extensions.java.types.ActionTemplate;
import com.iconclude.webservices.extensions.java.types.RASBinding;
import com.iconclude.webservices.extensions.java.util.RASBindingFactory;
import com.opsware.pas.content.commons.util.StringUtils;

/**
 *  IAction - used to extract the username password from a custom properties 
 *
 *
 */

public class OoAction implements IAction 
{
    // Input names (parameters) - required
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	
	// output names
	public static final String RETURNED_PWD = "returned_pwd";

	
	// constants
    public static String SUCCESS = "success";
    public static String FAILURE = "failure";

    // Return codes
    public static int PASSED =  0; // IAction succeeded
    public static int FAILED = -1; // IAction error'd out
    
    // Input binding (RASBinding) values
    public static final boolean ENCRYPTED      = true;
    public static final boolean NOT_ENCRYPTED  = false;
    public static final boolean REQUIRED       = true;
    public static final boolean NOT_REQUIRED   = false;

	
	 /**
     * Called when the OO run engine executes an Operation
     * based on this IAction.
     *
     * @param ctx containing Flow scoped session object cache
     * @param request input parameters
     * @param reg can be used to call other IActions
     *
     * @return action result with response/returnCode/results set
     */
    public ActionResult execute(ISessionContext ctx,
                                ActionRequest request,
                                IActionRegistry reg) throws Exception {

		// Get inputs using ContentCommons API (handles system accounts)
    	String username  = StringUtils.resolveString(request, USERNAME);
    	String password  = StringUtils.resolveString(request, PASSWORD);

    	//
        // Parse results into an Action Result
        //
        ActionResult result = new ActionResult();

    	result.add(RETURNED_PWD, password);
    	
    	result.setReturnCode(PASSED);
    	result.add("returnResult", "Search completed successfully for " + username + ":" + password);
    	
	return result;
		
	}

    /**
     * Called by Studio.
     *
     * This may get called when a new Operation is to be created by Studio from this IAction.
     * This will get called periodically by Studio so it can examine the result fields.
     *
     * @return IAction signature (template)
     */
    @Override
	public ActionTemplate getActionTemplate() {
        // ActionTemplate to return
        ActionTemplate actionTemplate = new ActionTemplate();

        // Inputs:  [Name / RASBinding] pairs
        com.iconclude.webservices.extensions.java.types.Map inputs
            = new com.iconclude.webservices.extensions.java.types.Map();

        // Bindings are setting for an input
        RASBinding binding;

        // Username
        binding = RASBindingFactory.createEmptyRASBinding(REQUIRED, NOT_ENCRYPTED);
        inputs.add(USERNAME, binding);

        // Username
        binding = RASBindingFactory.createEmptyRASBinding(REQUIRED, ENCRYPTED);
        inputs.add(PASSWORD, binding);
   
        // Responses: [Name / ReturnCode] pairs
        com.iconclude.webservices.extensions.java.types.Map responses
            = new com.iconclude.webservices.extensions.java.types.Map();
        responses.add(SUCCESS, PASSED);
        
        // Results: [Name / null] pairs
        com.iconclude.webservices.extensions.java.types.Map resultFields
            = new com.iconclude.webservices.extensions.java.types.Map();
        resultFields.add("returnResult", "");
        resultFields.add(RETURNED_PWD,    "");


        // Description field
        actionTemplate.setDescription("Extract Passord from system account");

        // Add collections to the IAction signature
        actionTemplate.setParameters(inputs);
        actionTemplate.setResponses(responses);
        actionTemplate.setResultFields(resultFields);

        return actionTemplate;
	}
}


      