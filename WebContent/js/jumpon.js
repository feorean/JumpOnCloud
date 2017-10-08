/**
 * 
 */
var typingTimer;                //timer identifier
var doneTypingInterval = 5000;  //time in ms, 5 second for example


$(function () {
	  $('[data-toggle="tooltip"]').tooltip()
	});

$(function() {
	  
	  var $contextMenu = $("#contextMenu");
	  
	  $("body").on("contextmenu", ".searchList", function(e) {
	    $contextMenu.css({
	      display: "block",
	      left: e.pageX,
	      top: e.pageY
	    });
	    return false;
	  });
	  
	  $contextMenu.on("click", "a", function() {
	     $contextMenu.hide();
	  });
	  
	  $("body").on("click", "div", function() {
	     $contextMenu.hide();
	  });
	  
	});

function decodeHtml(html) {
    var txt = document.createElement("textarea");
    txt.innerHTML = html;
    return txt.value;
}

function guid() {
	  function s4() {
	    return Math.floor((1 + Math.random()) * 0x10000)
	      .toString(16)
	      .substring(1);
	  }
	  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
	    s4() + '-' + s4() + s4() + s4();
	}

		  var app = angular.module('myApp', ['ngSanitize', 'ngRoute' ]);		 
		  
		  app.controller('searchListCtrl', function($scope,  $sanitize, $sce, $http, $location, $window, $route, $interval) {
			  
			 $scope.getTag = function(tag) {
				  
				 //fool check
				  if ((tag==null) && ($scope.searchTag == null)) {
					  return;
				  }
				  var vtag = (tag==null)?$scope.searchTag:tag ; 
				  console.log("Jump:"+vtag);
				  //return;
				  //alert(vtag);
				  //decodeURI(document.getElementById("newTag").value);
				  //alert(vtag+'    gettag?tag=' + decodeURI(document.getElementById("searchTag").value));
				  $http({
		    		  method: 'GET',
		    		  url: 'getjump?q=' + vtag
		    		}).then(function successCallback(response) {
		    		    // this callback will be called asynchronously
		    		    // when the response is available
		    		    
		    		    //console.log(response.data);
		    		    //var url = JSON.parse(response.data);
		    		    
		    		    if (response.data.hasOwnProperty('JUMP')) {
		    		    	
		    		    	//console.log("URL:"+response.data["JUMP"]);
		    		    	//Redirecting to web site
		    		    	window.location.href = decodeURIComponent(response.data["JUMP"]);
		    		    } else {
		    		    	
		    		    	console.log("Not sure what to do!");
		    		    	
		    		    }
		    		    
		    		  }, function errorCallback(response) {
		    		    // called asynchronously if an error occurs
		    		    // or server returns response with an error status.
		    		  });				  
				  
			  }
			 
			 $scope.deleteTag = function(tag, type) {
				  
				 //fool check
				  if (tag==null)  {
					  return;
				  }
				  
				  $http({
		    		  method: 'GET',
		    		  url: 'deletejump?jump=' + tag + '&t='+type
		    		}).then(function successCallback(response) {
		    		    
		    		    if (response.data.hasOwnProperty('INFO')) {
		    		    	console.log(response.data['INFO']);
		    		    }
		    		    
		    		  }, function errorCallback(response) {

		    		  });				  
				  
			  }			 
			 
			 
			 $scope.getTags = function() {
				  
				  if (!$scope.searchTag) {
					  return;
				  }
				 
				  $http({
		    		  method: 'GET',
		    		  url: 'getjumps?jumpbeg='+$scope.searchTag
		    		}).then(function successCallback(response) {
		    		    // this callback will be called asynchronously
		    		    // when the response is available
		    		    
		    		    var found = false;
		    		    
		    		    if (Array.isArray(response.data.tags)) {
		    		    	
		    		    	if ($scope.searchTag == response.data.query) {
		    		    		
		    		    		$scope.props = response.data.tags;
			    		    				    		    	
			    		    	//HERE should be props instead response!  (possible solution for unicode)
			    		    	for(var i=0; i < response.data.length; i++) {
			    		    		
			    		    		//console.log($sce.trustAsHtml("&copy; Acme"));//response.data[i].tag));
			    		    		//response.data[i].tag = $sce.trustAsHtml(response.data[i].tag) ;
			    		    		response.data[i].tag =	decodeHtml(response.data[i].tag);
			    		    	}
		    		    	}	
		    		    	
		    		    	if (($scope.searchTag == null || $scope.searchTag == "") && response.data.query=="empty") {
		    		    		$scope.tagCount = Object.keys(response.data.tags).length;
		    		    		$scope.currTag = -1;
		    		    		$scope.props = response.data.tags;
		    		    	}
		    		    	
		    		    	found=true;
		    		    } else {
		    		    	
		    		    	console.log(response.data);
		    		    	console.log("No tags beginning with this tag.");
		    		    	
		    		    }
		    		    
		    		    var hidden = $( "#searchList" ).is( ":hidden" );		    		    
		    		    if ((found && hidden) || (!found && !hidden)) {		    		 
		    		    	
		    		    	$("#searchList").toggle();
		    		    	
		    			}
		    		    
		    		    //if (found && !hidden) $scope.searchTag.focus();
		    		    //console.log($scope.a0);
		    		    document.getElementById("searchTag").focus();
		    		    
		    		    
		    		  }, function errorCallback(response) {
		    		    // called asynchronously if an error occurs
		    		    // or server returns response with an error status.
		    			  
		    		  });				  
				  
			  }			 
			  
			 $scope.getTopTags = function() {
				  
				 console.log("Getting top tags...");
				  $http({
		    		  method: 'GET',
		    		  url: 'gettoptags' //?t='+guid()
		    		}).then(function successCallback(response) {
		    		    // this callback will be called asynchronously
		    		    // when the response is available
		    			//console.log(JSON.stringify(response.data));
		    			//console.log(response.data);
		    		    if (Array.isArray(response.data.tags)) {
		    		    	
		    		    	if (($scope.searchTag == null || $scope.searchTag == "") && response.data.query=="empty") {
		    		    		
		    		    		$scope.tagCount = Object.keys(response.data.tags).length;
		    		    		$scope.currTag = -1;
		    		    		$scope.props = response.data.tags;
		    		    		
		    		    	}
		    		    	
		    		    } else {
		    		    	
		    		    	console.log(response.data);
		    		    	console.log("No top tags.");
		    		    	
		    		    }
		    		    
		    		  }, function errorCallback(response) {
		    		    // called asynchronously if an error occurs
		    		    // or server returns response with an error status.
		    		  });				  
				  
			  }			 
			 
			 $scope.getUserName = function() {
				  
				 console.log("Get user name");
				 
				 $http({
		    		  method: 'GET',
		    		  url: 'getusername'
		    		}).then(function successCallback(response) {
		    		    // this callback will be called asynchronously
		    		    // when the response is available
		    			
		    			
		    			//console.log("Getusername res:"+response.data);
		    		    
		    			if (response.data.hasOwnProperty('RES')) {
		    		    	
		    		    	console.log("Response getusername:"+response.data["RES"]);
		    		    	
		    		    	var un = response.data["RES"];
		    			
		    		    	if (un != 'NOT_AUTHENTICATED') {
		    		    		$scope.userName = un.split(" ")[0];
		    		    	}
		    		    	
		    		    	$scope.setVisibilities();
		    			}
		    		    
		    		  }, function errorCallback(response) {
		    		    // called asynchronously if an error occurs
		    		    // or server returns response with an error status.
		    		      $scope.userName = null;
		    		      $scope.setVisibilities();
		    		  });				  
				  
			  }	
			 
			 $scope.addNewTag = function() {
				  
				  $http({
		    		  method: 'POST',
		    		  url: 'addtag?tag='+$scope.newTag+'&url='+encodeURIComponent($scope.newTagUrl)+'&overwrite='+$scope.overwriteTag
		    		}).then(function successCallback(response) {
		    		    console.log(response.data);
		    		    
		    		    if (response.data.hasOwnProperty('INFO')) {
		    		    	
		    		    	console.log("Response:"+response.data["INFO"]);
		    		    	
		    		    	$scope.newTag="";
		    		    	$scope.newTagUrl="";
		    		    	$scope.overwriteTag="unchecked";
		    		    	$scope.duplicateMessageState = $scope.hideElement();
		    		    	$scope.invalidUrlMessageState = $scope.hideElement();
		    		    	$('#newItemModal').modal('hide');
		    		    	
		    		    	 $scope.showSuccessMessage("Successfully added!"); 
		    		    	
		    		    } else {
		    		    	
		    		    	console.log("Not sure what to do!");
		    		    	
		    		    }
		    		    
		    		  }, function errorCallback(response) {

		    			  $scope.duplicateMessageState = $scope.hideElement();
		    			  $scope.invalidUrlMessageState = $scope.hideElement();
		    			  
		    			  if (response.data.indexOf("INVALID_URL") > 0) {
		    				  $scope.showInvalidUrlMessage();
		    				  console.log("Url is not correct");
		    			  }
		    			  if (response.data.indexOf("DUPLICATE_TAG") > 0) {
		    				  $scope.showDuplicateMessage();
		    				  console.log("Tag already exists");
		    			  }		    			  
		    			  
		    		  });				  
				  
			  }			 
			 
			 

			  $scope.hideSuccessMessage = function() {
				  $scope.successMessageState = {'display': 'none'};
			  }
			  
			  $scope.showSuccessMessage = function(message) {				  
				  $scope.successMessage = message;
				  $scope.successMessageState = {'display': 'block'};				  
			  }
			  
			  $scope.hideErrorMessage = function() {
				  $scope.errorMessageState = {'display': 'none'};
			  }
			
			  $scope.showErrorMessage = function(message) {
				  $scope.errorMessage = message;
				  $scope.errorMessageState = {'display': 'block'};
			  }


			  $scope.hideAllMessages = function () {
					$scope.hideSuccessMessage();
					$scope.hideErrorMessage();	
					//hideWarningMessage();	
			  }			  
			  
			  
			  $scope.hideInputTips = function() {				  
				  $scope.inputTips = {'display': 'none'}; 
			  }
			  
			  $scope.showInputTips = function() {				  
				  $scope.inputTips = {'display': 'inherit'}; 
			  }				  
			  
			  $scope.hideElement = function() {				  
				  return {'display': 'none'}; 
				  //$scope.loginButtonState = {'display': 'none'}; 
				  //console.log($scope.loginButtonState);
				  				  
			  }
			  
			  $scope.showElement = function() {
				  //console.log("State:"+((typeof state == 'undefined')?'initial':state));
				  return {'display': 'initial'};//(typeof state == 'undefined')?'initial':state}; 
			  }
			  		 			  
			  $scope.showContextMenuVisibility = function() {				  
				  $scope.contextMenuVisibility = {'display': 'inherit'}; 
			  }				  
			  
			  $scope.showDuplicateMessage = function() {				  
				  $scope.duplicateMessageState = {'display': 'block'}; 
			  }				   

			  $scope.showInvalidUrlMessage = function() {				  
				  $scope.invalidUrlMessageState = {'display': 'block'}; 
			  }					  
			  
			  $scope.duplicateMessageState = $scope.hideElement();
			  $scope.invalidUrlMessageState = $scope.hideElement();
			  
			  $scope.setVisibilities = function() {
				  
				  if ($scope.userName != null) {
					  console.log("Hide login");					  
					  
					  $scope.usernameLabelState = $scope.showElement();
					  
					  $scope.addTagButtonState = $scope.showElement();
					  
					  $scope.loginButtonState = $scope.hideElement();
					  
					  $scope.signedState = $scope.showElement();
					  
					  $scope.showContextMenuVisibility();
					  
				  }else {
					  console.log("Show login");					  					  
					  $scope.usernameLabelState = $scope.hideElement();
					  $scope.addTagButtonState = $scope.hideElement();
					  
					  $scope.loginButtonState = $scope.showElement();
					  $scope.signedState = $scope.hideElement();
					  
					  $scope.contextMenuVisibility = $scope.hideElement();
				  }
				 
			  }
			 
			  $scope.addSearchEngine = function() {
				  
				  $route.reload();
				  $scope.showSuccessMessage("Successfully added.You can now set Jumpon as a your default search engine now in Chrome->Settings. "+
						  					"This will allow you to jump directly from address bar!");
				  
			  }			  
			  
			  
			 
			  $scope.jump = function(prop){  //$scope.selectedprop = prop; 			  											
													//document.getElementById("searchText").focus();
													$scope.getTag(prop.tag); 
			  										};
			  $scope.search = function(prop){  //$scope.selectedprop = prop; 			  											
														//document.getElementById("searchText").focus();
														$scope.getTag(prop.tag+"?"); 
				  										};			  										
			  $scope.searchKeyUp = function(event){ switch(event.keyCode) {
													    case 9://Tab
													    	{
													    	event.preventDefault();
													    	$scope.searchTag=$scope.props[$scope.currTag].tag;}
													        break;			  
													    case 13:
													    	{ if ($scope.currTag > -1) { $scope.getTag($scope.props[$scope.currTag].tag);
													    	
													    		} else {
													    			$scope.getTag();
													    		}
													    	console.log("Return!");}
													        break;													        
													    case 40: //Down
													    	{
													    		//document.getElementById("a0").focus();
													    		if ($scope.currTag < $scope.tagCount-1) { 
													    			
													    			if ($scope.currTag > -1)
													    				document.getElementById("a"+$scope.currTag).className = "list-group-item ng-binding ng-scope";
													    			
													    			$scope.currTag++;
													    			
													    			document.getElementById("a"+$scope.currTag).className = "list-group-item ng-binding ng-scope active";
													    			
													    		}
													    	}
													        break;
													    case 38: //Up
												    	{
													    	if ($scope.currTag > 0) { 
												    			
												    			document.getElementById("a"+$scope.currTag).className = "list-group-item ng-binding ng-scope";
												    			
												    			$scope.currTag--;
												    			
												    			document.getElementById("a"+$scope.currTag).className = "list-group-item ng-binding ng-scope active";
												    			
												    		}
												    	}
												        break;													        
													    default:
													    	
													}
			  
			  										};	
			  $scope.suggestTags = function() {
				  
				  if ($scope.getTagsPromise) $interval.cancel($scope.getTagsPromise); 
				  
				  //Check if empty
				  if (!$scope.searchTag) {
				    	console.log("Get top tags. empty tag");
				    	$scope.getTagsPromise = $interval($scope.getTopTags(), 150, 1);
				    } else {
				    	
			    	  $scope.showInputTips();
	
			    	  $scope.getTagsPromise = $interval($scope.getTags, 100, 1);
			    	  
			    	  //$scope.getTags();
			    	  
			    	}
			    
				  console.log("Tag:"+$scope.searchTag);
				  
			  }										
			  $scope.searchKeyDown = function(event){ switch(event.keyCode) {
												    case 9:
												    	{
												    	console.log("Tab!");
												    	event.preventDefault();
												    	$scope.searchTag=$scope.props[0].tag;
												    	}
												        break;			  
												    case 13:
												    	break;
												    case 40:
												    	break;
												    default:
												    	{
												    	if ($scope.getTagsPromise)	$interval.cancel($scope.getTagsPromise); 
												    	
												    	}
												}
		  
		  										};
		  										
		  	  $scope.listKeyPress = function(keyCode, index){ if (keyCode === 38 && index === 0) document.getElementById("searchTag").focus(); 
		  										};											
			  										
			  $scope.addKeyPress = function(keyCode){ if (keyCode === 13) $scope.addNewTag();
			  										};			  										
			  
			  console.log("Get top tags. load");
			  $scope.getTopTags(); 
			  $scope.getUserName();
			  //$scope.setVisibilities();
			    
			  $scope.login = function() {
					  $window.location.href = "/auth";
				    };
			  $scope.logout = function() {
				  $window.location.href = "/logout";
			    };							    

			  $scope.handleClick = function(event, index) {
				  switch(event.which) {
			        case 1:
			        	console.log("Left click!");
			            break;
			        case 2:
			            // in case you need some middle click things
			            break;
			        case 3:
			        	console.log("Right click! +"+ index);
			        	$scope.rightClickedIndex = index;
			            break;
			        default:			            
			            break;
			    }							
				  
			  }
			  
			  $scope.rightClick = function(index) {
				  
				  var elementId = $scope.rightClickedIndex;
				  
				  switch(index) {
			        case 1:
			        	//Jump
			        	$scope.getTag($scope.props[elementId].tag);
			            break;
			        case 2:
			            // Search
			        	$scope.getTag($scope.props[elementId].tag+"?");
			            break;
			        case 3:
			        	//DELETE
			        	console.log("Delete: "+$scope.props[elementId].tag);	
			        	$scope.deleteTag($scope.props[elementId].tag, $scope.props[elementId].type);
			        	var element = document.getElementById("a"+elementId);
			        	element.parentNode.removeChild(element);
			            break;
			        default:			            
			            break;
			    }
			  }
			    
			    
			});
		  
		  app.config(function ($routeProvider) {
			       $routeProvider
			         .when('/access_token=:accessToken', {
			           template: '',
			           controller: function ($location,$rootScope) {
			             var hash = $location.path().substr(1);
			             
			            var splitted = hash.split('&');
			            var params = {};
			  
			            for (var i = 0; i < splitted.length; i++) {
			              var param  = splitted[i].split('=');
			              var key    = param[0];
			              var value  = param[1];
			              params[key] = value;
			              console.log(key+":"+value);
			              $rootScope.accesstoken=params;
			            }
			            $location.path("/about");
			          }
			        })
			});
		  
		  app.config(['$httpProvider', function($httpProvider) {
			    //initialize get if not there
			    if (!$httpProvider.defaults.headers.get) {
			        $httpProvider.defaults.headers.get = {};    
			    }    

			    // Answer edited to include suggestions from comments
			    // because previous version of code introduced browser-related errors

			    //disable IE ajax request caching
			    $httpProvider.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
			    // extra
			    $httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
			    $httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
			}]);
		  
		  
		  //Focus url element on show
		  $('#newItemModal').on('shown.bs.modal', function () {
			    $('#urlInput').focus();
			});
		  
		 
		  
		  
		  