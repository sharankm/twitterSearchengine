app.controller('searchController', function($scope, $http) {
	
	angular.extend($scope, {
        map: {
            center: { latitude: 37, longitude: -95 },
            zoom: 4,
            markers: [],
            events: {
            click: function (map, eventName, originalEventArgs) {
                var e = originalEventArgs[0];
                var lat = e.latLng.lat(),lon = e.latLng.lng();
                var marker = {
                    id: Date.now(),
                    coords: {
                        latitude: lat,
                        longitude: lon
                    }
                };
                $scope.map.markers.push(marker);
                $scope.$apply();
            }
        }
        }
    });
	
	
	$scope.indexType = 'lucene';
	
	$scope.submitQuery = function() {
		var urlString = '';
		$scope.map.markers = [];
		var hashKeyString = $scope.queryKey;
		if(hashKeyString.includes('#')){
			var formatted = hashKeyString.substring(1, hashKeyString.length);
			hashKeyString = 'HashTag'.concat(formatted); 
		}	
		if ($scope.indexType == 'lucene')
			urlString = '/luceneSearch/' + hashKeyString;
		else
			urlString = '/hadoopSearch/' + hashKeyString;
		$http({
			method : 'GET',
			url : urlString
		}).then(function(response) {
		      $scope.tweets = response.data;
		      $scope.currentPage = 0;
		      $scope.pageSize = 10;
		      $scope.numberOfPages = function(){
		      		return Math.ceil($scope.tweets.length/$scope.pageSize);
		      }
		      var tweets = $scope.tweets;
		      var arrayLength = tweets.length;
		      for (var i = 0; i < arrayLength; i++) {
		
		    	    var location = tweets[i].location;
		    	    if(location.length > 1){
		    	    	$http.get('https://maps.googleapis.com/maps/api/geocode/json?address=' + 
					    		  location + '&key=')
					      .then(function(loc_response){
					         console.log(loc_response.data);
					         $scope.queryResults = loc_response.data.results;
					         $scope.geodata = $scope.queryResults[0].geometry.location;
					         var marker = {
					                    id: Date.now(),
					                    coords: {
					                        latitude: $scope.geodata.lat,
					                        longitude: $scope.geodata.lng
					                    }
					                };
					         $scope.map.markers.push(marker);
				             $scope.$apply();
					       }, 
					       function error(error_response){
					          $scope.queryError = error_response;
					       })
		    	    }
		      }
		      $scope.currentPage = 0;
		      $scope.pageSize = 10;
		      $scope.numberofPages = function(){
		      		return Math.ceil($scope.tweets.length/$scope.pageSize);
		      }
	    });
	};	
});
app.filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    }
});