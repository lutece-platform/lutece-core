<#macro searchBox id deprecated...>
<@deprecatedWarning args=deprecated />
  <div id="${id}" class="card rounded-pill px-4 py-0 pe-2 bg-form mb-0">
    <div class="row g-2 align-items-center">
      <#nested>
          <div class="col-md-auto d-flex align-items-center justify-content-center position-relative align-self-stretch py-2">
      <button id="searchButton" type="submit" class="btn btn-primary rounded-pill h-100 align-center d-flex align-items-center px-3">
        <i class="ti ti-search"></i>
      </button>
    </div>
    </div>
  </div>
<script type="module">
document.getElementById('searchButton').addEventListener('click', function() {
  var searchButton = this;
  var icon = searchButton.querySelector('i');
  icon.classList.remove('ti-search');
  icon.classList.add('ti-loader-2','spinSearchBox');
  searchButton.classList.add('btn-dark','shadow-lg')
});
  var inputs = document.querySelectorAll('#${id} input');
  for(var i = 0; i < inputs.length; i++) {
    inputs[i].addEventListener('keydown', function(e) {
    if(e.key === 'Enter' || e.keyCode == 13) { 
        e.preventDefault();
        document.getElementById('searchButton').click();
      }
    });
  }
</script>
<style>
    @keyframes spinSearchBox {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }
    .spinSearchBox {
        animation: spinSearchBox 0.3s linear infinite;
    }
</style>
</#macro>