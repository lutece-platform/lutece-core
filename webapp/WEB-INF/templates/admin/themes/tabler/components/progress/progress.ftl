<#-- Macro: progress

Description: Generates a progress bar with optional label, text, and report for progress updates.

Parameters:
- color (string, optional): the color of the progress bar, using a Bootstrap color class (e.g. "primary").
- id (string, optional): the ID of the progress bar container.
- params (string, optional): additional parameters to add to the progress bar container.
- value (number, optional): the value of the progress bar, as a percentage (0-100).
- min (number, optional): the minimum value of the progress bar (default: 0).
- max (number, optional): the maximum value of the progress bar (default: 100).
- text (string, optional): the text to display on the progress bar (default: percentage value).
- progressId (string, optional): the ID of the progress bar itself.
- token (string, optional): a unique identifier for the progress bar to enable progress updates.
- label (string, optional): a label to display above the progress bar.
- showReport (boolean, optional): whether to display a report for progress updates.
- intervalTime (number, optional): the interval time in milliseconds to check for progress updates (default: 2000).

-->
<#macro progress color='primary' id='' params='' value=0 min=0 max=100 text='' progressId='progressbar' token='' label='' showReport=false intervalTime=2000 deprecated...>
<@deprecatedWarning args=deprecated />
<#if label!='' >
<div id="${progressId}-label" >${label}</div>
</#if>
<div class="progress"<#if id!=''> id="${id}"</#if><#if params!=''> ${params}</#if>>
	<div id="${progressId}" class="progress-bar progress-bar-${color}<#if token!=''> progressmanager</#if>" role="progressbar" style="width: ${value}%;" aria-valuenow="${value}" aria-valuemin="${min}" aria-valuemax="${max}" <#if token!=''>token="${token}" intervalTime=${intervalTime} showReport=${showReport?c}</#if> >
        <#if text=''>${value}%<#else>${text}</#if>
	</div>       
</div>
<#if showReport >
<div id="${progressId}-report" class="progress-bar-report" lastline=0></div>
</#if>
<#if token !='' && !luteceProgressLoaded??>
<script type="module">
import LuteceProgress from "./themes/shared/modules/luteceProgress.js";
new LuteceProgress();
</script>
<#assign luteceProgressLoaded=true >
<#else>
<!-- Add you own scripts to update width value un style attribute, aria-valud-now and progress-bar text content 
-- Example --
<script>
document.addEventListener( "DOMContentLoaded", function(){
	let i = 0;
	if ( i == 0 ){
    i = 1;
    const elem = document.getElementById('${id!}');
    let width = ${value!};
    const idInterval = setInterval( progressFrame, ${intervalTime!});
    function progressFrame() {
      if ( width >= $[max!]) {
        clearInterval( idInterval );
        i = 0;
      } else {
        width++;
        elem.style.width = width + "%";
        elem.setAttribute('aria-value-now', width )
        elem.textConent = width + "%";
      }
    }
	}
});
</script>
-- End example --
-->
</#if>
</#macro>