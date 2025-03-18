function isValidEntry(nameInputValue, regex) 
{
	const reg = new RegExp(regex);
	return  reg.test(nameInputValue);
}

function isValidURL(url)
{
	try 
	{
	    new URL(url); 
	    return true;   
	} 
	catch (error) 
	{
	    return false;  
	}
}
 	
function setIcon(isSuccess, icon) 
{	
	if(isSuccess)
	{
	 	icon.classList.remove('ti', 'ti-circle-x','ti-info-circle');
	 	icon.classList.add('ti', 'ti-circle-check');
	}
	else
	{
		icon.classList.remove('ti','ti-circle-check','ti-info-circle');
	 	icon.classList.add('ti', 'ti-circle-x');			
	}
}

	
function setColorMessage(isSuccess, span) 
{
    setIcon(isSuccess,span.querySelector('i'));
    	
    if(isSuccess)
	{
 		span.classList.remove('text-muted','text-danger');
 		span.classList.add('text-success');
 	}
	else
	{
 		span.classList.remove('text-muted', "text-success");
 		span.classList.add('text-danger');
 	}
}

function setColorInput(isSuccess, input) 
{
    if (isSuccess) 
	{
        input.classList.remove('is-invalid');
        input.classList.add('is-valid');
    } else 
	{
        input.classList.remove('is-valid');
        input.classList.add('is-invalid');
    }
}

function cleanClassListInput(inputToClean) 
{
    inputToClean.classList.remove('is-invalid');
    inputToClean.classList.remove('is-valid');
}

/* Function to update help message div and input front according to the regex*/
function switchInteractiveValidationMessages(targetInput) 
{     
	
 	if(targetInput)
	{    	
 		//Get value of target input
 		const targetInputValue = targetInput.value?targetInput.value:'';
		const divMessageHelp = document.getElementById( targetInput.id + '-help-message-div');
		
		var bAllRuleRespected = false; //Check if all rules are respected
		var bCurrentRuleRespected = false; //Check if current rule is respected
		var nCounter = 0;
		
		//Loop through dictionnary to get the id of spans and their regex associated
		divMessageHelp.querySelectorAll('span').forEach(function(span)
		{	
			if(span)
			{	
				var strRule = span.getAttribute('rule')?span.getAttribute('rule'):'';
				
				if(strRule!=="")
				{
					bCurrentRuleRespected = strRule==="url"? isValidURL(targetInputValue):isValidEntry(targetInputValue,strRule);
					
					//Switch front of the span selected (change color and icon ; failure -> red and success -> green)
					setColorMessage(bCurrentRuleRespected,span);
					
					bAllRuleRespected = nCounter===0? bCurrentRuleRespected: bAllRuleRespected && bCurrentRuleRespected;
					nCounter=1;
				}
			}
		});
		
		setColorInput(bAllRuleRespected,targetInput);
	}
}
