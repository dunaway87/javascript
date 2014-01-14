var displayedPolygon = null;
var parcelNumber = null;

map.on('click', function(e){
	var layerNames = new Array();
	for(var i =0; i < layers.length; i++){
		layerNames.push(layers[i].layerName);

	}	
	console.log(e.layerPoint);
	console.log(map.getBounds());
	console.log(layerNames.join());
	var parcelNumber = getLayerInfo(e.latlng.lat, e.latlng.lng, layerNames, e);
});

function getLayerInfo(lat, lng, layerNames, e){
	if(displayedPolygon != null){
		map.removeLayer(displayedPolygon);
	}
	var json;




	$.get(
			"/getLayerInfo?lat="+lat+"&lng="+lng+"&layers="+layerNames,function(result){
				console.log(result);
				var polygon = result.members.elements;
				if(polygon != ""){
					console.log(polygon);
					displayedPolygon = L.polygon(polygon, {
						color: 'red',
						fillColor: '#ffffff',
						fillOpacity: 0.5
					});
					displayedPolygon.addTo(map);
				}
				var popUpHtml = result.members.layersInfo;
				if(popUpHtml != ""){
					var popup = L.popup();
					popup.setLatLng(e.latlng).setContent(result.members.layersInfo).openOn(map);
				}



			});

}
