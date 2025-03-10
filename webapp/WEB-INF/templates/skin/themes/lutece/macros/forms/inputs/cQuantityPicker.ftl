<#-- Macro: cQuantityPicker

Description: permet de définir un champs de sélection de quantité.

Parameters:

@param - name - string - required - permet de définir la valeur de l'attribut 'name' du champs
@param - label - string - optional - permet de définir le libellé du champs (par défaut: 'Choisir une quantité')
@param - labelDecrease - string - optional - permet de définir le libellé du bouton de diminution (par défaut: 'Retirer une unité')
@param - labelIncrease - string - optional - permet de définir le libellé du bouton d'augmentation (par défaut: 'Ajouter une unité')
@param - minQty - number - optional - permet de définir la valeur minimal selectionnable par l'utilisateur (par défaut: 0)
@param - maxQty - number - optional - permet de définir la valeur maximale selectionnable par l'utilisateur (par défaut: 10)
@param - id - string - optional - permet de définir la valeur de l'attribut 'id' du champs
@param - class - string - optional - permet de définir la classe CSS du champs
@param - required - boolean - optional - permet d'indiquer si le champs est obligatoire (par défaut: false)
@param - disabled - boolean - optional - permet d'indiquer si le champs est desactivé (par défaut: false)
@param - helpMsg - string - optional - permet de définir le message d'aide du champs
@param - errorMsg - string - optional - permet de définir le message d'erreur du champs
-->
<#macro cQuantityPicker name label='Choisir une quantité' showLabel=false labelDecrease='Retirer une unité' labelIncrease='Ajouter une unité'  minQty='0' maxQty='10' id='' class='' required=false disabled=false helpMsg='' errorMsg=''>
<#local isInvalid='' />
<#local cId><#if id!=''>${id!}<#else>${name!}</#if></#local>
<#if errorMsg!=''><#assign isInvalid>is-invalid</#assign></#if>
<#if helpMsg !=''><@cFormHelp idMsg helpMsg /></#if>
<@cBlock class='quantity-picker d-flex ${isInvalid} ${class!}'>
    <@cLabel label=label for=cId showLabel=showLabel required=required />
    <@cInputGroup class='w-auto'>
        <@cBtn label='&#8722;' class='light quantity-btn decrement-quantity' params='aria-label="${labelDecrease}" data-direction="-1"' />
        <@cInput type='number' name=name id=cId value=minQty class='form-control quantity-input' params="data-min='${minQty}' data-max='${maxQty}'" disabled=disabled required=required />
        <@cBtn label='&#43;' class='light quantity-btn increment-quantity' params='aria-label="${labelIncrease}" data-direction="1"'  />
    </@cInputGroup>
</@cBlock>
<#if errorMsg !=''><@cFormError idMsg errorMsg /></#if>
<script>
document.addEventListener("click", function(ev) {
  if ( ev.target.classList.contains('quantity-btn') ) {
    const parentDiv = ev.target.closest('.quantity-picker');
    const input = parentDiv.querySelector('input[name="${name}"]');
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
  if (ev.target.matches('input[name="quantity"]')) {
    const input = ev.target;
    const parentDiv = input.closest('.quantity-picker');
    let minQty = parseInt(input.getAttribute("data-min"));
    if (input.value.trim() === "") {
      input.value = minQty;
      parentDiv.querySelector(".decrement-quantity").disabled = true;
    }
  }
}, true);
</script>
</#macro>