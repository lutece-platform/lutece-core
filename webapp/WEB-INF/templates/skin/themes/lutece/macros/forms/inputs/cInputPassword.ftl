<#-- Macro: cInputPassword                               
Parameters:
@param - name - string - required - Nom du champ
@param - name - string - optionnal - Libellé du champ
@param - btnShowPassword - boolean - Default true, affiche le bouton pour afficher le mot de passe
@param - passwordMeter - boolean - Default false, affiche les informations de force du mot de passe
@param - pmLabel - string - Default '#i18n{portal.theme.labelPasswordStrength} #i18n{portal.theme.labelPasswordNoPasswordTyped}' Label asssocié ua message sur la force du mot de passe
@param - pmUrl - string - Default '' Url to generate password
@param - pmConfirmFieldId - string - Default '' If not empty, the field will be used to link the confirm field by id
@param - id - string - Default '', Id de l'input                           
@param - class - string -  Default 'custom-checkbox', classe css à ajouter à l'input
@param - size - string - Default '', Size of input (sm, lg)
@param - maxlength - number - Maximum length of the input
@param - value : - string - Default '', Valeur par défaut de l'input         
@param - placeholder- string - Default '' , placeholder de l'input
@param - autocomplete- string - Default '' , autocomplete pour l'input https://developer.mozilla.org/fr/docs/Web/HTML/Attributes/autocomplete                         
@param - required - boolean - Default true, champ obligatoire ou non                 
@param - disabled - boolean - Default false, champ désactivé ou non
@param - readonly - boolean - Default false, champ en lecture seule ou non                
@param - helpMsg - string - Default '#i18n{portal.theme.labelPasswordHelp}', Message d'aide pour l'input                                   
@param - errorMsg - string - Default '', Message d'erreur pour l'input                      
@param - params - string - Default '', Tous autres paramètres à ajouter à l'input                   

@sample : 

 -->
<#macro cInputPassword name label='#i18n{portal.theme.labelPassword}' btnShowPassword=true passwordMeter=false pmLabel='#i18n{portal.theme.labelPasswordStrength} #i18n{portal.theme.labelPasswordNoPasswordTyped}' pmUrl='' pmConfirmFieldId='' placeholder='' autocomplete='' class='' id='' size='' value='' required=true disabled=false maxlength=100 helpMsg='#i18n{portal.theme.labelPasswordHelp}' errorMsg='' params='' deprecated...>
<@deprecatedWarning args=deprecated />
<#local passId><#if id !=''>${id!}<#else>${name!}</#if></#local>
<#local passLabel><#if pmLabel !=''>${pmLabel!}<#else>#i18n{portal.theme.labelPasswordStrength} #i18n{portal.theme.labelPasswordNoPasswordTyped}</#if></#local>
<#local passClass>form-control pwd<#if class!=''> ${class!}</#if><#if size!=''> form-control-${size!}</#if><#if errorMsg!=''> is-invalid</#if></#local>
<@cLabel label=label for=passId required=required />
<#if helpMsg !=''><@cFormHelp passId helpMsg /></#if>
<@cInputGroup class='password'>
    <@cInput type='password' class='${passClass!}' size='lg' id=passId name='${name}' maxlength=maxlength required=required placeholder=placeholder autocomplete=autocomplete params='autocomplete="off" ${params!}'>
    <#if btnShowPassword>
    <@cBtn class='secondary toggle-password' type='button' label='' params='data-bs-toggle="#${passId}" aria-pressed="false" title="#i18n{portal.theme.labelPasswordShow}" tabindex="0"'>
         <@parisIcon name='eye-off' class='main-info-color' />
    </@cBtn>
    </#if>
    </@cInput>
</@cInputGroup>
<#nested>
<#if errorMsg !=''><@cFormError passId errorMsg /></#if>
<#if passwordMeter>
<@cPasswordMeter id=passId label=passLabel url=pmUrl />
<#if isScriptPasswordLoaded?? && isScriptPasswordLoaded>
<#else>
<script>
/* PASSWORD */
function passwordStrength(field, defaultLabel, labelStrength) {
  const password = document.getElementById(field);
  const wrapper = <#noparse>document.getElementById(`password-${field}-wrapper`);</#noparse>
  const meter = wrapper.querySelector("[role=meter]");
  const output = wrapper.querySelector("output");
  const indicator = wrapper.querySelector(".indicator");
  const dot = indicator.querySelector(".dot");
  const text = indicator.querySelector(".meter-text");

  password.addEventListener("keydown", (e)=>{
    showIndicator(field, defaultLabel, labelStrength, e.key );
  });
}

