document.addEventListener('DOMContentLoaded', () => {
    /* Check if enter key is pressed and force submit */
    const formValidate = document.querySelector("#form-validate")
    if( formValidate != undefined  ){
        formValidate.addEventListener( 'keypress input', (event) => {
            if( e.which == 13){
                e.preventDefault();
                document.querySelector('button[name="action_doSaveStep"]').click();
            }
        });
    }
});