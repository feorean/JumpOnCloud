<!DOCTYPE html>
<html lang="en">

<head>
  <title>jumpOn.io smart links</title>
  <meta charset="utf-8">
 
  
  <meta http-equiv="cache-control" content="max-age=0" />
  <meta http-equiv="cache-control" content="no-cache" />
  <meta http-equiv="expires" content="0" />
  <meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
  <meta http-equiv="pragma" content="no-cache" />  
  
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no" >
  <link rel="search" type="application/opensearchdescription+xml" href="opensearch.xml"/>
  
   <link rel="icon" href="/images/logo_16.ico">
   
   <link rel="stylesheet" href="/css/bootstrap.min.css">
   <link rel="stylesheet" href="css/style.css">
   <!-- <link rel="stylesheet" href="/css/bootstrap-theme.css">-->



   <script src="/js/jquery.min.js"></script>
   <script src="/js/bootstrap.min.js"></script>

   <script src="/js/angular.min.js"></script> 
   <script src="/js/angular-route.min.js"></script> 
   <script src="/js/angular-sanitize.min.js"></script>  
   <script src = "https://plus.google.com/js/client:platform.js" async defer></script> 
   <script  src = "/js/jumpon.js"></script>  

   
	<script>
	  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
	
	  ga('create', 'UA-84987581-1', 'auto');
	  ga('send', 'pageview');
	
	</script>  

  
  <style>li.selected {text-decoration: underline;}</style>
</head>


<body class="no-padding background-colour"  >
<div ng-app="myApp"  ng-controller="searchListCtrl" style="float: none; margin: 0 auto;">

	
	<nav class="navbar navbar-inverse">
	  <div class="container-fluid">
	    <div class="navbar-header">
	      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	      </button>
	      <a class="navbar-brand" href="#">
	      	<h3 class="inline-display" style="margin-top: 0px;"><b class="desc-font">jumpOn</b></h3>
	      	<div class="inline-display tiny-font" style="color:yellowgreen">beta</div>
	      </a>
	    </div>
	    
	    <div class="collapse navbar-collapse navbar-right" id="myNavbar">
	      
	      
	      
	      	<ul class="nav navbar-nav top-padding">
	        
				<!-- ADD BUTTON (SMALL) -->
				<li><a href="#" data-toggle="modal" 
						data-target="#newItemModal"
						ng-style="addTagButtonState" 
						class="hidden-md hidden-lg"><span class="glyphicon glyphicon-plus"></span> Add</a></li>  
			</ul>			     	 	
			<ul class="nav navbar-nav ">	
				<!-- ADD BUTTON (BIG) -->
				<button type="button"  
						class="btn btn-default navbar-btn hidden-xs hidden-sm" 
						ng-style="addTagButtonState" 
						data-toggle="modal" 
						data-target="#newItemModal">
						
						<span class="glyphicon glyphicon-plus"></span>
						Add
						
				</button> 
				
	
				
				
				<!-- MODAL FORM TO ADD A NEW JUMP -->
				<div id="newItemModal" class="modal fade" role="dialog">
				  <div class="modal-dialog">
				
				    <!-- Modal content-->
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal">&times;</button>
				        <h4 class="modal-title">Add new jump</h4>
				        <h5><span class="label label-info">Note: It's much easier to use Chrome extension to add a new jump</span></h5>
				      </div>
				      <div class="modal-body">
						<div class="alert alert-danger" ng-style="duplicateMessageState">
						  <strong>Error!</strong> Jump already exists
						</div>		
						<div class="alert alert-danger" ng-style="invalidUrlMessageState">
						  <strong>Error!</strong> Invalid URL (Did you forget http:// ?). Please correct the web address.
						</div>	
	
				        <div class="form-group">
						  <label for="newTagUrl">Web address (URL):</label>
						  <input type="text" id="urlInput" class="form-control" 
						  		ng-model="newTagUrl" 
						  		placeholder="https://www.youtube.com/watch?v=u02tycroA30&list=PLLJGWsxcML3kNLMvrJxTPVgpprvfNl1PA&index=2" >
						</div>
														      
				        <div class="form-group">
						  <label for="newTag">Name of jump:</label>
						  <input type="text" class="form-control" 
						  			ng-model="newTag" 
						  			ng-keyup="addKeyPress($event.keyCode)"
						  			placeholder="e.g. my music, peppa pig" >
						</div>
	
						<div class="checkbox">
	 							<label><input type="checkbox" value="" ng-model="overwriteTag">Overwrite</label>
						</div>
				      </div>
				      <div class="modal-footer">
				        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				        <button type="button" class="btn btn-success" ng-click="addNewTag()">Save jump</button>
				      </div>
				    </div>
				
				  </div>
				</div>	
				
				
				       
	        </ul>
	        
	        <ul class="nav navbar-nav navbar-right">
		        
		        <li class="dropdown" ng-style="signedState">
		          <a class="dropdown-toggle" data-toggle="dropdown" href="#"><span class="glyphicon glyphicon-user"></span> {{userName}} <span class="caret"></span></a>
		          <ul class="dropdown-menu">
		            <li><a href="/myjumps">My jumps</a></li>
		            <!--<li><a href="#">History</a></li>
		            <li><a href="#">Profile</a></li>-->
		            <li role="separator" class="divider"></li>
		            <li><a href="#" ng-click="logout()"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
		          </ul>
		        </li>
			      
		        <li ng-style="loginButtonState"><a href="#" 
		        		data-toggle="modal" 
						data-target="#loginModal"><span class="glyphicon glyphicon-user"></span> Login</a></li>
	
				<!-- MODAL FORM TO ADD A NEW JUMP -->
				<div id="loginModal" class="modal fade" role="dialog">
					<div class="modal-dialog">
					  
						
						<!-- Modal content-->
					    <div class="modal-content">
		
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal">&times;</button>
					        <h4 class="modal-title">Login using your google account. Simple!</h4>			        
					      </div>
					      
					      <div class="modal-body">
							<center>
						  		<button class="btn btn-link btn-lg" ng-click="login()" > 		      		 
						      		<img src="/images/googleplus_icon.png" title="Login with Google account"/>
						    	</button>		
					        </center>
					      </div>
					      
					    </div>
					
					 
					</div>
				</div>		
				
				
										
	
	
	     	 </ul>
	     	 
	    </div>
	    
	  </div>
	</nav>
	  

	<div class="container-fluid">

	
	