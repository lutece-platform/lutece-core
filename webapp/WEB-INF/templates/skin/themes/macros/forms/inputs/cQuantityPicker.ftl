<#--
Macro: cQuantityPicker

Description: Generates a quantity picker with increment/decrement buttons and a number input, supporting min/max constraints.

Parameters:
- name (string, required): the name attribute of the input.
- label (string, optional): the label text for the picker. Default: '#i18n{portal.theme.labelFormQuantityChoose}'.
- showLabel (boolean, optional): displays or hides the label. Default: false.
- labelDecrease (string, optional): accessible label for the decrease button. Default: '#i18n{portal.theme.labelFormQuantityDecrease}'.
- labelIncrease (string, optional): accessible label for the increase button. Default: '#i18n{portal.theme.labelFormQuantityIncrease}'.
- paramsDecrease (map, optional): additional attributes of the decrease button, e.g. {'disabled': true}. Default: {}.
- paramsIncrease (map, optional): additional attributes of the increase button, e.g. {'disabled': true}. Default: {}.
- minQty (string, optional): minimum selectable value. Default: '0'.
- maxQty (string, optional): maximum selectable value. Default: '10'.
- id (string, optional): the ID of the input. Default: ''.
- class (string, optional): CSS class for the picker container. Default: ''.
- required (boolean, optional): marks the field as required. Default: false.
- disabled (boolean, optional): disables the input and buttons. Default: false.
- helpMsg (string, optional): help message displayed below the picker. Default: ''.
- errorMsg (string, optional): error message displayed on validation failure. Default: ''.

Showcase:
- desc: "Sélecteur de quantité - @cQuantityPicker"
- newFeature: false

Snippet:

    Basic quantity picker:

    <@cQuantityPicker name='quantity' />

    Quantity picker with custom range:

    <@cQuantityPicker name='tickets' label='Number of tickets' showLabel=true minQty='1' maxQty='5' />

    Required quantity picker:

    <@cQuantityPicker name='seats' label='Number of seats' showLabel=true minQty='1' maxQty='20' required=true />

-->
<#macro cQuantityPicker name label='#i18n{portal.theme.labelFormQuantityChoose}' showLabel=false labelDecrease='#i18n{portal.theme.labelFormQuantityDecrease}' labelIncrease='#i18n{portal.theme.labelFormQuantityIncrease}' paramsDecrease={} paramsIncrease={} minQty='0' maxQty='10' id='' class='' required=false disabled=false helpMsg='' errorMsg='' hideErrorMsg=false params={} deprecated...>
<@deprecatedWarning args=deprecated />
<#local isInvalid='' />
<#local cId = id?has_content?then( id, name ) />
<#local inputParams = params + { 'data-min': minQty, 'data-max': maxQty } />
<#if errorMsg?has_content><#assign isInvalid = 'is-invalid' /></#if>
<#local errorClass = ( hideErrorMsg )?then( 'visually-hidden', '' ) />
<@cBlock class='quantity-picker ${isInvalid} ${class!}'>
    <@cLabel label=label for=cId showLabel=showLabel required=required />
    <@cInputGroup class='w-auto'>
        <@cBtn label='−' class='light quantity-btn decrement-quantity' params=paramsDecrease + {'aria-label': labelDecrease, 'data-direction': '-1'} />
        <@cInput type='number' name=name id=cId value=minQty class='form-control quantity-input' disabled=disabled required=required params=inputParams />
        <@cBtn label='+' class='light quantity-btn increment-quantity' params=paramsIncrease + {'aria-label': labelIncrease, 'data-direction': '1'} />
    </@cInputGroup>
</@cBlock>
<#if helpMsg?has_content><@cFormHelp cId helpMsg /></#if>
<#if errorMsg?has_content><@cFormError cId errorMsg errorClass /></#if>
<script>
document.addEventListener( 'DOMContentLoaded', function() {
  document.addEventListener( 'click', function(ev) {
   if ( ev.target.classList.contains('quantity-btn') ) {
    const parentDiv = ev.target.closest('.quantity-picker');
    const input = parentDiv.querySelector('input[name="${name?js_string?no_esc}"]');
    const currentQty = parseInt(input.value);
    const qtyDirection = parseInt(ev.target.getAttribute("data-direction"));
    let newQty = currentQty + qtyDirection;
    let minQty = parseInt(input.getAttribute("data-min"));
    let maxQty = parseInt(input.getAttribute("data-max"));

    newQty = Math.max(minQty, Math.min(maxQty, newQty));

    parentDiv.querySelector(".decrement-quantity").disabled = (newQty === minQty);
    parentDiv.querySelector(".increment-quantity").disabled = (newQty === maxQty);

    input.value = newQty;
    }
  });

  document.addEventListener("blur", function(ev) {
    if ( ev.target.name ==='quantity') {
      const input = ev.target;
      const parentDiv = input.closest('.quantity-picker');
      let minQty = parseInt(input.getAttribute("data-min"));
      if (input.value.trim() === "") {
        input.value = minQty;
        parentDiv.querySelector(".decrement-quantity").disabled = true;
      }
    }
  }, true);
});
</script>
</#macro>