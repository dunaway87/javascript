var googleLayer;	
function map(){
		map = new L.Map('map', {center: new L.LatLng(61.15, -149.9), zoom: 11});
	addMyGoogle(); 
	// addLeafletBase();
	 // addGoogle();
	  //addGina();
}
	function addMyGoogle(){
	googleLayer = L.tileLayer('https://khms0.google.com/kh/v=144&src=app&x={x}&y={y}&z={z}&s=Ga');
//	googleLayer = L.tileLayer('https://mts0.google.com/vt/x={x}&y={y}&z={z}');
		
		map.addLayer(googleLayer);
}
        function addLeafletBase(){
        googleLayer=L.tileLayer('http://{s}.tile.cloudmade.com/0e47d723b540402a81f1f972e021d7fe/997/256/{z}/{x}/{y}.png', {
    attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, <a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="http://cloudmade.com">CloudMade</a>',
    maxZoom: 18
});
	map.addLayer(googleLayer);
    }
	function addGina(){
		 googleLayer = L.tileLayer.wms(" http://wms.alaskamapped.org/bdl?", {
		    SRS:'EPSG:3857',
		    layers: 'BestDataAvailableLayer'
		});
		map.addLayer(googleLayer);
	}
	
	function addGoogle(){
	    
		googleLayer = new L.Google('SATELLITE');
	     	
		map.addLayer(googleLayer,true);

	}
	
	function switchBaseMap(){
		map.removeLayer(googleLayer);
		var mylist=document.getElementById("baseMapTypes");
		console.log(mylist.options[mylist.selectedIndex].text);
		var bln = mylist.options[mylist.selectedIndex].text;
		var url;
		if(bln == 'Satellite'){
			googleLayer = L.tileLayer('https://khms0.google.com/kh/v=144&src=app&x={x}&y={y}&z={z}&s=Ga');
		} else if (bln=='Road Map'){
			googleLayer = L.tileLayer('https://mts0.google.com/vt/x={x}&y={y}&z={z}');
		} else if (bln=='Terrain'){
			googleLayer = L.tileLayer('https://mts1.google.com/vt/lyrs=t@132,r@248000000&x={x}&y={y}&z={z}');
		}
		
		map.addLayer(googleLayer);
		googleLayer.bringToBack();
	}
	
var layers = new Array();
function mapLayer(layerName){
 	var layer = L.tileLayer.wms("http://198.199.81.64:8080/geoserver/cite/wms", {
    layers: 'cite:'+layerName,
    format: 'image/png',
    transparent: true,
    SRS:'EPSG:3857',
	
});
map.addLayer(layer);
return layer;
}


function manageLayer(layername){
	var hasLayer = false;
	
	console.log(layers);
	console.log(layername);
	for(var i =0; i < layers.length; i++){
		if(layers[i].layerName == layername){
			$("#"+layername).removeClass("clicked");
			removeLayer(layers[i].layer, i);
			hasLayer = true;
			break;
		
		}
	}
	if(hasLayer == false){
		$("#"+layername).addClass("clicked");
		addLayerToArray(layername);
	}
}

function addLayerToArray(layername){
	var layerInfo = new Object();
	layerInfo.layer =  mapLayer(layername);
	layerInfo.layerName= layername;
	layers.push(layerInfo);
}

function removeLayer(layer, i){
	map.removeLayer(layer);
	layers.splice(i,1);
}

