
var addr;
var marker = null;
var searchInput = "#" + genericAttributes_id + "_address";
var xInput = "#" + genericAttributes_id + "_x";
var yInput = "#" + genericAttributes_id + "_y";
var resultAff = "#" + genericAttributes_id + "_addr_list";

function addr_search(genericAttributes_id) {
 searchInput = "#" + genericAttributes_id + "_address";
 xInput = "#" + genericAttributes_id + "_x";
 yInput = "#" + genericAttributes_id + "_y";
 resultAff = "#" + genericAttributes_id + "_addr_list";

  addr = $(searchInput).val();
  $.getJSON('https://nominatim.openstreetmap.org/search?format=json&limit=5&q=' + addr, function(data) {
	  var resultList = "";
	  if (data.length > 0){
		  resultList += '<li class="list-group-item disabled main-bg-color text-white text-center"> <small><i class="fas fa-info-circle"></i> Cliquer sur une adresse dans la liste ci-dessous pour la sélectionner</small></li>';
		  $.each(data, function(key, val) {
		  resultList += "<li class='list-group-item' onclick='chooseAddr(" +
		    val.lat + ", " + val.lon + ",this,"+ genericAttributes_id +");return false;'>" + val.display_name +
		    '</li>';
		  });

	  } else {
		  	resultList += '<li class="list-group-item bg-warning"> <i class="fas fa-exclamation-triangle"></i>  <strong>Aucune adresse pour cette recherche</strong></li>';
	  }
	  $(resultAff).html(resultList);
  }); 
	  
}

function onMapClick(e) {
if (marker != null)
	map.removeLayer(marker);
	lat = e.latlng.lat;
	lon = e.latlng.lng;
	$.getJSON('https://nominatim.openstreetmap.org/reverse?format=json&lat=' + lat + '&lon=' + lon , function(data) {
		addr = data.display_name;
		  var location = new L.LatLng(lat, lon);
		  marker = L.marker(location).addTo(map);
		  map.panTo(location);
		  $(searchInput).val(addr);
		  $(xInput).val(lon);
		  $(yInput).val(lat);
	  }); 

}

function chooseAddr(latChoose,lonChoose, elem, genericAttributes_id ){
	var listItems = document.querySelectorAll(".col-osm.list-group .list-group-item");
	listItems.forEach(function(lItem) {
	  lItem.classList.remove('active');
	});
	searchInput = "#" + genericAttributes_id + "_address";
	xInput = "#" + genericAttributes_id + "_x";
	yInput = "#" + genericAttributes_id + "_y";
	resultAff = "#" + genericAttributes_id + "_addr_list";
	if (marker != null)
		map.removeLayer(marker);
	lat = latChoose;
	lon = lonChoose;
	map.invalidateSize();
	var location = new L.LatLng(lat, lon);
	marker = L.marker(location).addTo(map);
	map.panTo(location);
	map.setZoom(20);
	var latLngs = [ marker.getLatLng() ];
	var markerBounds = L.latLngBounds(latLngs);
	map.fitBounds(markerBounds);
	addr = elem.innerHTML;
	if (elem.classList.contains('active')) {
		elem.classList.remove('active');
	} else {
		elem.classList.add('active');
	}
	$(searchInput).val(addr);
	$(xInput).val(lon);
	$(yInput).val(lat);
}
