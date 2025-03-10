<#-- Macro: cStepContent

Description: Defines a macro that show a content of a step

Parameters:
@param - title - string - optional - required - the title of the step
@param - help - string - optional - 
@param - class - string - optional - the CSS class of the element, default '' 
@param - id - string - optional - the ID of the element, default ''
@param - iterable - boolean - optional - Add iteration number, default false 
@param - iteration - number - optional - Number of iteration default 0
@param - labelAddIteration - string - optional - Label to add an iteration, default '#i18n{theme.labelAdd}'
@param - labelDelIteration - string - optional - Label to remove an iteration, default '#i18n{theme.labelDelete}'
@param - params - string - optional - additional HTML attributes to include in the parent block element default ''
-->
<#macro cStepContent title id='' class='' help='' iterable=false iteration=0 labelAddIteration='#i18n{theme.labelAdd}' labelDelIteration='#i18n{theme.labelDelete}' params=''></#macro>