function removeClassesPrefixedWith( element, prefix ) {
  const classes = element.className.split(" ");
  let newClasses = classes.filter(function(className) {
      return !className.startsWith(prefix);
  });
  // Update the element's className property with the new classes
  element.className = newClasses.join(" ");
}

function showIndicator(field, defaultLabel, labelStrength, typedKey) {
  const strength = {
    1: { msg: "Faible", bar: "main-danger-bg-color", txt: "main-danger-color" },
    2: { msg: "Moyen", bar: "main-warning-bg-color-text", txt: "main-warning-color-text"},
    3: { msg: "Excellent", bar: "main-success-bg-color", txt: "main-success-color" },
  };
 <#noparse>const wrapper = document.getElementById(`password-${field}-wrapper`);</#noparse>
  const meter = wrapper.querySelector("[role=meter]");
  const output = wrapper.querySelector("output");
  const indicator = wrapper.querySelector(".indicator");
  const dot = indicator.querySelector(".dot");
  const text = indicator.querySelector(".meter-text");

  let val = password.value;
  let score = getPasswordStrength(val);

  // Update the password strength meter
  meter.setAttribute("aria-valuenow", 20 * score);

  // Update the text indicator
  <#noparse>
  if (val !== "") {
    indicator.classList.remove("visually-hidden");
    meter.setAttribute(
      "aria-valuetext",
      `${labelStrength}: ${strength[score].msg}`
    );
    
    // Remove all classes prefixed with "main-"
    removeClassesPrefixedWith( dot, "main-");
    removeClassesPrefixedWith( text, "main-");

    dot.classList.add(strength[score].bar);
    text.classList.add(strength[score].txt);
    text.textContent = strength[score].msg;
    output.textContent = "";
    setTimeout(function () {
      output.textContent = `${labelStrength}: ${strength[score].msg}`;
    }, 200);
  } else {
    meter.setAttribute("aria-valuetext", defaultLabel);
    indicator.classList.add("visually-hidden");
    output.textContent = defaultLabel;
  }
  </#noparse>
}

