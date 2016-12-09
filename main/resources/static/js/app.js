var app = angular.module("app", [ 'uiGmapgoogle-maps' ]);

app.config(function(uiGmapGoogleMapApiProvider) {
	uiGmapGoogleMapApiProvider.configure({
		key: '',
		v : '3.20', // defaults to latest 3.X anyhow
		libraries : 'weather,geometry,visualization'
	});
})