<#-- Macro: cGoToTop

Description: Affiche le bouton retour en haut.

Parameters:
@param - scrollLabel - string - optional -  Nom du menu par défaut - #i18n{portal.theme.gohome} / Haut de page - 
@param - scrollClass - string - optional - Classe CSS de l'effet de scroll sur le bouton. Par défaut "primary fadein"
@param - scrollBtnClass - string - optional - Classe CSS du bouton. Par défaut "btn-gototop"
@param - scrollTop - number - optional - Offset top par défaut pour apparition du bouton. Par défaut 100
-->
<#macro cGoToTop scrollLabel='#i18n{portal.theme.gohome}' scrollClass='primary fadein' scrollBtnClass='btn-gototop' scrollTop=100 deprecated...>
<@deprecatedWarning args=deprecated />
<@cBtn type='button' class='${scrollClass!} ${scrollBtnClass!}' label=scrollLabel nestedPos='before'><@cInline class='position-relative white fw-bold pb-3' params='style="bottom:2px;"'>&#8593;</@cInline></@cBtn>
<script>
const gototop = document.querySelector('.${scrollBtnClass!}');
document.addEventListener( "DOMContentLoaded", () => {
    document.addEventListener("scroll", (e) => {
        if ( document.documentElement.scrollTop  > ${scrollTop}) {
            gototop.classList.add('active');
        } else {
            gototop.classList.remove('active');
        }
    });
  
    gototop.addEventListener( 'click', (e) =>  {
        e.preventDefault();
       window.scrollTo({
            top: 0,
            left: 0,
        });
    });
});
</script>
</#macro>