function getPasswordStrength(v) {
  let no = 1,
    hasLower = false,
    hasUpper = false,
    hasDigit = false,
    hasMinChar = false,
    hasExtraChar = false,
    hasSpecialChar = false;
  if (/[A-Z]/.test(v)) {
    hasUpper = true;
  } else {
    hasUpper = false;
  }
  if (/[a-z]/.test(v)) {
    hasLower = true;
  } else {
    hasLower = true;
  }
  if (/\d/.test(v)) {
    hasDigit = true;
  } else {
    hasDigit = false;
  }
  if (/^.*(?=.{8,99})/.test(v)) {
    hasMinChar = true;
  } else {
    hasMinChar = false;
  }
  if (/^.*(?=.{12,99})/.test(v)) {
    hasExtraChar = true;
  } else {
    hasExtraChar = false;
  }
  const regex = /[!#$%()*+,\-./:;=?@[\]^_{|}]/u;
  if (regex.test(v)) {
    $(".special_char").addClass("text-success");
    hasSpecialChar = true;
  } else {
    $(".special_char").removeClass("text-success");
    hasSpecialChar = false;
  }
  if (
    (!hasMinChar && (hasLower || hasDigit || hasUpper)) ||
    (hasExtraChar && hasLower && hasDigit && !hasUpper) ||
    (hasExtraChar && hasLower && !hasDigit && hasUpper) ||
    (hasExtraChar && hasLower && !hasDigit && !hasUpper)
  )
  no = 1;
  if (hasMinChar && hasLower && hasDigit && hasUpper && hasSpecialChar) no = 2;
  if (
    hasMinChar &&
    hasLower &&
    hasDigit &&
    hasUpper &&
    hasExtraChar &&
    hasSpecialChar
  )
  no = 3;
  return no;
}

function generateLocalPassword() {
  const lowercase = "abcdefghijklmnopqrstuvwxyz";
  const uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  const numbers = "0123456789";
  const symbols = "!@#$%^&*()_+-=[]{}|;:,.<>?";
  let password = "";
  let passwordLength = Math.floor(Math.random() * 4) + 12;
  // Use at least one character from each character set
  password += lowercase.charAt(Math.floor(Math.random() * lowercase.length));
  password += uppercase.charAt(Math.floor(Math.random() * uppercase.length));
  password += numbers.charAt(Math.floor(Math.random() * numbers.length));
  password += symbols.charAt(Math.floor(Math.random() * symbols.length));
  // Use remaining characters from any character set
  let remainingLength = passwordLength - 4;
  for (let i = 0; i < remainingLength; i++) {
    let characterSet = Math.floor(Math.random() * 4);
    switch (characterSet) {
      case 0:
        password += lowercase.charAt(
          Math.floor(Math.random() * lowercase.length)
        );
        break;
      case 1:
        password += uppercase.charAt(
          Math.floor(Math.random() * uppercase.length)
        );
        break;
      case 2:
        password += numbers.charAt(Math.floor(Math.random() * numbers.length));
        break;
      case 3:
        password += symbols.charAt(Math.floor(Math.random() * symbols.length));
        break;
    }
  }
  return password;
}

/* PASSWORD */
<#if !isTogglePasswordLoaded?? || !isTogglePasswordLoaded>
function togglePasswordIcon( field, show=false ){
	<#noparse>const input = $(`${field}`), btnToggle = $(`${field} + .input-group-append .toggle-password`), icon=$(`${field} + .input-group-append .toggle-password .paris-icon use`)</#noparse>
  if( show && show !=undefined ){
      icon.attr('href','#paris-icon-eye')
  } else {
      if( icon.attr('href') == '#paris-icon-eye'  ){
          icon.attr('href','#paris-icon-eye-off')
      } else {
          icon.attr('href','#paris-icon-eye')
      }
  }
  if( show && show !=undefined ){
      if( input.attr("type") == "password") {
          input.attr("type", "text");
          $(this).attr('aria-pressed','true');
          $(this).attr('title','#i18n{portal.theme.labelPasswordHide}');
      }
  } else {
    if( input.attr("type") == "password") {
        input.attr("type", "text");
        btnToggle.attr('aria-pressed','true');
        btnToggle.attr('title','#i18n{portal.theme.labelPasswordHide}');
    } else {
        input.attr("type", "password");
        btnToggle.attr('aria-pressed','false');
        btnToggle.attr('title','#i18n{portal.theme.labelPasswordShow}');
    }
  }
}
</#if>
document.addEventListener( "DOMContentLoaded", function(e){
    const btnGenerate = document.getElementById('password-${passId!}-generator');
    const fieldDest = document.getElementById('${passId!}');
    <#if pmConfirmFieldId !=''>const fieldConfirmDest = document.getElementById('${pmConfirmFieldId!}');</#if>
    btnGenerate.addEventListener( "click", (e) => {
      <#if pmUrl !=''>
        $.ajax({
            url: "${pmUrl!}",
            type: "GET",
            dataType: "json",
            success: function ( data) {
                fieldDest.value = data.password;
                togglePasswordIcon( '#${passId}', true ) 	
                <#if pmConfirmFieldId !=''>
                fieldConfirmDest.value = data.password;
                togglePasswordIcon( '#${pmConfirmFieldId}', true )
                </#if>
                showIndicator( '${passId}', '${pmLabel}', '#i18n{portal.theme.labelPasswordStrength}'  )
            },
            error: function (jqXHR, textStatus, errorThrown) {
            }
        });
      <#else>
        fieldDest.value = generateLocalPassword()
        togglePasswordIcon( '#${passId}', true ) 	
        <#if pmConfirmFieldId !=''>
        fieldConfirmDest.value = generateLocalPassword()
        togglePasswordIcon( '#${pmConfirmFieldId}', true )
        </#if>
        showIndicator('${passId}', '${pmLabel}', '#i18n{portal.theme.labelPasswordStrength}')
      </#if>  
    });
    passwordStrength( '${passId}', '${pmLabel}', '#i18n{portal.theme.labelPasswordStrength}' )
<#if !isTogglePasswordLoaded?? || !isTogglePasswordLoaded>
    $(".toggle-password").each( function(){ 
        $(this).on('click', function(e){
            const fieldId = $(this).attr('data-bs-toggle')
            togglePasswordIcon( fieldId )
        })
        $(this).bind("keydown keypress",  function(e) {
            if(  e.which === 32 ){
                e.preventDefault();
                const fieldId = $(this).attr('data-bs-toggle')
                togglePasswordIcon( fieldId )
            }
        })
    })
</#if>
});
</script>
<#assign isScriptPasswordLoaded = true />
</#if>
</#if>
</#macro>